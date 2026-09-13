package sw.world.blocks.distribution;

import arc.*;
import arc.audio.*;
import arc.graphics.g2d.*;
import arc.util.io.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.world.blocks.distribution.*;
import sw.annotations.*;
import sw.content.*;

import static mindustry.Vars.*;

public class MechanicalBridge extends DuctBridge {
	public @Annotations.Load("@name$-over") TextureRegion overRegion;
	public Sound changeSound = Sounds.door;
	public Effect changeEffect = SWFx.changeEffect;

	public MechanicalBridge(String name) {
		super(name);
		drawCached = false;

		consumesTap = true;

		config(Boolean.class, (MechanicalBridgeBuild build, Boolean toggle) -> {
			build.isEnd = toggle;
		});
	}

	public class MechanicalBridgeBuild extends DuctBridgeBuild {
		public boolean isEnd;

		@Override public Boolean config(){
			return isEnd;
		}

		@Override
		public void draw() {
			Draw.rect(block.region, x, y);

			if (isEnd) {
				Draw.rect(overRegion, x, y, rotdeg());
			} else {
				Draw.rect(dirRegion, x, y, rotdeg());
				super.draw();
			}
		}

		@Override
		public void drawCached() {
		}

		@Override
		public DirectionBridgeBuild findLink() {
			return isEnd ? null : super.findLink();
		}

		@Override
		public Graphics.Cursor getCursor(){
			return interactable(player.team()) ? Graphics.Cursor.SystemCursor.hand : Graphics.Cursor.SystemCursor.arrow;
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);

			if (revision >= 1) isEnd = read.bool();
		}

		@Override
		public void tapped() {
			configure(!isEnd);
			changeSound.at(x, y);
			changeEffect.at(x, y, 0, block);
		}

		@Override public byte version() {
			return 1;
		}

		@Override
		public void write(Writes write) {
			super.write(write);

			write.bool(isEnd);
		}
	}
}

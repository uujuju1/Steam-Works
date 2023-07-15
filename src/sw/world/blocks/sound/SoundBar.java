package sw.world.blocks.sound;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.struct.*;
import arc.util.io.*;
import mindustry.gen.*;
import mindustry.world.*;
import sw.util.*;
import sw.world.interfaces.*;
import sw.world.modules.*;

public class SoundBar extends Block {
	public TextureRegion bottomRegion, barRegion;
	public TextureRegion[] regions;

	public SoundBar(String name) {
		super(name);
		solid = destructible = true;
		rotate = update = true;
	}

	@Override
	public void load() {
		super.load();
		bottomRegion = Core.atlas.find(name + "-bottom");
		barRegion = Core.atlas.find(name + "-bar");
		regions = SWDraw.getRegions(Core.atlas.find(name + "-top"), 4, 1, 32);
	}

	public class SoundBarBuild extends Building implements HasSound {
		SoundModule sound = new SoundModule();

		@Override public SoundModule sound() {
			return sound;
		}

		@Override public Seq<HasSound> soundProximity() {
			return Seq.with(front(), back()).removeAll(b -> !(b instanceof HasSound) || b.rotation() % 2 != rotation() % 2).as();
		}

		@Override
		public boolean connectsTo(HasSound to) {
			if (to instanceof SoundBar) return to.rotation() % 2 == rotation() % 2;
			return to == front() || to == back();
		}

		@Override
		public void onProximityUpdate() {
			super.onProximityUpdate();
			soundGraph().addBuild(this);
			for(HasSound build : soundProximity()) soundGraph().mergeGraph(build.soundGraph());
		}

		@Override
		public void onProximityRemoved() {
			super.onProximityRemoved();
			soundGraph().removeBuild(this);
		}

		// funky xor math
		public int drawIndex() {
			int i = 0;
			if (front() instanceof HasSound b && connectsTo(b)) i += (rotation() ^ 3) % 3 > 0 ? 2 : 1;
			if (back() instanceof HasSound b && connectsTo(b)) i += (rotation() ^ 3) % 3 == 0 ? 2 : 1;
			return i;
		}
		@Override
		public void draw() {
			Draw.rect(bottomRegion, x, y, 0);
			drawSound(barRegion, Color.white);
			Draw.rect(regions[drawIndex()], x, y, 90 - rotdeg() % 180 - 90);
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);
			sound.read(read);
		}
		@Override
		public void write(Writes write) {
			super.write(write);
			sound.write(write);
		}
	}
}

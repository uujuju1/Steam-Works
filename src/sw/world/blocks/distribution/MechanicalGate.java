package sw.world.blocks.distribution;

import arc.*;
import arc.audio.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.blocks.distribution.*;
import sw.annotations.Annotations.*;
import sw.content.*;

import static mindustry.Vars.*;

public class MechanicalGate extends OverflowGate {
	public @Load("@-invert") TextureRegion invertRegion;
	public Sound changeSound = Sounds.door;
	public Effect changeEffect = SWFx.changeEffect;

	public MechanicalGate(String name) {
		super(name);
		saveConfig = copyConfig = true;
		config(Boolean.class, (MechanicalGateBuild build, Boolean invert) -> {
			build.invert = invert;
		});
	}

	@Override public void drawPlanConfig(BuildPlan plan, Eachable<BuildPlan> list) {
		if (plan.config == Boolean.TRUE) Draw.rect(invertRegion, plan.drawx(), plan.drawy(), 0);
	}

	public class MechanicalGateBuild extends OverflowGateBuild {
		public boolean invert = false;

		@Override
		public Boolean config(){
			return invert;
		}

		@Override
		public void draw() {
			super.draw();
			if (invert) Draw.rect(invertRegion, x, y, 0);
		}

		@Override
		public Graphics.Cursor getCursor(){
			return interactable(player.team()) ? Graphics.Cursor.SystemCursor.hand : Graphics.Cursor.SystemCursor.arrow;
		}

		@Override
		public @Nullable Building getTileTarget(Item item, Building src, boolean flip){
			int from = relativeToEdge(src.tile);
			if(from == -1) return null;
			Building to = nearby((from + 2) % 4);
			boolean
				fromInst = src.block.instantTransfer,
				canForward = to != null && to.team == team && !(fromInst && to.block.instantTransfer) && to.acceptItem(this, item),
				inv = invert == enabled;

			if(!canForward || inv){
				Building a = nearby(Mathf.mod(from - 1, 4));
				Building b = nearby(Mathf.mod(from + 1, 4));
				boolean ac = a != null && !(fromInst && a.block.instantTransfer) && a.team == team && a.acceptItem(this, item);
				boolean bc = b != null && !(fromInst && b.block.instantTransfer) && b.team == team && b.acceptItem(this, item);

				if(!ac && !bc){
					return inv && canForward ? to : null;
				}

				if(ac && !bc){
					to = a;
				}else if(bc && !ac){
					to = b;
				}else{
					to = (rotation & (1 << from)) == 0 ? a : b;
					if(flip) rotation ^= (1 << from);
				}
			}

			return to;
		}

		@Override
		public void tapped() {
			configure(!invert);
			changeSound.at(x, y);
			changeEffect.at(x, y, 0, block);
		}

		@Override
		public void read(Reads read, byte revision){
			super.read(read, revision);
			invert = read.bool();
		}
		@Override
		public void write(Writes write){
			super.write(write);
			write.bool(invert);
		}
	}
}

package sw.world.blocks.distribution;

import arc.*;
import arc.audio.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.blocks.distribution.*;
import sw.annotations.Annotations.*;
import sw.content.*;

public class MechanicalSorter extends Sorter {
	public @Load("@-invert") TextureRegion invertRegion;
	public Sound changeSound = Sounds.door;
	public Effect changeEffect = SWFx.changeEffect;
	public Color crossColor = Color.valueOf("2D2F39");

	private boolean wasInverted = false;

	public MechanicalSorter(String name) {
		super(name);
	}

	@Override public void drawPlanConfig(BuildPlan plan, Eachable<BuildPlan> list) {
		if (wasInverted) Draw.rect(invertRegion, plan.drawx(), plan.drawy(), 0);
	}

	@Override protected TextureRegion[] icons() {
		return new TextureRegion[]{Core.atlas.find("sw-mechanical-conduit-router-bottom"), region};
	}

	public class MechanicalSorterBuild extends SorterBuild {
		public boolean invert = wasInverted;

		@Override
		public void buildConfiguration(Table table){
			super.buildConfiguration(table);
			table.row();
			table.table(Styles.black6, t -> t.check("@sw-sorter-inverted", invert, c -> {
				changeSound.at(x, y);
				changeEffect.at(x, y, 0f, block);
				invert = wasInverted = c;
			})).growX();
		}

		@Override
		public void draw() {
			Draw.color(sortItem == null ? crossColor : sortItem.color);
			Draw.rect(cross, x, y);
			Draw.color();
			Draw.rect(region, x, y);
			if (invert) Draw.rect(invertRegion, x, y);
		}

		@Override
		public Building getTileTarget(Item item, Building source, boolean flip){
			int dir = source.relativeTo(tile.x, tile.y);
			if(dir == -1) return null;
			Building to;

			if(((item == sortItem) != invert) == enabled){
				//prevent 3-chains
				if(isSame(source) && isSame(nearby(dir))){
					return null;
				}
				to = nearby(dir);
			}else{
				Building a = nearby(Mathf.mod(dir - 1, 4));
				Building b = nearby(Mathf.mod(dir + 1, 4));
				boolean ac = a != null && !(a.block.instantTransfer && source.block.instantTransfer) &&
					             a.acceptItem(this, item);
				boolean bc = b != null && !(b.block.instantTransfer && source.block.instantTransfer) &&
					             b.acceptItem(this, item);

				if(ac && !bc){
					to = a;
				}else if(bc && !ac){
					to = b;
				}else if(!bc){
					return null;
				}else{
					to = (rotation & (1 << dir)) == 0 ? a : b;
					if(flip) rotation ^= (1 << dir);
				}
			}

			return to;
		}

		@Override
		public void write(Writes write) {
			super.write(write);
			write.bool(invert);
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);
			invert = read.bool();
		}
	}
}

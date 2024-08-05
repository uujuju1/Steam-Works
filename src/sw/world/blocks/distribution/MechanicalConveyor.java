package sw.world.blocks.distribution;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.distribution.*;
import sw.util.*;

import static mindustry.Vars.*;

/**
 * please ignore the mess
 */
public class MechanicalConveyor extends Conveyor {
	public boolean armored = false;
	public TextureRegion[] tiles, frames;

	public MechanicalConveyor(String name) {
		super(name);
	}

	@Override
	public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
		Point2
			frontPos = new Point2(1, 0).rotate(plan.rotation).add(plan.x, plan.y),
			backPos = new Point2(-1, 0).rotate(plan.rotation).add(plan.x, plan.y);

		var ref = new Object() {
			int val = 0;
		};
		list.each(otherPlan -> {
			if (new Point2(otherPlan.x, otherPlan.y).equals(frontPos) && otherPlan.rotation == plan.rotation && otherPlan.block instanceof Conveyor) ref.val |= 1;
			if (new Point2(otherPlan.x, otherPlan.y).equals(backPos) && otherPlan.rotation == plan.rotation && otherPlan.block instanceof Conveyor) ref.val |= 2;
		});

		Draw.rect(frames[0], plan.drawx(), plan.drawy(), plan.rotation * 90);
		Draw.rect(tiles[ref.val], plan.drawx(), plan.drawy(), plan.rotation * 90);
	}

	@Override public TextureRegion[] icons() {
		return new TextureRegion[]{region};
	}

	@Override
	public void init(){
		super.init();

		if(junctionReplacement == Blocks.junction) junctionReplacement = null;
		if(bridgeReplacement == Blocks.itemBridge) bridgeReplacement = null;
	}

	@Override
	public void load() {
		super.load();
		tiles = SWDraw.getRegions(Core.atlas.find(name + "-tiles"), 4, 1, 32, 32);
		frames = SWDraw.getRegions(Core.atlas.find(name + "-frames"), 4, 1, 32, 32);
	}

	public class MechanicalConveyorBuild extends ConveyorBuild {
		public int getIndex() {
			int val = 0;
			if (front() instanceof ConveyorBuild && front().rotation == rotation) val++;
			if (back() instanceof ConveyorBuild && back().rotation == rotation) val+=2;
			return val;
		}

		@Override
		public boolean acceptItem(Building source, Item item){
			return super.acceptItem(source, item) && (!armored || (source.block instanceof Conveyor || Edges.getFacingEdge(source.tile(), tile).relativeTo(tile) == rotation));
		}

		@Override
		public void draw() {
			Draw.z(Layer.block - 0.2f);
			Draw.rect(
				frames[(enabled && clogHeat <= 0.5f ? (int)(((Time.time * speed * 6f * timeScale * efficiency)) % 4) : 0)],
				x, y, rotdeg()
			);
			Draw.rect(tiles[getIndex()], x, y, rotdeg());
			Draw.z(Layer.block - 0.1f);
			float layer = Layer.block - 0.1f, wwidth = world.unitWidth(), wheight = world.unitHeight(), scaling = 0.01f;
			for(int i = 0; i < len; i++){
				Item item = ids[i];
				Tmp.v1.trns(rotation * 90, tilesize, 0);
				Tmp.v2.trns(rotation * 90, -tilesize / 2f, xs[i] * tilesize / 2f);

				float
					ix = (x + Tmp.v1.x * ys[i] + Tmp.v2.x),
					iy = (y + Tmp.v1.y * ys[i] + Tmp.v2.y);

				//keep draw position deterministic.
				Draw.z(layer + (ix / wwidth + iy / wheight) * scaling);
				Draw.rect(item.fullIcon, ix, iy, itemSize, itemSize);
			}
		}
	}
}

package sw.world.blocks.liquid;

import arc.*;
import arc.func.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.liquid.*;
import mindustry.world.meta.*;

public class LiquidPipe extends Block {
	public final int timerFlow = timers++;

	public float padding;

	public @Nullable LiquidJunction junctionReplacement;

	public TextureRegion[] tiles;
	public TextureRegion arrowRegion, bottomRegion;

	public LiquidPipe(String name) {
		super(name);
		rotate = true;
		update = true;
		destructible = true;
		hasLiquids = true;
		solid = false;
		floating = true;
		underBullets = true;
		conveyorPlacement = true;
		noUpdateDisabled = true;
		canOverdrive = false;
		group = BlockGroup.liquids;
		outputsLiquid = true;
		priority = TargetPriority.transport;
	}

	@Override
	public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
		Draw.rect(region, plan.drawx(), plan.drawy(), (plan.rotation * 90f + 90f) % 180f - 90f);
		Draw.rect(arrowRegion, plan.drawx(), plan.drawy(), plan.rotation * 90f);
	}

	@Override
	public Block getReplacement(BuildPlan req, Seq<BuildPlan> plans) {
		if(junctionReplacement == null) return this;

		Boolf<Point2> cont = p -> plans.contains(o -> o.x == req.x + p.x && o.y == req.y + p.y && (req.block instanceof LiquidPipe || req.block instanceof LiquidJunction));
		return
			cont.get(Geometry.d4(req.rotation)) &&
			cont.get(Geometry.d4(req.rotation - 2)) &&
			req.tile() != null && req.tile().block() instanceof LiquidPipe &&
			Mathf.mod(req.build().rotation - req.rotation, 2) == 1 ?
				junctionReplacement : this;
	}

	@Override
	protected TextureRegion[] icons() {
		return new TextureRegion[]{region, arrowRegion};
	}

	@Override
	public void load() {
		super.load();

		tiles = Core.atlas.find(name + "-tiles").split(32, 32)[0];
		arrowRegion = Core.atlas.find(name + "-arrow");
		bottomRegion = Core.atlas.find(name + "-bottom");
	}

	public class LiquidPipeBuild extends Building {
		public int tiling;

		public float smoothLiquid;

		@Override
		public boolean acceptLiquid(Building source, Liquid liquid){
			noSleep();
			return (liquids.current() == liquid || liquids.currentAmount() < 0.2f)
			&& (tile == null || source == this || (source.relativeTo(tile.x, tile.y) + 2) % 4 != rotation);
		}

		@Override
		public void draw() {
			tiling = 0;

			for(int i = 0; i < 4; i++) {
				if (
					nearby(i) != null &&
					(nearby(i) != front() ? nearby(i).block.outputsLiquid : nearby(i).block.hasLiquids) &&
					nearby(i).team == team &&
					(!(nearby(i) instanceof LiquidPipeBuild) || (nearby(i).front() == this ^ front() == nearby(i)))
				) tiling |= 1 << i;
			}

			Draw.z(Layer.blockUnder);
			Draw.rect(bottomRegion, x, y);

			LiquidBlock.drawTiledFrames(size, x, y, padding, liquids.current(), smoothLiquid);

			Draw.rect(tiles[tiling], x, y);

			Draw.rect(arrowRegion, x, y, rotdeg());
		}

		@Override
		public void onProximityUpdate() {
			super.onProximityUpdate();
		}

		@Override
		public void updateTile() {
			smoothLiquid = Mathf.lerpDelta(smoothLiquid, liquids.currentAmount() / liquidCapacity, 0.05f);

			if (liquids.currentAmount() > 0.0001f && timer(timerFlow, 1)) {
				moveLiquidForward(false, liquids.current());
				noSleep();
			} else {
				sleep();
			}
		}
	}
}

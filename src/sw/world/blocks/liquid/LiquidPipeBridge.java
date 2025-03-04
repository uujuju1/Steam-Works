package sw.world.blocks.liquid;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.input.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class LiquidPipeBridge extends Block {
	public final int timerFlow = timers++;

	public int range;

	public TextureRegion closedRegion, arrowRegion;

	public LiquidPipeBridge(String name) {
		super(name);
		rotate = true;
		update = true;
		destructible = true;
		hasLiquids = true;
		drawArrow = false;
		solid = false;
		floating = true;
		underBullets = true;
		noUpdateDisabled = true;
		canOverdrive = false;
		group = BlockGroup.liquids;
		outputsLiquid = true;
		priority = TargetPriority.transport;
	}

	@Override
	public void changePlacementPath(Seq<Point2> points, int rotation) {
		Placement.calculateNodes(points, this, rotation, (point, other) -> Math.max(Math.abs(point.x - other.x), Math.abs(point.y - other.y)) <= range + 1);
	}

	@Override
	public void drawPlace(int x, int y, int rotation, boolean valid) {
		Drawf.dashLine(
			valid ? Pal.accent : Pal.remove,
			x * tilesize + Geometry.d4x(rotation) * (tilesize / 2f + 2f),
			y * tilesize + Geometry.d4y(rotation) * (tilesize / 2f + 2f),
			x * tilesize + Geometry.d4x(rotation) * (range + 1) * tilesize,
			y * tilesize + Geometry.d4y(rotation) * (range + 1) * tilesize
		);

		Draw.reset();
	}

	@Override
	public void load() {
		super.load();

		closedRegion = Core.atlas.find(name + "-closed");
		arrowRegion = Core.atlas.find(name + "-arrow");
	}

	public class LiquidPipeBridgeBuild extends Building {
		public LiquidPipeBridgeBuild next, prev;

		@Override
		public boolean acceptLiquid(Building source, Liquid liquid) {
			noSleep();
			return
				(liquids.current() == liquid || liquids.currentAmount() < 0.2f) &&
				(next == null || prev == null || source == prev) &&
				(source != back() || next != null || source == prev);
		}

		@Override
		public void draw() {
			next = findNext();
			prev = findPrev();

			if (rotation > 0 && rotation < 3) Draw.xscl = -1f;
			if (next != null) Draw.xscl *= -1f;
			Draw.rect(prev != null && next != null ? closedRegion : region, x, y, (rotdeg() + 90f) % 180f - 90f);
			Draw.xscl = 1f;

			Draw.rect(arrowRegion, x, y, rotdeg());
		}

		@Override
		public void drawSelect() {
			if (next != null) {
				Drawf.line(
					Pal.accent,
					x + Geometry.d4x(rotation) * (tilesize / 2f),
					y + Geometry.d4y(rotation) * (tilesize / 2f),
					next.x - Geometry.d4x(rotation) * (tilesize / 2f),
					next.y - Geometry.d4y(rotation) * (tilesize / 2f)
				);
				Drawf.square(next.x, next.y, next.block.size * 4f, Pal.accent);
			}
			if (prev != null) {
				Drawf.line(
					Pal.place,
					prev.x + Geometry.d4x(rotation) * (tilesize / 2f),
					prev.y + Geometry.d4y(rotation) * (tilesize / 2f),
					x - Geometry.d4x(rotation) * (tilesize / 2f),
					y - Geometry.d4y(rotation) * (tilesize / 2f)
				);
				Drawf.square(prev.x, prev.y, prev.block.size * 4f, Pal.place);
			}
			Draw.reset();
		}

		public @Nullable LiquidPipeBridgeBuild findNext() {
			for(int i = 1; i < range + 2; i++) {
				Building other = nearby(
					Geometry.d4x((rotation) % 4) * i,
					Geometry.d4y((rotation) % 4) * i
				);

				if (
					other instanceof LiquidPipeBridgeBuild &&
					other.rotation == rotation &&
					other.team == team
				) return (LiquidPipeBridgeBuild) other;
			}
			return null;
		}

		public @Nullable LiquidPipeBridgeBuild findPrev() {
			for(int i = 1; i < range + 2; i++) {
				Building other = nearby(
					Geometry.d4x((rotation + 2) % 4) * i,
					Geometry.d4y((rotation + 2) % 4) * i
				);

				if (
					other instanceof LiquidPipeBridgeBuild &&
					other.rotation == rotation &&
					other.team == team
				) return (LiquidPipeBridgeBuild) other;
			}
			return null;
		}

		@Override
		public void updateTile() {
			if (liquids.currentAmount() > 0.0001f && timer(timerFlow, 1)) {
				moveLiquid(next == null ? front() : next, liquids.current());
				noSleep();
			} else {
				sleep();
			}
		}
	}
}

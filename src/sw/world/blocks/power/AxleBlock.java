package sw.world.blocks.power;

import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;
import sw.world.*;
import sw.world.interfaces.*;

public class AxleBlock extends GenericSpinBlock {
	public DrawBlock drawer = new DrawDefault();

	public AxleBlock(String name) {
		super(name);
		update = true;
		rotate = true;
		drawArrow = false;
		group = BlockGroup.power;
	}

	@Override
	public boolean canReplace(Block other) {
		return super.canReplace(other) || other instanceof AxleBlock;
	}

	@Override
	public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
		if (spinConfig != null) spinConfig.drawPlace(this, plan.x, plan.y, plan.rotation, true);
		drawer.drawPlan(this, plan, list);
	}

	@Override
	public void drawPlanConfigTop(BuildPlan plan, Eachable<BuildPlan> list) {
		drawer.drawPlan(this, plan, list);
	}

	@Override
	public void getRegionsToOutline(Seq<TextureRegion> out) {
		drawer.getRegionsToOutline(this, out);
	}

	@Override
	public TextureRegion[] icons(){
		return drawer.finalIcons(this);
	}

	@Override
	public void load() {
		super.load();
		drawer.load(this);
	}

	public class AxleBlockBuild extends GenericSpinBuild {
		public int tiling;
		
		@Override public void draw() {
			drawer.draw(this);
		}
		@Override public void drawLight() {
			drawer.drawLight(this);
		}

		@Override
		public void onGraphUpdate() {
			tiling = 0;
			for (int i = 0; i < (spinConfig.allowedEdges == null ? getEdges().length : spinConfig.allowedEdges[rotate ? rotation : 0].length); i++) {
				Point2 edge = getEdges()[spinConfig.allowedEdges == null ? i : spinConfig.allowedEdges[rotate ? rotation : 0][i]];
				if (nearby(edge.x, edge.y) instanceof HasSpin spinBuild && HasSpin.connects(this, spinBuild)) tiling |= 1 << i;
			}
		}

		@Override public float totalProgress() {
			return getRotation();
		}
	}
}

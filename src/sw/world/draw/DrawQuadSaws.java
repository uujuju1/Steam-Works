package sw.world.draw;

import arc.graphics.g2d.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;
import sw.annotations.Annotations.*;

public class DrawQuadSaws extends DrawQuadArms {
	protected float totalProgress;

	public float rotateSpeed = 1f;

	public @Load(value = "@loadBlock.name$-arm-tip", fallBack = "sw-manifold-arm-tip") TextureRegion tipRegion;

	@Override
	public void draw(Building build) {
		totalProgress = build.totalProgress() * rotateSpeed;
		super.draw(build);
	}

	@Override
	public void drawArm(float tipX, float tipY, float baseX, float baseY) {
		Draw.rect(tipRegion, tipX, tipY, totalProgress);
		super.drawArm(tipX, tipY, baseX, baseY);
	}

	@Override
	public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list) {
		totalProgress = 0f;
		super.drawPlan(block, plan, list);
	}
}

package sw.world.draw;

import arc.*;
import arc.func.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;
import sw.annotations.Annotations.*;
import sw.math.*;

public class DrawQuadArms extends BlockDrawer {
	public float armBaseOffset = 8f;
	public float armLength = 1f;
	public float armExtension = 0f;

	public Floatf<Building> maxTipRadius = b -> 1f;
	public float armHeight = 1f;
	public float armSpeedScl = 1f;

	public float layer = -1f;

	public Interp progressCurve = Interp.linear;

	public Effect tipEffect = Fx.none;
	public float tipEffectChance = 0.5f;

	public @Load(value = "@loadBlock.name$-arm", fallBack = "sw-manifold-arm") TextureRegion armRegion;
	public @Load(value = "@loadBlock.name$-arm-base", fallBack = "sw-manifold-arm-base") TextureRegion armBaseRegion;

	@Override
	public void draw(Building build) {
		float z = Draw.z();
		if (layer > 0) Draw.z(layer);
		float progress = Mathf.clamp(progressCurve.apply(build.progress()));
		for (Point2 offset : Geometry.d8edge) {
			float totalProgress = build.totalProgress() + armSpeedScl / 2f * Mathf.num(offset.x + offset.y == 0);

			float tipX = (Mathf.slope((totalProgress % armSpeedScl) / armSpeedScl) * Mathf.slope(progress) + Math.max(2f * progress, 1f) - 1f) * maxTipRadius.get(build) * offset.x;
			float tipY = ((1f - Mathf.slope((totalProgress % armSpeedScl) / armSpeedScl)) * Mathf.slope(progress) + Math.max(2f * progress, 1f) - 1f) * maxTipRadius.get(build) * offset.y;
			float baseX = armBaseOffset * offset.x;
			float baseY = armBaseOffset * offset.y;

			drawArm(
				tipX + build.x, tipY + build.y,
				baseX + build.x, baseY + build.y
			);

			if (Mathf.chance(tipEffectChance * build.warmup()) && !Vars.state.isPaused()) {
				tipEffect.at(tipX + build.x, tipY + build.y);
			}
		}
		Draw.z(z);
	}

	public void drawArm(float tipX, float tipY, float baseX, float baseY) {
		Vec2 midPos = Parallax.getParallaxFrom(Tmp.v1.trns(Angles.angle(tipX, tipY, baseX, baseY), armLength).add(tipX, tipY), Core.camera.position, armHeight);

		Lines.stroke((float) armBaseRegion.height / 4f);
		Lines.line(armBaseRegion, baseX, baseY, midPos.x, midPos.y, false);

		Parallax.getParallaxFrom(Tmp.v1.trns(Angles.angle(tipX, tipY, baseX, baseY), armLength + armExtension).add(tipX, tipY), Core.camera.position, armHeight);

		Lines.stroke((float) armRegion.height / 4f);
		Lines.line(armRegion, midPos.x, midPos.y, tipX, tipY, false);
	}

	@Override
	public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list) {
		for (Point2 offset : Geometry.d8edge) {
			float baseX = armBaseOffset * offset.x + plan.drawx();
			float baseY = armBaseOffset * offset.y + plan.drawy();

			drawArm(plan.drawx(), plan.drawy(), baseX, baseY);
		}
	}
}

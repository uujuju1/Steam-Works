package sw.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import arc.util.noise.*;
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

	public float maxTipRadius = 1f;
	public float armHeight = 1f;
	public float armSpeedScl = 0.1f;

	public float layer = -1f;

	public Effect tipEffect = Fx.none;
	public float tipEffectChance = 0.5f;

	public @Load(value = "@loadBlock.name$-arm", fallBack = "sw-manifold-arm") TextureRegion armRegion;
	public@Load(value = "@loadBlock.name$-arm-base", fallBack = "sw-manifold-arm-base")  TextureRegion armBaseRegion;

	@Override
	public void draw(Building build) {
		float z = Draw.z();
		if (layer > 0) Draw.z(layer);
		for (Point2 offset : Geometry.d8edge) {
			float tipX = Mathf.clamp((float) Simplex.raw2d(offset.hashCode(), build.totalProgress() * armSpeedScl, 0)) * maxTipRadius * offset.x;
			float tipY = Mathf.clamp((float) Simplex.raw2d(offset.hashCode(), 0, build.totalProgress() * armSpeedScl)) * maxTipRadius * offset.y;
			float baseX = armBaseOffset * offset.x;
			float baseY = armBaseOffset * offset.y;

			Vec2 midPos = Parallax.getParallaxFrom(Tmp.v1.trns(Angles.angle(tipX, tipY, baseX, baseY), armLength).add(tipX, tipY).add(build), Core.camera.position, armHeight);

			Lines.stroke((float) armBaseRegion.height / 4f);
			Lines.line(armBaseRegion, build.x + baseX, build.y + baseY, midPos.x, midPos.y, false);

			Parallax.getParallaxFrom(Tmp.v1.trns(Angles.angle(tipX, tipY, baseX, baseY), armLength + armExtension).add(tipX, tipY).add(build), Core.camera.position, armHeight);

			Lines.stroke((float) armRegion.height / 4f);
			Lines.line(armRegion, midPos.x, midPos.y, tipX + build.x, tipY + build.y, false);

			if (Mathf.chance(tipEffectChance * build.warmup()) && !Vars.state.isPaused()) {
				tipEffect.at(tipX + build.x, tipY + build.y);
			}
		}
		Draw.z(z);
	}

	@Override
	public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list) {
		for (Point2 offset : Geometry.d8edge) {
			float tipX = Mathf.clamp((float) Simplex.raw2d(offset.hashCode(), 0, 0)) * maxTipRadius * offset.x;
			float tipY = Mathf.clamp((float) Simplex.raw2d(offset.hashCode(), 0, 0)) * maxTipRadius * offset.y;
			float baseX = armBaseOffset * offset.x;
			float baseY = armBaseOffset * offset.y;

			Vec2 midPos = Parallax.getParallaxFrom(Tmp.v1.trns(Angles.angle(tipX, tipY, baseX, baseY), armLength).add(tipX, tipY).add(plan.drawx(), plan.drawy()), Core.camera.position, armHeight);

			Lines.stroke((float) armBaseRegion.height / 4f);
			Lines.line(armBaseRegion, plan.drawx() + armBaseOffset * offset.x, plan.drawy() + armBaseOffset * offset.y, midPos.x, midPos.y, false);

			Parallax.getParallaxFrom(Tmp.v1.trns(Angles.angle(tipX, tipY, baseX, baseY), armLength + armExtension).add(tipX, tipY).add(plan.drawx(), plan.drawy()), Core.camera.position, armHeight);

			Lines.stroke((float) armRegion.height / 4f);
			Lines.line(armRegion, midPos.x, midPos.y, tipX + plan.drawx(), tipY + plan.drawy(), false);
		}
	}
}

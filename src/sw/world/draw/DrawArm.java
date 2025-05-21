package sw.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;
import sw.annotations.Annotations.*;
import sw.entities.*;
import sw.math.*;
import sw.world.interfaces.*;

public class DrawArm extends BlockDrawer {
	public float layer = -1;

	public float armHeight = 0.5f;
	public float armExtension = 0;
	public float armLength = 10f;
	public float armOffset = 0;
	public Interp armCurve = Interp.linear;

	public @Load(value = "@loadBlock.name$-arm", fallBack = "%-mechanical-arm") TextureRegion armRegion;
	public @Load(value = "@loadBlock.name$-arm-base", fallBack = "%-mechanical-arm-base") TextureRegion armBaseRegion;

	public HasArm cast(Building build) {
		try {
			return (HasArm) build;
		} catch(Exception e) {
			throw new ClassCastException("This drawer requires the building to have an instance of HasArm. Use a different drawer.");
		}
	}

	@Override
	public void draw(Building build) {
		float z = Draw.z();
		if (layer > 0) Draw.z(layer);

		Arm arm = cast(build).arm();

		Tmp.v1.set(arm.startPos);
		if (!arm.startPos.equals(arm.targetPos)) Tmp.v1.lerp(arm.targetPos, armCurve.apply(Mathf.clamp(arm.time/cast(build).getArmTime())));
		Tmp.v1.add(build).add(Tmp.v4.trns(build.rotdeg(), armOffset));

		Tmp.v2.set(build).sub(Tmp.v1).setLength(armLength).add(Tmp.v1);
		Parallax.getParallaxFrom(Tmp.v2, Core.camera.position, armHeight);
		Tmp.v3.set(Tmp.v2).sub(Tmp.v1).setLength(armExtension);

		Lines.stroke(armBaseRegion.height/4f);
		Lines.line(armBaseRegion, build.x, build.y, Tmp.v2.x, Tmp.v2.y, false);
		Lines.stroke(armRegion.height/4f);
		Lines.line(armRegion, Tmp.v2.x + Tmp.v3.x, Tmp.v2.y + Tmp.v3.y, Tmp.v1.x, Tmp.v1.y, false);

		Draw.z(z);
		Draw.reset();
	}

	@Override
	public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list) {
		float z = Draw.z();
		if (layer > 0) Draw.z(layer);

		Tmp.v1.trns(plan.rotation * 90f, (armLength + armExtension)/2f).add(plan.drawx(), plan.drawy());

		Tmp.v2.set(plan.drawx(), plan.drawy()).sub(Tmp.v1).setLength(armLength).add(Tmp.v1);
		Parallax.getParallaxFrom(Tmp.v2, Core.camera.position, armHeight);
		Tmp.v3.set(Tmp.v2).sub(Tmp.v1).setLength(armExtension);

		Lines.stroke(armBaseRegion.height/4f);
		Lines.line(armBaseRegion, plan.drawx(), plan.drawy(), Tmp.v2.x, Tmp.v2.y, false);
		Lines.stroke(armRegion.height/4f);
		Lines.line(armRegion, Tmp.v2.x + Tmp.v3.x, Tmp.v2.y + Tmp.v3.y, Tmp.v1.x, Tmp.v1.y, false);

		Draw.z(z);
		Draw.reset();
	}
}

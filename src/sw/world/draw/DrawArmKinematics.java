package sw.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import sw.annotations.Annotations.*;
import sw.math.*;
import sw.world.interfaces.*;

public class DrawArmKinematics extends BlockDrawer {
	public float layer = -1;

	public float armHeight = 0.5f;
	public float armExtension = 0;
	public float armBaseExtension = 0f;
	public float armLength = 10f;
	public float armBaseLength = 10f;
	public Vec2 armStartingOffset = new Vec2();

	public @Load(value = "@loadBlock.name$-arm", fallBack = "%-arm") TextureRegion armRegion;
	public @Load(value = "@loadBlock.name$-arm-base", fallBack = "%-arm-base") TextureRegion armBaseRegion;
	public @Load(value = "@loadBlock.name$-arm-grabber") TextureRegion armGrabberRegion;

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

		Vec2 pos = cast(build).getArmPos();

		drawArm(build.x, build.y, pos.x - build.x, pos.y - build.y);

		Draw.z(z);
		Draw.reset();
	}

	public void drawArm(float x, float y, float offsetX, float offsetY) {
		InverseKinematics.solve(armLength, armBaseLength, Tmp.v1.set(-offsetX, -offsetY), true, Tmp.v2);

		Parallax.getParallaxFrom(Tmp.v2.add(x, y).add(offsetX, offsetY), Core.camera.position, armHeight);
		Tmp.v1.add(x, y).add(offsetX, offsetY);

		if (armGrabberRegion.found()) Draw.rect(armGrabberRegion, x + offsetX, y + offsetY);

		Lines.stroke(armBaseRegion.height/4f);
		Lines.line(
			armBaseRegion,
			x + Angles.trnsx(Tmp.v2.angleTo(x, y), armBaseExtension),
			y + Angles.trnsy(Tmp.v2.angleTo(x, y), armBaseExtension),
			Tmp.v2.x,
			Tmp.v2.y,
			false
		);
		Lines.stroke(armRegion.height/4f);
		Lines.line(
			armRegion,
			Tmp.v2.x + Angles.trnsx(Tmp.v2.angleTo(x + offsetX, y + offsetY) + 180f, armExtension),
			Tmp.v2.y + Angles.trnsy(Tmp.v2.angleTo(x + offsetX, y + offsetY) + 180f, armExtension),
			x + offsetX,
			y + offsetY,
			false
		);

	}

	@Override
	public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list) {
		float z = Draw.z();
		if (layer > 0) Draw.z(layer);

		Vec2 pos = Tmp.v1.set(armStartingOffset).rotate(plan.rotation * 90f);

		drawArm(plan.drawx(), plan.drawy(), pos.x, pos.y);

		Draw.z(z);
		Draw.reset();
	}
}

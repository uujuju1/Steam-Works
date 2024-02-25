package sw.entities.bullet;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.graphics.*;

public class SoundLaserBulletType extends LaserBulletType {
	public Interp lengthCurve = Interp.circleOut;

	@Override
	public void draw(Bullet b) {
		float baseLen = b.fdata * lengthCurve.apply(b.fin());

		Draw.color(colors[0]);
		Lines.stroke(width * b.fout());
		Draw.alpha(b.fout());
		Lines.lineAngle(b.x(), b.y(), b.rotation(), baseLen, false);
		Tmp.v1.trns(b.rotation(), baseLen);
		Drawf.tri(b.x() + Tmp.v1.x, b.y() + Tmp.v1.y, Lines.getStroke(), width, b.rotation());
		Drawf.tri(b.x(), b.y(), Lines.getStroke(), width/5f * b.fout(), b.rotation() + 180f);
		Draw.reset();
	}
}

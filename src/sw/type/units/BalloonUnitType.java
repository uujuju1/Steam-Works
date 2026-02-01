package sw.type.units;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.world.blocks.payloads.*;
import sw.math.*;
import sw.type.*;

/**
 * Unit type whose body ties to its payload and has parallax scrolling.
 * Do not put weapons on this thing.
 */
public class BalloonUnitType extends SWUnitType {
	public float bodyHeight = 0f;
	public float ropeThickness = 1f;
	public float ropeSpacing = -1f;

	public BalloonUnitType(String name) {
		super(name);
		drawCell = false;
		engineSize = 0;
	}

	@Override
	public void drawBody(Unit unit) {
		applyColor(unit);

		if(unit instanceof UnderwaterMovec){
			Draw.alpha(1f);
			Draw.mixcol(unit.floorOn().mapColor.write(Tmp.c1).mul(0.9f), 1f);
		}
		Draw.alpha(Mathf.clamp(Core.camera.position.dst(unit) / ropeSpacing));

		Draw.rect(
			region,
			Parallax.getParallaxFrom(unit.x, Core.camera.position.x, bodyHeight),
			Parallax.getParallaxFrom(unit.y, Core.camera.position.y, bodyHeight),
			0
		);

		Draw.reset();
	}

	@Override
	public void drawOutline(Unit unit) {
		Draw.reset();

		float x = Parallax.getParallaxFrom(unit.x, Core.camera.position.x, bodyHeight);
		float y = Parallax.getParallaxFrom(unit.y, Core.camera.position.y, bodyHeight);

		if(Core.atlas.isFound(outlineRegion)){
			applyColor(unit);
			applyOutlineColor(unit);
			Draw.alpha(Mathf.clamp(Core.camera.position.dst(unit) / ropeSpacing));
			Draw.rect(outlineRegion, x, y, 0);
		}
		Draw.color(outlineColor);
		Draw.alpha(Mathf.clamp(Core.camera.position.dst(unit) / ropeSpacing));
		if (unit instanceof Payloadc courier && !courier.payloads().isEmpty()) {
			Payload currentPayload = courier.payloads().first();
			for (Point2 offset : Geometry.d4) {
				Lines.stroke(ropeThickness);
				Lines.line(
					unit.x + currentPayload.size() / 2f * offset.x,
					unit.y + currentPayload.size() / 2f * offset.y,
					x + ropeSpacing * offset.x,
					y + ropeSpacing * offset.y,
					false
				);
			}
		}
		Draw.reset();
	}

	@Override
	public void init() {
		super.init();

		if (ropeSpacing < 0) ropeSpacing = hitSize / 2f;
	}
}

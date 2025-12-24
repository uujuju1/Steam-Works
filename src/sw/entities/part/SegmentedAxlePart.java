package sw.entities.part;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.entities.part.*;
import sw.annotations.Annotations.*;
import sw.gen.*;
import sw.graphics.*;

public class SegmentedAxlePart extends DrawPart {
	public String name;
	public String suffix = "";
	public float x, y;
	public float minWidth, maxWidth, height;
	public int pixelWidth = 1, pixelHeight = 1;
	public float rotation;
	public int sides = -1;
	public int[] segmentSides = new int[0];
	public float layer = -1, layerOffset;
	
	public Color lightTint = SWPal.axleLight;
	public Color mediumTint = SWPal.axleMedium;
	public Color darkTint = SWPal.axleDark;
	
	public boolean filled = true;
	
	public PartProgress progress = PartProgress.reload;
	
	public @Load(value = "@name$-main", splits = true, width = "pixelWidth", height = "pixelHeight") TextureRegion[][] mainAxleRegions;
	public @Load("@name$-segment-top") TextureRegion topSegmentRegion;
	public @Load("@name$-segment-right") TextureRegion rightSegmentRegion;
	public @Load("@name$-segment-bottom") TextureRegion bottomSegmentRegion;
	public @Load("@name$-segment-left") TextureRegion leftSegmentRegion;

//	public Axle axle = new Axle("-axle");


//	public boolean outline = true;

//	@Override
//	public void draw(PartParams params) {
//		float z = Draw.z();
//		if (layer > 0) Draw.z(layer);
//
//		Draw.z(Draw.z() + layerOffset);
//
//		float rot = params.rotation + axle.rotation - 90f;
//		float spin = progress.getClamp(params) * axle.spinScl;
//		float dx = params.x + Angles.trnsx(rot, axle.x, axle.y);
//		float dy = params.y + Angles.trnsy(rot, axle.x, axle.y);
//
//		Draws.palette(axle.paletteLight, axle.paletteMedium, axle.paletteDark);
//		if (axle.hasSprites) {
//			Draws.regionCylinder(axle.regions, dx, dy, axle.width, axle.height, spin, rot, axle.circular);
//		} else {
//			Draws.polyCylinder(axle.polySides, dx, dy, axle.width, axle.height, spin, rot, axle.circular);
//		}
//		Draws.palette();
//		if (axle.circular) Draw.rect(axle.shadowRegion, dx, dy, rot);
//
//		Draw.reset();
//		Draw.z(z);
//	}
//
//	@Override
//	public void getOutlines(Seq<TextureRegion> out) {
//		if (axle.hasIcon && outline) out.add(axle.iconRegion);
//	}
//
//	@Override
//	public void load(String name) {
//		if (axle.hasSprites) axle.regions = Core.atlas.find(name + axle.suffix).split(axle.pixelWidth, axle.pixelHeight)[0];
//		if (axle.hasIcon) axle.iconRegion = Core.atlas.find(axle.iconOverride == null ? name + axle.suffix + "-icon" : axle.iconOverride);
//		if (axle.circular) axle.shadowRegion = Core.atlas.find(name + axle.suffix + "-shadow");
//	}
	
	@Override
	public void draw(PartParams params) {
		float z = Draw.z();
		Draw.z((layer > 0 ? layer : z) + layerOffset);
		
		float p = progress.get(params);
		
		Seq<Integer> drawOrder = new Seq<>();
		for(int i : segmentSides) drawOrder.add(i);
		
		float blockRotation = (params.rotation + this.rotation - 90f) / 90f;
		Color light = lightTint;
		Color dark = darkTint;
		if (blockRotation == 1 || blockRotation == 2) {
			lightTint = dark;
			darkTint = light;
		}
		
		drawOrder.each(i -> -Mathf.sin(Mathf.degreesToRadians * (p + 360f/sides * (i + 0.5f))) < 0, i -> drawSegment(params, p + i * 360f/sides));
		if (filled) {
			Draws.palette(lightTint, mediumTint, darkTint);
			Draws.regionCylinder(
				mainAxleRegions[0],
				params.x + Angles.trnsx(params.rotation - 90f, this.x, this.y),
				params.y + Angles.trnsy(params.rotation - 90f, this.x, this.y),
				-height, minWidth, -p, params.rotation + this.rotation - 90, false
			);
			Draws.palette();
		}
		drawOrder.each(i -> -Mathf.sin(Mathf.degreesToRadians * (p + 360f/sides * (i + 0.5f))) >= 0, i -> drawSegment(params, p + i * 360f/sides));
		
		lightTint = light;
		darkTint = dark;
		
		Draw.reset();
		Draw.z(z);
	}
	
	public void drawSegment(PartParams params, float angle) {
		float x = params.x + Angles.trnsx(params.rotation - 90f, this.x, this.y);
		float y = params.y + Angles.trnsy(params.rotation - 90f, this.x, this.y);
		float rotation = params.rotation + this.rotation;
		float radius = maxWidth / 2f;
		float inRadius = minWidth / 2f;
		float arc = 1f / sides;
		
		float mod = Mathf.mod(angle, 360);
		
		float cosMin = Mathf.cos(Mathf.degreesToRadians * mod, 1, 1);
		float cosMiddle = Mathf.cos(Mathf.degreesToRadians * (mod + 180 * arc), 1, 1);
		float cosMax = Mathf.cos(Mathf.degreesToRadians * (mod + 360 * arc), 1, 1);
		
//		float sinMin = Mathf.sin(Mathf.degreesToRadians * (mod), 1, 1);
		float sinMiddle = Mathf.sin(Mathf.degreesToRadians * (mod + 180 * arc), 1, 1);
//		float sinMax = Mathf.sin(Mathf.degreesToRadians * (mod + 360 * arc), 1, 1);
		
		// 1 - top
		// 2 - right
		// 3 - bottom
		// 4 - left
		int[] drawOrder = new int[!filled ? 3 : 4];
		
		if (!filled) drawOrder[sinMiddle < 0 ? 0 : drawOrder.length - 1] = 3;
		drawOrder[sinMiddle < 0 ? drawOrder.length - 1 : 0] = 1;
		
		drawOrder[(!filled || sinMiddle < 0 ? 1 : 0) + (cosMin < 0 ? 1 : 0)] = 2;
		drawOrder[(!filled || sinMiddle < 0 ? 1 : 0) + (cosMin < 0 ? 0 : 1)] = 4;
		
		for(int i : drawOrder) {
			switch (i) {
				case 1 -> {
					float mx = Angles.trnsx(rotation, radius * cosMin);
					mx += Angles.trnsx(rotation, radius * cosMax);
					mx /= 2f;
			
					float my = Angles.trnsy(rotation, radius * cosMin);
					my += Angles.trnsy(rotation, radius * cosMax);
					my /= 2f;
					
					if (cosMiddle > 0f) {
						Tmp.c1.set(mediumTint).lerp(lightTint, Mathf.clamp(cosMiddle));
					} else {
						Tmp.c1.set(darkTint).lerp(mediumTint, Mathf.clamp(cosMiddle + 1f));
					}
					Draw.mixcol(Tmp.c1, Tmp.c1.a);
					
					Draw.rect(
						topSegmentRegion,
						x + mx,
						y + my,
						2 * radius * Mathf.sin(Mathf.degreesToRadians * (180 * arc)) * sinMiddle,
						height,
						rotation
					);
				}
				case 2 -> {
					if (sinMiddle > 0f) {
						Tmp.c1.set(mediumTint).lerp(darkTint, Mathf.clamp(sinMiddle));
					} else {
						Tmp.c1.set(lightTint).lerp(mediumTint, Mathf.clamp(sinMiddle + 1f));
					}
					Draw.mixcol(Tmp.c1, Tmp.c1.a);
					Draw.rect(
						rightSegmentRegion,
						x + Angles.trnsx(rotation, (radius/2 + inRadius/2) * cosMax),
						y + Angles.trnsy(rotation, (radius/2 + inRadius/2) * cosMax),
						(radius - inRadius) * cosMax,
						height,
						rotation
					);
				}
				case 3 -> {
					float mx = Angles.trnsx(rotation, inRadius * cosMin);
					mx += Angles.trnsx(rotation, inRadius * cosMax);
					mx /= 2f;
			
					float my = Angles.trnsy(rotation, inRadius * cosMin);
					my += Angles.trnsy(rotation, inRadius * cosMax);
					my /= 2f;
					
					if (cosMiddle > 0f) {
						Tmp.c1.set(mediumTint).lerp(darkTint, Mathf.clamp(cosMiddle));
					} else {
						Tmp.c1.set(lightTint).lerp(mediumTint, Mathf.clamp(cosMiddle + 1f));
					}
					Draw.mixcol(Tmp.c1, Tmp.c1.a);
					Draw.rect(
						bottomSegmentRegion,
						x + mx,
						y + my,
						2 * inRadius * Mathf.sin(Mathf.degreesToRadians * (180 * arc)) * sinMiddle,
						height,
						rotation
					);
				}
				case 4 -> {
					if (sinMiddle > 0f) {
						Tmp.c1.set(mediumTint).lerp(lightTint, Mathf.clamp(sinMiddle));
					} else {
						Tmp.c1.set(darkTint).lerp(mediumTint, Mathf.clamp(sinMiddle + 1f));
					}
					Draw.mixcol(Tmp.c1, Tmp.c1.a);
					Draw.rect(
						leftSegmentRegion,
						x + Angles.trnsx(rotation, (radius/2 + inRadius/2) * cosMin),
						y + Angles.trnsy(rotation, (radius/2 + inRadius/2) * cosMin),
						(radius - inRadius) * cosMin,
						height,
						rotation
					);
				}
				default -> {}
			}
		}
		
//		a.forEach(cos => {
//			Draw.color(cos == cosMin ? Color.red : Color.green)
//			Draw.z(z + Mathf.sign(!(cos == cosMin ^ cosMin > 0)))
//			Draw.rect(
//				regions[cos == cosMin ? 0 : 2],
//				x + Angles.trnsx(rotation, (radius/2 + inRadius/2) * cos),
//				y + Angles.trnsy(rotation, (radius/2 + inRadius/2) * cos),
//				(radius - inRadius) * cos,
//				height,
//				rotation
//			)
//		})
//
//		let mx = Angles.trnsx(rotation, radius * cosMin)
//		mx += Angles.trnsx(rotation, radius * cosMax)
//		mx /= 2
//
//		let my = Angles.trnsy(rotation, radius * cosMin)
//		my += Angles.trnsy(rotation, radius * cosMax)
//		my /= 2
//
//		Draw.color()
//		Draw.z(z + 2 * Mathf.sign(sinMiddle < 0))
//		Draw.rect(
//			regions[1],
//			x + mx,
//			y + my,
//			2 * radius * Mathf.sin(Mathf.degreesToRadians * (180 * arc)) * sinMiddle,
//			height,
//			rotation
//		)
//
//		mx = Angles.trnsx(rotation, inRadius * cosMin)
//		mx += Angles.trnsx(rotation, inRadius * cosMax)
//		mx /= 2
//
//		my = Angles.trnsy(rotation, inRadius * cosMin)
//		my += Angles.trnsy(rotation, inRadius * cosMax)
//		my /= 2
//
//		Draw.color(Color.blue)
//		Draw.z(z + 2 * Mathf.sign(sinMiddle > 0))
//		Draw.rect(
//			regions[3],
//			x + mx,
//			y + my,
//			2 * inRadius * Mathf.sin(Mathf.degreesToRadians * (180 * arc)) * sinMiddle,
//			height,
//			rotation
//		)
	}
	
	@Override
	public void load(String name) {
		if (this.name == null) this.name = name + suffix;
		SWContentRegionRegistry.load(this);
		sides = mainAxleRegions[0].length;
	}
}

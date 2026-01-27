package sw.tools.processors;

import arc.graphics.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import arc.util.noise.*;
import mindustry.*;
import sw.tools.GeneratedAtlas.*;
import sw.tools.*;
import sw.type.*;

public class UnitProcessor implements SpriteProcessor {

	@Override
	public void process() {
		Vars.content.units().select(u -> u.minfo != null && u.minfo.mod == Tools.mod && u instanceof SWUnitType).map(u -> (SWUnitType) u).each(unit -> {
			try {
				unit.init();
				unit.load();
				unit.loadIcon();

				if (!unit.outlineRegion.found()) {
					Pixmap reg = outline(Tools.atlas.castRegion(unit.region).pixmap(), unit.outlineRadius, unit.outlineColor);

					if (unit.treadRegion.found() && unit.treadsCutOutline) {
						reg.each((x, y) -> {
							if (Tools.atlas.castRegion(unit.treadRegion).pixmap().in(x, y) && Tools.atlas.castRegion(unit.treadRegion).pixmap().getA(x, y) > 0) {
								reg.set(x, y, Color.clear);
							}
						});
					}

					unit.outlineRegion = new GeneratedRegion(
						unit.name + "-outline",
						reg,
						Tools.atlas.castRegion(unit.region).file.sibling(unit.name.substring("sw-".length()) + "-outline.png")
					).save(true);
				}

				Seq.with(unit.treadRegion, unit.legRegion, unit.legBaseRegion, unit.jointRegion, unit.baseJointRegion, unit.footRegion).each(region -> {
					if (region.found()) {
						Tools.atlas.castRegion(region).pixmap().draw(
							outline(Tools.atlas.castRegion(region).pixmap(), unit.outlineRadius, unit.outlineColor, true)
						);
						Tools.atlas.castRegion(region).save(false);
					}
				});

				processWeapons(unit);
				processRotors(unit);
				processWreck(unit);
				
				processFull(unit);

			} catch (Exception e) {
				Log.err(e);
				Log.warn("Error processing sprites for unit @. Skipping", unit);
			}
		});
	}
	
	public void processFull(SWUnitType unit) {
		if (unit.fullIcon == unit.region) {
			Seq<GeneratedRegion> fullRegions = new Seq<>();
			
			unit.rotors.each(rotor -> rotor.layerOffset < 0 && (rotor.mirrored || !rotor.flipped), rotor -> {
				fullRegions.add(new GeneratedRegion(
					"uwu",
					grow(
						Tools.atlas.castRegion(rotor.region).pixmap(),
						(int) Mathf.maxZero((rotor.x * 4 * 2)),
						(int) Mathf.maxZero((rotor.x * 4 * -2)),
						(int) Mathf.maxZero((rotor.y * 4 * -2)),
						(int) Mathf.maxZero((rotor.y * 4 * 2))
					),
					null
				));
				if (rotor.topRegion.found()) fullRegions.add(new GeneratedRegion(
					"uwu",
					grow(
						Tools.atlas.castRegion(rotor.topRegion).pixmap(),
						(int) Mathf.maxZero((rotor.x * 4 * 2)),
						(int) Mathf.maxZero((rotor.x * 4 * -2)),
						(int) Mathf.maxZero((rotor.y * 4 * -2)),
						(int) Mathf.maxZero((rotor.y * 4 * 2))
					),
					null
				));
			});
			
			if (unit.treadRegion.found()) fullRegions.add(Tools.atlas.castRegion(unit.treadRegion));
			
			unit.weapons.each(w -> w.layerOffset < 0 && w.region.found(), w -> {
				Pixmap pix = Tools.atlas.find(w.name + "-preview").pixmap();
				if (!w.flipSprite) pix = pix.flipX();
				pix = grow(
					pix,
					(int) Mathf.maxZero(w.x * 4 * 2),
					(int) Mathf.maxZero(w.x * 4 * -2),
					(int) Mathf.maxZero(w.y * 4 * -2),
					(int) Mathf.maxZero(w.y * 4 * 2)
				);
				fullRegions.add(new GeneratedRegion("uwu", pix, null));
			});
			
			fullRegions.add(
				Tools.atlas.castRegion(unit.outlineRegion),
				Tools.atlas.castRegion(unit.region)
			);
			
			if (unit.cellRegion.found() && unit.drawCell) {
				fullRegions.add(tintCell(Tools.atlas.castRegion(unit.cellRegion)));
			}
			
			unit.weapons.each(w -> w.layerOffset >= 0 && w.region.found(), w -> {
				Pixmap pix = Tools.atlas.find(w.name + "-preview").pixmap();
				if (!w.flipSprite) pix = pix.flipX();
				pix = grow(
					pix,
					(int) Mathf.maxZero(w.x * 4 * 2),
					(int) Mathf.maxZero(w.x * 4 * -2),
					(int) Mathf.maxZero(w.y * 4 * -2),
					(int) Mathf.maxZero(w.y * 4 * 2)
				);
				fullRegions.add(new GeneratedRegion("uwu", pix, null));
			});
			
			unit.rotors.each(rotor -> rotor.layerOffset >= 0 && (rotor.mirrored || !rotor.flipped), rotor -> {
				fullRegions.add(new GeneratedRegion(
					"uwu",
					grow(
						Tools.atlas.castRegion(rotor.region).pixmap(),
						(int) Mathf.maxZero((rotor.x * 4 * 2)),
						(int) Mathf.maxZero((rotor.x * 4 * -2)),
						(int) Mathf.maxZero((rotor.y * 4 * -2)),
						(int) Mathf.maxZero((rotor.y * 4 * 2))
					),
					null
				));
				if (rotor.topRegion.found()) fullRegions.add(new GeneratedRegion(
					"uwu",
					grow(
						Tools.atlas.castRegion(rotor.topRegion).pixmap(),
						(int) Mathf.maxZero((rotor.x * 4 * 2)),
						(int) Mathf.maxZero((rotor.x * 4 * -2)),
						(int) Mathf.maxZero((rotor.y * 4 * -2)),
						(int) Mathf.maxZero((rotor.y * 4 * 2))
					),
					null
				));
			});
			
			
			unit.fullIcon = stackCentered(unit.name + "-full", fullRegions).save(true);
		}
	}
	
	public void processRotors(SWUnitType unit) {
		unit.rotors.each(rotor -> !rotor.flipped, rotor -> {
			if (!rotor.blurRegion.found()) {
				rotor.blurRegion = new GeneratedRegion(
					(rotor.isSuffix ? unit.name + rotor.name : rotor.name) + "-blur",
					radialSprite(outline(Tools.atlas.castRegion(rotor.region).pixmap(), unit.outlineRadius, unit.outlineColor, true), rotor.blades),
					Tools.atlas.castRegion(rotor.region).file.sibling(
						((rotor.isSuffix ? unit.name + rotor.name : rotor.name)).substring("sw-".length()) + "-blur.png"
					)
				).save(true);
			}
			
			if (!rotor.shineRegion.found()) {
				rotor.shineRegion = new GeneratedRegion(
					(rotor.isSuffix ? unit.name + rotor.name : rotor.name) + "-shine",
					shine(Tools.atlas.castRegion(rotor.blurRegion), rotor.blades),
					Tools.atlas.castRegion(rotor.region).file.sibling(
						((rotor.isSuffix ? unit.name + rotor.name : rotor.name)).substring("sw-".length()) + "-shine.png"
					)
				).save(true);
			}
			
			Pixmap full = new Pixmap(rotor.region.width, rotor.region.height);
			for (int i = 0; i < rotor.blades; i++) {
				float deg = 360f / rotor.blades * i;
				Pixmap copy = Tools.atlas.castRegion(rotor.region).pixmap().copy();
				full.draw(rotate(copy, deg + rotor.rotation), 0, 0, true);
			}
			Tools.atlas.castRegion(rotor.region).pixmap().draw(outline(full, unit.outlineRadius, unit.outlineColor, true));
			Tools.atlas.castRegion(rotor.region).save(true);
			
			if (rotor.topRegion.found()) {
				Pixmap outlined = outline(Tools.atlas.castRegion(rotor.topRegion).pixmap(), unit.outlineRadius, unit.outlineColor, true);
				Tools.atlas.castRegion(rotor.topRegion).pixmap().draw(outlined);
				Tools.atlas.castRegion(rotor.topRegion).save(false);
			}
		});
	}
	
	public void processWeapons(SWUnitType unit) {
		unit.weapons.each(weapon -> weapon.region.found(), weapon -> {
			if (!weapon.outlineRegion.found()) {
				weapon.outlineRegion = new GeneratedRegion(
					weapon.name + "-outline",
					outline(Tools.atlas.castRegion(weapon.region).pixmap(), unit.outlineRadius, unit.outlineColor),
					Tools.atlas.castRegion(weapon.region).file.sibling(weapon.name.substring("sw-".length()) + "-outline.png")
				).save(true);
			}
			
			if (!Tools.atlas.find(weapon.name + "-preview").found()) {
				Seq<GeneratedRegion> previewRegions = Seq.with(
					Tools.atlas.castRegion(weapon.outlineRegion),
					Tools.atlas.castRegion(weapon.region)
				);
				
				if (weapon.cellRegion.found() && unit.drawCell) {
					previewRegions.add(tintCell(Tools.atlas.castRegion(weapon.cellRegion)));
				}
				
				stack(weapon.name + "-preview", previewRegions).save(true);
			}
		});
	}

	public void processWreck(SWUnitType unit) {
		if (unit.wrecks > 0) {
			VoronoiNoise noise = new VoronoiNoise(unit.id, true);
			for(int i = 0; i < unit.wrecks; i++) {
				Pixmap wreck = Tools.atlas.castRegion(unit.fullIcon).pixmap().copy();
				int finalI = i;
				wreck.each((x, y) -> {
					float angle = Mathf.mod(
						(float) (
							Angles.angle(x, y, wreck.width/2f, wreck.height/2f) +
								noise.noise(x, y, unit.wrecks/90f) * 360f/unit.wrecks +
								noise.noise(x, y, unit.wrecks/45f) * 180f/unit.wrecks
						),
						360f
					);
					if (
						angle < 360f/unit.wrecks * finalI ||
							angle > 360f/unit.wrecks * (finalI + 1)
					) wreck.set(x, y, Color.clear);
				});
				unit.wreckRegions[i] = new GeneratedRegion(
					unit.name + "-wreck-" + (i + 1),
					wreck,
					Tools.atlas.castRegion(unit.fullIcon).file.sibling(unit.name.substring("sw-".length()) + "-wreck-" + (i + 1) + ".png")
				).save(true);
			}
		}
	}
	
	
	// makes a circular sprite based on a center line in a region, turns less opaque based on the amount of rotor blades and the angle.
	public Pixmap radialSprite(Pixmap region, int blades) {
		Pixmap out = new Pixmap(region.width, region.height);

		out.each((x, y) -> {
			float dst = Tmp.v1.set(x, y).dst(
				out.width/2 - (x > out.width/2 ? 1 : 0),
				out.height/2 - (y > out.height/2 ? 1 : 0)
			);
			float angle = Mathf.slope(
				(
					Tmp.v1.angleTo(
						out.width/2f,
						out.height/2f
					) % (360f/blades)
				)/(360f/blades)
			);
			Color color = new Color(region.get(
				out.width/2 - (x > out.width/2 ? 1 : 0),
				out.height/2 - ((int) dst)
			));
			color.a *= angle/2f + 0.5f;
			out.set(x, y, color);
		});

		return out;
	}

	public Pixmap shine(GeneratedRegion base, int blades) {
		Pixmap out = base.pixmap().copy();

		out.each((x, y) -> {
			if (out.get(x, y) != 0) {
				if (Mathf.mod(
					Mathf.angle(x - out.width / 2f, y - out.height / 2f) + Mathf.randomSeed(
						(long) Mathf.dst(x - out.width / 2f, y - out.height / 2f) / 4,
						-360f / blades / 8f,
						360f / blades / 8f
					), 180
				) < 360f / blades / 2f) {
					out.set(x, y, Color.white);
				} else out.set(x, y, Color.clear);
			}
		});

		return out;
	}

	// creates a region using other regions drawn on top of each other
	public GeneratedRegion stack(String name, Seq<GeneratedRegion> regions) {
		if (!regions.contains(r -> r.file != null)) throw new IllegalArgumentException("please provide atleast one GeneratedRegion with a file");
		Pixmap out = null;

		for(GeneratedRegion region : regions) {
			if (out == null) {
				out = region.pixmap().copy();
			} else {
				out.draw(region.pixmap(), 0, 0, true);
			}
		}

		return new GeneratedRegion(name, out, regions.find(r -> r.file != null).file.sibling(name.substring("sw-".length()) + ".png"));
	}

	// creates a region using other regions drawn on top of each other, grows to accommodate every region in the middle
	public GeneratedRegion stackCentered(String name, Seq<GeneratedRegion> regions) {
		if (!regions.contains(r -> r.file != null)) throw new IllegalArgumentException("please provide atleast one GeneratedRegion with a file");
		Pixmap out = new Pixmap(
			regions.max(r -> r.width).width,
			regions.max(r -> r.height).height
		);

		for(GeneratedRegion region : regions) {
			out.draw(region.pixmap(), (out.width - region.width)/2, (out.height - region.height)/2, true);
		}

		return new GeneratedRegion(name, out, regions.find(r -> r.file != null).file.sibling(name.substring("sw-".length()) + ".png"));
	}

	// tints the cell with the proper color
	public GeneratedRegion tintCell(GeneratedRegion region) {
		Pixmap out = region.pixmap().copy();

		out.each((x, y) -> {
			if (out.get(x, y) == Color.whiteRgba) {
				out.set(x, y, Color.valueOf("FFA664"));
			}
			if (out.get(x, y) == Color.valueOf("DCC6C6").rgba()) {
				out.set(x, y, Color.valueOf("D06B53"));
			}
		});

		return new GeneratedRegion(region.name, out, null);
	}
}

package sw.world.blocks.power;

import arc.*;
import arc.audio.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import sw.*;
import sw.util.*;
import sw.world.graph.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

// TODO needs fix
public class ShaftTransmission extends Block {
	public SpinConfig spinConfig = new SpinConfig();

	public float breakingDamage = 1f;

	public Sound breakingSound = Sounds.breaks;
	public float breakingSoundPitchMin = 0.25f;
	public float breakingSoundPitchMax = 0.5f;
	public float breakingSoundVolume = 1f;
	public Effect breakingEffect = Fx.colorSpark;
	public Color breakingEffectColor = Pal.accent;

	public int[] bigSectionID = new int[]{2, 3};

	public int barEndThickness = 7, barSmallThickness = 4, barMiddleThickness = 9, barBigThickness = 10;

	public TextureRegion topRegion, bottomRegion, barShadowRegion1, barShadowRegion2;
	public TextureRegion[] barRegionsEnd, barRegionsSmall, barRegionsMiddle, barRegionsBig;

	public ShaftTransmission(String name) {
		super(name);
		update = rotate = true;
	}

	@Override
	public void load() {
		super.load();
		topRegion = Core.atlas.find(name + "-top");
		bottomRegion = Core.atlas.find(name + "-bottom");
		barShadowRegion1 = Core.atlas.find(name + "-shaft-shadow-1");
		barShadowRegion2 = Core.atlas.find(name + "-shaft-shadow-2");

		barRegionsEnd = Core.atlas.find(name + "-shaft-end").split(16, barEndThickness)[0];
		barRegionsSmall = Core.atlas.find(name + "-shaft-small").split(32, barSmallThickness)[0];
		barRegionsMiddle = Core.atlas.find(name + "-shaft-middle").split(16, barMiddleThickness)[0];
		barRegionsBig = Core.atlas.find(name + "-shaft-big").split(32, barBigThickness)[0];
	}

	public class ShaftTransmissionBuild extends Building implements HasSpin {
		public SpinModule spin = new SpinModule();

		public SpinSection bigSection, smallSection;

		public boolean breaking;

		@Override public boolean connectTo(HasSpin other) {
			return HasSpin.super.connectTo(other) && !(other instanceof ShaftTransmissionBuild);
		}

		@Override
		public void draw() {
			float rot = (rotation * 90f + 90f) % 180f - 90f;
			float spin = (spinGraph().rotation * (smallSection == null ? (bigSection == null ? 1f : bigSection.ratio * 2) : smallSection.ratio));
			Draw.rect(bottomRegion, x, y);
			if (breaking) spin += Mathf.random(-15f, 15f);

			SWDraw.rotatingRects(barRegionsEnd,
				x + Angles.trnsx(rotation * 90f, -6f, -4f),
				y + Angles.trnsy(rotation * 90f, -6f, -4f),
				4f, 3.5f, rot, spin % 360f
			);
			SWDraw.rotatingRects(barRegionsEnd,
				x + Angles.trnsx(rotation * 90f, -6f, 4f),
				y + Angles.trnsy(rotation * 90f, -6f, 4f),
				4f, 3.5f, rot, spin/2f % 360f
			);
			SWDraw.rotatingRects(barRegionsEnd,
				x + Angles.trnsx(rotation * 90f, 6f, -4f),
				y + Angles.trnsy(rotation * 90f, 6f, -4f),
				4f, 3.5f, rot, spin % 360f
			);
			SWDraw.rotatingRects(barRegionsEnd,
				x + Angles.trnsx(rotation * 90f, 6f, 4f),
				y + Angles.trnsy(rotation * 90f, 6f, 4f),
				4f, 3.5f, rot, spin/2f % 360f
			);

			SWDraw.rotatingRects(barRegionsSmall,
				x + Angles.trnsx(rotation * 90f, 0f, -4f),
				y + Angles.trnsy(rotation * 90f, 0f, -4f),
				8f, 2f, rot, spin % 360f
			);
			SWDraw.rotatingRects(barRegionsMiddle,
				x + Angles.trnsx(rotation * 90f, 0f, -0.75f),
				y + Angles.trnsy(rotation * 90f, 0f, -0.75f),
				4f, 4.5f, rot, -spin/2 % 360f
			);
			SWDraw.rotatingRects(barRegionsBig,
				x + Angles.trnsx(rotation * 90f, 0f, 4f),
				y + Angles.trnsy(rotation * 90f, 0f, 4f),
				8f, 5f, rot, spin/2f % 360f
			);

			Draw.rect(
				(rotation > 0 && rotation < 3) ? barShadowRegion2 : barShadowRegion1,
				x, y, rotation % 2 * -90
			);

			Draw.rect(topRegion, x, y, rot);
		}

		@Override
		public HasSpin getSpinSectionDestination(HasSpin from) {
			Point2 p = new Point2(3, 0).rotate((relativeTo((Building) from) + 2) % 4);
			return from.nearby(p.x, p.y) instanceof HasSpin a ? a : null;
		}

		@Override public void onGraphUpdate() {}

		@Override
		public void onProximityUpdate() {
			super.onProximityUpdate();

			new SpinGraph().mergeFlood(this);
		}

		@Override
		public void onProximityRemoved() {
			super.onProximityRemoved();
			spinGraph().remove(this, true);
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);
			spin.read(read);
		}

		@Override public SpinModule spin() {
			return spin;
		}
		@Override public SpinConfig spinConfig() {
			return spinConfig;
		}

		@Override
		public void updateTile() {
			bigSection = smallSection = null;
			boolean found = false;
			for(int p = 0; p < connectionPoints(rotation).size; p++) {
				Point2 point = connectionPoints(rotation).get(p);
				if (nearby(point.x, point.y) instanceof HasSpin other && HasSpin.connects(this, other)) {
					for (int i : bigSectionID) {
						if (p == i) {
							found = true;
							break;
						}
					}
					if (found) {
						bigSection = other.spinSection();
					} else{
						smallSection = other.spinSection();
					}
				}
			}

			float ratio = (smallSection == null) ? 0.5f : smallSection.ratio / 2f;

			if (
				!(bigSection != null && bigSection.ratio < SWVars.minRatio) &&
				!(smallSection != null && smallSection.ratio > SWVars.maxRatio) &&
					!(bigSection != null && smallSection != null && bigSection == smallSection)
			) {
				breaking = false;
				if (bigSection != null) {
					if ((!bigSection.set || bigSection.ratio != 1f) && bigSection.ratio != ratio) {
						if (smallSection != null) {
							smallSection.ratio = bigSection.ratio * 2;
							smallSection.set = true;
						}
					} else {
						bigSection.ratio = ratio;
						bigSection.set = true;
					}
				}
			} else {
				breaking = true;
				HasSpin target = spinGraph().builds.random();
				target.damage(breakingDamage * Time.delta);
				breakingEffect.at(target.x(), target.y(), Mathf.random(360f), breakingEffectColor);
				breakingSound.at(target.x(), target.y(), Mathf.random(breakingSoundPitchMin, breakingSoundPitchMax), breakingSoundVolume);
			}
		}

		@Override
		public void write(Writes write) {
			super.write(write);
			spin.write(write);
		}
	}
}

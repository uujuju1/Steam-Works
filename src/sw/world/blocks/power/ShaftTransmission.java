package sw.world.blocks.power;

import arc.audio.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import sw.*;
import sw.world.graph.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

// TODO needs fix
public class ShaftTransmission extends Block {
	public SpinConfig spinConfig = new SpinConfig();

	public DrawBlock drawer = new DrawDefault();

	public float breakingDamage = 1f;
	public Sound breakingSound = Sounds.breaks;
	public float breakingSoundPitchMin = 0.25f;
	public float breakingSoundPitchMax = 0.5f;
	public float breakingSoundVolume = 1f;
	public Effect breakingEffect = Fx.colorSpark;
	public Color breakingEffectColor = Pal.accent;

	public int[] bigSectionID = new int[]{2, 3};

	public ShaftTransmission(String name) {
		super(name);
		update = rotate = true;
	}

	@Override
	public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list){
		drawer.drawPlan(this, plan, list);
	}

	@Override
	public void getRegionsToOutline(Seq<TextureRegion> out){
		drawer.getRegionsToOutline(this, out);
	}

	@Override
	public TextureRegion[] icons(){
		return drawer.finalIcons(this);
	}

	@Override
	public void load() {
		super.load();
		drawer.load(this);
	}

	public class ShaftTransmissionBuild extends Building implements HasSpin {
		public SpinModule spin = new SpinModule();

		public SpinSection bigSection, smallSection;

		public boolean breaking;

		@Override public boolean connectTo(HasSpin other) {
			return HasSpin.super.connectTo(other) && !(other instanceof ShaftTransmissionBuild);
		}

		@Override public void draw() {
			drawer.draw(this);
		}
		@Override public void drawLight() {
			drawer.drawLight(this);
		}

		@Override
		public HasSpin getSpinSectionDestination(HasSpin from) {
			Point2 p = new Point2(3, 0).rotate((relativeTo((Building) from) + 2) % 4);
			return (from.nearby(p.x, p.y) instanceof HasSpin a && HasSpin.connects(this, a)) ? a : null;
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
		public float totalProgress() {
			return spinGraph().rotation * (smallSection == null ? (bigSection == null ? 0f : bigSection.ratio) : smallSection.ratio);
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

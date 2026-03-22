package sw.world.blocks.defense;

import arc.*;
import arc.func.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;
import sw.world.*;

public class PartialRegenProjector extends GenericSpinBlock {
	public DrawBlock drawer = new DrawDefault();

	public float radius = 80f;

	public float warmupSpeed = 0.014f;

	public int maxTargets = 5;
	public float regenAmount = 0f;
	public float regenPercentage = 0f;
	public float regenTime = 60f;

	public Effect healEffect = Fx.none;

	public PartialRegenProjector(String name) {
		super(name);
		update = true;
	}

	@Override
	public void drawOverlay(float x, float y, int rotation) {
		Drawf.dashCircle(x, y, radius, Pal.accent);
	}

	@Override
	public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
		super.drawPlanRegion(plan, list);
		drawer.drawPlan(this, plan, list);
	}

	@Override
	public void getRegionsToOutline(Seq<TextureRegion> out) {
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

	@Override
	public void setStats() {
		super.setStats();

		stats.add(Stat.healing, Core.bundle.format("stat.sw-regen-heal.format", Strings.autoFixed(regenPercentage * 100f, 2), Strings.autoFixed(regenAmount, 2)));
	}

	public class PartialRegenProjectorBuild extends GenericSpinBuild {
		public float progress, warmup;

		private final Seq<Building> possibleHeals = new Seq<>();

		@Override public void draw() {
			drawer.draw(this);
		}
		@Override public void drawLight() {
			drawer.drawLight(this);
		}

		public boolean healBlocks() {
			if (team.data().buildingTree == null) {
				Log.err("how??, team's buildingTree is null");
				return false;
			}
			possibleHeals.clear();
			Cons<Building> cons = building -> {
				if (building.dst(this) < radius && building.health < building.maxHealth) {
					possibleHeals.add(building);
				}
			};
			team.data().buildingTree.intersect(x - radius, y - radius, radius * 2, radius * 8, cons);

			possibleHeals.sort(b -> b.health);

			for (int i = 0; i < Math.min(maxTargets, possibleHeals.size); i++) {
				Building healed = possibleHeals.get(i);
				float heal = healed.maxHealth * regenPercentage + regenAmount;
				healed.heal(heal);
				healEffect.at(x, y, rotdeg(), healed);
			}

			return !possibleHeals.isEmpty();
		}

		@Override public float progress() {
			return progress;
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);

			progress = read.f();
			warmup = read.f();
		}

		@Override
		public void updateTile() {
			boolean healed = true;
			if (efficiency > 0) {
				progress += getProgressIncrease(regenTime);

				if (progress >= 1) {
					if (healBlocks()) {
						consume();
						progress %= 1f;
					} else {
						healed = false;
					}
				}
			} else {
				healed = false;
			}

			warmup = Mathf.approachDelta(warmup, healed ? 1 : 0, warmupSpeed);
		}

		@Override public float warmup() {
			return warmup;
		}

		@Override
		public void write(Writes write) {
			super.write(write);

			write.f(progress);
			write.f(warmup);
		}
	}
}

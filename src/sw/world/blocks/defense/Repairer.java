package sw.world.blocks.defense;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.entities.units.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.world.draw.*;
import sw.world.*;

public class Repairer extends GenericSpinBlock {
	public float baseRepairTime = 300f;
	public float intervalRepairTime = 60f;

	public Color mixColor = Color.valueOf("FFFFFF7F");
	public float alpha = 0.5f;
	public float alphaScl = 1;
	public float alphaMag = 0.1f;

	public DrawBlock drawer = new DrawDefault();

	public Repairer(String name) {
		super(name);
		solid = true;
		update = true;
		sync = true;

		updateClipRadius(1000 * 8f);
	}

	@Override
	public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
		if (spinConfig != null) spinConfig.drawPlace(this, plan.x, plan.y, plan.rotation, true);
		drawer.drawPlan(this, plan, list);
	}

	@Override
	public void drawPlanConfigTop(BuildPlan plan, Eachable<BuildPlan> list) {
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

	public class RepairerBuild extends GenericSpinBuild {
		public float interval;
		public Teams.BlockPlan current;

		@Override
		public void draw() {
			drawer.draw(this);

			Draw.blend(Blending.additive);
			Draw.mixcol(mixColor, mixColor.a);
			Core.camera.bounds(Tmp.r1).grow(16 * 8f);
			team.data().plans.each(plan -> {
				if (Tmp.r1.contains(Tmp.r2.setCentered(plan.x * Vars.tilesize + plan.block.offset, plan.y * Vars.tilesize + plan.block.offset, plan.block.size * Vars.tilesize))) {
					Tmp.r2.getCenter(Tmp.v1);
					Draw.alpha(Mathf.clamp(alpha + Mathf.sin(Mathf.degreesToRadians * (Time.time + Mathf.randomSeed(plan.x + plan.y, 360f)), alphaScl, alphaMag)));
					Draw.rect(plan.block.fullIcon, Tmp.v1.x, Tmp.v1.y);
				}
			});
			Draw.blend();
		}
		@Override public void drawLight() {
			drawer.drawLight(this);
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);

			interval = read.f();
		}

		@Override
		public void updateTile() {
			if (!team.data().plans.isEmpty() && current != team.data().plans.first()) {
				current = team.data().plans.first();
				interval = baseRepairTime;
			}
			if (efficiency > 0 && !team.data().plans.isEmpty()) {
				interval -= edelta();

				if (interval <= 0) {
					Call.constructFinish(Vars.world.tile(current.x, current.y), current.block, null, (byte) current.rotation, team, current.config);
					interval = intervalRepairTime;
					current = team.data().plans.isEmpty() ? null : team.data().plans.first();
				}
			} else {
				interval = baseRepairTime;
			}
		}

		@Override
		public float warmup() {
			return efficiency;
		}

		@Override
		public void write(Writes write) {
			super.write(write);

			write.f(interval);
		}
	}
}

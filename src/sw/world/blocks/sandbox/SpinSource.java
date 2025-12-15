package sw.world.blocks.sandbox;

import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.meta.*;
import sw.world.blocks.power.*;
import sw.world.meta.*;

public class SpinSource extends AxleBlock {
	public SpinSource(String name) {
		super(name);
		rotate = false;
		saveConfig = true;
		configurable = true;
		envEnabled = Env.any;
		buildVisibility = BuildVisibility.sandboxOnly;
		category = Category.power;
		group = BlockGroup.power;

		config(SpinSourceEntry.class, (SpinSourceBuild build, SpinSourceEntry cons) -> {
			build.targetSpeed = cons.targetSpeed;
			build.force = cons.force;
		});
		configClear((SpinSourceBuild build) -> {
			build.targetSpeed = build.force = 0;
		});
	}

	public class SpinSourceBuild extends AxleBlockBuild {
		public float targetSpeed, force;

		@Override
		public SpinSourceEntry config() {
			return new SpinSourceEntry(targetSpeed, force);
		}

		@Override
		public void buildConfiguration(Table table) {
			table.table(Styles.black6, cont -> {
				cont.add(SWStat.spinOutput.localized()).padRight(5f);
				cont.field(Float.toString(targetSpeed), TextField.TextFieldFilter.floatsOnly, s -> {
					configure(new SpinSourceEntry(Strings.parseFloat(s, 0), force));
				});
				cont.add(SWStat.spinMinute.localized()).padLeft(5f);
				cont.row();
				cont.add(SWStat.spinOutputForce.localized()).padRight(5f);
				cont.field(Float.toString(force), TextField.TextFieldFilter.floatsOnly, s -> {
					configure(new SpinSourceEntry(targetSpeed, Strings.parseFloat(s, 0)));
				});
				cont.add(SWStat.spinMinuteSecond.localized()).padLeft(5f);
			}).margin(10f);
		}

//		@Override public float getForce() {
//			float diff = Mathf.maxZero(targetSpeed/10f - getSpeed());
//			return Math.min(diff, force/600f * getRatio());
//		}
		@Override public float getForce() {
			return force/600f * getRatio();
		}
		@Override public float getTargetSpeed() {
			return targetSpeed/10f * getRatio();
		}
		
		@Override
		public boolean outputsSpin() {
			return true;
		}

		@Override
		public void write(Writes write) {
			super.write(write);

			write.f(targetSpeed);
			write.f(force);
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);

			targetSpeed = read.f();
			force = read.f();
		}
	}

	public static class SpinSourceEntry {
		public float targetSpeed;
		public float force;

		public SpinSourceEntry(float targetSpeed, float force) {
			this.targetSpeed = targetSpeed;
			this.force = force;
		}
	}
}

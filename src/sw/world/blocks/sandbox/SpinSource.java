package sw.world.blocks.sandbox;

import arc.graphics.*;
import arc.scene.ui.layout.*;
import arc.util.io.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.meta.*;
import sw.ui.*;
import sw.world.blocks.power.*;

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

		config(float[].class, (SpinSourceBuild build, float[] values) -> {
			build.targetSpeed = values[0];
			build.force = values[1];
		});
		configClear((SpinSourceBuild build) -> build.targetSpeed = build.force = 0);
	}

	public class SpinSourceBuild extends AxleBlockBuild {
		public float targetSpeed, force;

		@Override
		public float[] config() {
			return new float[]{targetSpeed, force};
		}

		@Override
		public void buildConfiguration(Table cont) {
			cont.table(Styles.black6, table -> {
				SWTables.buildFloatSlider(table, "@ui.sw-max-speed", value -> configure(new float[]{value / 10f, force}), () -> targetSpeed * 10f);
				table.image(Tex.whiteui).color(Color.gray).padTop(10f).padBottom(10f).height(4f).growX().row();
				SWTables.buildFloatSlider(table, "@stat.sw-spin-output-force", value -> configure(new float[]{targetSpeed, value / 600f}), () -> force * 600f);
			}).margin(10f);
		}

		@Override public float getForce() {
			return force / getRatio();
		}
		@Override public float getTargetSpeed() {
			return targetSpeed * getRatio();
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

//	public static class SpinSourceEntry {
//		public float targetSpeed;
//		public float force;
//
//		public SpinSourceEntry(float targetSpeed, float force) {
//			this.targetSpeed = targetSpeed;
//			this.force = force;
//		}
//	}
}

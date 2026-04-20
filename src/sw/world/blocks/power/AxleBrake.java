package sw.world.blocks.power;

import arc.graphics.*;
import arc.math.*;
import arc.scene.ui.layout.*;
import arc.util.io.*;
import mindustry.gen.*;
import mindustry.ui.*;
import sw.ui.*;
import sw.world.meta.*;

public class AxleBrake extends AxleBlock {
	public float speedGradient = 1f;
	
	public float strength = 1f / 600f;
	
	public AxleBrake(String name) {
		super(name);
		configurable = true;

		saveConfig = true;

		config(float[].class, (AxleBrakeBuild build, float[] list) -> {
			build.speedTarget = list[0];
			build.torqueTarget = list[1];
			build.torqueGradient = list[2];
		});
	}
	
	@Override
	public void init() {
		super.init();
		
		if (spinConfig != null) {
			spinConfig.checkSpeed = false;
		}
	}
	
	@Override
	public void setStats() {
		super.setStats();
		if (spinConfig != null && strength >= 0) stats.add(SWStat.maxFriction, (spinConfig.resistance + strength) * 600f, SWStat.force);
	}
	
	public class AxleBrakeBuild extends AxleBlockBuild {
		public float speedTarget = 0, torqueTarget = 0f, torqueGradient = 0f;
		
		@Override
		public void buildConfiguration(Table cont) {
			cont.table(Styles.black6, table -> {
				SWTables.buildFloatSlider(table, "@ui.sw-max-speed", value -> configure(new float[]{value / 10f, torqueTarget, torqueGradient}), () -> speedTarget * 10f);
				if (strength < 0) {
					table.image(Tex.whiteui).color(Color.gray).padTop(10f).padBottom(10f).height(4f).growX().row();
					SWTables.buildFloatSlider(table, "@ui.sw-brake-strength", value -> configure(new float[]{speedTarget, value / 600f, torqueGradient}), () -> torqueTarget * 600f);
				}
				if (speedGradient < 0) {
					table.image(Tex.whiteui).color(Color.gray).padTop(10f).padBottom(10f).height(4f).growX().row();
					SWTables.buildFloatSlider(table, "@ui.sw-brake-gradient", value -> configure(new float[]{speedTarget, torqueTarget, value / 10f}), () -> torqueGradient * 10f);
				}
			}).margin(10f);
		}

//		public void buildFloatSlider(Table table, String name, Floatc setter, Floatp getter) {
//			TextField field = new TextField(Strings.fixed(getter.get(), 0));
//			field.setFilter(TextField.TextFieldFilter.floatsOnly);
//			field.changed(() -> setter.get(field.getText().isEmpty() ? 0f : Float.parseFloat(field.getText())));
//			field.setStyle(new TextField.TextFieldStyle() {{
//				font = Fonts.def;
//				fontColor = Color.white;
//
//				cursor = Tex.cursor;
//			}});
//
//			CenterSlider slider = table.add(new CenterSlider(8f, 1f, value -> {
//				float delta = value == 0 ? 0f : Mathf.pow(2f, Math.abs(value) - 1f) * Mathf.sign(value);
//				setter.get(Mathf.maxZero(getter.get() + delta));
//			})).minWidth(300f).growX().get();
//
//			slider.update(() -> {
//				if (slider.isDragging()) {
//					field.setProgrammaticChangeEvents(false);
//					field.setText(Strings.fixed(Mathf.maxZero(getter.get() + (slider.getValue() == 0 ? 0f : Mathf.pow(2f, Math.abs(slider.getValue()) - 1f)) * Mathf.sign(slider.getValue())), 0));
//					field.setProgrammaticChangeEvents(true);
//				} else {
//					slider.setValue(slider.getValue() * 0.49f);
//				}
//			});
//
//			slider.setStyle(new Slider.SliderStyle() {{
//				background = Tex.sliderBack;
//				knob = Tex.buttonSelect;
//			}});
//
//			table.row();
//			table.table(below -> {
//				below.add("@" + name).padRight(10f).color(Color.lightGray);
//
//				below.add(field);
//			}).left().padTop(5f).row();
//		}

		@Override public float[] config() {
			return new float[]{speedTarget, torqueTarget, torqueGradient};
		}

		@Override public float getForce() {
			return Mathf.clamp(progress()) * -(strength < 0 ? torqueTarget : strength) / getRatio();
		}
		
		@Override public boolean outputsSpin() {
			return true;
		}

		@Override
		public float progress() {
			float val = Mathf.clamp(Mathf.map(getSpeed(), speedTarget - (speedGradient < 0 ? torqueGradient : speedGradient), speedTarget, 0f, 1f));
			return Float.isNaN(val) ? (getSpeed() < speedTarget ? 0f : 1f) : val;
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);
			speedTarget = read.f();

			if (strength < 0) torqueTarget = read.f();
			if (speedGradient < 0) torqueGradient = read.f();
		}
		
		@Override
		public void write(Writes write) {
			super.write(write);
			write.f(speedTarget);

			if (strength < 0) write.f(torqueTarget);
			if (speedGradient < 0) write.f(speedGradient);
		}
	}
}

package sw.world.blocks.power;

import arc.math.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.ui.*;
import sw.gen.*;
import sw.ui.elements.*;
import sw.world.meta.*;

public class AxleBrake extends AxleBlock {
	public float brakeSpeedGradient = 1f;
	
	public float brakeStrength = 1f / 600f;
	
	public AxleBrake(String name) {
		super(name);
		configurable = true;
		
		config(Float.class, (AxleBrakeBuild build, Float value) -> {
			build.brakeSpeedTarget = value;
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
		if (spinConfig != null) stats.add(SWStat.maxFriction, (spinConfig.resistance + brakeStrength) * 600f, SWStat.force);
	}
	
	public class AxleBrakeBuild extends AxleBlockBuild {
		public float brakeSpeedTarget = 0;
		
		@Override
		public void buildConfiguration(Table cont) {
			float[] delta = {0};
			BoundlessSlider slider = new BoundlessSlider(
				sliderTable -> {
					sliderTable.setBackground(Tex.sliderBack);
					sliderTable.fill(SWTex.barEdge, a -> {});
				},
				button -> {
					button.setSize(54f, 30f);
					button.table(SWTex.barCenter).grow().pad(0f, 8f, 0f, 8f);
				}
			).setChanged(lastDrag -> {
				configure(Mathf.floor((delta[0] + brakeSpeedTarget * 10) * 10f)/100f);
				if (!Vars.headless) Sounds.click.play();
				delta[0] = 0;
			}).setOnDrag(drag -> {
				float speed = 0f;
				
				float alpha = drag / 100f;
				if (Math.abs(alpha) > 0.1) {
					speed = Mathf.pow(10f, Mathf.floor(Math.abs(alpha)) - 1) * Mathf.sign(alpha) * Time.delta;
					speed /= 30f;
				}
				if ((delta[0] + brakeSpeedTarget * 10 + speed) < 0) speed -= (delta[0] + brakeSpeedTarget * 10 + speed);
				delta[0] += speed;
			});
			cont.table(Styles.black6, table -> {
				table.add(slider).size(400f, 30f);
				table.table(label -> {
					label.label(() -> Strings.autoFixed(delta[0] + brakeSpeedTarget * 10f, 1));
				}).growY().width(100f);
			}).margin(10f);
		}
		
		@Override public float getForce() {
			return Mathf.clamp(Mathf.map(getSpeed(), brakeSpeedTarget - brakeSpeedGradient, brakeSpeedTarget, 0f, 1f)) * -brakeStrength;
		}
		
		@Override public boolean outputsSpin() {
			return true;
		}
		
		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);
			brakeSpeedTarget = read.f();
		}
		
		@Override
		public void write(Writes write) {
			super.write(write);
			write.f(brakeSpeedTarget);
		}
	}
}

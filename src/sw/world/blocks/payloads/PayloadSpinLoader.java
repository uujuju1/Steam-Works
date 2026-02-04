package sw.world.blocks.payloads;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.entities.units.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.draw.*;
import sw.world.blocks.power.*;
import sw.world.blocks.power.RotationBattery.*;
import sw.world.graph.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

public class PayloadSpinLoader extends PayloadBlock {
	public SpinConfig spinConfig;

	public DrawBlock drawer = new DrawDefault();
	
	public boolean reverse;
	public float maxPayloadSize = 3f;
	
	public PayloadSpinLoader(String name) {
		super(name);
		
		solid = true;
		rotate = true;
		outputsPayload = true;
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
	
	@Override
	public void setBars() {
		super.setBars();
		if (spinConfig != null) spinConfig.addBars(this);
	}
	
	@Override
	public void setStats() {
		super.setStats();
		if (spinConfig != null) spinConfig.addStats(stats);
	}
	
	public class PayloadSpinLoaderBuild extends PayloadBlockBuild<BuildPayload> implements HasSpin {
		public SpinModule spin;
		
		@Override public boolean acceptPayload(Building source, Payload payload) {
			return
				super.acceptPayload(source, payload) &&
				payload.size() <= maxPayloadSize * Vars.tilesize &&
				payload.content() instanceof RotationBattery;
		}
		
		@Override public boolean consumesSpin() {
			return !reverse;
		}
		
		@Override
		public Building create(Block block, Team team) {
			if (spinConfig != null) spin = new SpinModule();
			return super.create(block, team);
		}
		
		@Override public void draw()  {
			drawer.draw(this);
			
			Draw.z(Layer.blockOver);
			
			if (payload != null) {
				updatePayload();
				
				payload.draw();
			}
		}
		@Override public void drawLight() {
			drawer.drawLight(this);
		}
		@Override public float drawrot() {
			return ((rotation + 1) % 2 - 1) * 90f;
		}
		@Override public void drawSelect() {
			if (spin != null) spinConfig.drawPlace(block, tileX(), tileY(), rotation, true);
		}
		
		@Override public float getForce() {
			return reverse ? (payload != null && payload.build instanceof RotationBatteryBuild ? ((RotationBattery) payload.block()).outputForce : 0f) : 0f;
		}
		
		@Override
		public float getResistance() {
			float base = HasSpin.super.getResistance();
			if (payload != null && payload.build instanceof RotationBatteryBuild battery) {
				if (!reverse) return base + battery.getResistance();
			}
			return base;
		}
		
		@Override
		public float getTargetSpeed() {
			return (payload != null && payload.block() instanceof RotationBattery payblock && reverse && holdBattery() && hasArrived()) ? payblock.speed : 0f;
		}
		
		public boolean holdBattery() {
			return
				payload != null &&
				payload.build instanceof RotationBatteryBuild battery &&
				(
					(battery.wind != ((RotationBattery) payload.block()).maxWindup && !reverse && !Mathf.zero(spinGraph().speed)) ||
					(battery.wind != 0 && reverse)
				);
		}
		
		@Override
		public void onProximityUpdate() {
			super.onProximityUpdate();
			
			if (spin != null) new SpinGraph().mergeFlood(this);
		}
		
		@Override
		public void onProximityRemoved() {
			super.onProximityRemoved();
			
			if (spin != null) spinGraph().removeBuild(this);
		}
		
		@Override public boolean outputsSpin() {
			return reverse;
		}
		
		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);

			if (spinConfig != null) (spin == null ? new SpinModule() : spin).read(read);
		}
		
		@Override public float totalProgress() {
			return getRotation();
		}
		
		@Override
		public void updateTile() {
			if (!holdBattery()) {
				moveOutPayload();
			} else {
				if (efficiency > 0 && payload != null && moveInPayload()) {
					RotationBatteryBuild pay = (RotationBatteryBuild) payload.build;
					if (!reverse) {
						pay.wind = Math.min(((RotationBattery) payload.block()).maxWindup, pay.wind + spinGraph().speed * Time.delta);
					} else {
						pay.wind = Mathf.maxZero(pay.wind - spinGraph().speed * Time.delta);
					}
				}
			}
		}
		
		@Override
		public void write(Writes write) {
			super.write(write);

			if (spinConfig != null) spin.write(write);
		}
	}
}

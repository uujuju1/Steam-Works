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
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;
import sw.world.blocks.power.*;
import sw.world.blocks.power.RotationBattery.*;
import sw.world.graph.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

public class SWPayloadLoader extends PayloadBlock {
	public SpinConfig spinConfig;

	public DrawBlock drawer = new DrawDefault();

	public float payloadCapacity = 3f;

	/**
	 * Loading speed in items or liquids * 60 per second.
	 * Spin storage will be proportional to speed instead.
	 * for items this value is truncated.
	 */
	public float loadingSpeed = 1f;

	public boolean reverse;
	
	public SWPayloadLoader(String name) {
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
	
	public class SWPayloadLoaderBuild extends PayloadBlockBuild<BuildPayload> implements HasSpin {
		public SpinModule spin;

		@Override
		public boolean acceptItem(Building source, Item item) {
			return items.total() < itemCapacity && !(source instanceof SWPayloadLoaderBuild);
		}

		@Override
		public boolean acceptLiquid(Building source, Liquid liquid){
			return (liquids.current() == liquid || liquids.currentAmount() < 0.2f) && !(source instanceof PayloadUnloader.PayloadUnloaderBuild);
		}

		@Override
		public boolean acceptPayload(Building source, Payload payload) {
			return super.acceptPayload(source, payload) && payload.fits(payloadCapacity) && payload instanceof BuildPayload buildPayload && (
				(buildPayload.block().hasItems && hasItems && buildPayload.block().unloadable && buildPayload.block().itemCapacity > 10) ||
				(buildPayload.block().hasLiquids && hasLiquids && buildPayload.block().liquidCapacity > 10f) ||
				(buildPayload.block() instanceof RotationBattery b && buildPayload.build instanceof RotationBatteryBuild  && b.spinConfig != null)
			);
		}

		@Override
		public int acceptStack(Item item, int amount, Teamc source) {
			return !acceptItem(source instanceof Building b ? b : this, item) || !hasItems || source != null && source.team() != this.team ? 0 : Math.min(getMaximumAccepted(item) - items.get(item), amount);
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
		
//		@Override
//		public float getTargetSpeed() {
//			return (payload != null && payload.block() instanceof RotationBattery payblock && reverse && holdBattery() && hasArrived()) ? payblock.speed : 0f;
//		}
		public void handlePayloadResources() {
			if (payload == null) return;
			if (reverse) {
				if (payload.block().hasItems) Vars.content.items().each(item -> {
					int count = Math.min((int) (loadingSpeed * edelta()), getMaximumAccepted(item));
					count = Math.min(payload.build.items.get(item), acceptStack(item, count, payload.build));
					if (count > 0) {
						handleStack(item, count, payload.build);
						payload.build.items.remove(item, count);
					}
				});
				if (payload.block().hasLiquids && payload.build.liquids.currentAmount() > 0.001f) {
					float count = Math.min(loadingSpeed * edelta(), Math.min(payload.build.liquids.currentAmount(), liquidCapacity - liquids.get(liquids.current())));
					if (count > 0) {
						handleLiquid(payload.build, liquids.current(), count);
						payload.build.liquids.remove(liquids.current(), count);
					}
				}
			} else {
				if (payload.block().hasItems) Vars.content.items().each(item -> {
					int count = Math.min((int) (loadingSpeed * edelta()), payload.build.getMaximumAccepted(item));
					count = Math.min(items.get(item), payload.build.acceptStack(item, count, this));
					if (count > 0) {
						payload.build.handleStack(item, count, this);
						items.remove(item, count);
					}
				});
				if (payload.block().hasLiquids && liquids.currentAmount() > 0.001f) {
					float count = Math.min(loadingSpeed * edelta(), Math.min(liquids.currentAmount(), payload.block().liquidCapacity - payload.build.liquids.get(liquids.current())));
					if (count > 0) {
						payload.build.handleLiquid(this, liquids.current(), count);
						liquids.remove(liquids.current(), count);
					}
				}
			}
		}
		
		public boolean hold() {
			if (payload == null) return false;

			if (reverse) return (payload.block().hasItems && payload.build.items.any()) ||
				(payload.block().hasLiquids && payload.build.liquids.currentAmount() > 0.001f) ||
				(payload.build instanceof RotationBatteryBuild build && build.wind > 0.001f);

			return (payload.block().hasItems && !Vars.content.items().contains(item -> payload.build.items.get(item) == payload.block().itemCapacity)) ||
				(payload.block().hasLiquids && payload.build.liquids.currentAmount() <= payload.block().liquidCapacity - 0.001f) ||
				(payload.build instanceof RotationBatteryBuild build && build.wind < ((RotationBattery) build.block).maxWindup);
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
			if (reverse) {
				if (hasItems) dumpAccumulate();
				if (hasLiquids) dumpLiquid(liquids.current());
			}
			if (hold()) {
				if (moveInPayload() && efficiency > 0) handlePayloadResources();
			} else moveOutPayload();
		}
		
		@Override
		public void write(Writes write) {
			super.write(write);

			if (spinConfig != null) spin.write(write);
		}
	}
}

package sw.world.blocks.defense;

import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.world.blocks.defense.turrets.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

public class SWConsumeTurret extends Turret {
	public TensionConfig tensionConfig = new TensionConfig();
	public BulletType shootType = Bullets.placeholder;

	public SWConsumeTurret(String name) {
		super(name);
	}

	@Override
	public void setStats() {
		super.setStats();
		tensionConfig.addStats(stats);
	}
	@Override
	public void setBars() {
		super.setBars();
		tensionConfig.addBars(this);
	}

	public class SWConsumeTurretBuild extends TurretBuild implements HasTension {
		public TensionModule tension = new TensionModule();

		@Override public boolean hasAmmo(){
			return canConsume();
		}

		@Override
		public void onProximityUpdate() {
			super.onProximityAdded();
			tensionGraph().removeBuild(this, true);
		}
		@Override
		public void onProximityRemoved() {
			super.onProximityRemoved();
			tensionGraph().removeBuild(this, false);
		}

		@Override public BulletType peekAmmo(){
			return shootType;
		}

		@Override
		protected void shoot(BulletType type) {
			super.shoot(type);
			consume();
		}

		@Override public TensionModule tension() {
			return tension;
		}
		@Override public TensionConfig tensionConfig() {
			return tensionConfig;
		}

		@Override
		public void updateTile() {
			if (tensionGraph().getOverallTension() > tensionConfig.maxTension) kill();
			super.updateTile();
		}

		@Override public BulletType useAmmo(){
			return shootType;
		}
	}
}

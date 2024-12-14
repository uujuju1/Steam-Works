package sw.world.blocks.defense;

import arc.util.io.*;
import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.world.blocks.defense.turrets.*;
import sw.world.graph.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

public class SWConsumeTurret extends Turret {
	public SpinConfig spinConfig = new SpinConfig();

	public BulletType shootType = Bullets.placeholder;

	public SWConsumeTurret(String name) {
		super(name);
	}

	@Override
	public void setBars() {
		super.setBars();
		spinConfig.addBars(this);
	}

	@Override
	public void setStats() {
		super.setStats();
		spinConfig.addStats(stats);
	}

	public class SWConsumeTurretBuild extends TurretBuild implements HasSpin {
		public SpinModule spin = new SpinModule();

		@Override public boolean hasAmmo(){
			return canConsume();
		}

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

		@Override public BulletType peekAmmo(){
			return shootType;
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);
			spin.read(read);
		}

		@Override
		protected void shoot(BulletType type) {
			super.shoot(type);
			consume();
		}

		@Override public SpinModule spin() {
			return spin;
		}
		@Override public SpinConfig spinConfig() {
			return spinConfig;
		}

		@Override public BulletType useAmmo(){
			return shootType;
		}

		@Override
		public void write(Writes write) {
			super.write(write);
			spin.write(write);
		}
	}
}

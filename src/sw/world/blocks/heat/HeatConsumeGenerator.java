package sw.world.blocks.heat;

import arc.*;
import arc.util.io.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.blocks.power.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

public class HeatConsumeGenerator extends ConsumeGenerator {
	public HeatConfig heatConfig = new HeatConfig();

	public HeatConsumeGenerator(String name) {
		super(name);
		update = sync = true;
		destructible = true;
		underBullets = true;
	}

	@Override
	public void setBars() {
		super.setBars();
		addBar("heat", (HeatConsumeGeneratorBuild entity) -> new Bar(Core.bundle.get("bar.heat"), Pal.accent, entity::fraction));
	}
	@Override
	public void setStats() {
		super.setStats();
		heatConfig.heatStats(stats);
	}

	public class HeatConsumeGeneratorBuild extends ConsumeGeneratorBuild implements HasHeat {
		HeatModule module = new HeatModule();

		@Override public HeatModule heat() {
			return module;
		}
		@Override public HeatConfig heatC() {
			return heatConfig;
		}

		@Override
		public void updateTile() {
			super.updateTile();
			updateHeat(this);
		}

		@Override
		public void onProximityAdded() {
			super.onProximityAdded();
			hGraph().builds.addUnique(this);
			hGraph().reloadConnections();
		}
		@Override
		public void onProximityRemoved() {
			super.onProximityRemoved();
			hGraph().builds.remove(this);
			hGraph().links.removeAll(b -> b.has(this));
		}
		@Override
		public void onProximityUpdate() {
			super.onProximityUpdate();
			hGraph().reloadConnections();
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);
			module.read(read);
		}
		@Override
		public void write(Writes write) {
			super.write(write);
			module.write(write);
		}

	}
}

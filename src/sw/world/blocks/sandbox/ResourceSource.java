package sw.world.blocks.sandbox;

import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.io.*;
import mindustry.world.blocks.heat.*;
import mindustry.world.blocks.power.*;
import mindustry.world.meta.*;
import sw.ui.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

import static mindustry.Vars.*;

public class ResourceSource extends PowerGenerator {
	public TensionConfig tensionConfig = new TensionConfig() {{
		tier = -1;
	}};

	public ResourceSource(String name) {
		super(name);
		hasItems = hasLiquids = hasPower = true;
		outputsPower = outputsLiquid = true;
		clearOnDoubleTap = true;
		update = configurable = true;
		saveConfig = true;
		displayFlow = false;
		itemCapacity = 100;
		liquidCapacity = 100f;
		envEnabled = Env.any;
		buildVisibility = BuildVisibility.sandboxOnly;
		group = BlockGroup.transportation;

		config(Config.class, (ResourceSourceBuild b, Config config) -> {
			b.con = config;
		});
		configClear((ResourceSourceBuild b) -> b.con = new Config());
	}

	@Override public boolean outputsItems() {
		return true;
	}

	@Override
	public void setBars() {
		super.setBars();
		removeBar("liquid");
	}

	public class ResourceSourceBuild extends GeneratorBuild implements HasTension, HeatBlock {
		Config con = new Config();
		TensionModule tension = new TensionModule();

		@Override public void buildConfiguration(Table table) {
			SWTables.buildSandboxSelector(table, con, () -> {
				configure(con);
			});
		}

		@Override
		public float getPowerProduction(){
			return enabled ? 1000000 : 0f;
		}

		@Override public float heatFrac(){
			return 1;
		}
		@Override public float heat(){
			return con.heatValue;
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);
			for (int i = 0; i < content.items().size; i++) if (read.bool()) con.itemBits.set(i);
			for (int i = 0; i < content.liquids().size; i++) if (read.bool()) con.liquidBits.set(i);
			con.heatValue = read.f();
			con.sTension = read.f();
			con.mTension = read.f();
		}

		@Override
		public TensionModule tension() {
			return tension;
		}
		@Override
		public TensionConfig tensionConfig() {
			return tensionConfig;
		}

		@Override public float tensionMobile() {
			return con.mTension;
		}
		@Override public float tensionStatic() {
			return con.sTension;
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

		@Override
		public void updateTile() {
			for (int i = 0; i < con.itemBits.length(); i++) if (con.itemBits.get(i)) {
				items.set(content.item(i), 1);
				dump(content.item(i));
				items.set(content.item(i), 0);
			}
			for (int i = 0; i < con.liquidBits.length(); i++) if (con.liquidBits.get(i)) {
				liquids.add(content.liquid(i), liquidCapacity);
				dumpLiquid(content.liquid(i));
			}
		}

		@Override
		public void write(Writes write) {
			super.write(write);
			for (int i = 0; i < content.items().size; i++) write.bool(con.itemBits.get(i));
			for (int i = 0; i < content.liquids().size; i++) write.bool(con.liquidBits.get(i));
			write.f(con.heatValue);
			write.f(con.sTension);
			write.f(con.mTension);
		}
	}

	public static class Config {
		public Bits
			itemBits = new Bits(content.items().size),
			liquidBits = new Bits(content.liquids().size);
		public float heatValue, sTension, mTension;
	}
}

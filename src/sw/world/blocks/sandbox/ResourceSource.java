package sw.world.blocks.sandbox;

import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.io.*;
import mindustry.ui.*;
import mindustry.world.blocks.heat.*;
import mindustry.world.blocks.power.*;
import mindustry.world.meta.*;
import sw.ui.*;

import static mindustry.Vars.*;

public class ResourceSource extends PowerGenerator {
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

	public class ResourceSourceBuild extends GeneratorBuild implements HeatBlock {
		Config con = new Config();

		@Override public void buildConfiguration(Table table) {
			SWTables.resourceSourceSelector(table.table(Styles.black6).get(), con, () -> {
				configure(con);
			});
		}

		@Override public Config config() {
			return con;
		}

		@Override
		public float getPowerProduction(){
			return enabled ? con.power : 0f;
		}

		@Override public float heatFrac(){
			return 1;
		}
		@Override public float heat(){
			return con.heat;
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);
			for (int i = 0; i < content.items().size; i++) if (read.bool()) con.itemBits.set(i);
			for (int i = 0; i < content.liquids().size; i++) if (read.bool()) con.liquidBits.set(i);
			con.heatValue = read.f();
			con.power = read.f();
			con.heat = read.f();
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
			write.f(con.power);
			write.f(con.heat);
		}
	}

	public static class Config {
		public Bits
			itemBits = new Bits(content.items().size),
			liquidBits = new Bits(content.liquids().size);
		public float heatValue, power, heat;
	}
}

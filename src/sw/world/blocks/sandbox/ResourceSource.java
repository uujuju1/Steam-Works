package sw.world.blocks.sandbox;

import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.io.*;
import mindustry.ui.*;
import mindustry.world.blocks.power.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class ResourceSource extends PowerNode {
	public ResourceSource(String name) {
		super(name);
		hasItems = hasLiquids = hasPower = true;
		outputsPower = outputsLiquid = true;
		update = sync = true;
		saveConfig = true;
		displayFlow = false;
		itemCapacity = maxNodes = 100;
		liquidCapacity = 100f;
		envEnabled = Env.any;
		buildVisibility = BuildVisibility.sandboxOnly;
		group = BlockGroup.transportation;
		config(Config2.class, (ResourceSourceBuild tile, Config2 entry) -> {
			tile.itemBits.set(entry.bits);
			tile.liquidBits.set(entry.bits2);
		});
	}

	public static class Config2 {
		public Bits bits;
		public Bits bits2;

		public Config2(Bits bits, Bits bits2) {
			this.bits = bits;
			this.bits2 = bits2;
		}
	}

	@Override public boolean outputsItems() {
		return true;
	}

	@Override
	public void setBars() {
		super.setBars();
		removeBar("liquid");
	}

	public class ResourceSourceBuild extends PowerNodeBuild {
		public Bits itemBits = new Bits(content.items().size);
		public Bits liquidBits = new Bits(content.liquids().size);

		@Override
		public float getPowerProduction(){
			return enabled ? 35791393 : 0f;
		}

		@Override
		public void buildConfiguration(Table table) {
			table.table(Styles.black5, t -> {
				t.table(Styles.black5, items -> content.items().each(c -> {
					Button button = items.button(b -> b.image(c.uiIcon), Styles.clearTogglei, () -> {}).pad(2.5f).get();
					button.changed(() -> {
						itemBits.flip(c.id);
						configure(new Config2(itemBits, liquidBits));
					});
					button.update(() -> button.setChecked(itemBits.get(c.id)));
					if (c.id % 4 == 3) items.row();
				})).pad(5f);
				t.table(Styles.black5, liquids ->  content.liquids().each(c -> {
					Button button = liquids.button(b -> b.image(c.uiIcon), Styles.clearTogglei, () -> {}).pad(2.5f).get();
					button.changed(() -> {
						liquidBits.flip(c.id);
						configure(new Config2(itemBits, liquidBits));
					});
					button.update(() -> button.setChecked(liquidBits.get(c.id)));
					if (c.id % 4 == 3) liquids.row();
				})).pad(5f);
			});
		}

		@Override
		public void updateTile() {
			for (int i = 0; i < itemBits.length(); i++) if (itemBits.get(i)) {
				items.set(content.item(i), 1);
				dump(content.item(i));
				items.set(content.item(i), 0);
			}
			for (int i = 0; i < liquidBits.length(); i++) if (liquidBits.get(i)) {
				liquids.add(content.liquid(i), liquidCapacity);
				dumpLiquid(content.liquid(i));
			}
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);
			for (int i = 0; i < content.items().size; i++) {
				if(read.bool()) itemBits.set(i);
			}
			for (int i = 0; i < content.liquids().size; i++) {
				if(read.bool()) liquidBits.set(i);
			}
		}

		@Override
		public void write(Writes write) {
			super.write(write);
			for (int i = 0; i < content.items().size; i++) {
				write.bool(itemBits.get(i));
			}
			for (int i = 0; i < content.liquids().size; i++) {
				write.bool(liquidBits.get(i));
			}
		}
	}
}

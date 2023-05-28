package sw.world.blocks.sandbox;

import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.type.*;
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
			return enabled ? 1000000 : 0f;
		}

		@Override
		public void buildConfiguration(Table table) {
			table.table(Styles.black5, t -> {
				Table items = new Table(ti -> {
					for(int i = 0; i < Vars.content.items().size; i++) {
						Item item = Vars.content.item(i);
						int finalI = i;
						Button button = ti.button(b -> b.image(item.uiIcon), Styles.clearNoneTogglei, () -> itemBits.flip(finalI)).size(40f).get();
						button.update(() -> {
							button.setChecked(itemBits.get(finalI));
						});
						if (i%4 == 3) ti.row();
					}
				});
				Table liquids = new Table(tl -> {
					for(int i = 0; i < Vars.content.liquids().size; i++) {
						Liquid liquid = Vars.content.liquid(i);
						int finalI = i;
						Button button = tl.button(b -> b.image(liquid.uiIcon), Styles.clearNoneTogglei, () -> liquidBits.flip(finalI)).size(40f).get();
						button.update(() -> {
							button.setChecked(liquidBits.get(finalI));
						});
						if (i%4 == 3) tl.row();
					}
				});

				ScrollPane
					pane1 = new ScrollPane(items, Styles.smallPane),
					pane2 = new ScrollPane(liquids, Styles.smallPane);

				pane1.setScrollingDisabled(true, false);
				pane2.setScrollingDisabled(true, false);

				t.add(pane1).maxHeight(40f * 4f);
				t.add(pane2).maxHeight(40f * 4f);
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

package sw.content;

import arc.struct.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.type.*;
import sw.ui.dialog.SectorLaunchDialog.*;

public class SWSectorPresets {
	public static SectorPreset
		nowhere, anemoia, nostalgia, coast, island;

	public static void load() {
		nowhere = new SectorPreset("nowhere", SWPlanets.wendi, 0) {{
		}};
		anemoia = new SectorPreset("anemoia", SWPlanets.wendi, 1) {{
			rules = rule -> {
				rule.winWave = 10;
				rule.loadout.set(ItemStack.with(SWItems.nickel, 500));
			};
		}};
		nostalgia = new SectorPreset("nostalgia", SWPlanets.wendi, 2) {{
			rules = rule -> {
				rule.winWave = 20;
				rule.loadout.set(ItemStack.with(SWItems.nickel, 500, Items.graphite, 500));
			};
		}};
		coast = new SectorPreset("coast", SWPlanets.wendi, 3) {{
			rules = rule -> {
				rule.winWave = 20;
				rule.loadout.set(ItemStack.with(SWItems.iron, 500, SWItems.nickel, 200, Items.graphite, 300));
			};
		}};
		island = new SectorPreset("island", SWPlanets.wendi, 5) {{
			rules = rule -> {
				rule.winWave = 20;
				rule.loadout.set(ItemStack.with(SWItems.iron, 500, SWItems.nickel, 200, Items.graphite, 300));
			};
		}};
	}

	public static void init() {
		new SectorNode(nowhere.sector, 0f, 0f, Seq.with(), nowhereSec -> {
			nowhereSec.top = Icon.terrain;
			new SectorNode(anemoia.sector, 149f, 0f, Seq.with(), anemoiaSec -> {
				anemoiaSec.top = Icon.waves;
				new SectorNode(nostalgia.sector, 149f, 149f, Seq.with(), nostalgiaSec -> {
					nostalgiaSec.top = Icon.waves;
					new SectorNode(coast.sector, -149f, 149f, Seq.with(), coastSec -> {
						coastSec.top = Icon.waves;
						new SectorNode(island.sector, -298f, 298f, Seq.with(), islandSec -> {
							islandSec.top = Icon.waves;
						});
					});
				});
			});
		});

		new SectorNode(getSector(4), 0f, 149f, Seq.with(getSector(0), getSector(2)), filler -> {
			filler.lock = self -> false;
			filler.visible = self -> !self.requirements.contains(req -> !req.lock.get(req) || !req.visible.get(req));
			filler.region = "sw-sector-filler-1";
		});
		new SectorNode(getSector(4), -149f, 298f, Seq.with(getSector(3), getSector(5)), filler -> {
			filler.lock = self -> false;
			filler.visible = self -> !self.requirements.contains(req -> !req.lock.get(req) || !req.visible.get(req));
			filler.region = "sw-sector-filler-2";
		});
		new SectorNode(getSector(4), -298f, 149f, Seq.with(getSector(3), getSector(5)), filler -> {
			filler.lock = self -> false;
			filler.visible = self -> !self.requirements.contains(req -> !req.lock.get(req) || !req.visible.get(req));
			filler.region = "sw-sector-filler-4";
		});
	}

	public static Sector getSector(int id) {
		return SWPlanets.wendi.sectors.get(id);
	}
}

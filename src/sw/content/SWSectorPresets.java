package sw.content;

import mindustry.content.*;
import mindustry.gen.*;
import mindustry.type.*;
import sw.type.*;

public class SWSectorPresets {
	public static SectorPreset
		terrain1, terrain2, terrain3,

		nowhere, anemoia, nostalgia, coast, island;

	public static void load() {
		nowhere = new PositionSectorPreset("nowhere", SWPlanets.wendi, 0) {{
			x = y = 0;
			icon = () -> Icon.terrain;
			alwaysUnlocked = true;
		}};
		anemoia = new PositionSectorPreset("anemoia", SWPlanets.wendi, 1) {{
			x = 1;
			y = 0;
			rules = rule -> {
				rule.winWave = 10;
				rule.loadout.set(ItemStack.with(SWItems.nickel, 500));
			};
		}};
		nostalgia = new PositionSectorPreset("nostalgia", SWPlanets.wendi, 2) {{
			x = 1;
			y = 1;
			rules = rule -> {
				rule.winWave = 20;
				rule.loadout.set(ItemStack.with(SWItems.nickel, 500, Items.graphite, 500));
			};
		}};
		coast = new PositionSectorPreset("coast", SWPlanets.wendi, 3) {{
			x = -1;
			y = 1;
			rules = rule -> {
				rule.winWave = 20;
				rule.loadout.set(ItemStack.with(SWItems.iron, 500, SWItems.nickel, 200, Items.graphite, 300));
			};
		}};
		island = new PositionSectorPreset("island", SWPlanets.wendi, 4) {{
			x = -2;
			y = 2;
			rules = rule -> {
				rule.winWave = 20;
				rule.loadout.set(ItemStack.with(SWItems.iron, 500, SWItems.nickel, 200, Items.graphite, 300));
			};
		}};

		terrain1 = new PositionSectorPreset("denied1", SWPlanets.wendi, 11) {{
			accessible = false;
			x = 0;
			y = 1;
			visibleReq.add((PositionSectorPreset) nowhere, (PositionSectorPreset) nostalgia);
		}};
		terrain2 = new PositionSectorPreset("denied2", SWPlanets.wendi, 12) {{
			accessible = false;
			x = -1;
			y = 2;
			visibleReq.add((PositionSectorPreset) coast, (PositionSectorPreset) island);
		}};
		terrain3 = new PositionSectorPreset("denied3", SWPlanets.wendi, 13) {{
			accessible = false;
			x = -2;
			y = 1;
			visibleReq.add((PositionSectorPreset) coast, (PositionSectorPreset) island);
		}};
	}

//	public static void init() {
//		new SectorNode(nowhere.sector, 0f, 0f, Seq.with(), nowhereSec -> {
//			nowhereSec.top = Icon.terrain;
//			new SectorNode(anemoia.sector, 149f, 0f, Seq.with(), anemoiaSec -> {
//				anemoiaSec.top = Icon.waves;
//				new SectorNode(nostalgia.sector, 149f, 149f, Seq.with(), nostalgiaSec -> {
//					nostalgiaSec.top = Icon.waves;
//					new SectorNode(coast.sector, -149f, 149f, Seq.with(), coastSec -> {
//						coastSec.top = Icon.waves;
//						new SectorNode(island.sector, -298f, 298f, Seq.with(), islandSec -> {
//							islandSec.top = Icon.waves;
//						});
//					});
//				});
//			});
//		});
//
//		new SectorNode(getSector(4), 0f, 149f, Seq.with(getSector(0), getSector(2)), filler -> {
//			filler.lock = self -> false;
//			filler.visible = self -> !self.requirements.contains(req -> !req.lock.get(req) || !req.visible.get(req));
//			filler.region = "sw-sector-filler-1";
//		});
//		new SectorNode(getSector(4), -149f, 298f, Seq.with(getSector(3), getSector(5)), filler -> {
//			filler.lock = self -> false;
//			filler.visible = self -> !self.requirements.contains(req -> !req.lock.get(req) || !req.visible.get(req));
//			filler.region = "sw-sector-filler-2";
//		});
//		new SectorNode(getSector(4), -298f, 149f, Seq.with(getSector(3), getSector(5)), filler -> {
//			filler.lock = self -> false;
//			filler.visible = self -> !self.requirements.contains(req -> !req.lock.get(req) || !req.visible.get(req));
//			filler.region = "sw-sector-filler-4";
//		});
//	}

	public static Sector getSector(int id) {
		return SWPlanets.wendi.sectors.get(id);
	}
}

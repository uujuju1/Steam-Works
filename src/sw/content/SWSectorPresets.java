package sw.content;

import mindustry.gen.*;
import mindustry.type.*;
import sw.type.*;

public class SWSectorPresets {
	public static SectorPreset
		jezero, myeik;
//
//		nowhere, anemoia, nostalgia, coast, island;

	public static void load() {
		jezero = new PositionSectorPreset("jezero", SWPlanets.wendi, 0) {{
			x = y = 0;
			width = height = 100;
			icon = () -> Icon.terrain;
			alwaysUnlocked = true;

			rules = r -> {
				r.waves = false;
				r.weather.add(new Weather.WeatherEntry(SWWeathers.souesiteDust, 36000f, 54000f, 9000f, 27000f));
			};
		}};
		myeik = new PositionSectorPreset("myeik", SWPlanets.wendi, 1) {{
			x = -100f;
			y = 100f;
			width = 200f;
			height = 100f;
			icon = () -> Icon.waves;
			alwaysUnlocked = true;

			rules = r -> {
				r.waves = false;
				r.weather.add(new Weather.WeatherEntry(SWWeathers.souesiteDust, 36000f, 54000f, 9000f, 27000f));
			};
		}};

//		nowhere = new PositionSectorPreset("nowhere", SWPlanets.wendi, 0) {{
//			x = y = 0;
//			icon = () -> Icon.terrain;
//			alwaysUnlocked = true;
//		}};
//		anemoia = new PositionSectorPreset("anemoia", SWPlanets.wendi, 1) {{
//			x = 1;
//			y = 0;
//			rules = rule -> {
//				rule.winWave = 10;
//				rule.loadout.set(ItemStack.with(SWItems.nickel, 500));
//			};
//		}};
//		nostalgia = new PositionSectorPreset("nostalgia", SWPlanets.wendi, 2) {{
//			x = 1;
//			y = 1;
//			rules = rule -> {
//				rule.winWave = 20;
//				rule.loadout.set(ItemStack.with(SWItems.nickel, 500, Items.graphite, 500));
//			};
//		}};
//		coast = new PositionSectorPreset("coast", SWPlanets.wendi, 3) {{
//			x = -1;
//			y = 1;
//			rules = rule -> {
//				rule.winWave = 20;
//				rule.loadout.set(ItemStack.with(SWItems.iron, 500, SWItems.nickel, 200, Items.graphite, 300));
//			};
//		}};
//		island = new PositionSectorPreset("island", SWPlanets.wendi, 4) {{
//			x = -2;
//			y = 2;
//			rules = rule -> {
//				rule.winWave = 20;
//				rule.loadout.set(ItemStack.with(SWItems.iron, 500, SWItems.nickel, 200, Items.graphite, 300));
//			};
//		}};
//
//		terrain1 = new PositionSectorPreset("denied1", SWPlanets.wendi, 11) {{
//			accessible = false;
//			x = 0;
//			y = 1;
//			visibleReq.add((PositionSectorPreset) nowhere, (PositionSectorPreset) nostalgia);
//		}};
//		terrain2 = new PositionSectorPreset("denied2", SWPlanets.wendi, 12) {{
//			accessible = false;
//			x = -1;
//			y = 2;
//			visibleReq.add((PositionSectorPreset) coast, (PositionSectorPreset) island);
//		}};
//		terrain3 = new PositionSectorPreset("denied3", SWPlanets.wendi, 13) {{
//			accessible = false;
//			x = -2;
//			y = 1;
//			visibleReq.add((PositionSectorPreset) coast, (PositionSectorPreset) island);
//		}};
	}

	public static Sector getSector(int id) {
		return SWPlanets.wendi.sectors.get(id);
	}
}

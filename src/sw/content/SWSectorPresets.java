package sw.content;

import arc.util.*;
import mindustry.gen.*;
import mindustry.type.*;
import sw.type.*;

public class SWSectorPresets {
	public static SectorPreset
		crevasse, theDelta, kettle;

	public static void load() {
		crevasse = new PositionSectorPreset("crevasse", SWPlanets.wendi, 0) {{
			x = y = 0;
			width = height = 200;
			icon = () -> Icon.terrain;
			alwaysUnlocked = true;

			rules = r -> {
				r.winWave = 5;
				r.weather.add(new Weather.WeatherEntry(
					SWWeathers.souesiteDust,
					5f * Time.toMinutes,
					7.5f * Time.toMinutes,
					2.5f * Time.toMinutes,
					5f * Time.toMinutes
				));
			};
		}};
		theDelta = new PositionSectorPreset("the-delta", SWPlanets.wendi, 1) {{
			x = 300;
			y = 100;
			width = height = 200;
			icon = () -> Icon.units;
		}};
		kettle = new PositionSectorPreset("kettle", SWPlanets.wendi, 2) {{
			x = 200;
			y = -200;
			width = height = 300;
			icon = () -> Icon.waves;

			rules = r -> {
				r.winWave = 15;
			};
		}};
	}

	public static Sector getSector(int id) {
		return SWPlanets.wendi.sectors.get(id);
	}
}

package sw.content;

import arc.util.*;
import mindustry.gen.*;
import mindustry.type.*;
import sw.type.*;

public class SWSectorPresets {
	public static SectorPreset
		crevasse;

	public static void load() {
		crevasse = new PositionSectorPreset("crevasse", SWPlanets.wendi, 0) {{
			x = y = 0;
			width = height = 200;
			icon = () -> Icon.terrain;
			alwaysUnlocked = true;

			rules = r -> {
				r.winWave = 4;
				r.weather.add(new Weather.WeatherEntry(
					SWWeathers.souesiteDust,
					5f * Time.toMinutes,
					7.5f * Time.toMinutes,
					2.5f * Time.toMinutes,
					5f * Time.toMinutes
				));
			};
		}};
	}

	public static Sector getSector(int id) {
		return SWPlanets.wendi.sectors.get(id);
	}
}

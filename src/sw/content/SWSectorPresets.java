package sw.content;

import arc.util.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.blocks.storage.*;
import sw.content.blocks.*;
import sw.type.*;

public class SWSectorPresets {
	public static SectorPreset
		crevasse, theDelta, kettle, abandonedMaze;

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
			
			core = (CoreBlock) SWStorage.coreScaffold;
		}};
		theDelta = new PositionSectorPreset("the-delta", SWPlanets.wendi, 1) {{
			x = 300;
			y = 100;
			width = height = 200;
			icon = () -> Icon.units;
			
			core = (CoreBlock) SWStorage.coreScaffold;
		}};
		kettle = new PositionSectorPreset("kettle", SWPlanets.wendi, 2) {{
			x = 200;
			y = -200;
			width = height = 300;
			icon = () -> Icon.waves;

			rules = r -> {
				r.winWave = 15;
				r.weather.add(new Weather.WeatherEntry(
					SWWeathers.souesiteDust,
					5f * Time.toMinutes,
					7.5f * Time.toMinutes,
					2.5f * Time.toMinutes,
					5f * Time.toMinutes
				));
			};
			
			core = (CoreBlock) SWStorage.coreScaffold;
			
			launcher = (PositionSectorPreset) theDelta;
		}};
		abandonedMaze = new PositionSectorPreset("abandoned-maze", SWPlanets.wendi, 3) {{
			x = -300;
			y = 300;
			width = height = 200;
			icon = () -> Icon.terrain;
			
			rules = r -> {
				r.waves = false;
			};
			
			core = (CoreBlock) SWStorage.coreScaffold;
		}};
	}
}

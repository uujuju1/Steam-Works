package sw.content;

import arc.util.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.blocks.storage.*;
import sw.content.blocks.*;
import sw.gen.*;
import sw.type.*;

public class SWSectorPresets {
	public static SectorPreset
		crevasse, theDelta,
		liveStorm, bayOfEmbers,
		abandonedMaze, cavern;

	public static void load() {
		crevasse = new PositionSectorPreset("crevasse", SWPlanets.wendi, 0) {{
			x = y = 0;
			width = height = 200;
			icon = () -> Icon.terrain;
			alwaysUnlocked = true;
			landMusic = SWMusics.chamadoDoVazio;

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
			icon = () -> Icon.effect;
			landMusic = SWMusics.asTerras;

			rules = r -> {
				r.weather.add(new Weather.WeatherEntry(
					SWWeathers.souesiteDust,
					5f * Time.toMinutes,
					7.5f * Time.toMinutes,
					2.5f * Time.toMinutes,
					5f * Time.toMinutes
				));
				r.weather.add(new Weather.WeatherEntry(
					Weathers.fog,
					2.5f * Time.toMinutes,
					5f * Time.toMinutes,
					5f * Time.toMinutes,
					7.5f * Time.toMinutes
				));
			};

			core = (CoreBlock) SWStorage.coreScaffold;
		}};
		liveStorm = new PositionSectorPreset("live-storm", SWPlanets.wendi, 2) {{
			x = 500;
			y = 200;
			width = height = 300;
			icon = () -> Icon.waves;

			landMusic = SWMusics.motorPerpetuo;

			rules = r -> {
				r.winWave = 20;
				r.weather.add(new Weather.WeatherEntry(
					SWWeathers.souesiteDust,
					5f * Time.toMinutes,
					7.5f * Time.toMinutes,
					2.5f * Time.toMinutes,
					5f * Time.toMinutes
				));
				r.weather.add(new Weather.WeatherEntry(Weathers.fog) {{
					always = true;
				}});
			};

			core = (CoreBlock) SWStorage.coreScaffold;

			launcher = (PositionSectorPreset) theDelta;
		}};
		abandonedMaze = new PositionSectorPreset("abandoned-maze", SWPlanets.wendi, 3) {{
			x = -100;
			y = 300;
			width = height = 200;
			icon = () -> Icon.terrain;

			landMusic = SWMusics.passadoEsquecido;
			
			rules = r -> {
				r.waves = false;
			};
			
			core = (CoreBlock) SWStorage.coreScaffold;
		}};
		cavern = new PositionSectorPreset("cavern", SWPlanets.wendi, 4) {{
			x = -400;
			y = 200;
			width = height = 300;
			icon = () -> Icon.modeSurvival;
			landMusic = SWMusics.passadoEsquecido;
			
			rules = r -> {
				r.cleanupDeadTeams = false;
			};
			
			launcher = (PositionSectorPreset) abandonedMaze;
			core = (CoreBlock) SWStorage.coreMole;
		}};
		bayOfEmbers = new PositionSectorPreset("bay-of-embers", SWPlanets.wendi, 5) {{
			x = 700f;
			y = 400f;
			width = 600f;
			height = 300f;
			icon = () -> Icon.modeAttack;

			landMusic = SWMusics.pendaratanPertama;

			core = (CoreBlock) SWStorage.coreScaffold;
		}};
	}
}

package sw.content;

import mindustry.content.*;
import mindustry.game.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.meta.*;
import sw.content.blocks.*;
import sw.maps.generators.*;

public class SWPlanets {
	public static Planet wendi;

	public static void load() {
		wendi = new Planet("wendi", Planets.sun, 1f, 3) {{
			sectorSeed = 2;
			allowWaves = true;
			visible = accessible = false;
			updateLighting = false;

			defaultEnv = Env.terrestrial | Env.groundWater | Env.oxygen;
			clearSectorOnLose = true;
			ruleSetter = r -> {
				r.waveTeam = Team.crux;
				r.placeRangeCheck = false;
				r.showSpawns = false;
			};

			iconColor = Pal.lancerLaser;
			startSector = 0;

			defaultCore = SWStorage.coreScaffold;

			generator = new ModularPlanetGenerator() {{
				defaultLoadout = Schematics.readBase64("bXNjaAF4nGNgZmBmZmDJS8xNZeBNSizOTA5OTkxLy89JYeBOSS1OLsosKMnMz2NgYGDLSUxKzSlmYIqOZWQQKC7XTc4vStUthqlmYGAEISABAKHWFQU=");
			}};
		}};
	}
}

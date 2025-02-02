package sw.content;

import arc.graphics.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.graphics.*;
import mindustry.graphics.g3d.*;
import mindustry.type.*;
import sw.content.blocks.*;
import sw.maps.generators.*;

public class SWPlanets {
	public static Planet wendi;

	public static void load() {
		wendi = new Planet("wendi", Planets.sun, 1f, 3) {{
			meshLoader = () -> new HexMesh(this, 7);

			sectorSeed = 2;
			allowWaves = true;
			hasAtmosphere = true;
			allowLaunchToNumbered = updateLighting = drawOrbit = false;
			alwaysUnlocked = true;
			orbitRadius = 12831f;

			ruleSetter = r -> {
				r.waveTeam = Team.crux;
				r.placeRangeCheck = false;
				r.showSpawns = false;
			};

			iconColor = Pal.lancerLaser;
			atmosphereColor = Pal.lancerLaser.cpy().mul(0.3f);
			atmosphereRadIn = 0f;
			atmosphereRadOut = 0.2f;
			startSector = 0;

			itemWhitelist.addAll(
				SWItems.verdigris,
				SWItems.iron,
				SWItems.compound,
				SWItems.denseAlloy,
				SWItems.thermite,
				SWItems.aluminium,
				SWItems.steel,
				SWItems.oxycarbide,
				SWItems.coke,
				SWItems.iron,
				Items.graphite,
				Items.silicon
			);

			defaultCore = SWStorage.coreScaffold;

			generator = new ModularPlanetGenerator() {{
				baseColor = Color.black;
				defaultLoadout = Schematics.readBase64("bXNjaAF4nGNgZmBmZmDJS8xNZeBNSizOTA5OTkxLy89JYeBOSS1OLsosKMnMz2NgYGDLSUxKzSlmYIqOZWQQKC7XTc4vStUthqlmYGAEISABAKHWFQU=");
			}};
		}};
	}
}

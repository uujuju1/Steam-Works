package sw.content;

import arc.math.geom.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.graphics.*;
import mindustry.graphics.g3d.*;
import mindustry.type.*;
import sw.content.blocks.*;
import sw.dream.*;
import sw.maps.*;
import sw.maps.generators.*;

public class SWPlanets {
	public static Planet wendi, unknown;

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
			startSector = 69;

			itemWhitelist.addAll(
				SWItems.nickel,
				SWItems.iron,
				SWItems.arsenic,
				SWItems.compound,
				SWItems.denseAlloy,
				SWItems.thermite,
				SWItems.bismuth,
				SWItems.scorch,
				SWItems.graphene,
				Items.graphite,
				Items.silicon
			);

			defaultCore = SWStorage.coreScaffold;

			generator = new ModularPlanetGenerator() {{
				baseColor = SWEnvironment.soil.mapColor;
				defaultLoadout = Schematics.readBase64("bXNjaAF4nGNgZmBmZmDJS8xNZeBNSizOTA5OTkxLy89JYeBOSS1OLsosKMnMz2NgYGDLSUxKzSlmYIqOZWQQKC7XTc4vStUthqlmYGAEISABAKHWFQU=");
			}};
		}};
		unknown = new Planet("unknown", Planets.sun, 1f, 1) {{
			defaultCore = DreamContent.brutalistCore;
			hasAtmosphere = updateLighting = false;
			clearSectorOnLose = true;
			meshLoader = () -> new HexMesh(this, 7);
			generator = new ModularPlanetGenerator() {{
				defaultLoadout = Schematics.readBase64("bXNjaAF4nGNgYmBiZmDJS8xNZeB1zi9KjUxPTylKLM7MYeBOSS1OLsosKMnMz2NgYGDLSUxKzSlmYIqOZWQQLC7XTSoqLUnMySwu0U0G6gOqYGQAA0YAwcYVpg==");
				baseColor = DreamContent.voidTile.mapColor;
				sectors.each(sector -> {
					heights.add(new HeightPass.SphereHeight() {{
						pos.set(sector.tile.v);
						radius = 0.1f;
						offset = 0.5f;
						set = true;
					}});
					colors.add(new ColorPass.SphereColorPass(new Vec3(sector.tile.v), 0.15f, DreamContent.concrete.mapColor));
				});
			}};
			drawOrbit = false;
			orbitRadius = 1000f;
			solarSystem = this;
		}};
	}
}

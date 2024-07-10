package sw.content;

import arc.graphics.*;
import arc.math.geom.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.graphics.g3d.*;
import mindustry.type.*;
import sw.content.blocks.*;
import sw.maps.*;
import sw.maps.generators.*;

public class SWPlanets {
	public static Planet wendi, unknown;

	public static void load() {
		wendi = new Planet("wendi", Planets.sun, 1f, 2) {{
			meshLoader = () -> new HexMesh(this, 6);

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

			iconColor = Color.valueOf("469662");
			hasAtmosphere = false;
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

			defaultCore = SWBlocks.coreScaffold;

			generator = new ModularPlanetGenerator() {{
				heights.addAll(
					new HeightPass.CraterHeight() {{
						pos.set(Vec3.X);
						radius = 1f;
						offset = 0.7f;
						set = true;
					}},
					new HeightPass.CraterHeight() {{
						pos.set(Vec3.Y);
						radius = 0.8f;
						offset = 0.7f;
						set = true;
					}},
					new HeightPass.CraterHeight() {{
						pos.set(Vec3.Z);
						radius = 0.6f;
						offset = 0.7f;
						set = true;
					}},
					new HeightPass.CraterHeight() {{
						pos.set(Vec3.X).scl(-1f).rotate(new Vec3(Vec3.Z).scl(-1f), 45f);
						radius = 0.5f;
						offset = 0.7f;
						set = true;
					}},
					new HeightPass.CraterHeight() {{
						pos.set(Vec3.Y).scl(-1f).rotate(new Vec3(Vec3.X).scl(-1f), 75f);
						radius = 0.3f;
						offset = 0.7f;
						set = true;
					}},
					new HeightPass.NoiseHeight() {{
						offset.set(100, 220, 50442);
						scale = 1;
						octaves = 7;
						persistence = 0.5;
						magnitude = 2;
						heightOffset = -2;
					}},
					new HeightPass.ClampHeight(-0.7f, 1f)
				);
				baseColor = SWEnvironment.soil.mapColor;
				colors.addAll(
					new ColorPass.NoiseColorPass() {{
						offset.set(100, 220, 50442);
						seed = 5;
						scale = 1;
						octaves = 7;
						persistence = 0.5;
						magnitude = 2;
						out = SWEnvironment.gravel.mapColor;
					}},
					new ColorPass.NoiseColorPass() {{
						offset.set(100, 220, 50442);
						seed = 2;
						scale = 1;
						octaves = 7;
						persistence = 0.5;
						magnitude = 2;
						out = SWEnvironment.roots.mapColor;
					}},
					new ColorPass.FlatColorPass() {{
						min = max = -0.7f;
						out = SWEnvironment.solventRegular.mapColor;
					}}
				);
				defaultLoadout = Schematics.readBase64("bXNjaAF4nGNgZmBmZmDJS8xNZeBNSizOTA5OTkxLy89JYeBOSS1OLsosKMnMz2NgYGDLSUxKzSlmYIqOZWQQKC7XTc4vStUthqlmYGAEISABAKHWFQU=");
			}};
		}};
//		unknown = new Planet("unknown", null, 0.12f) {{
//			defaultCore = DreamContent.brutalistCore;
//			hasAtmosphere = false;
//			sectors.add(new Sector(this, PlanetGrid.Ptile.empty));
//			meshLoader = () -> new HexMesh(this, 1);
//			generator = new UnknownPlanetGenerator();
//			orbitRadius = 4;
//		}};
	}
}

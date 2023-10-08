package sw.content;

import arc.graphics.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.graphics.g3d.*;
import mindustry.type.*;
import sw.content.blocks.*;
import sw.maps.generators.*;

public class SWPlanets {
	public static Planet wendi;

	public static void load() {
		wendi = new Planet("wendi", Planets.sun, 1f, 2) {{
			meshLoader = () -> new HexMesh(this, 6);

			sectorSeed = 2;
			allowWaves = true;
			hasAtmosphere = true;
			allowLaunchToNumbered = false;
			alwaysUnlocked = true;

			ruleSetter = r -> {
				r.waveTeam = Team.crux;
				r.placeRangeCheck = false;
				r.showSpawns = false;
			};

			iconColor = atmosphereColor = Color.valueOf("469662");
			atmosphereRadIn = -0.1f;
			atmosphereRadOut = 0.25f;
			startSector = 1;

			itemWhitelist.addAll(
				SWItems.nickel, SWItems.compound, SWItems.denseAlloy, SWItems.neodymium, SWItems.frozenMatter, Items.graphite, Items.titanium, Items.silicon
			);

			generator = new ModularPlanetGenerator() {{
				minHeight = -5f;
				heights.add(
					new Noise3DSettings() {{
						offset.set(453, 259, -345);
						mag = 5f;
						scl = 1f;
						heightOffset = -3f;
						max = 2f;
						min = -1f;
					}},
					new Noise3DSettings() {{
						offset.set(389, 583, -745);
						max = 0.5f;
						mag = 6f;
						scl = 1.2f;
						heightOffset = -5f;
					}}
				);
				colors.add(
					new ColorPatch() {{
						block = SWEnvironment.heavySnow;
						noise = new Noise3DSettings() {{
							min = max = 1;
						}};
					}},
					new ColorPatch() {{
						block = SWEnvironment.charoite;
						noise = new Noise3DSettings() {{
							min = max = 0;
						}};
					}},
					new ColorPatch() {{
						block = SWEnvironment.purpleSand;
						noise = new Noise3DSettings() {{
							offset.set(453, 259, -345);
							max = 0f;
							min = -0.5f;
							mag = 3f;
							scl = 2f;
							heightOffset = -1.75f;
						}};
						maxT = 1f;
						minT = -10f;
					}},
					new ColorPatch() {{
						block = SWEnvironment.heavyWater;
						noise = new Noise3DSettings() {{
							min = max = -0.5f;
						}};
						maxT = minT = -0.5f;
					}}
				);
			}};
		}};
	}
}

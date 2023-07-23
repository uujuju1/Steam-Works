package sw.content;

import arc.graphics.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.graphics.g3d.*;
import mindustry.type.*;
import sw.maps.generators.*;

public class SWPlanets {
	public static Planet aboba;

	public static void load() {
		aboba = new Planet("aboba", Planets.sun, 1f, 2) {{
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
						color = Color.white;
						noise = new Noise3DSettings() {{
							offset.set(389, 583, -745);
							min = max = -0.5f;
							mag = 8f;
							scl = 1.2f;
							heightOffset = -3f;
						}};
						minT = -5;
						maxT = 0;
					}},
					new ColorPatch() {{
						color = Blocks.water.mapColor;
						noise = new Noise3DSettings() {{
							offset.set(389, 583, -745);
							min = max = 0f;
						}};
					}}
				);
			}};
			meshLoader = () -> new HexMesh(this, 6);

			sectorSeed = 2;
			allowWaves = true;
			hasAtmosphere = false;

			ruleSetter = r -> {
				r.waveTeam = Team.crux;
				r.placeRangeCheck = false;
				r.showSpawns = false;
			};
			iconColor = Color.valueOf("7d4dff");
//			atmosphereColor = Color.valueOf("3c1b8f");
//			atmosphereRadIn = 0.02f;
//			atmosphereRadOut = 0.3f;
			startSector = 0;
			alwaysUnlocked = true;
		}};
	}
}

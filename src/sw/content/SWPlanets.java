package sw.content;

import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.noise.*;
import mindustry.ai.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.graphics.g3d.*;
import mindustry.type.*;
import mindustry.world.*;
import sw.maps.generators.*;

import static sw.content.blocks.SWEnvironment.*;

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

			generator = new ModularPlanetGenerator() {
				@Override
				public float getSizeScl(){
					return 2000 * 1.07f * 5f / 5f;
				}{
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
						block = sediment;
						noise = new Noise3DSettings() {{
							min = max = 1;
						}};
					}},
					new ColorPatch() {{
						block = charoite;
						noise = new Noise3DSettings() {{
							min = max = 0;
						}};
					}},
					new ColorPatch() {{
						block = grime;
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
						block = chloro;
						noise = new Noise3DSettings() {{
							min = max = -0.5f;
						}};
						maxT = minT = -0.5f;
					}}
				);

				Seq<Block> blocks = Seq.with(moss, sediment, grime, charoite);
				gen = generator -> {
					pass((x, y) -> {
						block = Blocks.stoneWall;
					});
					Vec2 starts = new Vec2().trns(rand.random(360f), width/2.6f);
					Vec2 copy = new Vec2(starts);

					// region base terrain

					pass((x, y) -> {
						if (
							Mathf.dst(width / 2 - x, height / 2 - y) < width/2.6f &&
							noise(x + 1000, y, 3, 0.5, 80, 1) < 0.5
						) {
							block = Blocks.air;
						}
					});

//					inverseFloodFill(tiles.get(Math.round(starts.x), Math.round(starts.y)));
					Log.info(tiles.get(Math.round(starts.x), Math.round(starts.y)));
					brush(Astar.pathfind(
						Math.round(width/2f + starts.x), Math.round(height/2f + starts.y),
						Math.round(width/2f - starts.x), Math.round(height/2f - starts.y),
						tile -> tile.solid() ? 3000 : 0,
						Mathf::dst,
						tile -> true
					), 7);

					distort(39, 5);
					distort(39, 5);
					distort(19, 17);
					distort(100, 17);
					median(4, 0.5f);
					distort(100, 17);

					//endregion
					// region floors

					pass((x, y) -> {
						for (int i = 0; i < blocks.size; i++) {
							if (noise(x + 500 + 500 * i, y, 4.3, 0.7, 101 + 33 * i, 1) > (i == 0 ? 0 : 0.6f)) {
								floor = blocks.get(i);
								if (floor.asFloor().wall != Blocks.air && block != Blocks.air) block = floor.asFloor().wall;
							}
						}

						float waterNoise = noise(x + 500, y, 8, 0.7, 200, 1);
						if (waterNoise > 0.6f) floor = chloro;

					});
					if (getHeight(sector.tile.v) * 10 < 0.2f) Astar.pathfind(
						Math.round(width / 2f - copy.x),
						Math.round(height / 2f - copy.y),
						Math.round(width / 2f + copy.x),
						Math.round(height / 2f + copy.y),
						tile -> (tile.floor().isLiquid ? 0 : 300) + (tile.solid() ? 3000 : 0),
						Mathf::dst,
						tile -> true
					).each(tile -> {
						replace(tile.centerX(), tile.centerY(), 4, chloro, null, null);
					});
					blend(chloro, shallowChloro, 5f);
					if (getHeight(sector.tile.v) * 10 < 0.2f) Log.info(getHeight(sector.tile.v) * 10 + " naval sector");
					pass((x, y) -> {
						float waterNoise = noise(x + 500, y, 8, 0.7, 200, 1);
						if (waterNoise > 0.7f) floor = deepChloro;
						if (
							!nearFloor(x, y, 3, shallowChloro)
							&& tiles.get(x, y).dst(Math.round(width / 2f + copy.x), Math.round(height / 2f + copy.y)) > 30f
							&& floor == chloro
							&& getHeight(sector.tile.v) * 10 < 0.2f
						) floor = deepChloro;

						if(
							noise(x + 500, y - 500, 7, 0.5, 500, 1) > 0.5 &&
							(floor == chloro || floor == shallowChloro || floor == deepChloro) &&
							getHeight(sector.tile.v) * 10 > 0.2f
						) floor = chloroIce;
						if ((floor == chloro || floor == shallowChloro || floor == deepChloro) && block != Blocks.air) block = floor.asFloor().wall;
					});

					median(2, 0.6f);
					if (Simplex.noise3d(seed, 2, 0.5, 1, sector.tile.v.x + 2, sector.tile.v.y, sector.tile.v.z) > 0.5) {
						Block craterBlock = scorched;
						for (int i = 0; i < Mathf.maxZero(sector.id % 10 - 4); i++) {
							Vec2 craterpos = new Vec2().trns(rand.random(360f), width/2.6f * rand.nextFloat());
							pass((x, y) -> {
								if (Mathf.dst(x, y, width/2f + craterpos.x, width/2f + craterpos.y) < 16) {
									floor = craterBlock;
									if (block != Blocks.air) block = craterBlock.asFloor().wall;
								}
								float noise = noise(x + 1000, y, 8, 0.7, 50, 1);
								if (Mathf.dst(x, y, width/2f + craterpos.x, width/2f + craterpos.y) < 20 && noise < 0.5f) {
									floor = craterBlock;
									if (block != Blocks.air) block = craterBlock.asFloor().wall;
								}
							});
						}
					}
					distort(20, 9);
					median(2, 0.6f);

					// endregion
					// region ores

					pass((x, y) -> {
						if(!floor.asFloor().hasSurface()) return;

						int offsetX = x - 4, offsetY = y + 23;
						if(Math.abs(0.5f - noise(offsetX, offsetY + 999, 2, 0.7, 42)) > 0.22f + 0.01 &&
							   Math.abs(0.5f - noise(offsetX, offsetY - 999, 1, 1, 34)) > 0.37f){
							ore = oreNickel;
						}
						if(nearAir(x, y) && noise(x + 78, y, 4, 0.7f, 33f, 1f) > 0.62f){
							if (block == charoiteWall) block = graphiteCharoiteWallOre;
							if (block == grimeWall) block = graphiteGrimeWallOre;
						}
						if (noise(x + 500, y, 8, 0.7, 50, 1) > 0.7f && floor == scorched) {
							ore = Blocks.oreTitanium;
						}
					});

					// endregion

					erase(Math.round(width/2f + copy.x), Math.round(height/2f + copy.y), 10);
					erase(Math.round(width/2f - copy.x), Math.round(height/2f - copy.y), 10);

					Schematics.placeLaunchLoadout(Math.round(width/2f + copy.x), Math.round(height/2f + copy.y));
					tiles.get(Math.round(width/2f - copy.x), Math.round(height/2f - copy.y)).setOverlay(Blocks.spawn);

//					float threat = Math.abs(sector.tile.v.y) * 10f;

//					Vars.state.rules.waves = true;
//					Vars.state.rules.winWave = sector.info.winWave = (int) (threat * 15);
//					Vars.state.rules.spawns = SWWaveGeneration.navalGen(threat, Vars.state.rules.winWave, new Rand(sector.id));
				};
				defaultLoadout = Schematics.readBase64("bXNjaAF4nGNgYWBhZmDJS8xNZeBNSizOTA5OTkxLy89JYeBOSS1OLsosKMnMz2NgYGDLSUxKzSlmYIqOZWQQKC7XTc4vStUthqlmYGAEISAEAKJ5FQg=");
			}};
		}};
	}
}

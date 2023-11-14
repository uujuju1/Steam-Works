package sw.content;

import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.noise.*;
import mindustry.*;
import mindustry.ai.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.graphics.g3d.*;
import mindustry.type.*;
import mindustry.world.*;
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
				SWItems.nickel,
				SWItems.compound,
				SWItems.denseAlloy,
				SWItems.thermite,
				SWItems.bismuth,
				SWItems.scorch,
				SWItems.graphene,
				Items.graphite,
				Items.titanium,
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
						block = SWEnvironment.sediment;
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
						block = SWEnvironment.grime;
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
						block = SWEnvironment.chloro;
						noise = new Noise3DSettings() {{
							min = max = -0.5f;
						}};
						maxT = minT = -0.5f;
					}}
				);

				Seq<Block> blocks = Seq.with(SWEnvironment.moss, SWEnvironment.sediment, SWEnvironment.grime, SWEnvironment.charoite);
				gen = generator -> {
					pass((x, y) -> {
						block = Blocks.stoneWall;
					});
					Vec2 starts = new Vec2().trns(rand.random(360f), width/2.6f);
					Vec2 copy = new Vec2(starts);

					// region base terrain
					brush(Astar.pathfind(
						Math.round(width/2f + starts.x), Math.round(height/2f + starts.y),
						Math.round(width/2f - starts.x), Math.round(height/2f - starts.y),
						tile -> 0,
						Mathf::dst,
						tile -> true
					), 12);

					for(int i = 0; i < 3; i++) {
						Seq<Room> rooms1 = rooms(
							5,
							Angles.angle(-starts.x, -starts.y),
							45f + rand.range(10f),
							80f,
							40f,
							64f,
							16f,
							Math.round(width / 2f + starts.x),
							Math.round(height / 2f + starts.y)
						);
						rooms1.each(room -> {
							if (room.child != null) {
								brush(Astar.pathfind(
									room.x, room.y,
									room.child.x, room.child.y,
									tile -> 0,
									Mathf::dst,
									tile -> true
								), Math.round((room.rad + room.child.rad) / 8));
							}
						});
						Seq<Room> rooms2 = rooms(
							5,
							Angles.angle(starts.x, starts.y),
							45f + rand.range(10f),
							80f,
							40f,
							64f,
							16f,
							Math.round(width / 2f - starts.x),
							Math.round(height / 2f - starts.y)
						);
						rooms2.each(room -> {
							if (room.child != null) {
								brush(Astar.pathfind(
									room.x, room.y,
									room.child.x, room.child.y,
									tile -> 0,
									Mathf::dst,
									tile -> true
								), Math.round((room.rad + room.child.rad) / 8));
							}
							tiles.get(room.x, room.y).setBlock(Blocks.sandWall);
						});

						brush(Astar.pathfind(
							rooms1.peek().x, rooms1.peek().y,
							rooms2.peek().x, rooms2.peek().y,
							tile -> 0,
							Mathf::dst,
							tile -> true
						), 12);
						starts = new Vec2().trns(rand.random(360f), width/2.6f);
					}

					distort(39, 5);
					distort(39, 5);
					distort(19, 17);
					distort(100, 17);
					median(4, 0.5f);
					distort(100, 17);

					//endregion
					// region floors
					pass((x, y) -> {
						floor = blocks.getFrac(noise(x - 500, y + 1000, 5, 0.7, 270, 1));
						if (floor.asFloor().wall != Blocks.air && block != Blocks.air) block = floor.asFloor().wall;

						float waterNoise = noise(x + 500, y, 8, 0.7, 200, 1);
						if (waterNoise > 0.6f) floor = SWEnvironment.chloro;

					});
					if (getHeight(sector.tile.v) * 10 < 0.2f) pathfind(
						Math.round(width / 2f - copy.x),
						Math.round(height / 2f - copy.y),
						Math.round(width / 2f + copy.x),
						Math.round(height / 2f + copy.y),
						tile -> (tile.floor().isLiquid ? 0 : 300) + (tile.solid() ? 300 : 0),
						Mathf::dst
					).each(tile -> {
						replace(tile.centerX(), tile.centerY(), 4, SWEnvironment.chloro, null, null);
					});
					blend(SWEnvironment.chloro, SWEnvironment.shallowChloro, 5f);
					if (getHeight(sector.tile.v) * 10 < 0.2f) Log.info(getHeight(sector.tile.v) * 10 + " naval sector");
					pass((x, y) -> {
						float waterNoise = noise(x + 500, y, 8, 0.7, 200, 1);
						if (waterNoise > 0.7f) floor = SWEnvironment.deepChloro;
						if (
							!nearFloor(x, y, 3, SWEnvironment.shallowChloro)
							&& tiles.get(x, y).dst(Math.round(width / 2f + copy.x), Math.round(height / 2f + copy.y)) > 30f
							&& floor == SWEnvironment.chloro
							&& getHeight(sector.tile.v) * 10 < 0.2f
						) floor = SWEnvironment.deepChloro;
						
						if(
							noise(x + 500, y - 500, 7, 0.5, 500, 1) > 0.5 &&
							(floor == SWEnvironment.chloro || floor == SWEnvironment.shallowChloro || floor == SWEnvironment.deepChloro) &&
							getHeight(sector.tile.v) * 10 > 0.2f
						) {
							floor = Blocks.ice;
							if (block != Blocks.air) block = Blocks.iceWall;
						}
					});

					median(2, 0.6f);
					if (Simplex.noise3d(seed, 2, 0.5, 1, sector.tile.v.x + 2, sector.tile.v.y, sector.tile.v.z) > 0.5) {
						Block craterBlock = SWEnvironment.purpleIce;
						Log.info(Mathf.maxZero(sector.id % 10 - 4) + " craters");
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
							ore = SWEnvironment.oreNickel;
						}
						if(nearAir(x, y) && noise(x + 78, y, 4, 0.7f, 33f, 1f) > 0.62f){
							if (block == SWEnvironment.charoiteWall) block = SWEnvironment.graphiteCharoiteWallOre;
							if (block == SWEnvironment.grimeWall) block = SWEnvironment.graphiteGrimeWallOre;
						}
						if (noise(x + 500, y, 8, 0.7, 50, 1) > 0.7f && floor == SWEnvironment.purpleIce) {
							ore = Blocks.oreTitanium;
						}
					});

					// endregion

					erase(Math.round(width/2f + copy.x), Math.round(height/2f + copy.y), 10);
					erase(Math.round(width/2f - copy.x), Math.round(height/2f - copy.y), 10);

					Schematics.placeLaunchLoadout(Math.round(width/2f + copy.x), Math.round(height/2f + copy.y));
					tiles.get(Math.round(width/2f - copy.x), Math.round(height/2f - copy.y)).setOverlay(Blocks.spawn);

					float threat = Math.abs(sector.tile.v.y) * 10f;

					Vars.state.rules.waves = true;
					Vars.state.rules.winWave = sector.info.winWave = (int) (threat * 15);
					Vars.state.rules.spawns = SWWaveGeneration.navalGen(threat, Vars.state.rules.winWave, new Rand(sector.id));
				};
				defaultLoadout = Schematics.readBase64("bXNjaAF4nGNgYWBhZmDJS8xNZeBNSizOTA5OTkxLy89JYeBOSS1OLsosKMnMz2NgYGDLSUxKzSlmYIqOZWQQKC7XTc4vStUthqlmYGAEISAEAKJ5FQg=");
			}};
		}};
	}
}

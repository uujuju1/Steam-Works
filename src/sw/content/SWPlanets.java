package sw.content;

import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.geom.Point2;
import arc.math.geom.Vec2;
import arc.math.geom.Vec3;
import arc.struct.Seq;
import arc.util.Tmp;
import arc.util.noise.Simplex;
import mindustry.Vars;
import mindustry.ai.Astar;
import mindustry.content.Blocks;
import mindustry.content.Planets;
import mindustry.game.Schematics;
import mindustry.game.Team;
import mindustry.game.Waves;
import mindustry.graphics.g3d.HexMesh;
import mindustry.graphics.g3d.HexSkyMesh;
import mindustry.graphics.g3d.MultiMesh;
import mindustry.type.Planet;
import mindustry.world.Block;
import mindustry.world.meta.Env;
import sw.maps.generators.ModularPlanetGenerator;

public class SWPlanets {
  public static Planet orion;

  public static void load() {
    orion = new Planet("orion", Planets.sun, 1f, 1) {
      {
      meshLoader = () -> new HexMesh(this, 5);
      cloudMeshLoader = () -> new MultiMesh(
              new HexSkyMesh(this, 3, -0.4f, 0.13f, 5, Color.valueOf("8F5538").mul(0.8f).a(0.75f), 2, 0.45f, 0.9f, 0.38f),
              new HexSkyMesh(this, 8, 0.4f, 0.128f, 5, Color.valueOf("8F5538").a(0.75f), 2, 0.45f, 0.9f, 0.2f)
      );
      sectorSeed = 5;
      alwaysUnlocked = true;
      atmosphereColor = iconColor = Color.valueOf("DE5D3A");
      atmosphereRadIn = 0.09f;
      defaultCore = SWBlocks.coreScaffold;
      defaultEnv = Env.terrestrial;
      ruleSetter = r -> {
        r.lighting = true;
        r.ambientLight = atmosphereColor.cpy().a(0.3f);
        r.waveTeam = Team.crux;
        r.attributes.clear();
      };
      generator = new ModularPlanetGenerator() {{
        seed = 5;
        octaves = 7.0;
        persistence = 0.8;
        scl = 0.7;
        sizeScl = 1280f;
        regions = new NoiseRegion[]{
          new NoiseRegion() {{
            map = new Block[]{Blocks.basalt, Blocks.stone, Blocks.sand};
            clamp = true;
            scl = 1.5;
            persistence = 0.3;
            octaves = 12;
          }},
          new NoiseRegion() {{
            map = new Block[]{Blocks.darksand, Blocks.dirt, Blocks.dirt, Blocks.dirt};
            seed = 2;
            scl = 1.5;
            persistence = 0.3;
            octaves = 12;
            min = 0.7f;
          }},
        };
      }
        @Override
        protected void generate() {
          pass((x, y) -> {
            Vec3 offset = new Vec3(
              x/300f,
              y/300f,
              width/300f - (x/300f)
            ).add(sector.tile.v);
            floor = getBlock(offset);
          });
          cells(10, 16, 16, 10);

          pass((x, y) -> {
            if (noise((float) x, (float) y, 3, 0.4, 60, 1.0) > 0.3f) block = floor.asFloor().wall;
          });


          Vec2 trns = Tmp.v1.trns(rand.random(360f), width/3f);
          Point2 start = new Point2((int) (trns.x + (width/2f)), (int) (trns.y + (height/2f))),
                 end = new Point2((int) (-trns.x + (width/2f)), (int) (-trns.y + (height/2f)));

          brush(pathfind(start.x, start.y, end.x, end.y, (tile) -> (tile.solid() ? 300f : 0f) + Mathf.dst(width/2f, height/2f) - tile.dst(width/2f, height/2f) / 10f, Astar.manhattan), 14);
          for (int i = 1; i <= 3; i++) distort(100f / i, 40f / i);
          brush(pathfind(start.x, start.y, end.x, end.y, (tile) -> (tile.solid() ? 300f : 0f) + Mathf.dst(width/2f, height/2f) - tile.dst(width/2f, height/2f) / 10f, Astar.manhattan), 9);
          distort(32f, 12f);
          distort(40f, 16f);
          erase(start.x, start.y, 20);
          erase(end.y, end.y, 20);
          distort(21f, 11f);
          median(4, 0.1);

          inverseFloodFill(tiles.getn(start.x, start.y));

          pass((x, y) -> {
            if (noise(x, y, 13, 0.5, 13, 1) > 0.7f && block.isAir()) block = floor.asFloor().wall;
          });

          Seq<Block> ores = Seq.with(Blocks.oreCopper, Blocks.oreLead);
          float poles = Math.abs(sector.tile.v.y);
          if (Simplex.noise3d(this.seed, 2.0, 0.5, scl, sector.tile.v.x + 100, sector.tile.v.y, sector.tile.v.z) * 0.5f + poles > 0.25F * 1.5f) {
            ores.add(Blocks.oreCoal);
          }
          if (Simplex.noise3d(this.seed, 2.0, 0.5, scl, sector.tile.v.x + 200, sector.tile.v.y, sector.tile.v.z) * 0.5f + poles > 0.5f * 1.5f) {
            ores.add(Blocks.oreTitanium);
          }
          if (Simplex.noise3d(this.seed, 2.0, 0.5, scl, sector.tile.v.x + 300, sector.tile.v.y, sector.tile.v.z) * 0.5f + poles > 0.7f * 1.5f) {
            ores.add(Blocks.oreThorium);
          }

          pass((x, y) -> {
            for (Block nOre : ores) {
              if (noise(x + (ores.indexOf(nOre) * 1000f), y + (ores.indexOf(nOre) * 100f), 16, 0.5, 20, 1) > 0.75f + (0.02f * ores.indexOf(nOre))) {
                ore = nOre;
              }
            }
          });

          Schematics.placeLaunchLoadout(start.x, start.y);
          tiles.getn(end.x, end.y).setOverlay(Blocks.spawn);

          float difficulty = sector.threat;

          Vars.state.rules.env = sector.planet.defaultEnv;
          Vars.state.rules.waves = true;
          Vars.state.rules.waveSpacing = 3600f * difficulty * 4f + ores.size;
          Vars.state.rules.winWave = Math.max(10, 10 * ((int) (difficulty)) * 2);
          Vars.state.rules.spawns = new Waves().get();
        }
      };
    }};
  }
}

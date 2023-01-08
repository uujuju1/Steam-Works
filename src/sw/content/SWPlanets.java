package sw.content;

import arc.graphics.Color;
import mindustry.content.Blocks;
import mindustry.content.Planets;
import mindustry.graphics.g3d.HexMesh;
import mindustry.graphics.g3d.HexSkyMesh;
import mindustry.graphics.g3d.MultiMesh;
import mindustry.type.Planet;
import mindustry.world.Block;
import sw.maps.generators.ModularPlanetGenerator;

public class SWPlanets {
  public static Planet orion;

  public static void load() {
    orion = new Planet("orion", Planets.sun, 1f, 1) {{
      meshLoader = () -> new HexMesh(this, 5);
      cloudMeshLoader = () -> new MultiMesh(new HexSkyMesh(this, 3, -0.4f, 0.13f, 5, Color.valueOf("8F5538"), 2, 0.45f, 0.9f, 0.38f));
      sectorSeed = 5;
      alwaysUnlocked = true;
      atmosphereColor = iconColor = Color.valueOf("DE5D3A");
      atmosphereRadIn = 0.09f;
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
      }};
    }};
  }
}

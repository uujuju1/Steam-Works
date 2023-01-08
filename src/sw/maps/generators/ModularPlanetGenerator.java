package sw.maps.generators;

import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.geom.Vec3;
import arc.util.noise.Simplex;
import mindustry.content.Blocks;
import mindustry.maps.generators.PlanetGenerator;
import mindustry.world.Block;

/**
 * @author uujuju
 */
public class ModularPlanetGenerator extends PlanetGenerator {
  // scale for sector size
  public float sizeScl = 3200;
  // Height noise params
  public double octaves = 1, persistence = 1, scl = 1;
  // Color noise regions
  public NoiseRegion[] regions = new NoiseRegion[0];
  // Block used when getBlock() would return null
  public Block defaultBlock = Blocks.grass;
  public class NoiseRegion {
    // Block heightMap
    public Block[] map = new Block[0];
    // Noise pos offset
    public Vec3 noiseOffset = new Vec3();
    // Noise seed
    public int seed = 1;
    // Noise params
    public double octaves = 1, persistence = 1, scl = 1;
    // Min an max values that the noise applies (ignored if clamp == true)
    public float min = 0f, max = 1;
    // Maps the block height map onto the entire planet (dont use it on the last noiseRegions of regions!)
    public boolean clamp = false;

    /**
     * Gets the current block based on the noise
     * doesnt care about min or max fields
     */
    public Block getBlock(Vec3 pos) {
      int i = (int) Mathf.map(
              Simplex.noise3d(seed, octaves, persistence, scl, noiseOffset.x + pos.x, noiseOffset.y + pos.y, noiseOffset.z + pos.z),
              0,
              map.length
      );
      return map[i];
    }

    /**
    * Gets the current block based on the noise
    * Returns null if out of bounds
    */
    public Block getBlockn(Vec3 pos) {
      float noise = Simplex.noise3d(seed, octaves, persistence, scl, noiseOffset.x + pos.x, noiseOffset.y + pos.y, noiseOffset.z + pos.z);
      if (noise > max || noise < min) return null;
      int i = (int) Mathf.map(Mathf.map(noise, min, max), min, max, 0, map.length);
      return map[i];
    }
  }

  @Override public float getHeight(Vec3 pos) {
    return Simplex.noise3d(seed, octaves, persistence, scl, pos.x, pos.y, pos.z);
  }
  @Override public Color getColor(Vec3 pos) {
    return getBlock(pos).mapColor;
  }
  @Override public float getSizeScl() {
    return sizeScl;
  }

  public Block getBlock(Vec3 pos) {
    Block out = null;
    for (NoiseRegion region : regions) {
      if (region.clamp) {
        out = region.getBlock(pos);
      } else if (region.getBlockn(pos) != null) out = region.getBlockn(pos);
    }
    if (out == null) return defaultBlock;
    return out;
  }
}

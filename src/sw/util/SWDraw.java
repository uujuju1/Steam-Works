package sw.util;

import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;

public class SWDraw {
  public static TextureRegion[] getRegions(TextureRegion base, int width, int height, int size) {
    int arraySize = width * height;
    TextureRegion[] out = new TextureRegion[arraySize];
    for (int i = 0; i < arraySize; i++) {
      TextureRegion n = new TextureRegion(base);
      float ix = i % width;
      float iy = Mathf.floor((float) i/width);

      n.width = n.height = size;
      n.u = Mathf.map(ix + 0.02f, 0, width, base.u, base.u2);
      n.u2 = Mathf.map(ix + 0.98f, 0, width, base.u, base.u2);
      n.v = Mathf.map(iy + 0.02f, 0, height, base.v, base.v2);
      n.v2 = Mathf.map(iy + 0.98f, 0, height, base.v, base.v2);

      out[i] = n;
    }
    return out;
  }
}

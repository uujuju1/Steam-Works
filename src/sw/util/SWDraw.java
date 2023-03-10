package sw.util;

import arc.graphics.Color;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import mindustry.graphics.Pal;

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

  public static void square(Color color, float x, float y, float rad, float rot) {
    float oldStroke = Lines.getStroke();
    Lines.stroke(oldStroke * 3f, Pal.gray);
    Lines.square(x, y, rad, rot);
    Lines.stroke(oldStroke, color);
    Lines.square(x, y, rad, rot);
  }
  public static void line(Color color, float x, float y, float angle, float length) {
    float oldStroke = Lines.getStroke();
    Lines.stroke(Lines.getStroke() * 3f, Pal.gray);
    Lines.lineAngle(x, y, angle, length);
    Lines.stroke(oldStroke, color);
    Lines.lineAngle(x, y, angle, length);
  }
}

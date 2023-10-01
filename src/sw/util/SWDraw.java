package sw.util;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.graphics.*;

public class SWDraw {
  public static final Color
    heatPal = Pal.accent.cpy(),
    denseAlloyBase = Color.valueOf("565666"),
    denseAlloyMiddle = Color.valueOf("989AA4"),
    denseAlloySerration = Color.valueOf("6E7080"),
    compoundBase = Color.valueOf("81726D"),
    compoundMiddle = Color.valueOf("BEB5B2"),
    compoundSerration = Color.valueOf("A69A96");

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
  public static TextureRegion getRegion(TextureRegion base, int width, int height, int size, int index) {
    return getRegions(base, width, height, size)[index];
  }

  public static void square(Color color, float x, float y, float rad, float rot) {
    float oldStroke = Lines.getStroke();
    Lines.stroke(oldStroke * 3f, Pal.gray);
    Lines.square(x, y, rad, rot);
    Lines.stroke(oldStroke, color);
    Lines.square(x, y, rad, rot);
  }
  public static void square(Color color, float sx, float sy, float rad) {
    float oldStroke = Lines.getStroke();
    float x = sx - rad, y = sy - rad;
    Lines.stroke(oldStroke * 3f, Pal.gray);
    Lines.line(x, y, x + rad*2f, y);
    Lines.line(x + rad*2f, y, x + rad*2f, y +  rad*2f);
    Lines.line(x + rad*2f, y +  rad*2f, x, y +  rad*2f);
    Lines.line(x, y +  rad*2f, x, y);

    Lines.stroke(oldStroke, color);
    Lines.line(x, y, x + rad*2f, y);
    Lines.line(x + rad*2f, y, x + rad*2f, y +  rad*2f);
    Lines.line(x + rad*2f, y +  rad*2f, x, y +  rad*2f);
    Lines.line(x, y +  rad*2f, x, y);
  }
  public static void line(Color color, float x, float y, float angle, float length) {
    float oldStroke = Lines.getStroke();
    Lines.stroke(Lines.getStroke() * 3f, Pal.gray);
    Lines.lineAngle(x, y, angle, length);
    Lines.stroke(oldStroke, color);
    Lines.lineAngle(x, y, angle, length);
  }
  public static void linePoint(Color middle, Color base, float x, float y, float x2, float y2) {
    float oldStroke = Lines.getStroke();
    Lines.stroke(Lines.getStroke() * 3f, base);
    Lines.line(x, y, x2, y2);
    Lines.stroke(oldStroke, middle);
    Lines.line(x, y, x2, y2);
  }
  public static void beltLine(Color serration, Color base, Color middle, float x, float y, float x2, float y2, float rot) {
    Vec2 p1 = new Vec2(x, y), p2 = new Vec2(x2, y2);
    int serrations = Mathf.ceil(p1.dst(p2) / 8f);
    float time = serrations * 20f;
    rot += rot > 0 ? time : -time;

    linePoint(middle, base, x, y, x2, y2);
    Draw.color(serration);

    for (int j = 0; j < serrations; j++) {
      float progress = ((rot + time/serrations * j) %time / time) + (rot > 0 ? 0f : 1f);

      Tmp.v2.set(p1).lerp(p2, progress);
      Fill.rect(Tmp.v2.x, Tmp.v2.y, 1, 3, Tmp.v1.set(p1).sub(p2).angle());
      Fill.rect(Tmp.v2.x, Tmp.v2.y, 3, 1, Tmp.v1.angle());
    }
  }
}

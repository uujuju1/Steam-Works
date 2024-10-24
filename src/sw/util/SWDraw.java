package sw.util;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import mindustry.graphics.*;

public class SWDraw {
//  public static final Color;

  public static TextureRegion[] getRegions(TextureRegion base, int rows, int columns, int width, int height) {
    int arraySize = rows * columns;
    TextureRegion[] out = new TextureRegion[arraySize];
    for (int i = 0; i < rows; i++) for (int j = 0; j < columns; j++) {

      out[i + j * rows] = new TextureRegion(base, i * width, j * height, width, height);
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
  public static void linePoint(Color middle, Color base, float x, float y, float x2, float y2) {
    float oldStroke = Lines.getStroke();
    Lines.stroke(Lines.getStroke() * 3f, base);
    Lines.line(x, y, x2, y2);
    Lines.stroke(oldStroke, middle);
    Lines.line(x, y, x2, y2);
  }

  public static void rotatingRects(TextureRegion[] regions, float x, float y, float width, float height, float rot, float angle) {
    int sides = regions.length;
    for (int i = 0; i < sides; i++) {
      float
        angle1 = angle + (360f/sides * i),
        angle2 = angle + (360f/sides) + (360f/sides * i),
        mod1 = Mathf.mod(angle1, 360f),
        mod2 = Mathf.mod(angle2, 360f);

      if (!(mod1 >= 180f && mod2 >= 180f)) {
        float
          cos1 = -Mathf.cos(angle1 * Mathf.degreesToRadians),
          cos2 = -Mathf.cos(angle2 * Mathf.degreesToRadians);
        if (mod1 > 180f) {
          cos1 = -1;
        } else if (mod2 > 180f) {
          cos2 = 1;
        }
        cos1 = Mathf.map(cos1, -1f, 1f, y - height / 2f, y + height / 2f);
        cos2 = Mathf.map(cos2, -1f, 1f, y - height / 2f, y + height / 2f);

        Draw.rect(regions[i], x, (cos1 + cos2) * 0.5f, width, cos2 - cos1, width * 0.5f, y - cos1, rot);
      }
    }
  }
}

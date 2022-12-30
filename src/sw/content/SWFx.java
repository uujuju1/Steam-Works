package sw.content;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.util.Tmp;
import mindustry.entities.Effect;
import mindustry.graphics.Pal;

public class SWFx {
  public static Effect
          tinCraft = new Effect(30f, e -> Angles.randLenVectors(e.id, 15, 40 * e.finpow(), (x, y) -> {
            Draw.color(Pal.accent);
            Lines.lineAngle(e.x + x, e.y + y, Angles.angle(e.x, e.y, e.x + x, e.y + y), 10 * e.foutpow());
            Draw.color(Color.darkGray);
            Fill.circle(e.x - x, e.y - y, 2 * e.fout());
          })),
          silverCraft = new Effect(60f, e -> {
            Draw.color(Color.darkGray);
            Lines.stroke(e.fout());
            Angles.randLenVectors(e.id, 15, 40 * e.finpow(), (x, y) -> Fill.circle(e.x - x, e.y - y, 2 * e.fout()));
            Lines.circle(e.x, e.y, 40 * e.finpow());
          }),

          quadPressureHoles = new Effect(60f, e -> {
            for (int i = 0; i < 4; i++) {
              float angle = 45 + 90 * i;
              Tmp.v1.trns(angle, 13);
              Angles.randLenVectors(e.id + i, 15, 40 * e.finpow(), angle, 15, (x, y) -> Fill.circle(
                      e.x + x + Tmp.v1.x,
                      e.y + y + Tmp.v1.y,
                      2 * e.fout()
              ));
            }
          });
}

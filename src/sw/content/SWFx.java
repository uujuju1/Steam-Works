package sw.content;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Mathf;
import mindustry.entities.Effect;
import mindustry.graphics.Pal;

public class SWFx {
  public static Effect
          compoundCraft = new Effect(30f, e -> Angles.randLenVectors(e.id, 15, 40 * e.finpow(), (x, y) -> {
            Draw.color(Pal.accent);
            Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 10 * e.foutpow());
            Draw.color(Color.darkGray);
            Fill.circle(e.x - x, e.y - y, 2 * e.fout());
          })),
          denseCraft = new Effect(60f, e -> {
            Draw.color(Color.darkGray);
            Lines.stroke(e.fout());
            Angles.randLenVectors(e.id, 15, 40 * e.finpow(), (x, y) -> Fill.circle(e.x - x, e.y - y, 2 * e.fout()));
            Lines.circle(e.x, e.y, 40 * e.finpow());
          }),
          baklerSiliconCraft = new Effect(30f, e -> Angles.randLenVectors(e.id, 15, 40 * e.finpow(), (x, y) -> {
            Draw.color(Pal.accent);
            Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 10 * e.foutpow());
            Draw.color(Color.darkGray);
            Fill.circle(e.x - x, e.y - y, 2 * e.fout());
          }));

}

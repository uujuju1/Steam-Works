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
          nickelCraft = new Effect(30f, e -> {
            Draw.color(Pal.accent);
            Angles.randLenVectors(e.id + 1, 10, e.finpow() * 30f, (x, y) -> Fill.circle(e.x + x, e.y + y, e.fout() * 3f));
          }),
          graphiteCraft = new Effect(30f, e -> {
            Draw.color(Pal.accent, Color.gray, e.fin());
            Angles.randLenVectors(e.id, 10, e.finpow() * 30f, (x, y) -> Lines.lineAngle(e.x + x, e.y + y, Angles.angle(x, y), 5f * e.foutpow()));
          }),
          baklerSiliconCraft = new Effect(30f, e -> Angles.randLenVectors(e.id, 15, 40 * e.finpow(), (x, y) -> {
            Draw.color(Pal.accent);
            Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 10 * e.foutpow());
            Draw.color(Color.darkGray);
            Fill.circle(e.x - x, e.y - y, 2 * e.fout());
          }));

}

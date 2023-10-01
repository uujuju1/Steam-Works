package sw.content;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.entities.*;
import mindustry.graphics.*;

public class SWFx {
  public static Effect
    sparks = new Effect(30f, e -> {
      Draw.color(Pal.accent);
      for (int i = 0; i < 4; i++) {
        float angle = i * 90f + 45f;
        Tmp.v1.trns(angle, 12).add(e.x, e.y);
        Angles.randLenVectors(e.id + i, 2, 8f * e.finpow(), 180f + angle, 15f, (x, y) -> {
          Lines.lineAngle(Tmp.v1.x + x, Tmp.v1.y + y, Mathf.angle(x, y), 4 * e.foutpow());
        });
      }
    }),
    longShootFlame = new Effect(30f, e -> {
      for (int i = 0; i < 4; i++) {
        Draw.color(Pal.lightFlame, Pal.darkFlame, Color.gray, Math.max(i/4f, e.fin()));
        Angles.randLenVectors(e.id + i, 10, 92f * e.finpow(), e.rotation, 10f, (x, y) -> {
          Fill.circle(e.x + x, e.y + y, 1f + 1.5f * e.fout());
        });
      }
    }),
    nickelCraft = new Effect(30f, e -> {
      Draw.color(Pal.accent);
      Angles.randLenVectors(e.id + 1, 10, e.finpow() * 30f, (x, y) -> Fill.circle(e.x + x, e.y + y, e.fout() * 3f));
    }),
    compoundCraft = new Effect(30f, e -> Angles.randLenVectors(e.id, 15, 40 * e.finpow(), (x, y) -> {
      Draw.color(Pal.accent);
      Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 10 * e.foutpow());
      Draw.color(Color.darkGray);
      Fill.circle(e.x - x, e.y - y, 2 * e.fout());
    })),
    neodymiumCraft = new Effect(60f, e -> {
      Draw.color(Pal.accentBack);
      Angles.randLenVectors(e.id, 10, 12f * e.finpow(), (x, y) -> {
        Fill.rect(e.x + x, e.y + y, 5f * e.fout(), 5f * e.fout(), 45f);
      });
    }),
    frozenMatterCraft = new Effect(60f, e -> {
      for (int i = 0; i < 4; i++) {
        Draw.color(Pal.lancerLaser);
        float angle = i * 90f;
        Angles.randLenVectors(e.id + i, 10, 20 * e.finpow(), i * 90 + 45, 25, (x, y) -> {
          Fill.rect(
            e.x + x + Angles.trnsx(angle, 4, 4),
            e.y + y + Angles.trnsy(angle, 4, 4),
            5 * e.fout(),
            5 * e.fout(),
            45
          );
        });

        Drawf.tri(
          e.x + Angles.trnsx(angle, 3, 3),
          e.y + Angles.trnsy(angle, 3, 3),
          8 * e.foutpow(),
          16 * e.finpow(),
          i * 90 + 45
        );
        Draw.color(Color.white);
        Drawf.tri(
          e.x + Angles.trnsx(angle, 3, 3),
          e.y + Angles.trnsy(angle, 3, 3),
          8 * e.foutpow(),
          16 * e.finpow(),
          i * 90 + 45
        );
      }
    }),
    graphiteCraft = new Effect(30f, e -> {
      Draw.color(Pal.accent, Color.gray, e.fin());
      Angles.randLenVectors(e.id, 10, e.finpow() * 30f, (x, y) -> Lines.lineAngle(e.x + x, e.y + y, Angles.angle(x, y), 5f * e.foutpow()));
    }),
    graphiteStackCraft = new Effect(30f, e ->{
      if (e.time < 2) Effect.shake(3, 3, e.x, e.y);
      for (int i = 0; i < 4; i++) {
        Tmp.v1.trns(i * 90, 5.75f);
        Draw.color(Pal.accent);
        Drawf.tri(e.x + Tmp.v1.x, e.y + Tmp.v1.y, 8 * e.foutpow(), 8 + e.foutpow(), i * 90);
        Draw.color();
        Drawf.tri(e.x + Tmp.v1.x, e.y + Tmp.v1.y, 4 * e.foutpow(), 4 + e.foutpow(), i * 90);
      }
    }),
    baklerSiliconCraft = new Effect(30f, e -> Angles.randLenVectors(e.id, 15, 40 * e.finpow(), (x, y) -> {
      Draw.color(Pal.accent);
      Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 10 * e.foutpow());
      Draw.color(Color.darkGray);
      Fill.circle(e.x - x, e.y - y, 2 * e.fout());
    })),
    shootFirery = new Effect(30, e -> {
      for (int i = 0; i < 3; i++) {
        e.scaled((10 * i), b -> {
          Draw.blend(Blending.additive);
          float fin = Interp.pow2.apply(b.fin());

          Draw.color(Color.gray);
          Draw.alpha(0.8f);
          Angles.randLenVectors(e.id, 5, 50 * fin, e.rotation, 20, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, 3 * (1 - fin));
          });
          Draw.color(Pal.turretHeat);
          Draw.alpha(0.7f);
          Angles.randLenVectors(e.id + 1, 7, 40 * fin, e.rotation, 20, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, 3 * (1 - fin));
          });
          Draw.color(Pal.accent);
          Draw.alpha(0.6f);
          Angles.randLenVectors(e.id + 2, 10, 30 * fin, e.rotation, 20, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, 3 * (1 - fin));
          });
          Draw.blend();
        });
      }
    }),
    chargeFiery = new Effect(60, e -> {
      for (int i = 0; i < 6; i++) {
        int in = i;
        e.scaled((10 * i), b -> {
          Draw.blend(Blending.additive);
          float
            fin = Interp.pow2.apply(b.fin()),
            fout = Interp.sine.apply(fin * 2);

          Draw.color(Color.gray);
          Draw.alpha(0.8f);
          Angles.randLenVectors(e.id + in, 5, 50 * fin, e.rotation, 20, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, 3 * fout);
          });
          Draw.color(Pal.turretHeat);
          Draw.alpha(0.7f);
          Angles.randLenVectors(e.id + 1 + in, 7, 40 * fin, e.rotation, 20, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, 3 * fout);
          });
          Draw.color(Pal.accent);
          Draw.alpha(0.6f);
          Angles.randLenVectors(e.id + 2 + in, 10, 30 * fin, e.rotation, 20, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, 3 * fout);
          });
          Draw.blend();
        });
      }
    });

}

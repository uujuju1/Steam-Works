package sw.content;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.entities.*;
import mindustry.graphics.*;
import mindustry.world.*;
import sw.util.*;

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

    compoundCraft = new Effect(30f, e -> {
      Draw.color(SWDraw.compoundMiddle, SWDraw.compoundBase, e.fin());
      Angles.randLenVectors(e.id, 20, 20f * e.finpow(), (x, y) -> {
        Fill.circle(
          e.x + x,
          e.y + y,
          2f * e.fout()
        );
      });
    }),
    denseAlloyCraft = new Effect(30f, e -> {
      for(int i = 0; i < 4; i++) {
        Tmp.v1.trns(i * 90f, 3.5f);
        Drawf.tri(
          e.x + Tmp.v1.x,
          e.y + Tmp.v1.y,
          8f * e.foutpow(), 16 * e.finpow(),
          i * 90f
        );
        Angles.randLenVectors(e.id, 10, 40 * e.finpow(), i * 90, 15, (x, y) -> {
          Fill.circle(
            e.x + x + Tmp.v1.x,
            e.y + y + Tmp.v1.y,
            2 * e.foutpow()
          );
        });
      }
    }),
    scorchCraft = new Effect(30f, e -> {
      Draw.blend(Blending.additive);
      for(int i = 0; i < 4; i++) {
        Tmp.v1.trns(i * 90f, 3.5f);
        Draw.color(Pal.accent, 0.6f);
        Angles.randLenVectors(e.id, 5, 20f * e.finpow(), i * 90f, 15f, (x, y) -> {
          Fill.circle(
            e.x + x + Tmp.v1.x,
            e.y + y + Tmp.v1.y,
            2f * e.fout()
          );
        });
        Draw.color(Pal.turretHeat, 0.6f);
        Angles.randLenVectors(e.id + 1, 5, 20 * e.finpow(), i * 90f, 15f, (x, y) -> {
          Fill.circle(
            e.x + x + Tmp.v1.x,
            e.y + y + Tmp.v1.y,
            2f * e.fout()
          );
        });
        Draw.color(Color.gray, 0.6f);
        Angles.randLenVectors(e.id + 2, 5, 20 * e.finpow(), i * 90f, 15f, (x, y) -> {
          Fill.circle(
            e.x + x + Tmp.v1.x,
            e.y + y + Tmp.v1.y,
            2f * e.fout()
          );
        });
      }
      Draw.blend();

    }),
    grapheneCraft = new Effect(30f, e -> {
    Angles.randLenVectors(e.id, 20, 10f * e.finpow(), (x, y) -> {
      Tmp.v1.trns(Mathf.angle(x, y), 8f);
      Fill.circle(
        e.x + x + Tmp.v1.x,
        e.y + y + Tmp.v1.y,
        3f * e.foutpow()
      );
    });
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
    shootFirery = new Effect(30f, e -> {
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
    chargeFiery = new Effect(60f, e -> {
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
    }),
    soundDecay = new Effect(60f, e -> {
      Draw.mixcol(Color.black, e.finpow());
      Tmp.v1.trns(e.rotation, 40f * e.finpow());
      Draw.rect("sw-sound-wave", e.x + Tmp.v1.x, e.y + Tmp.v1.y, 16, 10f * e.fout(), 90 + e.rotation);
    }),
    soundImpact = new Effect(60f, e -> {
      for (int i = 1; i < 5; i++) {
        final int index = i;
        e.scaled(15f * i, b -> {
          Lines.stroke(3 * b.foutpow());
          Lines.circle(e.x, e.y, 30 * index * b.finpow());
        });
      }
    }),
    changeEffect = new Effect(30f, e -> {
      if (!(e.data instanceof Block block)) return;

      Draw.mixcol(Pal.accent, 1);
      Draw.alpha(e.fout());
      Draw.rect(block.fullIcon, e.x, e.y, e.rotation);
      Draw.alpha(1f);
      Lines.stroke(4f * e.fout());
      Lines.square(e.x, e.y, block.size * 4f);
    });
}

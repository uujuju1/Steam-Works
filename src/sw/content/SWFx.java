package sw.content;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.entities.*;
import mindustry.graphics.*;
import mindustry.world.*;

public class SWFx {
  public static final Rand rand = new Rand();
  public static final Vec2 temp = new Vec2();

  public static Effect
    gasVent = new Effect(60f, e -> {
      Draw.color(e.color, Color.black, e.fin());
      Draw.alpha(e.fslope() * 0.6f);
      rand.setSeed(e.id);
      Angles.randLenVectors(e.id, 3, 20f * e.fin(), (x, y) -> {
        Fill.circle(e.x + x, e.y + y, 10 * Interp.sine.apply(e.fin() * 1.2f));
      });
    }).layer(Layer.darkness - 1),

    boreMine = new Effect(60f, 48f, e -> {
      Draw.color(e.color);
      Angles.randLenVectors(e.id, 5, 24f * e.finpow(), e.rotation + 180f, 30f, (x, y) -> {
        Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 5f * e.fout());
      });
      e.scaled(15f, b -> {
        Lines.stroke(b.foutpow());
        Lines.circle(e.x, e.y, 4f * b.finpow());
      });
    }),
    groundCrack = new Effect(60f, 64f, e -> {
      rand.setSeed(e.id);
      Draw.color(e.color, Color.grays(0.2f), Interp.exp10Out.apply(e.fout()));
      for(int j = 0; j < 5; j++) {
        Vec2 lastPos = new Vec2(e.x, e.y);
        float lastRot = rand.random(360);

        for(int i = 0; i < 8; i++) {
          temp.trns(lastRot, 4).add(lastPos);
          Lines.stroke((1.5f - 1f/8f * i) * Interp.exp5Out.apply(e.fout()));
          Lines.line(lastPos.x, lastPos.y, temp.x, temp.y);
          lastRot += rand.range(90);
          lastPos.set(temp);
        }
      }
    }).layer(Layer.block - 1),

    compoundCraft = new Effect(30f, e -> {
      rand.setSeed(e.id);
      Draw.color(Color.valueOf("BEB5B2"));
      float offset = rand.range(360f);
      for(int i = 0; i < 4; i++) {
        temp.trns((i * 90f + 360f * e.finpow()) + offset, 6f);

        Drawf.tri(temp.x + e.x, temp.y + e.y, 8f * e.foutpow(), 8f - 4f * e.foutpow(), temp.angle());
        Drawf.tri(temp.x + e.x, temp.y + e.y, 8f * e.foutpow(), 4f - 2f * e.foutpow(), temp.angle() + 180f);
      }

      Draw.z(Layer.effect);
      Angles.randLenVectors(e.id, 30, 40f * e.finpow(), (x, y) -> {
        Fill.circle(e.x + x, e.y + y, rand.random(3f) * e.foutpow());
      });
      Draw.z(Layer.effect + 1);
      Angles.randLenVectors(e.id + 1, 15, 40f * e.finpow(), (x, y) -> {
        Draw.color(Color.gray);
        Fill.circle(e.x + x, e.y + y, rand.random(3f) * e.fout());
      });
    }),
    denseAlloyCraft = new Effect(30f, e -> {
      for(int i = 0; i < 4; i++) {
        Draw.color(Pal.accent);
        temp.trns(i * 90f + 45f, 6f);

        Drawf.tri(temp.x + e.x, temp.y + e.y, 8f * e.foutpow(), 8f - 4f * e.foutpow(), temp.angle());
        Drawf.tri(temp.x + e.x, temp.y + e.y, 8f * e.foutpow(), 4f - 2f * e.foutpow(), temp.angle() + 180f);
      }

      Draw.z(Layer.effect);
      Angles.randLenVectors(e.id, 30, 40f * e.finpow(), (x, y) -> {
        Lines.stroke(e.fout(), Pal.accent);
        Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 5f * e.fout());
      });
      Draw.z(Layer.effect + 1);
      Angles.randLenVectors(e.id + 1, 15, 40f * e.finpow(), (x, y) -> {
        Lines.stroke(e.fout(), Color.gray.cpy().mul(0.5f));
        Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 5f * e.fout());
      });
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

    thermiteShoot = new Effect(20f, e -> {
      rand.setSeed(e.id);

      Draw.blend(Blending.additive);
      for(int i : Mathf.signs) {
        temp.trns(e.rotation - 90f, -6f * i, -15.75f).add(e.x, e.y);

        Angles.randLenVectors(e.id + i, 10, 8f * e.finpow(), e.rotation + 180f + 45f * i, 15f, (x, y) -> {
          Draw.color(Pal.accent, Pal.turretHeat, rand.random(1f));
          Fill.circle(temp.x + x, temp.y + y, 2f * e.fout());
        });
      }

      Angles.randLenVectors(e.id, 15, 32f * e.finpow(), e.rotation, 15f, (x, y) -> {
        Draw.color(Pal.accent, Pal.turretHeat, rand.random(1f));
        Fill.circle(e.x + x, e.y + y, 5f * e.fout());
      });
      Draw.blend();
    }),
    thermiteCharge = new Effect(60f, e -> {
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
    accelSparks = new Effect(20f, e -> {
      Angles.randLenVectors(e.id, 2, 10 * e.finpow(), e.rotation, 15, (x, y) -> {
        Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 4 * e.foutpow());
      });
    }),
    shootShockwaveColor = new Effect(60f, e -> {
      for(int i = 0; i < 3; i++) {
        e.scaled(30f + i * 10f, b -> {
          Draw.mixcol(e.color, Color.black, b.fin());
          Tmp.v1.trns(e.rotation, 30f * b.finpow() - 60f * b.fin());
          Draw.rect("sw-sound-wave", e.x + Tmp.v1.x, e.y + Tmp.v1.y, 16f + 16f * b.finpow(), 10f * b.fout(), 90f + e.rotation);
        });
      }
    }).followParent(false).rotWithParent(false),

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

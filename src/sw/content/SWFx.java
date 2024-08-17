package sw.content;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import sw.math.*;
import sw.world.*;

public class SWFx {
  public static final Rand rand = new Rand();
  public static final Vec2 temp = new Vec2();


  protected static final Rect[] rects = new Rect[]{
    new Rect(0, 0, 24, 16),
    new Rect(16, 0, 16, 24),
    new Rect(8, 16, 24, 16),
    new Rect(0, 8, 16, 24)
  };

  public static Effect
    gasVent = new Effect(120f, e -> {
      if (!(e.data instanceof MultiShape shape)) return;
      rand.setSeed(e.id);
      shape.tiles.each(tile -> {
        if (rand.chance(0.5d)) {
          Draw.color(Color.valueOf("E3D8B6"), e.fout() / 2f);
          e.scaled(30, b -> {
            Angles.randLenVectors(tile.pos() + e.id, 5, 4f * b.fin(), (x, y) -> {
              Fill.circle(
                tile.worldx() + x + Angles.trnsx(Mathf.angle(x, y), 4f),
                tile.worldy() + y + Angles.trnsy(Mathf.angle(x, y), 4f),
                3f * b.fout()
              );
            });
          });
          if (tile.block().isAir()) {
            float angle = rand.random(360f);
            Fill.circle(
              tile.worldx() + Angles.trnsx(angle, 4f * e.fin()),
              tile.worldy() + Angles.trnsy(angle, 4f * e.fin()),
              5f * Interp.circle.apply(Mathf.slope(e.fin()))
            );
          }
        }
      });
    }).layer(Layer.blockUnder),

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

    burnElevation = new Effect(120f, e -> {
      Draw.z(Layer.darkness + 1f);

      rand.setSeed(e.id);
      Draw.color(Pal.darkestGray, Color.gray, e.finpow());
      Draw.alpha(Mathf.slope(e.finpow()));

      temp.set(0f, 0f);
      Groups.weather.each(weather -> temp.add(Tmp.v1.set(weather.windVector).scl(weather.opacity)));
      temp.scl(1f/Math.max(1f, Groups.weather.size()));
      temp.scl(e.fin() * 30f).add(e.x, e.y);
      Parallax.getParallaxFrom(temp, Core.camera.position, e.finpow() * 0.5f);

      Fill.circle(temp.x, temp.y, (5 - 2 * e.fin()) * rand.random(0.75f, 1.25f));
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
    }),

    fragment = new Effect(60f, e -> {
      if (!(e.data instanceof Block block)) return;
      TextureRegion region = block.region;
      rand.setSeed(e.id);
      Vec2[] offsets = new Vec2[]{
        new Vec2(-1f, 2f).scl(1f + 5f * e.finpow()).rotate(rand.range(45f) * e.finpow()),
        new Vec2(2f, 1f).scl(1f + 5f * e.finpow()).rotate(rand.range(45f) * e.finpow()),
        new Vec2(1f, -2f).scl(1f + 5f * e.finpow()).rotate(rand.range(45f) * e.finpow()),
        new Vec2(-2f, -1f).scl(1f + 5f * e.finpow()).rotate(rand.range(45f) * e.finpow())
      };

      Draw.z(Layer.bullet - 1);
      Draw.alpha(Interp.exp10Out.apply(e.fout()));

      for(int i = 0; i < 4; i++) {
        Draw.rect(
          new TextureRegion(region, (int) rects[i].x, (int) rects[i].y, (int) rects[i].width, (int) rects[i].height),
          e.x + offsets[i].x, e.y + offsets[i].y, rand.random(720f) * e.finpow()
        );
      }
    }).layer(Layer.block),

    lightning = new Effect(60f, e -> {
      rand.setSeed(e.id);
      Draw.color(Pal.accent);

      for(int branch = 0; branch < 10; branch++) {
        float branchElevationEnd = rand.random(0.1f, 0.2f);

        temp.trns(rand.random(360f), 16f).add(e.x, e.y);
        float endX = temp.x, endY = temp.y;

        float lastX = 0, lastY = 0;
        for(int i = 0; i < 4; i++) {
          float
            elevationS = Mathf.map(i/4f, 0f, 1f, 0f, branchElevationEnd),
            elevationE = Mathf.map((i + 1f)/4f, 0f, 1f, 0f, branchElevationEnd);

          temp.set(e.x, e.y).lerp(endX, endY, i/4f).add(lastX, lastY);
          lastX = rand.range(i/4f * 8f);
          lastY = rand.range(i/4f * 8f);
          Tmp.v2.set(e.x, e.y).lerp(endX, endY, (i + 1f)/4f).add(lastX, lastY);

          Parallax.getParallaxFrom(temp, Core.camera.position, elevationS);
          Parallax.getParallaxFrom(Tmp.v2, Core.camera.position, elevationE);

          Lines.stroke(1);
          Draw.alpha(Mathf.clamp((1f - i/4f) - e.finpow()));
          Lines.line(temp.x, temp.y, Tmp.v2.x, Tmp.v2.y);
        }
      }

      float lastX = 0, lastY = 0;
      for(int i = 0; i < 12; i++) {
        float elevationS = i/12f, elevationE = (i + 1f)/12f;

        temp.set(e.x, e.y).add(lastX, lastY);
        lastX = rand.range(i/12f * 8f);
        lastY = rand.range(i/12f * 8f);
        Tmp.v2.set(e.x, e.y).add(lastX, lastY);

        Parallax.getParallaxFrom(temp, Core.camera.position, elevationS);
        Parallax.getParallaxFrom(Tmp.v2, Core.camera.position, elevationE);

        Lines.stroke(3f * (1f - i/12f));
        Draw.alpha(Mathf.clamp((1f - i/12f) - e.finpow()));
        Lines.line(temp.x, temp.y, Tmp.v2.x, Tmp.v2.y);
      }

      Fill.circle(e.x, e.y, 8f * e.foutpow());

      Lines.stroke(e.foutpow());
      Lines.circle(e.x, e.y, 24f * e.finpow());
    }),
    realityTear = new Effect(60f, e -> {
      Color[] colors = new Color[]{Color.red, Color.green, Color.blue};
      rand.setSeed(e.id);
      e.rotation = rand.random(24f);

      float p = Interp.exp10Out.apply(e.fout());
      Draw.blend(Blending.additive);
      for (Color value : colors) {
        rand.setSeed(e.id);
        float
          dx = e.x + Mathf.random(-8f, 8f),
          dy = e.y + Mathf.random(-8f, 8f);

        Draw.color(value);
        Fill.rect(dx, dy, e.rotation * p, e.rotation * p, rand.random(360f));
        for (int i = 0; i < Mathf.floor(e.rotation / 16); i++) {
          float
            ox = dx + Angles.trnsx(rand.random(360f), rand.random(-e.rotation / 2f * p, e.rotation / 2f * p)),
            oy = dy + Angles.trnsy(rand.random(360f), rand.random(-e.rotation / 2f * p, e.rotation / 2f * p));
          Drawf.tri(ox, oy, e.rotation / 8f * p, e.rotation * p, rand.random(360f) + 90f);
          Drawf.tri(ox, oy, e.rotation / 8f * p, e.rotation * p, rand.random(360f) - 90f);

          ox = dx + Angles.trnsx(rand.random(360f) + 180f, rand.random(-e.rotation / 2f * p, e.rotation / 2f * p));
          oy = dy + Angles.trnsy(rand.random(360f) + 180f, rand.random(-e.rotation / 2f * p, e.rotation / 2f * p));
          Drawf.tri(ox, oy, e.rotation / 8f * p, e.rotation * p, rand.random(360f) - 90f);
          Drawf.tri(ox, oy, e.rotation / 8f * p, e.rotation * p, rand.random(360f) + 90f);
        }
      }
      Draw.blend();
    });
}

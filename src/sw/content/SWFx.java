package sw.content;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.effect.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import sw.math.*;

public class SWFx {
  public static final Rand rand = new Rand();
  public static final Vec2 temp = new Vec2();

  public static Effect
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
		blockCrack = new Effect(60f, e -> {
			rand.setSeed(e.id);

			for(int j = 0; j < 3; j++) {
				float lx = Angles.trnsx(e.rotation, rand.random(2));
				float ly = Angles.trnsy(e.rotation, rand.random(2));

				Draw.color(Pal.darkestGray, e.color, e.finpowdown()/4f);
				for(int i = 1; i < 4; i++) {
					Lines.stroke((1f - 0.5f/4f * i) * (e.foutpowdown()/rand.random(1f, 2f)));

					float rx = rand.range(4f) + Angles.trnsx(e.rotation, 8f * i);
					float ry = rand.range(4f) + Angles.trnsy(e.rotation, 8f * i);

					Lines.line(e.x + lx, e.y + ly, e.x + rx, e.y + ry);

					lx = rx;
					ly = ry;
				}
			}

			Angles.randLenVectors(e.id, 5, 24f * e.finpow(), e.rotation, 10f, (x, y) -> {
				Draw.color(e.color, Pal.darkestGray, rand.random(0.5f));
				Fill.circle(e.x + x, e.y + y, rand.random(1, 3) * e.foutpowdown());
			});
		}).layer(Layer.effect + 1),
  
    parallaxFire = new Effect(90f, e -> {
      rand.setSeed(e.id);
      
      Draw.color(Color.darkGray, Color.gray, e.finpow());
      Angles.randLenVectors(e.id, 2, e.rotation / 2f, e.rotation * e.finpow(), (x, y) -> {
        Parallax.getParallaxFrom(temp.set(x, y).scl(e.foutpowdown()).add(e.x, e.y), Core.camera.position, e.fin() * rand.random(2f, 5f));
        Fill.circle(temp.x, temp.y, rand.random(1f, 3f) * Mathf.clamp(e.fslope() * 4f));
      });
      
      Draw.blend(Blending.additive);
      Angles.randLenVectors(e.id + 1, 2, e.rotation / 2f, e.rotation * e.finpow(), (x, y) -> {
        float a = rand.random(0f, 0.75f);
        Draw.color(Pal.accent, Pal.turretHeat, e.finpow() * (1f - a) + a);
        Parallax.getParallaxFrom(temp.set(e.x + x * e.fout(), e.y + y * e.fout()), Core.camera.position, e.fin() * rand.random(0f, 3f));
        Fill.circle(temp.x, temp.y, rand.random(1f, 3f) * Mathf.clamp(e.fslope() * 4f));
      });
      Draw.blend();
    }).layer(Layer.effect + 1),

    ballDeath = new Effect(30f, e -> {
      Draw.color(Color.white, Pal.lancerLaser, e.fin());
      Fill.circle(e.x, e.y, 6f * e.foutpowdown());

      Lines.stroke(3f * e.foutpow());

      rand.setSeed(e.id);
      Lines.circle(e.x, e.y, 8f + 24f * e.finpow());
      Angles.randVectors(e.id, 10, 8f + 24f * e.finpow(), (x, y) -> {
        Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y) + 180f, rand.random(3f, 5f) * e.foutpowdown());
      });
    }),
    balloonRelease = new Effect(120f, e -> {
      Draw.z(Layer.flyingUnit);
      Parallax.getParallaxFrom(temp.set(e.x, e.y), Core.camera.position, 3f + 7f * e.fin());
      Draw.alpha(e.foutpowdown());
      Draw.rect("sw-courier", temp.x, temp.y);

      Draw.z(Layer.darkness - 1f);
      Draw.mixcol(Pal.shadow, 1f);
      Draw.alpha(Pal.shadow.a * e.foutpowdown());
      Draw.rect("sw-courier", e.x + UnitType.shadowTX * (1f + e.fin()), e.y + UnitType.shadowTY * (1f + e.fin()));
    }),
  
    cokeBurn = new Effect(60f, e -> {
      rand.setSeed(e.id);

      Draw.blend(Blending.additive);
      Parallax.getParallaxFrom(
        temp.set(e.x + rand.range(1f), e.y + rand.range(4f)),
        Core.camera.position,
        rand.random(1f, 5f) * e.finpow()
      );
      Draw.color(Pal.turretHeat, Pal.accent, rand.random(1f));
      Draw.alpha(e.foutpowdown());
      Fill.circle(temp.x, temp.y, rand.random(1f, 2f));
      Draw.blend();
    }),
    cokeCraft = new Effect(120, e -> {
      rand.setSeed(e.id);

      for(int j = 0; j < 5; j++) {
        float fin = 2f * (e.fin() - j/30f);
        if (fin < 0f || fin > 1f) continue;

        float finpow = Interp.exp10Out.apply(fin);
        float foutpow = Interp.exp10In.apply(1f - fin);
        float foutpowdown = Interp.pow2Out.apply(1f - fin);

        for(int i : Mathf.signs) {
          Draw.blend(Blending.additive);
          Parallax.getParallaxFrom(
            temp.set(e.x + rand.range(1f), e.y + rand.range(4f)),
            Core.camera.position,
            rand.random(1f, 2f) * finpow
          ).add(i * 8f * foutpow, 0f);
          Draw.color(Pal.turretHeat, Pal.accent, rand.random(1f));
          Draw.alpha(foutpowdown);
          Fill.circle(temp.x, temp.y, rand.random(1f, 2f));
          Draw.blend();
        }
      }
    }),
    cokeIlluminate = new WrapEffect(parallaxFire, Color.white, 8f),

    combust = new Effect(60f, e -> {
      rand.setSeed(e.id);

      if (rand.chance(0.5)) {
        temp.trns(rand.random(4) * 90f, 4f, 4f);
      } else temp.set(0, 0);

      Angles.randLenVectors(e.id, 10, 4 * e.finpow(), (x, y) -> {
	      float h = rand.random(5);

	      Draw.color(Pal.accent, Color.gray, 1 - e.foutpow() * Mathf.clamp(h - rand.random(h)));
	      Draw.alpha((1 - h/5f) * e.fout());

	      Fill.circle(
		      Parallax.getParallaxFrom(temp.x + e.x + x, Core.camera.position.x, h * e.finpow()),
		      Parallax.getParallaxFrom(temp.y + e.y + y, Core.camera.position.y, h * e.finpow()),
		      rand.random(1, 5)
	      );

      });
    }),

    engineBurn = new Effect(120f, e -> {
      Draw.blend(Blending.additive);
      for(int s = 0; s < 2; s++) {
        rand.setSeed(e.id);
        for(int i = 0; i < 8; i++) {
          int sign = Mathf.signs[i % 2];
          float x = e.x + sign * 7f;
          float y = e.y + 4f * Mathf.floor(i / 2f) - 6f;
          
          if (s == 1) {
            Tmp.c1.set(Pal.gray).mul(0.5f).lerp(Color.darkGray, rand.random(1f));
          } else {
            Tmp.c1.set(Pal.accent).lerp(Pal.turretHeat, rand.random(0.5f, 1f));
          }
          
          Draw.color(Tmp.c1);
          
          if (s == 1) {
            Draw.alpha(Interp.smooth.apply(Mathf.clamp(e.fin() * 5f - 0.5f) * Math.min(10f * e.fin(), 1.1f - e.fin() * 1.1f)));
          } else {
            Draw.alpha(Interp.smooth.apply(Mathf.clamp(2f - e.fin() * 5f) * Math.min(10f * e.fin(), 1.1f - e.fin() * 1.1f)));
          }
          if (rand.chance(0.75f)) {
            Angles.randLenVectors(e.id + 1 + i, rand.random(1, 2), 16f * e.finpow(), 90f - sign * 90f, 30f, (ox, oy) -> {
              Parallax.getParallaxFrom(temp.set(x + ox, y + oy), Core.camera.position, rand.random(10f, 15f) * e.finpowdown());
              Fill.circle(temp.x, temp.y, rand.random(1f, 2f) * Math.min(10f * e.fin(), 1f));
            });
          }
        }
        Draw.blend();
      }
    }).layer(Layer.effect + 1),
    evaporate = new Effect(60f, e -> {
      rand.setSeed(e.id);

      Angles.randLenVectors(e.id, 10, 8f, (x, y) ->  {
        Parallax.getParallaxFrom(
          temp.set(e.x + x, e.y + y),
          Core.camera.position,
          rand.random(1f, 5f) * e.finpow()
        );

        Draw.color(Color.white, Pal.darkerGray, rand.random(1f));
        Draw.alpha(e.fslope() / 5f);
        Fill.circle(temp.x, temp.y, rand.random(1f, 2f) * e.finpow());
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

    healEmber = new Effect(30f, e -> {
      if (!(e.data instanceof Building data)) return;

      rand.setSeed(e.id);

      Draw.color(Pal.missileYellow);

      temp.x = 3 * Mathf.sign(rand.chance(0.5f));
      temp.y = rand.range(4f);
      temp.rotate(e.rotation);

      Tmp.v6.set(temp);

      float change = 0.5f;

      if (e.fin() < change) {
        float fin = e.fin() / change;
        Bezier.quadratic(temp, fin, Tmp.v2.set(temp).add(e.x, e.y), Tmp.v3.set(temp).setLength(40).add(e.x, e.y), Tmp.v4.set(data), Tmp.v5);
        Fill.square(temp.x, temp.y, Interp.pow2Out.apply(fin), 45 + rand.range(360) * fin);
      } else {
        Lines.stroke(Mathf.map(e.fin(), change, 1f, 3f, 0f));
        Lines.square(data.x, data.y, data.hitSize() / 2f);
      }

      temp.set(Tmp.v6);
      float ang = temp.angle();
      temp.add(e.x, e.y);
      Lines.stroke(e.fout());
      Angles.randLenVectors(e.id, 5, 8f * e.finpow(), ang, 15f, (x, y) -> {
        float angle = Angles.angle(e.x, e.y, temp.x + x, temp.y + y);
        Lines.lineAngle(temp.x + x, temp.y + y, angle, rand.random(2f, 4f) * e.foutpow(), false);
      });
    }),

    hydrogenShoot = new Effect(30f, e -> {
      rand.setSeed(e.id);
      Angles.randLenVectors(e.id, 20, 120f * e.fin(), e.rotation, 5f, (x, y) -> {
        
        Draw.alpha(rand.random(0.5f, 1f) * e.fout());
        Fill.circle(e.x + x, e.y + y, rand.random(1f, 2f));
      });
    }),
    hydrogenLaunch = new Effect(60, e -> {
      rand.setSeed(e.id);
      Draw.color(Liquids.hydrogen.color.cpy().mul(1.25f).clamp(), 0.5f);
      Angles.randLenVectors(e.id, 10, 5, 12 * e.finpow(), (x, y) -> {
        Fill.circle(e.x + x, e.y + y, rand.random(2, 3) * e.foutpowdown());
      });
    }).layer(Layer.blockOver),

    lightning = new Effect(60f, e -> {
      rand.setSeed(e.id);
      Draw.z(Layer.light);

      if (e.fin() * e.lifetime < 10f) {
        Draw.blend(Blending.additive);
        Draw.color(Pal.lancerLaser.cpy().mul(1.25f));
        Draw.alpha(0.5f * Interp.pow2In.apply(1f - Mathf.clamp(Core.camera.position.dst(e.x, e.y) / 120f)) * (1 - e.fin() / (10f / e.lifetime)));
        Draw.rect();
        Draw.blend();
      }

      Draw.reset();

      Drawf.light(e.x, e.y, 40f, Pal.lancerLaser, e.foutpow());
      Draw.color(Pal.lancerLaser.cpy().mul(1.25f));
      Draw.alpha(Interp.smooth.apply(e.fslope()));

      float baseAngle = rand.random(360f);
      float height = 20f;

      Vec2 saved = Tmp.v5.set(e.x, e.y);
      int savedIndex = 0;
      for(int j = 0; j < 6; j++) {
        Vec2 temp2 = Tmp.v6.set(saved);

        float lastRand = rand.range(5f);

        Vec2 old = Tmp.v2.set(temp2);
        float angle = rand.range(1f);
        for(int i = savedIndex; i < 20; i++) {
          lastRand += rand.range(5f);
          Tmp.v4.trns(baseAngle + angle, rand.random(1f, 10f), (lastRand) * i / 19f);
          Vec2 next = Tmp.v3.set(old).add(Tmp.v4);

          Lines.stroke(Interp.bounceIn.apply(e.fout()) * (1f + i / 19f * 3));
          Lines.line(
            Parallax.getParallaxFrom(old.x, Core.camera.position.x, (i - 1) / 19f * height),
            Parallax.getParallaxFrom(old.y, Core.camera.position.y, (i - 1) / 19f * height),
            Parallax.getParallaxFrom(next.x, Core.camera.position.x, i / 19f * height),
            Parallax.getParallaxFrom(next.y, Core.camera.position.y, i / 19f * height)
          );

          old.set(next);
          baseAngle += angle;

          if (rand.chance(0.25f) && i < 10f) {
            saved.set(next);
            savedIndex = i;
          }
        }
      }
    }),
  
    thermiteCrush = new Effect(120f, e -> {
      rand.setSeed(e.id);
      
      Angles.randLenVectors(e.id + 1, rand.random(1, 3), 16f * e.finpowdown(), (x, y) -> {
        Draw.color(Color.valueOf("6C5656"), Color.valueOf("B2A9AD"), rand.random(1f));
        
        Draw.alpha(0.5f * Interp.pow2.apply(e.fslope()));
        Parallax.getParallaxFrom(temp.set(e.x + x, e.y + y), Core.camera.position, e.fin() * rand.random(7f, 10f));
        
        Fill.circle(temp.x, temp.y, rand.random(2f, 3f) + rand.random(3f, 5f) * e.fin());
      });
    }).layer(Layer.effect + 1),

    thermiteShoot = new Effect(20f, e -> {
      rand.setSeed(e.id);

      Draw.blend(Blending.additive);
      for(int i : Mathf.signs) {
        temp.trns(e.rotation - 90f, -6f * i, -16f - 2f - 5.75f).add(e.x, e.y);

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
    thermiteTrail = new Effect(60f, e -> {
      rand.setSeed(e.id);
      for (int ignored : Mathf.zeroOne) {
        float offset = rand.range(4f);
        float move = rand.random(2f, 16f);
        Lines.stroke(rand.random(0.5f, 2f));
        Draw.color(Pal.accent, Pal.missileYellow, Color.black, 1 - rand.random(1) * e.foutpowdown());
        Lines.lineAngle(
          e.x + Angles.trnsx(e.rotation + 90f, offset, move * e.finpow()),
          e.y + Angles.trnsy(e.rotation + 90f, offset, move * e.finpow()),
          e.rotation + 180f, rand.random(3f, 8f)
        );
      }
    }),

    weld = new Effect(20f, e -> {
      Draw.color(Pal.accent);

      Fill.circle(e.x, e.y, e.foutpowdown() * 2f);

      for(int i : Mathf.signs) Drawf.tri(e.x, e.y, 2f, 4f * e.foutpowdown(), i * 90f + 90f);

	    Lines.stroke(e.fout());
	    Angles.randLenVectors(e.id, 10, 16f * e.finpow(), (x, y) -> {
		    Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.foutpow());
	    });

      Draw.alpha(1f - Mathf.clamp(Core.camera.position.dst(e.x, e.y)));

      Draw.rect();
    }),
    weldConstruct = new Effect(20f, e -> {
      Draw.color(Pal.accent);

	    Lines.stroke(e.fout() / 2);
	    Angles.randLenVectors(e.id, 10, 16f * e.finpow(), (x, y) -> {
		    Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.foutpow());
	    });
    }),
    weldDeconstruct = new Effect(20f, e -> {
      Draw.color(Pal.remove);

      Lines.stroke(e.fout() / 2f);
      rand.setSeed(e.id);
      for (int i = 0; i < 10; i++) {
        float angle = rand.random(360f);
        temp.trns(angle, rand.random(16f) * e.finpow());
        Lines.lineAngle(
          e.x + Angles.trnsx(angle - 90f, 1f) + temp.x,
          e.y + Angles.trnsy(angle - 90f, 1f) + temp.y,
          angle,
          e.foutpow() * 3f
        );
      }
    }),
    wispFire = new WrapEffect(parallaxFire, Color.white, 4f),

    changeEffect = new Effect(30f, e -> {
      if (!(e.data instanceof Block block)) return;

      Draw.mixcol(Pal.accent, 1);
      Draw.alpha(e.fout());
      Draw.rect(block.fullIcon, e.x, e.y, e.rotation);
      Draw.alpha(1f);
      Lines.stroke(4f * e.fout());
      Lines.square(e.x, e.y, block.size * 4f);
    }),

//    lightning = new Effect(60f, e -> {
//      rand.setSeed(e.id);
//      Draw.color(Pal.accent);
//
//      for(int branch = 0; branch < 10; branch++) {
//        float branchElevationEnd = rand.random(0.1f, 0.2f);
//
//        temp.trns(rand.random(360f), 16f).add(e.x, e.y);
//        float endX = temp.x, endY = temp.y;
//
//        float lastX = 0, lastY = 0;
//        for(int i = 0; i < 4; i++) {
//          float
//            elevationS = Mathf.map(i/4f, 0f, 1f, 0f, branchElevationEnd),
//            elevationE = Mathf.map((i + 1f)/4f, 0f, 1f, 0f, branchElevationEnd);
//
//          temp.set(e.x, e.y).lerp(endX, endY, i/4f).add(lastX, lastY);
//          lastX = rand.range(i/4f * 8f);
//          lastY = rand.range(i/4f * 8f);
//          Tmp.v2.set(e.x, e.y).lerp(endX, endY, (i + 1f)/4f).add(lastX, lastY);
//
//          Parallax.getParallaxFrom(temp, Core.camera.position, elevationS);
//          Parallax.getParallaxFrom(Tmp.v2, Core.camera.position, elevationE);
//
//          Lines.stroke(1);
//          Draw.alpha(Mathf.clamp((1f - i/4f) - e.finpow()));
//          Lines.line(temp.x, temp.y, Tmp.v2.x, Tmp.v2.y);
//        }
//      }
//
//      float lastX = 0, lastY = 0;
//      for(int i = 0; i < 12; i++) {
//        float elevationS = i/12f, elevationE = (i + 1f)/12f;
//
//        temp.set(e.x, e.y).add(lastX, lastY);
//        lastX = rand.range(i/12f * 8f);
//        lastY = rand.range(i/12f * 8f);
//        Tmp.v2.set(e.x, e.y).add(lastX, lastY);
//
//        Parallax.getParallaxFrom(temp, Core.camera.position, elevationS);
//        Parallax.getParallaxFrom(Tmp.v2, Core.camera.position, elevationE);
//
//        Lines.stroke(3f * (1f - i/12f));
//        Draw.alpha(Mathf.clamp((1f - i/12f) - e.finpow()));
//        Lines.line(temp.x, temp.y, Tmp.v2.x, Tmp.v2.y);
//      }
//
//      Fill.circle(e.x, e.y, 8f * e.foutpow());
//
//      Lines.stroke(e.foutpow());
//      Lines.circle(e.x, e.y, 24f * e.finpow());
//    }),
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

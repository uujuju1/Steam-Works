package sw.content;

import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import ent.anno.Annotations.*;
import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.entities.effect.*;
import mindustry.entities.part.*;
import mindustry.entities.pattern.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import sw.ai.*;
import sw.gen.*;
import sw.type.*;
import sw.type.units.*;

public class SWUnitTypes {
  @EntityDef({Revealc.class, WaterMovec.class, Unitc.class}) public static UnitType recluse, retreat, evade;

  @EntityDef({Copterc.class, Unitc.class}) public static UnitType fly, spin, gyro;

	@EntityDef({GrassHopperc.class, Unitc.class}) public static UnitType cinerea, robinia;

	@EntityDef({Intangiblec.class, Legsc.class, Unitc.class}) public static UnitType lambda;

  public static UnitType
		focus, precision, target,

    sentry, tower, castle,
		existence, remembered, presence;

  public static void load() {
    //region specialist
    recluse = new SWUnitType("recluse") {{
      speed = 1;
      health = 250;
      hitSize = 8f;
      accel = 0.4f;
      drag = 0.14f;
      rotateSpeed = 4f;
      range = maxRange = 40f;
      submerges = true;

      constructor = UnitWaterMoveReveal::create;

      weapons.add(new Weapon("sw-recluse-weapon") {{
        x = 3.75f;
        y = -1.25f;
        reload = 20f;
        shootSound = Sounds.sap;
        rotate = true;
        bullet = new SapBulletType() {{
          damage = 20f;
          length = 40f;
          color = Pal.sapBullet;
        }};
      }});
    }};
    retreat = new SWUnitType("retreat") {{
      health = 580;
      speed = 0.9f;
      hitSize = 11f;
      accel = 0.4f;
      drag = 0.14f;
      rotateSpeed = 3.5f;
      range = maxRange = 80f;
      targetAir = false;
      submerges = true;
      vulnerabilityTime = 150f;

      constructor = UnitWaterMoveReveal::create;

      weapons.add(new Weapon("sw-retreat-weapon") {{
        x = y = 0f;
        reload = 60f;
        mirror = false;
        rotate = true;
        shootSound = Sounds.artillery;
        bullet = new ArtilleryBulletType(3f, 40) {{
          width = height = 10f;
          lifetime = 26.75f;
          collidesGround = true;
          splashDamage = 40f;
          splashDamageRadius = 24f;
          hitEffect = despawnEffect = Fx.hitBulletColor;
          frontColor = hitColor = trailColor = Pal.sapBullet;
          backColor = Pal.sapBulletBack;
          trailLength = 40;
          status = StatusEffects.sapped;
          statusDuration = 120f;
        }};
      }});
    }};
    evade = new SWUnitType("evade") {{
      health = 1200;
      speed = 0.8f;
      accel = 0.4f;
      drag = 0.14f;
      hitSize = 18f;
      rotateSpeed = 3f;
      range = maxRange = 160f;
      submerges = true;
      vulnerabilityTime = 240f;

      constructor = UnitWaterMoveReveal::create;

      weapons.add(
        new Weapon() {{
          x = y = 0f;
          reload = 240f;
          rotateSpeed = 360f;
          mirror = false;
          rotate = true;
          shootSound = Sounds.missileLaunch;
          bullet = new BasicBulletType(4f, 200f) {{
            width = height = 15f;
            lifetime = 40f;
            drag = -0.01f;
            collideFloor = true;
            splashDamage = 120f;
            splashDamageRadius = 24f;
            frontColor = trailColor = hitColor = Pal.sapBullet;
            backColor = Pal.sapBulletBack;
            status = StatusEffects.sapped;
            statusDuration = 300f;
            trailLength = 80;
          }};
        }},
        new Weapon("sw-evade-cannon") {{
          x = 0f;
          y = -7.25f;
          reload = 120f;
          targetAir = false;
          mirror = false;
          rotate = true;
          shootSound = Sounds.artillery;
          bullet = new ArtilleryBulletType(4f, 120) {{
            width = height = 12f;
            lifetime = 25f;
            range = 100f;
            collidesGround = true;
            splashDamage = 120f;
            splashDamageRadius = 24f;
            hitEffect = despawnEffect = Fx.hitBulletColor;
            frontColor = hitColor = trailColor = Pal.sapBullet;
            backColor = Pal.sapBulletBack;
            status = StatusEffects.sapped;
            statusDuration = 180f;
            trailLength = 40;
          }};
        }},
        new Weapon("sw-evade-mount") {{
          x = 8.75f;
          y = 4f;
          reload = 60f;
          rotate = true;
          shootSound = Sounds.shotgun;
          bullet = new ShrapnelBulletType() {{
            damage = 80f;
            length = 12f;
            range = length;
            toColor = Pal.sapBullet;
            serrations = 4;
          }};
        }}
      );
    }};

    //endregion
    //region tanks
    sentry = new SWUnitType("sentry") {{
      health = 320;
      speed = 0.25f;
      range = maxRange = 800f;
      hitSize = 8f;
      rotateSpeed = 1;
      outlines = faceTarget = false;
      targetAir = false;
      squareShape = true;
      omniMovement = false;
      rotateMoveFirst = true;
      aiController = MortarAI::new;

      treadFrames = 16;
      treadRects = new Rect[]{
				new Rect(-24f, -29f, 14, 56)
			};

      constructor = TankUnit::create;

      weapons.add(
        new Weapon("sw-long-mortar") {{
          x = y = 0f;
          reload = 300f;
          recoil = 0f;
          recoilTime = 60f;
          cooldownTime = 150f;
          rotateSpeed = 0.5f;
          shootY = 16f;
          shootSound = Sounds.largeCannon;
          rotate = true;
          mirror = false;

          parts.add(
            new RegionPart("-cannon") {{
              moveY = -2f;
              outlineLayerOffset = 0f;
              under = true;
              progress = PartProgress.recoil.curve(Interp.bounceIn);
            }}
          );

          bullet = new ArtilleryBulletType(2f, 1, "missile-large") {{
            frontColor = Pal.missileYellow;
            backColor = trailColor = hitColor = Pal.missileYellowBack;
            splashDamage = 100f;
            splashDamageRadius = 16f;
            width = height = 16f;
            lifetime = 400;
						shrinkX = shrinkY = 0.5f;
            trailWidth = 3f;
            trailLength = 50;
            collides = collidesTiles = collidesGround = true;
            shootEffect = Fx.shootTitan;
            smokeEffect = Fx.shootSmokeTitan;
            trailEffect = Fx.none;
            hitEffect = despawnEffect = new MultiEffect(Fx.titanSmoke, Fx.titanExplosion);
            bullet.hitSound = bullet.despawnSound = Sounds.largeExplosion;
          }};
        }}
      );
    }};
    tower = new SWUnitType("tower") {{
      health = 650;
      speed = 0.25f;
      range = maxRange = 1000f;
      hitSize = 10f;
      rotateSpeed = 0.8f;
	    outlines = faceTarget = false;
	    targetAir = false;
	    squareShape = true;
	    omniMovement = false;
	    rotateMoveFirst = true;
	    aiController = MortarAI::new;

      treadFrames = 16;
      treadRects = new Rect[]{
        new Rect(-31, -21, 14, 56),
        new Rect(-13, -37, 13, 72)
      };

      constructor = TankUnit::create;

			weapons.add(
        new Weapon("sw-longer-mortar") {{
				  x = y = 0f;
				  reload = 450f;
				  recoil = 0f;
	  			recoilTime = 60f;
		  		cooldownTime = 225f;
			  	rotateSpeed = 0.4f;
				  shootY = 26f;
  				shootSound = Sounds.largeCannon;
	  			rotate = true;
		  		mirror = false;

			  	parts.add(
				  	new RegionPart("-cannon") {{
				  		moveY = -4f;
				  		outlineLayerOffset = 0f;
				  		under = true;
				  		progress = PartProgress.recoil.curve(Interp.bounceIn);
				  	}}
				  );

				  bullet = new ArtilleryBulletType(2f, 1, "missile-large") {{
				  	frontColor = Pal.missileYellow;
				  	backColor = trailColor = hitColor = Pal.missileYellowBack;
				  	splashDamage = 300f;
				  	splashDamageRadius = 16f;
				  	width = height = 18f;
				  	lifetime = 500;
				  	shrinkX = shrinkY = 0.5f;
				  	trailWidth = 3f;
				  	trailLength = 50;
				  	collides = collidesTiles = collidesGround = true;
				  	shootEffect = Fx.shootTitan;
				  	smokeEffect = Fx.shootSmokeTitan;
				  	trailEffect = Fx.none;
				  	hitEffect = despawnEffect = new MultiEffect(Fx.titanSmoke, Fx.titanExplosion);
				  	bullet.hitSound = bullet.despawnSound = Sounds.explosionbig;

            bulletInterval = 15f;
				  	intervalBullets = 2;
            intervalSpread = 20f;
            intervalRandomSpread = 0f;
				  	intervalBullet = new BasicBulletType(1f, 10) {{
              frontColor = Pal.missileYellow;
              backColor = trailColor = hitColor = Pal.missileYellowBack;
              trailWidth = 1f;
              trailLength = 10;
              homingPower = 0.4f;
              homingRange = 40f;
              lifetime = 20f;
              hitSound = despawnSound = Sounds.explosion;
				  	}};
				  }};
        }}
      );
    }};
    castle = new SWUnitType("castle") {{
      health = 1400;
      speed = 0.25f;
      range = maxRange = 1200f;
      hitSize = 14f;
      rotateSpeed = 0.7f;
	    outlines = faceTarget = false;
	    targetAir = false;
	    squareShape = true;
	    omniMovement = false;
	    rotateMoveFirst = true;
	    aiController = MortarAI::new;

      treadFrames = 16;
      treadRects = new Rect[]{
        new Rect(-34f, -20f, 14, 64),
        new Rect(-12f, -47f, 12, 95)
      };

      constructor = TankUnit::create;

      weapons.add(new Weapon("sw-longest-mortar") {{
        x = y = 0f;
        reload = 600f;
        recoil = 0f;
        recoilTime = 60f;
        cooldownTime = 225f;
        rotateSpeed = 0.3f;
        shootY = 34f;
        shootSound = Sounds.largeCannon;
        rotate = true;
        mirror = false;

        parts.add(
          new RegionPart("-cannon") {{
            moveY = -8.5f;
            outlineLayerOffset = 0f;
            under = true;
            progress = PartProgress.recoil.curve(Interp.bounceIn);
          }}
        );

        bullet = new ArtilleryBulletType(2f, 1, "missile-large") {{
          frontColor = Pal.missileYellow;
          backColor = trailColor = hitColor = Pal.missileYellowBack;
          splashDamage = 600f;
          splashDamageRadius = 16f;
          width = height = 18f;
          lifetime = 600;
          shrinkX = shrinkY = 0.5f;
          trailWidth = 3f;
          trailLength = 50;
          collides = collidesTiles = collidesGround = true;
          shootEffect = Fx.shootTitan;
          smokeEffect = Fx.shootSmokeTitan;
          trailEffect = Fx.none;
          hitEffect = despawnEffect = new MultiEffect(Fx.titanSmoke, Fx.titanExplosion);
          hitSound = despawnSound = Sounds.explosionbig;

          bulletInterval = 15f;
          intervalBullets = 2;
          intervalSpread = 20f;
          intervalRandomSpread = 0f;
          intervalBullet = new BasicBulletType(1f, 10) {{
            frontColor = Pal.missileYellow;
            backColor = trailColor = hitColor = Pal.missileYellowBack;
            trailWidth = 1f;
            trailLength = 10;
            homingPower = 0.4f;
            homingRange = 40f;
            lifetime = 20f;
            hitSound = despawnSound = Sounds.explosion;
          }};
        }};
      }});
    }};
    //endregion
    //region legs
    existence = new SWUnitType("existence") {{
      health = 250;
  		speed = 1.2f;
	  	rotateSpeed = 3f;
		  range = maxRange = 24f;

	  	constructor = MechUnit::create;

      weapons.add(new Weapon() {{
        bullet = new ExplosionBulletType(150, 80);
      }});
    }};
    remembered = new SWUnitType("remembered") {{
      health = 600;
	  	hitSize = 9f;
	  	speed = 0.7f;
	  	rotateSpeed = 3f;
	  	range = maxRange = 80f;

	  	constructor = LegsUnit::create;

			weapons.add(
				new Weapon("sw-bottom-gun") {{
          x = 0f;
          y = 5.75f;
          reload = 120f;
          mirror = false;
          layerOffset = -0.01f;

          bullet = new LaserBulletType(25) {{
            length = 100f;
          }};
        }},
				new Weapon("sw-top-gun") {{
          x = 4.75f;
          y = 2.25f;
          reload = 60f;

          bullet = new MissileBulletType(2, 14) {{
            frontColor = Color.white;
            backColor = hitColor = trailColor = Pal.lancerLaser;
            lifetime = 40f;
            width = height = 10f;
          }};
        }}
			);
    }};
    presence = new SWUnitType("presence") {{
      health = 1230;
	  	hitSize = 11f;
	  	speed = 0.7f;
	  	rotateSpeed = 3f;
	  	range = maxRange = 120f;

			legCount = 6;
			legLength = 16f;
			legExtension = 2f;

	  	constructor = LegsUnit::create;

      weapons.add(
	      new Weapon("sw-presence-cannon") {{
		      x = 7.25f;
		      y = 4f;
		      range = 100f;
		      reload = 60f;
		      rotate = false;
		      layerOffset = -0.01f;

		      shootSound = Sounds.artillery;
          shoot = new ShootBarrel() {{
						shots = 2;
            barrels = new float[] {
              -1.5f, 1.5f, 0f,
              2.5f, -2.5f, 0f
            };
          }};
		      bullet = new BasicBulletType(4, 20) {{
			      lifetime = 25f;
			      width = height = 12f;
		      }};
	      }},
        new Weapon("sw-laser-gun") {{
          x = 7.5f;
          y = -4.25f;
          reload = 120f;
          rotate = true;
          alternate = false;

          shootSound = Sounds.laser;
          bullet = new LaserBulletType(20) {{
            length = 120f;
          }};
        }}
      );
    }};
    //endregion
	  //region copter
    fly = new SWUnitType("fly") {{
      health = 250;
      speed = 2f;
      fallSpeed = 0.005f;
      rotateSpeed = 8f;
      range = maxRange = 120f;

      engineSize = 0f;

      flying = lowAltitude = true;

      loopSound = Sounds.cutter;
      constructor = UnitCopter::create;

      rotors.add(
        new UnitRotor("-rotor", true) {{
          speed = 10f;
        }}
      );

      weapons.add(
        new Weapon("sw-small-launcher") {{
          x = 3.75f;
          y = 3f;
          reload = 60;
          layerOffset = -0.01f;
          shootSound = Sounds.missileLarge;

          shoot = new ShootSpread(3, 5f) {{
            shotDelay = 5f;
          }};

          bullet = new BasicBulletType(2f, 5, "missile") {{
            frontColor = Pal.missileYellow;
            backColor = Pal.missileYellowBack;
            trailLength = 5;
            width = 7f;
            height = 9f;
            lifetime = 60f;
          }};
        }}
      );
    }};
	  spin = new SWUnitType("spin") {{
		  health = 560;
		  hitSize = 10f;
		  speed = 2f;
      fallSpeed = 0.005f;
		  rotateSpeed = 4f;
		  range = maxRange = 96f;

		  engineSize = 0f;

		  flying = lowAltitude = true;

      loopSound = Sounds.cutter;
		  constructor = UnitCopter::create;

      rotors.add(
        new UnitRotor("sw-bottom-rotor", false) {{
          speed = 10f;
          layerOffset = -0.02f;
          drawTop = false;
        }}
      );

		  weapons.add(
        new Weapon("sw-small-artillery") {{
          x = 0f;
          y = -3f;
          reload = 45;
          mirror = false;
          rotate = true;
          shootSound = Sounds.malignShoot;

          bullet = new BasicBulletType(3f, 20, "mine-bullet") {{
            frontColor = Pal.missileYellow;
            backColor = Pal.missileYellowBack;
            trailLength = 10;
            width = 8f;
            height = 12f;
            recoil = 1f;
            lifetime = 32f;
          }};
        }},
        new Weapon("sw-small-launcher") {{
          x = 6.5f;
          y = 0.75f;
          reload = 30;
          layerOffset = -0.01f;
          shootSound = Sounds.shootAlt;
          alternate = false;

          bullet = new BasicBulletType(2f, 12f, "missile-large") {{
            frontColor = Pal.missileYellow;
            backColor = Pal.missileYellowBack;
            trailLength = 5;
            width = 7f;
            height = 9f;
            homingPower = 0.012f;
            lifetime = 48f;
          }};
        }}
      );
	  }};
    gyro = new SWUnitType("gyro") {{
      health = 1300;
      hitSize = 14f;
      speed = 1.5f;
      fallSpeed = 0.005f;
      rotateSpeed = 4f;
      range = maxRange = 200f;

      engineSize = 0f;

      flying = lowAltitude = true;

      loopSound = Sounds.cutter;
      constructor = UnitCopter::create;

      rotors.add(
        new UnitRotor("-rotor", true) {{
          speed = 10f;
        }},
        new UnitRotor("sw-bottom-rotor", false) {{
          x = 10f;
          speed = 10f;
          layerOffset = -0.02f;
          mirrored = true;
          drawTop = false;
        }}
      );

      weapons.add(
        new Weapon("sw-small-artillery") {{
          x = 4.5f;
          y = -2.75f;
          reload = 60;
          rotate = true;
          shootSound = Sounds.missileSmall;

          bullet = new BasicBulletType(2f, 20, "missile-large") {{
            frontColor = Pal.missileYellow;
            backColor = Pal.missileYellowBack;
            width = height = 8f;
            shrinkY = 0f;
            trailLength  = 15;
            lifetime = 100f;
            homingPower = 0.02f;
          }};
        }},
        new Weapon("sw-medium-launcher") {{
          x = 7.75f;
          y = 5.5f;
          reload = 30;
          layerOffset = -0.01f;
          shootSound = Sounds.shootBig;

          bullet = new BasicBulletType(4f, 25) {{
            frontColor = Pal.missileYellow;
            backColor = Pal.missileYellowBack;
            width = height = 12f;
            lifetime = 25f;
          }};
        }}
      );
    }};
		//endregion
	  //region grasshopper
	  cinerea = new GrassHopperUnitType("cinerea") {{
		  outlines = false;

			health = 250f;
			drag = 0.1f;
			speed = 0.55f;
			range = maxRange = 80f;

			engineSize = engineOffset = 0f;
			boostMultiplier = 2f;

		  legContinuousMove = true;
		  legBaseOffset = 2f;
			legCount = 4;
		  legExtension = 4f;
			legGroupSize = 2;
		  legLength = 6f;
			legStraightness = 0.25f;

			wings.add(new GrassHopperWing("-wing", 3f, -0.5f) {{
				moveRotation = 15f;
				mag = 15f;
				scl = 2f;
				shineScl = 4f;
				shineMag = 15f;

				blurs = 3;
				blurOffset = 8f;
			}});

			weapons.add(new Weapon("sw-cinerea-mouth") {{
				mirror = false;
				x = 0f;
				y = 5f;
				layerOffset = -0.001f;
				reload = 30f;
				inaccuracy = 15f;
				shootSound = Sounds.spark;

				bullet = new BasicBulletType(0.01f, 10) {{
					keepVelocity = false;

					drag = -0.1f;
					lifetime = 70f;

					homingRange = 20f;
					homingPower = 0.2f;
					homingDelay = 60f;

					splashDamage = 5f;
					splashDamageRadius = 24f;
					scaledSplashDamage = false;

					width = 8f;
					height = 8f;
					frontColor = trailColor = backColor = Pal.heal;

					trailWidth = 2f;
					trailLength = 10;

					hitSound = despawnSound = Sounds.lasershoot;

					shootEffect = new WaveEffect() {{
						lifetime = 30f;
						sizeFrom = 24f;
						sizeTo = 0f;
						strokeFrom = 0f;
						strokeTo = 2f;
						colorFrom = Pal.heal.cpy();
						followParent = false;
					}};
					hitEffect = despawnEffect = new WaveEffect() {{
						sizeTo = 24f;
						colorFrom = Pal.heal.cpy();
						followParent = false;
					}};
				}};
			}});
	  }};
		robinia = new GrassHopperUnitType("robinia") {{
			outlines = false;

			health = 600;
			drag = 0.1f;
			speed = 0.55f;
			range = maxRange = 144f;

			engineSize = engineOffset = 0f;
			boostMultiplier = 2f;

			legContinuousMove = true;
			legCount = 4;
			legBaseOffset = 4f;
			legExtension = 4f;
			legGroupSize = 2;
			legLength = 16f;
			legMaxLength = 1.75f;
			legStraightness = 0.25f;

			parts.add(new RegionPart("-salvo") {{
				mirror = true;
				x = 3.75f;
				moveRot = 45f;

				progress = PartProgress.warmup.add(PartProgress.heat.mul(2f)).clamp();
			}});

			wings.add(new GrassHopperWing("-wing", 4, 0.25f) {{
				moveRotation = 45f;
				scl = 4f;
				mag = 30f;
				shineScl = 8f;
				shineMag = 30f;
				blurs = 4;
				blurOffset = 8f;
			}});

			weapons.add(
				new Weapon() {{
					reload = 240f;
					x = 4f;
					y = -4f;
					baseRotation = -45f;
					shootCone = 90f;
					minWarmup = 0.9f;

					shoot = new ShootBarrel() {{
						barrels = new float[]{
							-2f, 1f, 0f,
							0f, 1f, 0f,
							2f, 2f, 0f
						};
						shots = 3;
						shotDelay = 10f;
					}};

					shootSound = Sounds.missile;

					bullet = new BasicBulletType(2f, 15f, "missile") {{
						lifetime = 60f;
						trailInterval = 5f;
						weaveScale = 10f;
						weaveMag = 1f;
						weaveRandom = false;
						homingPower = 0.05f;

						shootEffect = Fx.shootSmallColor;

						frontColor = backColor = trailColor = Pal.heal;
					}};
				}},
				new Weapon() {{
					mirror = false;

					x = 0f;
					y = 6f;

					reload = 120f;

					shootSound = Sounds.release;

					bullet = new ShrapnelBulletType() {{
						layer = Layer.blockOver;
						length = 94f;
						damage = 30f;

						toColor = fromColor = hitColor = Pal.heal;

						shootEffect = SWFx.shootShockwaveColor;
					}};
				}}
			);
		}};
	  //endregion

    lambda = new UnitType("lambda") {{
			health = 300;
			speed = 1;
			hitSize = 8f;
			range = maxRange = 100;
			engineSize = engineOffset = shadowElevationScl = 0f;
			fallSpeed = buildSpeed = 1f;
			mineTier = 1;
			mineSpeed = 5;
	    coreUnitDock = lowAltitude = true;
			//shocking
			flying = true;

			legCount = 8;
			legGroupSize = 2;
			legForwardScl = 0f;
	    legBaseOffset = 4f;
	    legExtension = -2f;
	    legLength = 14f;
			legContinuousMove = true;

			constructor = UnitLegsIntangible::create;

			weapons.add(
				new Weapon("sw-lambda-weapon") {{
					x = 4.75f;
					y = 2.5f;
					reload = 60;
					layerOffset = -0.001f;
					shootSound = Sounds.shootAlt;
					shoot = new ShootPattern() {{
						shots = 3;
						shotDelay = 5;
					}};

					bullet = new BasicBulletType(2f, 7) {{
						lifetime = 50f;
						trailLength = 10;
						trailWidth = 1f;
					}};
				}}
			);
    }};
  }
}

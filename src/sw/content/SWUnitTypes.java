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
import sw.world.recipes.*;

import static mindustry.type.ItemStack.*;

public class SWUnitTypes {
  @EntityDef({Submarinec.class, WaterMovec.class, Unitc.class}) public static UnitType recluse, retreat, evade;
  @EntityDef({TetherUnitc.class, Unitc.class}) public static UnitType deltaShield, protMask;
  @EntityDef({Shieldedc.class, Legsc.class, Unitc.class}) public static UnitType delta, prot;
  @EntityDef({Copterc.class, Unitc.class}) public static UnitType fly, spin, gyro;
  @EntityDef({BuildingTetherc.class, Unitc.class}) public static UnitType terra;

  public static UnitType
		focus, precision, target,

    sentry, tower, castle, stronghold,
		existence, remembered, presence,
    bakler, structura;

  public static void load() {
    terra = new UnitType("terra") {{
      health = 250;
      speed = 3f;
      flying = lowAltitude = true;
      playerControllable = useUnitCap = false;
      rotateSpeed = 10f;
      engineOffset = 8.5f;
      engineSize = 5;
      setEnginesMirror(new UnitEngine(8.5f, 0, 5, 0));
      engines.add(new UnitEngine(0, 8.5f, 5, 90));

      constructor = UnitBuildingTether::create;
      controller = u -> new FillerAI();
    }};

    //region attack
		focus = new UnitType("focus") {{
			health = 200;
			speed = 0.5f;
      range = maxRange = 104f;

      constructor = MechUnit::create;

			weapons.add(new Weapon("sw-mine-cannon") {{
				x = y = 0f;
				reload = 30f;
        layerOffset = -0.01f;
				mirror = false;
        shootSound = Sounds.artillery;

				bullet = new BasicBulletType(5.5f, 0, "large-bomb") {{
          width = height = 16f;
          shrinkY = 0f;
          spin = 5f;
          trailWidth = 2f;
          trailLength = 10;

          hitSound = Sounds.plasmaboom;

					splashDamage = 16;
          splashDamageRadius = 40f;
          drag = 0.05f;
          lifetime = 60;
          collidesAir = false;
				}};
			}});
		}};
    precision = new UnitType("precision") {{
      health = 600;
      speed = 0.5f;
      hitSize = 8f;
      armor = 3;
      range = maxRange = 240f;

      constructor = MechUnit::create;

      weapons.add(new Weapon("artillery") {{
        x = 5.25f;
        y = -0.25f;
        reload = 60f;
        shake = 2f;
        ejectEffect = Fx.casing2;
        shootSound = Sounds.artillery;

				bullet = new ArtilleryBulletType(4, 40, "shell") {{
          hitEffect = Fx.blastExplosion;
          knockback = 0.8f;
          lifetime = 60f;
          width = height = 14f;
          collides = collidesGround = collidesAir = collidesTiles = true;
          splashDamageRadius = 35f;
          splashDamage = 40f;
          backColor = Pal.bulletYellowBack;
          frontColor = Pal.bulletYellow;
        }};
      }});
    }};
    target = new UnitType("target") {{
      health = 1000;
      speed = 0.45f;
      rotateSpeed = 2.5f;
      armor = 9;
      range = maxRange = 160f;

      constructor = MechUnit::create;

      weapons.add(
        new Weapon("sw-frag-cannon") {{
          x = 8f;
          y = 0.75f;
          reload = 60f;
          layerOffset = -0.01f;
          shootSound = Sounds.artillery;

          bullet = new BasicBulletType(5.5f, 0, "large-bomb") {{
            width = height = 16f;
            shrinkY = 0f;
            spin = 5f;
            trailWidth = 2f;
            trailLength = 10;

            hitSound = Sounds.plasmaboom;

            splashDamage = 16;
            splashDamageRadius = 40f;
            drag = 0.05f;
            lifetime = 60;
            collidesAir = false;

            fragBullets = 9;
            fragBullet = new BasicBulletType(2f, 5) {{
              lifetime = 20f;
              width = height = 4f;
              frontColor = backColor = Color.white;
            }};
          }};
        }},
        new Weapon("sw-frag-cannon") {{
          x = 5.75f;
          y = 0.25f;
          reload = 45f;
          shootSound = Sounds.missile;

          bullet = new MissileBulletType(4f, 40) {{
            lifetime = 40f;
            width = height = 12f;

            fragBullets = 2;
            fragRandomSpread = 0f;
            fragVelocityMin = 1f;
            fragBullet = new MissileBulletType(2f, 10) {{
              lifetime = 20f;
              width = height = 8f;
            }};
          }};
        }}
      );
    }};

    //endregion
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

      constructor = UnitWaterMoveSubmarine::create;

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

      constructor = UnitWaterMoveSubmarine::create;

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

      constructor = UnitWaterMoveSubmarine::create;

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

      weapons.add(new Weapon("sw-longest-mortal") {{
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
    protMask = new SWUnitType("prot-mask") {{
      health = 2300;
      speed = 2f;
      rotateSpeed = 3f;
      range = maxRange = 0f;

      engineOffset = 2f;

      flying = lowAltitude = hidden = true;
      playerControllable = targetable = false;

      controller = u -> new ShieldAI();
      constructor = UnitTetherUnit::create;
    }};
    prot = new SWUnitType("prot") {{
      health = 2300;
      hitSize = 16f;
      speed = 1f;
      rotateSpeed = 1f;
      range = maxRange = 160f;

      shieldSeparateRadius = 24f;
      shieldStartAng = -54f;
      shieldEndAng = 288f;
      shieldShootingStartAng = 40f;
      shieldShootingEndAng = 100f;

      shieldUnit = protMask;

      legCount = 6;
      legGroupSize = 3;
      legLength = 30f;
      legBaseOffset = 10f;
			legExtension = 15f;

      constructor = UnitLegsShielded::create;

      weapons.add(new Weapon("sw-prot-weapon") {{
				x = 12.75f;
				y = 0;
				shootY = 11f;
				reload = 60f;
				recoil = 2f;

				top = false;

				shootSound = Sounds.artillery;

				bullet = new LaserBulletType(250) {{
					length = 160f;
					shootEffect = Fx.shockwave;
					colors = new Color[]{Color.valueOf("ec7458aa"), Color.valueOf("ff9c5a"), Color.white};
				}};
			}});
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

          bullet = new BasicBulletType(4f, 10) {{
            frontColor = Pal.missileYellow;
            backColor = Pal.missileYellowBack;
            width = height = 12f;
            lifetime = 25f;
          }};
        }}
      );
    }};
		//endregion

    deltaShield = new SWUnitType("delta-shield") {{
      health = 500;
      speed = 2f;
      rotateSpeed = 3f;
      range = maxRange = 0f;

      engineOffset = 2f;

      flying = lowAltitude = hidden = true;
      playerControllable = drawCell = targetable = false;

      controller = u -> new ShieldAI();
      constructor = UnitTetherUnit::create;
    }};
    delta = new SWUnitType("delta") {{
      health = 650f;
      hitSize = 8f;
      speed = 1f;
      rotateSpeed = 2f;
      range = maxRange = 80f;

      shields = 4;
      shieldSeparateRadius = 24f;
      shieldStartAng = -45f;
      shieldEndAng = 270f;
      shieldShootingStartAng = 40f;
      shieldShootingEndAng = 100f;

      shieldUnit = deltaShield;

      legCount = 4;
      legGroupSize = 2;
      legLength = 18f;
      legBaseOffset = 2f;
      legExtension = 2f;
      lockLegBase = true;
      legContinuousMove = true;

      canBoost = true;
      boostMultiplier = 5f;
      engineSize = 0f;
      mineTier = 2;
      mineSpeed = 10f;

      buildSpeed = 0.75f;

      constructor = UnitLegsShielded::create;

			parts.add(
				new RegionPart("-spine") {{
          x = 4.5f;
          y = -2.5f;
          moveRot = -22.5f;
          mirror = under = true;
          layerOffset = -0.01f;
          moves.add(new PartMove(PartProgress.reload, 0, 0, -22.5f));
        }},
        new RegionPart("-weapon") {{
          x = y = 0;
          moveY = -1f;
          progress = PartProgress.reload;
        }}
			);

			weapons.add(
				new Weapon() {{
					x = y = 0f;
					reload = 60f;
					shootY = 8f;

					mirror = false;

          shootSound = Sounds.bolt;
					bullet = new LaserBulletType(20) {{
						length = 80f;
						shootEffect = Fx.shockwave;
						colors = new Color[]{Color.valueOf("ec7458aa"), Color.valueOf("ff9c5a"), Color.white};
					}};
				}}
			);
    }};

    bakler = new SWUnitType("bakler") {{
      health = 1500;
      speed = 2f;
      hitSize = 8f;
      payloadCapacity = 256f;
      useUnitCap = false;
      flying = true;

      engineOffset = 9f;
      engineSize = 3f;
      setEnginesMirror(new UnitEngine(7, -2, 3, -45));

      recipe = new GenericRecipe() {{
        consumeItems = with(Items.coal, 6, Items.sand, 10);
        craftTime = 60f;
        outputItems = with(Items.silicon, 10);
        craftEffect = SWFx.baklerSiliconCraft;
        updateEffect = Fx.smoke;
      }};

      constructor = UnitEntity::create;
    }};
    structura = new SWUnitType("structura") {{
      health = 1500;
      speed = 2f;
      hitSize = 8f;
      payloadCapacity = 256f;
      useUnitCap = false;
      flying = true;

      engineOffset = 9f;
      engineSize = 3f;
      setEnginesMirror(new UnitEngine(7, -2, 3, -45));

      recipe = new GenericRecipe() {{
        consumeItems = with(SWItems.nickel, 5, Items.copper, 10);
        craftTime = 60f;
        outputItems = with(SWItems.compound, 7);
        craftEffect = SWFx.baklerSiliconCraft;
        updateEffect = Fx.smoke;
      }};

      constructor = UnitEntity::create;
    }};
  }
}

package sw.content;

import arc.graphics.*;
import arc.math.geom.*;
import mindustry.content.*;
import mindustry.entities.abilities.*;
import mindustry.entities.bullet.*;
import mindustry.entities.effect.*;
import mindustry.entities.part.*;
import mindustry.entities.pattern.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.type.ammo.*;
import mindustry.type.unit.*;
import mindustry.world.blocks.units.*;
import sw.ai.*;
import sw.entities.bullet.*;
import sw.entities.comp.*;
import sw.type.*;
import sw.world.recipes.*;

import static mindustry.type.ItemStack.*;
import static mindustry.world.blocks.units.UnitFactory.*;

public class SWUnitTypes {
  public static UnitType
    terra,

    swarm, ambush, trap, misleading,
		focus, precision, target,

    recluse, retreat, evade,
    sentry, tower, castle, stronghold,
		existence, remembered, presence,
    prot, protMask,
    delta, deltaShield,
    bakler, structura;

  public static void load() {
    terra = new UnitType("terra") {{
      health = 250;
      speed = 3f;
      flying = lowAltitude = true;
      playerControllable = useUnitCap = false;
      rotateSpeed = 10f;
      engineOffset = 6.5f;
      range = maxRange = 120f;

      constructor = BuildingTetherUnit::new;
      controller = u -> new FillerAI();
    }};

    //region attack
    swarm = new UnitType("swarm") {{
      health = 250;
      speed = 2f;
      rotateSpeed = 8f;
      range = maxRange = 120f;

      flying = lowAltitude = true;

      constructor = UnitEntity::create;

      weapons.add(
        new Weapon("sw-swarm-weapon") {{
          x = y = 0f;
          reload = 15f;
					mirror = false;
          bullet = new BasicBulletType(2f, 12) {{
            lifetime = 60f;
            width = 7f;
            height = 9f;
          }};
        }}
      );
    }};
    ambush = new UnitType("ambush") {{
      health = 560;
      hitSize = 10f;
      speed = 2f;
      rotateSpeed = 4f;
      range = maxRange = 120f;

      engineSize = 3f;
      engineOffset = 9f;

      flying = lowAltitude = true;

      constructor = UnitEntity::create;

      weapons.add(
        new Weapon("sw-ambush-weapon") {{
          x = 4.75f;
          y = -0.75f;
          reload = 60;
          bullet = new BasicBulletType(3f, 25) {{
            lifetime = 40f;
            width = 9f;
            height = 11f;
          }};
        }}
      );

      abilities.add(new SpawnDeathAbility(swarm, 1, 0));
    }};
    trap = new UnitType("trap") {{
      health = 1300;
      hitSize = 14f;
      speed = 1.5f;
      rotateSpeed = 2f;
      range = maxRange = 144f;

      engineSize = 5.25f;
      engineOffset = 12.5f;

      flying = lowAltitude = true;

	    constructor = UnitEntity::create;

      weapons.add(
        new Weapon("sw-trap-cannon") {{
          x = y = 0f;
          reload = 60;
          mirror = false;
          shootSound = Sounds.shotgun;
          shake = 2f;
          bullet = new ArtilleryBulletType(4f, 30) {{
            width = height = 10f;
            lifetime = 36f;
            splashDamage = 30f;
            splashDamageRadius = 64f;
            collides = collidesAir = collidesGround = true;
            hitEffect = despawnEffect = Fx.hitBulletBig;
          }};
        }}
      );

      abilities.addAll(new SpawnDeathAbility(ambush, 1, 0));
    }};
		misleading = new UnitType("misleading") {{
      health = 8500;
      hitSize = 16f;
      speed = 1.5f;
      rotateSpeed = 3f;
      range = maxRange = 160f;

      engineSize = 5f;
      engineOffset = 13f;
      setEnginesMirror(
        new UnitEngine(12f, -8f, 3f, -45f),
        new UnitEngine(6f, -13f, 3f, -80f)
      );

      flying = lowAltitude = true;

      constructor = UnitEntity::create;

      weapons.add(
        new Weapon("sw-misleading-cannon") {{
          x = 7f;
          y = 1.5f;
          reload = 45f;
          recoil = 3f;
          inaccuracy = 2f;

          shootSound = Sounds.mediumCannon;

          bullet = new BasicBulletType(4f, 100, "missile-large") {{
            lifetime = 40f;
            width = height = 15f;
            splashDamage = 60f;
            splashDamageRadius = 40f;

            hitColor = backColor;

            shootEffect = Fx.shootTitan;
            smokeEffect = Fx.shootSmokeTitan;
            despawnEffect = new ExplosionEffect() {{
              smokeColor = Color.gray;
              waveColor = sparkColor = frontColor;
              waveStroke = 4f;
              waveRad = 40f;
            }};
          }};
        }}
      );

      abilities.add(new SpawnDeathAbility(trap, 1, 0));
		}};

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

      constructor = SubmarineUnit::new;

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

      constructor = SubmarineUnit::new;

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

      constructor = SubmarineUnit::new;

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

    sentry = new TankUnitType("sentry") {{
      health = 320;
      speed = 1f;
      range = maxRange = 160f;
      hitSize = 8f;
      rotateSpeed = 4;
      outlines = faceTarget = false;
      treadFrames = 14;
      treadRects = new Rect[]{new Rect(11f - 32f, 8f - 32f, 14, 53)};
      ammoType = new ItemAmmoType(Items.copper);
      constructor = TankUnit::create;

      weapons.add(new Weapon("sw-sentry-fuse") {{
        x = 0f;
        y = 0.25f;
        reload = 120f;
        recoil = 0f;
        range = 160f;
        rotate = true;
        rotateSpeed = 7f;
        mirror = false;
				shootY = 4;
        shootSound = Sounds.pulseBlast;
        bullet = new MultiBulletType() {{
          shootEffect = hitEffect = smokeEffect = Fx.sparkShoot;
          shake = 2f;
          recoil = 2f;
          range = rangeOverride = 160f;
          bullets.add(
            new ShrapnelBulletType() {{
              damage = 36;
              length = 160f;
              fromColor = Pal.accent;
              toColor = Color.gray;
            }},
            new BasicBulletType(2f, 0, "circle-bullet") {{
              lifetime = 80f;
              width = height = 10f;
              bulletInterval = 10;
              intervalBullets = 10;
              intervalBullet = new LightningBulletType() {{
                damage = 1;
                lightningColor = hitColor = Pal.accent;
                lightningLength = 5;
                lightningLengthRand = 3;
                lightningType = new BulletType(1.0E-4f, 0.0f) {{
                  lifetime = Fx.lightning.lifetime;
                  despawnEffect = Fx.none;
                  hittable = false;
                }};
              }};
            }}
          );
        }};
      }});
    }};
    tower = new TankUnitType("tower") {{
      health = 650;
      speed = 0.8f;
      range = maxRange = 160f;
      hitSize = 10f;
      rotateSpeed = 3;
      outlines = faceTarget = false;
      treadFrames = 16;
      treadRects = new Rect[]{new Rect(-25f, -36f, 13, 71)};
      ammoType = new ItemAmmoType(Items.copper);
      constructor = TankUnit::create;

			weapons.add(new Weapon("sw-tower-shotgun") {{
        x = 0f;
        y = 0.5f;
				reload = 60f;
        range = 160f;
        mirror = false;
        rotate = true;
        rotateSpeed = 5f;
        shootSound = Sounds.shootAltLong;
        bullet = new MultiBulletType() {{
          range = rangeOverride = 160f;
          shake = 3f;
          shootEffect = Fx.shootBigColor;
          smokeEffect = Fx.shootSmokeSquareSparse;
          bullets.add(
            new BasicBulletType(1f, 0) {{
              lifetime = 1;
              fragBullet = new BasicBulletType(4, 4) {{
                lifetime = 20f;
                knockback = 3f;
                trailWidth = 6f;
                trailLength = 3;
                hitEffect = despawnEffect = Fx.hitSquaresColor;
              }};
              fragVelocityMax = fragVelocityMin = 1;
              fragRandomSpread = 0;
              fragSpread = 4f;
              fragBullets = 15;
            }},
            new LaserBulletType(30) {{
              length = 160;
              width = 16f;
              colors = new Color[]{Pal.accent, Pal.accent, Color.white};
            }}
          );
        }};
      }});
    }};
    castle = new TankUnitType("castle") {{
      health = 1400;
      speed = 0.7f;
      range = maxRange = 220f;
      hitSize = 14f;
      rotateSpeed = 2.5f;
      outlines = false;
      treadFrames = 22;
      treadRects = new Rect[]{new Rect(-8f, -49f, 16, 95), new Rect(-34, -40, 13, 88)};
      constructor = TankUnit::create;

      weapons.add(new Weapon("sw-castle-gun") {{
        x = y = 0f;
        reload = 120f;
        range = 220f;
        shake = 5f;
        shootY = 8f;
        mirror = false;
        rotate = true;
        rotateSpeed = 3f;
        shootSound = Sounds.pulseBlast;
        bullet = new MultiBulletType() {{
          range = rangeOverride = 220f;
          shootEffect = smokeEffect = Fx.sparkShoot;
          bullets.add(
            new BulletType(10f, 0) {{
              lifetime = 22f;
              bulletInterval = 1;
              intervalBullets = 10;
              hitEffect = despawnEffect = Fx.none;
              intervalBullet = new LightningBulletType() {{
                damage = 5;
                lightningColor = hitColor = Pal.accent;
                lightningLength = 5;
                lightningLengthRand = 3;
                lightningType = new BulletType(1.0E-4f, 0.0f) {{
                  lifetime = Fx.lightning.lifetime;
                  despawnEffect = Fx.none;
                  hittable = false;
                }};
              }};
            }},
            new LaserBulletType(300) {{
              length = 220f;
              lifetime = 22f;
              colors = new Color[]{Pal.accent, Pal.accent, Color.white};
            }}
          );
        }};
      }});
    }};
    stronghold = new TankUnitType("stronghold") {{
      health = 9500;
      hitSize = 16f;
      speed = 0.6f;
      rotateSpeed = 1.5f;
      range = maxRange = 240f;

      treadFrames = 12;
      treadRects = new Rect[]{new Rect(-50f, -26f, 17, 53)};

      outlines = false;

      constructor = TankUnit::create;

      weapons.add(
        new Weapon("sw-stronghold-cannon") {{
          x = 0f;
          y = -4f;
          reload = 120f;
          recoil = 4f;
          rotateSpeed = 1f;
          inaccuracy = 3f;

          rotate = true;
          mirror = false;

          shootSound = Sounds.mediumCannon;

          bullet = new MultiBulletType() {{
            range = rangeOverride = 240f;
            shootEffect = Fx.shootTitan;
            smokeEffect = Fx.shootSmokeTitan;

            bullets.add(
              new BasicBulletType(4, 100) {{
                lifetime = 60f;
                width = height = 15f;
                splashDamage = 60f;
                splashDamageRadius = 40f;

                trailWidth = 4f;
                trailLength = 8;

                hitColor = backColor;
                despawnEffect = new ExplosionEffect() {{
                  smokeColor = Color.gray;
                  waveColor = sparkColor = frontColor;
                  waveStroke = 4f;
                  waveRad = 40f;
                }};
              }},
              new LaserBulletType(100) {{
                length = 220f;
                colors = new Color[]{Pal.accent, Pal.accent, Color.white};
              }}
            );
          }};
        }},
        new Weapon("missiles-mount") {{
          x = 9.5f;
          y = 8.5f;
          reload = 60;
          inaccuracy = 2f;

          rotate = true;

          shootSound = Sounds.missile;

          bullet = new MissileBulletType(2f, 30) {{
            lifetime = 60f;
            width = height = 8f;
          }};
        }}
      );
    }};

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
      constructor = UnitTetherUnit::new;
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

      constructor = ShieldedUnit::new;

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

    deltaShield = new SWUnitType("delta-shield") {{
      health = 500;
      speed = 2f;
      rotateSpeed = 3f;
      range = maxRange = 0f;

      engineOffset = 2f;

      flying = lowAltitude = hidden = true;
      playerControllable = drawCell = targetable = false;

      controller = u -> new ShieldAI();
      constructor = UnitTetherUnit::new;
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

      constructor = ShieldedUnit::new;

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

      constructor = CrafterUnit::new;
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

      constructor = CrafterUnit::new;
    }};

    ((UnitFactory) Blocks.groundFactory).plans.add(
      new UnitPlan(sentry, 60f * 40f, with(SWItems.compound, 16, Items.silicon, 7, Items.titanium, 7)),
      new UnitPlan(existence, 60f * 45f, with(SWItems.denseAlloy, 20, Items.silicon, 8, Items.pyratite, 7))
    );
    ((UnitFactory) Blocks.airFactory).plans.add(new UnitPlan(swarm, 60f * 10f, with(SWItems.compound, 12, Items.silicon, 7)));
    ((Reconstructor) Blocks.additiveReconstructor).upgrades.addAll(
      new UnitType[]{swarm, ambush},
      new UnitType[]{recluse, retreat},
      new UnitType[]{sentry, tower},
      new UnitType[]{existence, remembered}
    );
    ((Reconstructor) Blocks.multiplicativeReconstructor).upgrades.addAll(
      new UnitType[]{ambush, trap},
      new UnitType[]{retreat, evade},
      new UnitType[]{tower, castle},
      new UnitType[]{remembered, presence}
    );
		((Reconstructor) Blocks.exponentialReconstructor).upgrades.addAll(
      new UnitType[]{trap, misleading},
      new UnitType[]{castle, stronghold},
      new UnitType[]{presence, prot}
    );
  }
}

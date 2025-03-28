package sw.content;

import arc.graphics.*;
import arc.math.geom.*;
import ent.anno.Annotations.*;
import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.entities.part.*;
import mindustry.entities.pattern.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import sw.entities.*;
import sw.entities.part.*;
import sw.gen.*;
import sw.type.*;

public class SWUnitTypes {
  @EntityDef({Copterc.class, Unitc.class}) public static UnitType
		soar,
	  fly, spin, gyro;

	public static UnitType
		barrage;

	@EntityDef({Intangiblec.class, Legsc.class, Unitc.class}) public static UnitType lambda;

  public static void load() {
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

		soar = new SWUnitType("soar") {{
			constructor = UnitCopter::create;

			health = 250;
			speed = 2f;
			accel = drag = 0.05f;
			rotateSpeed = 6f;
			fallSpeed = 0.005f;

			wrecks = 4;

      hitSize = 10;
			engineSize = 0f;
			rotorSound = Sounds.cutter;
			rotorSoundVolumeFrom = 0.05f;
			rotorSoundVolumeTo = 0f;

			flying = lowAltitude = true;

			range = maxRange = 120f;

			rotors.add(new UnitRotor("-rotor", true) {{
				x = 0f;
				y = 3.75f;
        blades = 3;

				speed = 10f;
				shineSpeed = -2f;
			}});

			weapons.addAll(
				new Weapon("sw-soar-cannon") {{
					x = 7f;
					y = 5.5f;
					layerOffset = -0.01f;

          reload = 60f;

          recoil = 4f;
          recoilTime = 60f;

          shootSound = Sounds.cannon;
					bullet = new BasicBulletType(4, 10) {{
            recoil = 1f;

            width = 10f;
            height = 16f;
            lifetime = 30f;

						hitEffect = despawnEffect = Fx.hitBulletBig;

            trailEffect = Fx.disperseTrail;
            trailInterval = 1f;
            trailRotation = true;
            trailColor = Pal.accent;
					}};
				}}
			);
		}};
		//endregion

	  //region tanks
	  barrage = new SWUnitType("barrage") {{
			constructor = TankUnit::create;

		  omniMovement = false;
		  health = 250;
		  speed = 0.75f;
		  rotateSpeed = 3f;

			wrecks = 4;

			hitSize = 8;

			treadFrames = 8;
			treadRects = new Rect[]{
				new Rect(6, -28, 16, 56)
			};

			weapons.add(new Weapon("sw-barrage-cannon") {{
				x = y = 0;

				reload = 300f;

				parts.add(
					new AxlePart() {{
						axle = new Axle("-axle") {{
							y = 0.75f;

							width = 3.5f;
							height = 12f;

							circular = true;
							polySides = 3;
							hasSprites = false;

							paletteLight = Color.valueOf("B0BAC0");
							paletteMedium = Color.valueOf("B0BAC0");
							paletteDark = Color.valueOf("B0BAC0");
						}};
					}},
					new RegionPart("") {{
						layerOffset = 0.001f;
					}}
				);

				shoot = new ShootPattern() {{
					firstShotDelay = 60f;
				}};

				mirror = false;
			}});
	  }};
	  //endregion

    lambda = new SWUnitType("lambda") {{
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

						buildingDamageMultiplier = 0.01f;
					}};
				}}
			);
    }};
  }
}

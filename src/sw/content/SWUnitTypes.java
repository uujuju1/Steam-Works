package sw.content;

import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.entities.part.*;
import mindustry.entities.pattern.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import sw.type.*;

public class SWUnitTypes {
  public static UnitType
		soar,
	  fly, spin, gyro,
	  lambda, rho;

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
      constructor = UnitEntity::create;

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
		  constructor = UnitEntity::create;

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
      constructor = UnitEntity::create;

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
			health = 250;
			speed = 2f;
			accel = drag = 0.05f;
			rotateSpeed = 6f;
			fallSpeed = 0.005f;

			wrecks = 4;

      hitSize = 10;
			engineSize = 0f;

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
	  
	  //region core
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
			flying = true;

			legCount = 8;
			legGroupSize = 2;
			legForwardScl = 0f;
	    legBaseOffset = 4f;
	    legExtension = -2f;
	    legLength = 14f;
			legContinuousMove = true;

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
		
		rho = new SWUnitType("rho") {{
			health = 250;
			speed = 2;
			hitSize = 8f;
			range = maxRange = 100f;
			engineSize = engineOffset = shadowElevationScl = 0;
			fallSpeed = 1f;
			buildSpeed = 1f;
			coreUnitDock = true;
			lowAltitude = true;
			flying = true;
			
			outlineLayerOffset = -0.002f;
			
			parts.add(new RegionPart("-gear") {{
				x = 0f;
				y = -3.5f;
				
				layerOffset = -0.001f;
				
				progress = DrawPart.PartProgress.time.mul(1/300f).mod(1);
				moveRot =  360f;
			}});
		}};
		//endregion
  }
}

package sw.content;

import arc.math.*;
import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.entities.part.*;
import mindustry.entities.pattern.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import sw.gen.*;
import sw.entities.bullet.*;
import sw.type.*;

public class SWUnitTypes {
  public static UnitType
		soar,
	  lambda, rho;

  public static void load() {
	  //region copter
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
			speed = 4f;
			accel = drag = 0.05f;
			hitSize = 8f;
			
			float r = range = maxRange = 100f;
			
			engineSize = engineOffset = shadowElevationScl = 0;
			
			fallSpeed = 1f;
			buildSpeed = 1f;
			mineTier = 1;
			mineSpeed = 5;
			
			coreUnitDock = true;
			flying = true;
			
			outlineLayerOffset = -0.002f;
			
			parts.add(new RegionPart("-gear") {{
				x = 0f;
				y = -5f;
				
				layerOffset = -0.001f;
				
				progress = DrawPart.PartProgress.time.mul(1/300f).mod(1);
				moveRot =  360f;
			}});
			
			weapons.add(new Weapon() {{
				x = y = 0;
				mirror = false;
				
				reload = 15f;
				
				shootSound = SWSounds.chain;
				soundPitchMin = 0.3f;
				soundPitchMax = 0.4f;
				
				bullet = new SegmentBulletType() {{
					layer = Layer.bullet - 0.03f;
					
					sprite = "sw-rho-tentacle";
					spriteLength = 8f;
					spriteExtension = 6f;
					
					lengthInterp = a -> (1f - 4 * Mathf.pow(Math.max(0.5f, a) - 0.5f, 2f)) * Math.min(1, 16 * a * a);
					
					damage = 10f;
					buildingDamageMultiplier = 0f;
					attackTime = 0.25f;
					
					minLength = 100f;
					length = 200f;
					
					rangeOverride = r;
				}};
			}});
		}};
		//endregion
  }
}

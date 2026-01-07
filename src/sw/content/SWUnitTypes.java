package sw.content;

import arc.graphics.*;
import arc.math.*;
import mindustry.ai.types.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.entities.effect.*;
import mindustry.entities.part.*;
import mindustry.entities.pattern.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import sw.entities.bullet.*;
import sw.gen.*;
import sw.type.*;

public class SWUnitTypes {
  public static UnitType
		soar, volare,
		wisp,
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
				
				blurAlpha = 0.4f;
			}});

			weapons.addAll(
				new Weapon("sw-soar-cannon") {{
					x = 7f;
					y = 5.5f;
					layerOffset = -0.01f;

          reload = 60f;

          recoil = 4f;
          recoilTime = 60f;

          shootSound = Sounds.shootArtillery;
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
		
		volare = new SWUnitType("volare") {{
			health = 1200;
			speed = 2f;
			accel = drag = 0.05f;
			rotateSpeed = 3f;
			fallSpeed = 0.005f;
			
			hitSize = 38f;
			engineSize = 0f;
			
			flying = lowAltitude = true;
			
			rotors.add(
				new UnitRotor("-rotor-small", true) {{
					x = 0;
					y = -9.75f;
					rotation = 90f;
					blades = 2;
					
					speed = 8f;
					shineSpeed = -2f;
				}},
				new UnitRotor("-rotor", true) {{
					x = 12f;
					y = 3;
					blades = 3;
					
					mirrored = true;
					
					speed = 12f;
					shineSpeed = -3f;
					
					blurAlpha = 0.5f;
				}}
			);
			
			weapons.add(
				new Weapon("sw-volare-slag-gun") {{
					mirror = true;
					layerOffset = -0.01f;
					
					x = 4.5f;
					y = 13.75f;
					shootY = 6;
					
					reload = 1f;
					
					recoil = 0.5f;
					recoilTime = 60f;
					
					shootCone = 361;
					
					activeSound = Sounds.loopSpray;
					shootSound = Sounds.none;
					bullet = new LiquidBulletType(Liquids.slag) {{
						puddleSize = 12f;
						
						
						shootEffect = Fx.hitLiquid;
						orbSize = 2f;
						despawnHit = true;
					}};
				}},
				new Weapon("sw-volare-coil") {{
					mirror = true;
					
					x = 6.5f;
					y = 4f;
					shootY = 6f;
					
					reload = 90f;
					
					recoil = 2f;
					recoilTime = 45f;
					
					rotate = true;
					rotationLimit = 30f;
					
					shootSound = Sounds.shootEnergyField;
					
					bullet = new BasicBulletType(3f, 40f, "circle-bullet") {{
						lifetime = 70f;
						
						width = height = 6f;
						shrinkY = shrinkX = 0;
						
						frontColor = backColor = Pal.missileYellow;
						
						intervalBullets = 3;
						bulletInterval = 1;
						
						lightningColor = Pal.missileYellow;
						lightning = 10;
						lightningLength = 5;
						lightningLengthRand = 3;
						lightningDamage = 30;
						
						intervalBullet = new LightningBulletType() {{
							damage = 10f;
							
							lightningColor = Pal.missileYellow;
							lightningDamage = 10;
							lightningLength = 3;
							lightningLengthRand = 2;
						}};
					}};
				}}
			);
		}};
		//endregion
	  
	  wisp = new SWUnitType("wisp") {{
			health = 350;
		  speed = 2f;
		  accel = drag = 0.05f;
		  rotateSpeed = 6f;
			outlineLayerOffset = -0.002f;
			omniMovement = false;
			circleTarget = true;
			range = maxRange = 15f * 8f;
			hitSize = 10f;
			
			aiController = FlyingAI::new;
			
			wrecks = 3;
			
			lightRadius = 40f;
			lightOpacity = 0.1f;
			lightColor = Color.scarlet;
			
			loopSound = Sounds.loopFire;
			loopSoundVolume = 1f;
			
			parts.addAll(
				new RegionPart("-support") {{
					outline = false;
					
					layerOffset = 0.001f;
				}},
				new RegionPart("-gear") {{
					mirror = true;
					
					outlineLayerOffset = 0f;
					
					x = 4.75f;
					
					moveRot = -360f;
					
					progress = PartProgress.time.loop(240f);
				}},
				new RegionPart("-gear-big") {{
					y = 2f;
					
					layerOffset = -0.001f;
					outlineLayerOffset = 0f;
					
					moveRot = 360f;
					
					progress = PartProgress.time.loop(480f);
				}}
			);
			
			weapons.add(new Weapon() {{
				mirror = false;
				rotate = true;
				linearWarmup = true;
				
				x = 0f;
				y = 0f;
				
				reload = 180f;
				rotateSpeed = 360f;
				recoil = 0;
				
				minWarmup = 0.999f;
				shootWarmupSpeed = 1f / 90f;
				
				shootY = 0f;
				
				shootSound = Sounds.shootArtillery;
				
				parts.addAll(
					new EffectSpawnerPart() {{
						effect = SWFx.wispFire;
						
						effectChance = 0.5f;
						
						progress = PartProgress.warmup.mul(0.8f).mul(
							PartProgress.reload.inv()
						).add(0.2f);
					}},
					new ShapePart() {{
						circle = true;
						
						layer = Layer.effect;
						
						radius = 2f;
						
						color = Pal.turretHeat.cpy().mul(3f);
						colorTo = Pal.accent;
						
						progress = PartProgress.warmup.mul(PartProgress.reload.inv());
					}}
				);
				
				bullet = new BulletType(3f, 40f) {{
					parts.addAll(
						new ShapePart() {{
							circle = true;
							
							radius = 1f;
							
							x = 3f;
							y = 0.75f;
							moveX = -6.4f;
							moveY = 3f;
							
							color = Pal.accent;
							colorTo = Pal.turretHeat;
							
							progress = PartProgress.time.add(9f).loop(60f).curve(Interp.slope).curve(Interp.sine);
						}},
						new ShapePart() {{
							circle = true;
							
							radius = 1.1f;
							
							x = 2.6f;
							y = -1.2f;
							moveX = -4f;
							moveY = 4f;
							
							color = Pal.accent;
							colorTo = Pal.turretHeat;
							
							progress = PartProgress.time.add(18f).loop(60f).curve(Interp.slope).curve(Interp.sine);
						}},
						new ShapePart() {{
							circle = true;
							
							radius = 1.8f;
							
							x = 1.2f;
							y = -1.8f;
							moveX = 1f;
							moveY = 4f;
							
							color = Pal.accent;
							colorTo = Pal.turretHeat;
							
							progress = PartProgress.time.add(34f).loop(60f).curve(Interp.slope).curve(Interp.sine);
						}},
						new ShapePart() {{
							circle = true;
							
							radius = 1.7f;
							
							x = 1f;
							y = -0.1f;
							moveX = -2.7f;
							moveY = -1.6f;
							
							color = Pal.accent;
							colorTo = Pal.turretHeat;
							
							progress = PartProgress.time.add(22f).loop(60f).curve(Interp.slope).curve(Interp.sine);
						}},
						new ShapePart() {{
							circle = true;
							
							radius = 2f;
							
							x = -1.9f;
							y = -1.2f;
							moveX = 1.9f;
							moveY = -0.8f;
							
							color = Pal.accent;
							colorTo = Pal.turretHeat;
							
							progress = PartProgress.time.add(38f).loop(60f).curve(Interp.slope).curve(Interp.sine);
						}}
					);
					
					var e = new WrapEffect(
						new Effect(120f, SWFx.parallaxFire.renderer)
							.layer(Layer.effect + 1)
							.followParent(false),
						Color.white,
						16f
					);
					
					lifetime = 60f;
					
					homingRange = 180f;
					homingPower = 3f / 200f;
					
					lightOpacity = 0.1f;
					lightColor = Color.scarlet;
					
					shootEffect = new MultiEffect(e, e, e, e, e, e);
					
					trailEffect = new MultiEffect(
						new WrapEffect(SWFx.parallaxFire, Color.white, 4f),
						new Effect(90f, effect -> {
							Drawf.light(effect.x, effect.y, 6f * effect.foutpow(), Color.scarlet, 0.1f);
						})
					);
					trailInterval = 2.5f;
					
					despawnEffect = new MultiEffect(e, new Effect(90f, effect -> {
						Drawf.light(effect.x, effect.y, 24f * effect.foutpow(), Color.scarlet, 0.1f);
					}));
					hitEffect = Fx.none;
					
					fragBullets = 5;
					fragBullet = new BulletType(2f, 20f) {{
						parts.add(new ShapePart() {{
							circle = true;
							radius = 1f;
							color = Pal.accent;
							colorTo = Pal.turretHeat;
							
							progress = DrawPart.PartProgress.time.loop(60f).curve(Interp.slope).curve(Interp.sine);
						}});
						
						lifetime = 30f;
						
						homingRange = 60f;
						homingPower = 3f / 200f;
						
						lightOpacity = 0.1f;
						lightColor = Color.scarlet;
						
						trailEffect = new MultiEffect(
							new WrapEffect(SWFx.parallaxFire, Color.white, 4f),
							new Effect(90f, effect -> {
								Drawf.light(effect.x, effect.y, 6f * effect.foutpow(), Color.scarlet, 0.1f);
							})
						);
						trailInterval = 2.5f;
						
						despawnEffect = new MultiEffect(e, new Effect(90f, effect -> {
							Drawf.light(effect.x, effect.y, 24f * effect.foutpow(), Color.scarlet, 0.1f);
						}));
						hitEffect = Fx.none;
					}};
				}};
			}});
	  }};
	  
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
			stepSound = Sounds.none;

			weapons.add(
				new Weapon("sw-lambda-weapon") {{
					x = 4.75f;
					y = 2.5f;
					reload = 60;
					layerOffset = -0.001f;
					shootSound = Sounds.shootBreach;
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

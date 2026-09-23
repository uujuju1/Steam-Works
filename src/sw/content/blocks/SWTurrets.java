package sw.content.blocks;

import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.entities.effect.*;
import mindustry.entities.part.*;
import mindustry.entities.pattern.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.draw.*;
import sw.content.*;
import sw.entities.part.*;
import sw.gen.*;
import sw.world.blocks.defense.*;
import sw.world.consumers.*;
import sw.world.draw.*;
import sw.world.interfaces.*;
import sw.world.meta.*;

import static mindustry.type.ItemStack.*;

public class SWTurrets {
	public static Block
		imber, trebuchet,
		rainfall, anchor,
		push, thermikos,
		flurry;

	public static void load() {
		imber = new SWItemTurret("imber") {{
			requirements(Category.turret, with(
				SWItems.verdigris, 20,
				SWItems.iron, 30,
				Items.graphite, 10
			));
			researchCost = with(
				SWItems.verdigris, 40,
				SWItems.iron, 50,
				Items.graphite, 20
			);
			size = 2;
			scaledHealth = 150;
			reload = 240f;
			range = 160f;
			maxAmmo = 5;

			outlineIcon = false;

			bulletsChangeTargeting = true;

			drawer = new DrawTurret() {{
				parts.add(
					new RegionPart("-floor") {{
						under = true;
						outline = false;
					}},
					new RegionPart("-ammo") {{
						under = true;
						outline = false;

						progress = PartProgress.heat.curve(Interp.exp10Out);

						color = Color.white;
						colorTo = Color.clear;

						heatProgress = PartProgress.charge.compress(0.5f, 1f).curve(Interp.exp10);
					}},
					new RegionPart("-sparker") {{
						mirror = true;
						under = true;

						x = 3.5f;
						y = 3.75f;

						moves.add(
							new PartMove(PartProgress.charge.compress(0f, 0.5f).curve(Interp.exp10Out), 0f, -2f, 0f),
							new PartMove(PartProgress.charge.compress(0f, 0.5f).curve(Interp.pow10In), -0.75f, 0f, 0f),
							new PartMove(PartProgress.charge.compress(0.5f, 1f).curve(Interp.bounceOut), 0.75f, 3f, 0f),
							new PartMove(PartProgress.heat.curve(Interp.smooth), 0f, -2f, 0f)
						);
					}}
				);
			}
				// ANUKE WHY DID YOU MAKE ME DO THIS
				@Override public void getRegionsToOutline(Block block, Seq<TextureRegion> out) {}
			};

			cooldownTime = 60f;
			shoot = new ShootPattern() {{
				firstShotDelay = 60f;
			}};

			shootSound = Sounds.shootBreachCarbide;
			ammo(
				SWItems.coke, new BasicBulletType(4, 100) {{
					width = 8f;
					height = 16f;
					shrinkY = 0f;

					lifetime = 60;
					drag = 0.02f;
					ammoMultiplier = 2.5f;

					collidesGround = false;

					status = StatusEffects.burning;
					statusDuration = 180f;

					trailEffect = Fx.disperseTrail;
					trailInterval = 1f;
					trailRotation = true;

					hitSound = Sounds.shootFlame;
					hitEffect = Fx.hitBulletBig;
					despawnHit = true;

					intervalBullets = 3;
					intervalRandomSpread = 360f;
					bulletInterval = 10f;
					intervalDelay = 5f;
					intervalBullet = new BasicBulletType(1, 5, "casing") {{
						width = 2f;
						height = 4f;
						shrinkY = 0f;

						frontColor = Pal.accentBack;

						layer = Layer.block + 0.01f;

						lifetime = 120f;
						drag = 0.2f;

						collidesAir = false;
						pierce = true;

						status = StatusEffects.burning;
						statusDuration = 30f;

						hitSound = Sounds.shootFlame;
						hitEffect = Fx.hitBulletBig;
						despawnHit = true;
					}};
				}},
				SWItems.thermite, new BasicBulletType(4, 125) {{
					width = 8f;
					height = 16f;
					shrinkY = 0f;

					lifetime = 60;
					drag = 0.02f;
					ammoMultiplier = 1f;

					status = StatusEffects.melting;
					statusDuration = 180f;

					trailEffect = Fx.disperseTrail;
					trailInterval = 5f;
					trailRotation = true;

					hitSound = Sounds.shootFlame;
					hitEffect = Fx.hitBulletBig;
					despawnHit = true;

					intervalBullets = 3;
					intervalRandomSpread = 90f;
					bulletInterval = 5f;
					intervalBullet = new BasicBulletType(2, 5, "casing") {{
						width = 2f;
						height = 4f;
						shrinkY = 0f;

						frontColor = Pal.accentBack;

						layer = Layer.block + 0.01f;

						lifetime = 8f;

						pierce = true;

						status = StatusEffects.melting;
						statusDuration = 60f;

						hitSound = Sounds.shootFlame;
						hitEffect = Fx.hitBulletBig;
						despawnHit = true;
					}};
				}}
			);
			coolant = consume(new ConsumeLiquidBoosters() {{
				amount = 20f / 60f;
				boosters.put(Liquids.water, 2);
				boosters.put(SWLiquids.steam, 4);
			}});
		}};
		trebuchet = new SWItemTurret("trebuchet") {{
			requirements(Category.turret, with(
				SWItems.iron, 45,
				SWItems.aluminium, 20,
				Items.graphite, 30
			));
			size = 2;
			scaledHealth = 220;
			reload = 10f;
			range = 160f;
			
			outlineIcon = false;
			
			drawer = new DrawTurret() {
				@Override public void getRegionsToOutline(Block block, Seq<TextureRegion> out) {}
			{
				parts.add(new RegionPart("-side") {{
					mirror = true;
					under = true;
					
					moveY = 1.5f;
					moveX = -0.75f;
					
					clampProgress = false;
					progress = PartProgress.reload.curve(Interp.swing);
				}});
			}};
			
			shootSound = Sounds.shootBreach;
			shootY = 2f;
			
			ammo(
				SWItems.iron, new BasicBulletType(6f, 10) {{
					lifetime = 160f/6f;
					
					ammoMultiplier = 5;
					
					trailColor = hitColor = frontColor = Color.valueOf("A1A1B8");
					backColor = Color.valueOf("8D8DA0");
					
					shootEffect = SWFx.hydrogenShoot;
					smokeEffect = Fx.none;
					
					trailWidth = 1f;
					trailLength = 10;
				}}
			);
			consumeLiquid(Liquids.hydrogen, 3f/60f);
		}};

		rainfall = new SWLiquidTurret("rainfall") {{
			requirements(Category.turret, with(
				SWItems.aluminium, 170,
				SWItems.iron, 100,
				SWItems.verdigris, 120,
				Items.silicon, 150,
				Items.graphite, 90
			));
			size = 3;
			scaledHealth = 150f;
			range = 25f * 8f;
			reload = 90f;
			recoilTime = 30f;
			rotateSpeed = 5f;

			outlineIcon = false;

			loopSound = Sounds.loopMachineSpin;
			requireShootingSound = false;

			Cons<LiquidBulletType> defaults = b -> {
				b.speed = 4;
				b.lifetime = 100f;
				b.drag = 0.016f;
				b.despawnHit = true;
				b.hitSound = Sounds.stepWater;
				b.hitSoundVolume = 0.2f;

				b.scaleLife = true;
				b.velocityScaleRandMin = b.lifeScaleRandMin = 0.9f;
				b.velocityScaleRandMax = b.lifeScaleRandMax = 1.1f;
			};

			ammo(
				SWLiquids.solvent, new LiquidBulletType(SWLiquids.solvent) {{
					defaults.get(this);
					puddleSize = 3f;

					damage = 1;

					shootSound = Sounds.stepWater;

					trailEffect = Fx.vaporSmall;
					trailChance = 0.25f;
					trailColor = SWLiquids.solvent.color;

					layer = Layer.bullet - 2f;
				}
					@Override
					public void hit(Bullet b, float hitx, float hity, boolean createFrags) {
						super.hit(b, hitx, hity, createFrags);

						hitSound.at(b, hitSoundPitch + Mathf.range(hitSoundPitchRange), hitSoundVolume);
					}
				},
				Liquids.water, new LiquidBulletType(Liquids.water) {{
					defaults.get(this);

					shootSound = Sounds.stepWater;
				}
					@Override
					public void hit(Bullet b, float hitx, float hity, boolean createFrags) {
						super.hit(b, hitx, hity, createFrags);

						hitSound.at(b, hitSoundPitch + Mathf.range(hitSoundPitchRange), hitSoundVolume);
					}
				},
				Liquids.slag, new LiquidBulletType(Liquids.slag) {{
					defaults.get(this);

					shootSound = Sounds.stepWater;
				}
					@Override
					public void hit(Bullet b, float hitx, float hity, boolean createFrags) {
						super.hit(b, hitx, hity, createFrags);

						hitSound.at(b, hitSoundPitch + Mathf.range(hitSoundPitchRange), hitSoundVolume);
					}
				},
				Liquids.oil, new LiquidBulletType(Liquids.oil) {{
					defaults.get(this);

					shootSound = Sounds.stepWater;

					layer = Layer.bullet - 2f;
				}
					@Override
					public void hit(Bullet b, float hitx, float hity, boolean createFrags) {
						super.hit(b, hitx, hity, createFrags);

						hitSound.at(b, hitSoundPitch + Mathf.range(hitSoundPitchRange), hitSoundVolume);
					}
				},
				SWLiquids.gas, new LiquidBulletType(SWLiquids.gas) {{
					defaults.get(this);

					shootSound = Sounds.uiNotify;

					trailEffect = Fx.vaporSmall;
					trailChance = 0.05f;
					trailColor = SWLiquids.gas.color;

					boilTime = Float.POSITIVE_INFINITY;
					hitEffect = Fx.vapor;
					hitSound = Sounds.none;

					incendAmount = 20;
					incendChance = 0.5f;
					incendSpread = 10f;
				}
					@Override
					public void hit(Bullet b, float hitx, float hity, boolean createFrags) {
						super.hit(b, hitx, hity, createFrags);

						createIncend(b, hitx, hity);

						hitSound.at(b, hitSoundPitch + Mathf.range(hitSoundPitchRange), hitSoundVolume);
					}
				},
				SWLiquids.steam, new LiquidBulletType(SWLiquids.steam) {{
					defaults.get(this);

					damage = 6;

					shootSound = Sounds.uiNotify;

					boilTime = lifetime;
					hitEffect = Fx.vapor;
					hitSound = Sounds.none;
				}
					@Override
					public void hit(Bullet b, float hitx, float hity, boolean createFrags) {
						super.hit(b, hitx, hity, createFrags);

						hitSound.at(b, hitSoundPitch + Mathf.range(hitSoundPitchRange), hitSoundVolume);
					}
				}
			);
			consume(new ConsumeSpin() {{
				minSpeed = 1f;
				maxSpeed = 100f / 10f;

				showGraph = true;
				minEfficiency = 1f;
				maxEfficiency = 4f;
				efficiencyScale = a -> a < 1 ? 0 : (a > 8 ? 4 : Mathf.log2(a) + 1);
			}});
			shoot = new ShootPattern() {{
				shots = 10;
				shotDelay = 2f;
			}};
			inaccuracy = 2.5f;
			shootSound = Sounds.stepWater;
			shootSoundVolume = 0.2f;

			drawer = new DrawTurret("torque-base-") {{
				parts.add(
					new RegionPart("-pipe") {{
						mirror = true;

						x = 5.5f;
						y = -2f;
						moveX = 0.25f;
						moveY = -0.5f;

						progress = DrawPart.PartProgress.recoil.curve(Interp.circle);
					}},
					new RegionPart("-pipe-under") {{
						mirror = true;
						under = true;

						x = 4f;
						y = 6.75f;
						moveX = 0.5f;
						moveY = -1f;

						progress = DrawPart.PartProgress.recoil.curve(Interp.circle);
					}}
				);
			}
				@Override public void getRegionsToOutline(Block block, Seq<TextureRegion> out) {}
			};

			spinConfig = new SpinConfig() {{
				resistance = 30f / 600f;

				allowedEdges = new int[][] {
					new int[] {0, 3, 6, 9},
					new int[] {3, 6, 9, 0},
					new int[] {6, 9, 0, 3},
					new int[] {9, 0, 3, 6}
				};
			}};
		}};
		anchor = new ChainTurret("anchor") {{
			requirements(Category.turret, with(
				SWItems.verdigris, 200,
				SWItems.bloom, 150,
				Items.silicon, 180,
				Items.graphite, 175
			));
			size = 3;
			scaledHealth = 150f;
			range = 30f * 8f;
			rotateSpeed = 2.5f;

			outlineIcon = false;

			reload = 120f;

			holdTime = 60f;
			pull = 2f;
			pullStrengthScale = 0.005f;
			pullDamageScale = 20f / 60f / 8f;

			targetAsEffectData = true;
			shootEffect = SWFx.shockwave;
			endChainEffect = SWFx.chainBreak;

			chainInterp = a -> Interp.smooth.apply(Math.min(1.1f / (1 / 12f) * a, -0.1f / (11f / 12f) * a + 1.1f + 0.1f / (11f / 12f) * (1f / 12f)) / 1.1f) * 1.1f;

			shootSound = SWSounds.shootPressureChain;
			hitSound = SWSounds.hitPressureChain;
			shootSoundVolume = 1f;
			soundPitchMin = 0.9f;
			soundPitchMax = 1.1f;

			consume(new ConsumeSpin() {{
				minSpeed = 80f / 10f;
				maxSpeed = 100f / 10f;

				minEfficiency = 1f;
				maxEfficiency = 1.5f;
				showGraph = true;

				efficiencyScale = s -> Mathf.map(s, 80f / 10f, 100f / 10f, 1f, 1.5f);
			}});

			drawer = new DrawTurret("torque-base-") {{
				parts.add(new RegionPart("-gear") {{
					clampProgress = false;
					layerOffset = -0.001f;

					moveRot = -2f;

					progress = p -> p.rotation;
				}});
			}
				@Override public void getRegionsToOutline(Block block, Seq<TextureRegion> out) {}
			};

			spinConfig = new SpinConfig() {{
				resistance = 5f / 600f;

				allowedEdges = new int[][]{
					new int[]{0, 3, 6, 9},
					new int[]{3, 6, 9, 0},
					new int[]{6, 9, 0, 3},
					new int[]{9, 0, 3, 6}
				};
			}};
		}};

		push = new ConsumeTurret("push") {{
			requirements(Category.turret, with(
				SWItems.aluminium, 100,
				SWItems.iron, 80,
				Items.silicon, 120,
				Items.graphite, 150
			));
			size = 3;
			scaledHealth = 180f;
			range = 22.5f * 8f;
			reload = 60f;
			rotateSpeed = 5f;

			linearWarmup = true;
			minWarmup = 0.95f;

			outlineIcon = false;

			consumeItem(Items.silicon, 1);
			consumeLiquid(Liquids.ozone, 1f / 60f);

			drawer = new DrawTurret() {{
				parts.addAll(
					new RegionPart("-front") {{
						mirror = true;

						x = 6.25f;
						y = 6.75f;
						moveX = -1.75f;

						progress = PartProgress.warmup.curve(Interp.smooth);

						moves.add(new PartMove(PartProgress.reload.curve(Interp.pow5).mul(PartProgress.warmup).delay(0.75f), 0.5f, -2f, 0, 0, 0));
					}},
					new RegionPart("-handle") {{
						mirror = true;

						progress = DrawPart.PartProgress.warmup.curve(Interp.smooth);

						x = 6.25f;
						moveX = -1f;
						moveY = 1f;
						moveRot = 10f;

						moves.add(new PartMove(PartProgress.reload.curve(Interp.pow5).mul(PartProgress.warmup), 0.25f, -1f, 0, 0, -5));
					}}
				);
			}
				@Override public void getRegionsToOutline(Block block, Seq<TextureRegion> out) {}
			};

			shootSound = Sounds.blockExplodeExplosiveAlt;

			shoot = new ShootPattern() {{
				shots = 20;
			}};
			shootY = 6f;
			shootCone = 40f;
			inaccuracy = 40f;
			shootType = new BasicBulletType(8f, 10, "mine-bullet") {{
				width = height = 10f;
				shrinkX = shrinkY = 0f;
				lifetime = 22.5f * 8f / speed;

				lifeScaleRandMin = 0.9f;
				lifeScaleRandMax = 1.1f;
				velocityScaleRandMin = 0.8f;
				velocityScaleRandMax = 1.2f;
				knockback = 6f;
				trailWidth = 2.5f;
				trailLength = 10;
				trailEffect = Fx.disperseTrail;
				trailInterval = 1;
				hitEffect = despawnEffect = new MultiEffect(
					new WrapEffect(SWFx.hitCrossColor, hitColor, 4f),
					SWFx.hitBulletBigColor
				);
				trailRotation = true;
				impact = true;
				frontColor = trailColor = Items.silicon.color.cpy().mul(1.5f);
				backColor = hitColor = Items.silicon.color.cpy().mul(1.25f);
			}};
		}};
		thermikos = new ConsumeTurret("thermikos") {{
			requirements(Category.turret, with(
				SWItems.bloom, 150,
				SWItems.iron, 200,
				SWItems.verdigris, 175,
				Items.silicon, 100,
				Items.graphite, 125
			));
			researchCost = mult(requirements, 10);
			size = 3;
			scaledHealth = 180f;
			range = 50 * 8f;
			reload = 120f;
			recoil = 2f;
			rotateSpeed = 1f;

			outlineIcon = false;

			fullOverride = "sw-thermikos-full";

			shake = 3f;
			shootY = 16f;
			shoot = new ShootPattern() {{
				firstShotDelay = 30f;
			}};
			moveWhileCharging = false;

			shootSound = Sounds.unitExplode3;

			consumeItems(with(SWItems.bloom, 3, SWItems.thermite, 3));
			consumeLiquid(SWLiquids.solvent, 10f / 60f);
			consume(new ConsumeSpin() {{
				minSpeed = 20f / 10f;
				maxSpeed = 30f / 10f;

				efficiencyScale = Interp.one;
			}});

			drawer = new DrawTurret("torque-base-") {{
				parts.add(
					new RegionPart("-wheel-outline") {{
						under = true;
						outline = false;

						y = 6.25f;
					}},
					new SegmentedAxlePart() {{
						suffix = "-wheel";

						layerOffset = - 0.0001f;

						y = 6.25f;

						height = 20f;
						minWidth = 5f;
						maxWidth = 8f;
						rotation = -90f;

						lightTint = Color.valueOf("7B7B7B");
						mediumTint = Color.valueOf("636369");
						darkTint = Color.valueOf("4D4E58");

						segmentSides = new int[]{0, 2, 4, 6, 8};

						progress = DrawParts.spin.add(PartProgress.charge.curve(Interp.exp10In).mul(360));
					}},
					new RegionPart("-cannon") {{
						under = true;
						y = -7.25f;
						moveY = -2f;
						progress = PartProgress.heat.curve(Interp.bounceIn);
					}}
				);
			}
				@Override
				public void draw(Building build) {
					Turret turret = (Turret)build.block;
					TurretBuild tb = (TurretBuild)build;

					Draw.rect(base, build.x, build.y);
					Draw.color();

					Draw.z(shadowLayer);

					Drawf.shadow(preview, build.x + tb.recoilOffset.x - turret.elevation, build.y + tb.recoilOffset.y - turret.elevation, tb.drawrot());

					Draw.z(turretLayer);

					drawTurret(turret, tb);
					drawHeat(turret, tb);

					if(parts.size > 0){
						if(outline.found()){
							//draw outline under everything when parts are involved
							Draw.z(turretLayer - 0.01f);
							Draw.rect(outline, build.x + tb.recoilOffset.x, build.y + tb.recoilOffset.y, tb.drawrot());
							Draw.z(turretLayer);
						}

						float progress = tb.progress();

						//TODO no smooth reload
						DrawParts.BlockParams params = (DrawParts.BlockParams) DrawParts.params.set(build.warmup(), 1f - progress, 1f - progress, tb.heat, tb.curRecoil, tb.charge, tb.x + tb.recoilOffset.x, tb.y + tb.recoilOffset.y, tb.rotation);

						params.warmup = build.warmup();
						params.progress = build.progress();
						params.totalProgress = build.totalProgress();
						params.efficiency = build.efficiency;

						params.spin = build instanceof HasSpin spin && spin.spin() != null ? spin.getRotation() : 0f;
						params.ratio = build instanceof HasSpin spin && spin.spin() != null ? spin.getRatio() : 1f;
						params.speed = build instanceof HasSpin spin && spin.spin() != null ? spin.getSpeed() : 1f;

						for(var part : parts){
							params.setRecoil(part.recoilIndex >= 0 && tb.curRecoils != null ? tb.curRecoils[part.recoilIndex] : tb.curRecoil);
							part.draw(params);
						}
					}
				}

				@Override public void getRegionsToOutline(Block block, Seq<TextureRegion> out) {}
			};

			shootType = new ArtilleryBulletType(4f, 100f) {{
				splashDamage = 300f;
				splashDamageRadius = 64f;
				splashDamagePierce = true;

				lifetime = 100f;
				width = height = 20f;

				collides = collidesAir = collidesGround = true;

				shootEffect = SWFx.thermiteShoot;
				trailEffect = SWFx.thermiteTrail;
				hitEffect = new WrapEffect(Fx.dynamicExplosion, Color.white, 2f);
				hitShake = 3;

				trailRotation = true;
//				chargeEffect = SWFx.thermiteCharge;
			}};

			spinConfig = new SpinConfig() {{
				resistance = 20f / 600f;

				allowedEdges = new int[][] {
					new int[] {0, 3, 6, 9},
					new int[] {3, 6, 9, 0},
					new int[] {6, 9, 0, 3},
					new int[] {9, 0, 3, 6}
				};
			}};
			coolant = consume(new ConsumeLiquidBoosters() {{
				amount = 20f / 60f;
				boosters.put(Liquids.water, 2);
				boosters.put(SWLiquids.steam, 4);
			}});
		}};

		flurry = new ConsumeTurret("flurry") {{
			requirements(Category.turret, with(
				SWItems.verdigris, 300,
				SWItems.iron, 300,
				SWItems.bloom, 200,
				SWItems.aluminium, 350,
				Items.silicon, 250,
				Items.graphite, 400,
				Items.thorium, 100
			));
			size = 4;

			reload = 600f;
			recoil = 4f;
			recoilTime = 480;
			range = 75 * 8f;
			rotateSpeed = 1f;

			scaledHealth = 100f;

			outlineIcon = false;

			itemCapacity = 20;

			consumeItem(Items.lead, 10);
			consumeLiquid(SWLiquids.gas, 50f / 60f);
			consume(new ConsumeSpin() {{
				minSpeed = 75f / 10f;
				maxSpeed = 85f / 10f;

				efficiencyScale = Interp.one;
			}});

			chargeSound = SWSounds.chargeHum;
			shootSound = Sounds.acceleratorLaunch;
			soundPitchMin = 0.5f;
			soundPitchMax = 0.7f;
			shootEffect = SWFx.flurryShoot;
			shootY = 0f;
			shootType = new RailBulletType() {{
				length = 75f * 8f;

				damage = 300f;

				pierceCap = 4;
				pierceArmor = true;
				pierceDamageFactor = 0.05f;

				hitColor = trailColor = Color.valueOf("A294C6");
				trailWidth = 3f;
				trailLength = 20;

				Effect hit = new Effect(60f, e -> {
					Rand rand = SWFx.rand;
					Vec2 temp = SWFx.temp.set(e.x, e.y);
					if (e.data instanceof Vec2 data) temp.set(data);

					rand.setSeed(e.id);
					Draw.color(e.color, Color.white, rand.random(0.5f));
					Angles.randLenVectors(e.id, 20, 80 * e.finpow(), e.rotation, 20f, (x, y) -> {
						Lines.stroke(Mathf.dst(x, y) / 80f * rand.random(3f, 5f) * e.foutpowdown());
						Lines.lineAngle(temp.x + x, temp.y + y, Mathf.angle(x, y), rand.random(5f, 10f) * e.fout());
					});
				});

				lineEffect = new MultiEffect(
					new Effect(10f, 75 * 8f, e -> {
						if (!(e.data instanceof Vec2 data)) return;
						Tmp.v1.set(e.x, e.y).lerp(data, e.finpowdown());
						Lines.stroke(8f * e.foutpowdown());
						Draw.color(Color.white, e.color, e.finpow());
						Lines.line(Tmp.v1.x, Tmp.v1.y, data.x, data.y);
					}),
					hit
				);

				endEffect = hitEffect = hit;
			}};

			shoot.firstShotDelay = 60f;

			drawer = new DrawTurret("torque-base-") {{
				parts.add(
					new RegionPart("-rod") {{
						under = true;
						outline = false;

						yScl = 1f;
						growY = -1f;

						growProgress = PartProgress.recoil.curve(Interp.exp10);
					}}
				);
			}

				@Override public void getRegionsToOutline(Block block, Seq<TextureRegion> out) {}
			};

			spinConfig = new SpinConfig() {{
				resistance = 200f / 600f;

				allowedEdges = new int[][]{
					new int[]{0, 1, 4, 5, 8, 9, 12, 13},
					new int[]{12, 13, 0, 1, 4, 5, 8, 9},
					new int[]{8, 9, 12, 13, 0, 1, 4, 5},
					new int[]{4, 5, 8, 9, 12, 13, 0, 1}
				};
			}};
		}};
	}
}

package sw.content.blocks;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import mindustry.content.*;
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
import mindustry.world.meta.*;
import sw.content.*;
import sw.entities.part.*;
import sw.world.blocks.defense.*;
import sw.world.consumers.*;
import sw.world.draw.*;
import sw.world.interfaces.*;
import sw.world.meta.*;

import static mindustry.type.ItemStack.*;

public class SWTurrets {
	public static Block
//		flow, vniz, rozpad,
		imber, trebuchet,
		curve, sonar,
		push, thermikos, swing;

	public static void load() {
//		flow = new ItemTurret("flow") {{
//			requirements(Category.turret, with(
//				SWItems.verdigris, 20,
//				Items.graphite, 25
//			));
//			researchCost = with(
//				SWItems.verdigris, 100,
//				Items.graphite, 100
//			);
//			size = 1;
//			scaledHealth = 200;
//			reload = 60f;
//			range = 140f;
//			targetAir = false;
//
//			shootSound = Sounds.shootAlt;
//
//			shoot = new ShootPattern() {{
//				shotDelay = 5f;
//				shots = 2;
//			}};
//
//			ammo(
//				SWItems.verdigris, new BasicBulletType(2, 10) {{
//					frontColor = trailColor = Color.valueOf("A1A7AB");
//					backColor = Color.valueOf("595E61");
//					hitEffect = despawnEffect = Fx.hitBulletColor;
//					width = 8f;
//					height = 10f;
//					lifetime = 70f;
//					trailWidth = 1.5f;
//					trailLength = 5;
//					homingRange = 35f;
//					homingPower = 0.02f;
//					collidesAir = false;
//				}},
//				SWItems.iron, new BasicBulletType(2, 15) {{
//					frontColor = trailColor = Color.valueOf("A1A1B8");
//					backColor = Color.valueOf("6F6F85");
//					hitEffect = despawnEffect = Fx.hitBulletColor;
//					width = 8f;
//					height = 10f;
//					lifetime = 70f;
//					trailWidth = 1.5f;
//					trailLength = 5;
//					homingRange = 35f;
//					homingPower = 0.03f;
//					collidesAir = false;
//				}}
//			);
//		}};
//		vniz = new ItemTurret("vniz") {{
//			requirements(Category.turret, with(
//				SWItems.verdigris, 25,
//				Items.graphite, 20
//			));
//			researchCost = with(
//				SWItems.verdigris, 100,
//				Items.graphite, 100
//			);
//			size = 1;
//			scaledHealth = 200;
//			reload = 90f;
//			range = 180f;
//			targetGround = false;
//
//			BulletType frag = new BasicBulletType(2f, 5) {{
//				frontColor = trailColor = Color.valueOf("A1A7AB");
//				backColor = Color.valueOf("595E61");
//				lifetime = 30f;
//				trailWidth = 1f;
//				trailLength = 5;
//				homingRange = 60f;
//				homingPower = 0.5f;
//				collidesGround = false;
//			}};
//			ammo(
//				SWItems.verdigris, new ArtilleryBulletType(4, 10) {{
//					frontColor = trailColor = Color.valueOf("A1A7AB");
//					backColor = Color.valueOf("595E61");
//					hitEffect = despawnEffect = Fx.hitBulletColor;
//					width = 8f;
//					height = 10f;
//					lifetime = 45f;
//					trailWidth = 1.5f;
//					trailLength = 5;
//					collides = collidesAir = true;
//
//					fragBullets = 3;
//					fragBullet = frag;
//				}},
//				SWItems.iron, new ArtilleryBulletType(4, 15) {{
//					frontColor = trailColor = Color.valueOf("A1A1B8");
//					backColor = Color.valueOf("6F6F85");
//					hitEffect = despawnEffect = Fx.hitBulletColor;
//					width = 8f;
//					height = 10f;
//					lifetime = 45f;
//					trailWidth = 1.5f;
//					trailLength = 5;
//					collides = collidesAir = true;
//
//					fragBullets = 5;
//					fragBullet = frag;
//				}}
//			);
//		}};
//		rozpad = new ItemTurret("rozpad") {{
//			requirements(Category.turret, with(
//				SWItems.verdigris, 45,
//				SWItems.iron, 40,
//				Items.graphite, 20
//			));
//			size = 2;
//			scaledHealth = 200;
//			reload = 60f;
//			range = 200f;
//			targetGround = false;
//
//			shoot = new ShootAlternate();
//
//			BulletType frag = new BasicBulletType(2f, 10) {{
//				frontColor = trailColor = Color.valueOf("A1A7AB");
//				backColor = Color.valueOf("595E61");
//				lifetime = 30f;
//				trailWidth = 1f;
//				trailLength = 5;
//				homingRange = 60f;
//				homingPower = 0.5f;
//				collidesGround = false;
//			}};
//			ammo(
//				SWItems.verdigris, new ArtilleryBulletType(4, 20) {{
//					frontColor = trailColor = Color.valueOf("A1A7AB");
//					backColor = Color.valueOf("595E61");
//					hitEffect = despawnEffect = Fx.hitBulletColor;
//					width = 8f;
//					height = 10f;
//					lifetime = 50f;
//					trailWidth = 1.5f;
//					trailLength = 5;
//					homingRange = 60f;
//					homingPower = 0.2f;
//					collides = collidesAir = true;
//
//					fragBullets = 3;
//					fragBullet = frag;
//				}},
//				SWItems.iron, new ArtilleryBulletType(4, 25) {{
//					frontColor = trailColor = Color.valueOf("A1A1B8");
//					backColor = Color.valueOf("6F6F85");
//					hitEffect = despawnEffect = Fx.hitBulletColor;
//					width = 8f;
//					height = 10f;
//					lifetime = 50f;
//					trailWidth = 1.5f;
//					trailLength = 5;
//					homingRange = 60f;
//					homingPower = 0.2f;
//					collides = collidesAir = true;
//
//					fragBullets = 5;
//					fragBullet = frag;
//				}}
//			);
//		}};

		imber = new ItemTurret("imber") {{
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

			outlineIcon = false;

			targetGround = false;

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
				}}
			);
			coolant = consume(new ConsumeLiquidBoosters() {{
				amount = 20f / 60f;
				boosters.put(Liquids.water, 2);
				boosters.put(SWLiquids.steam, 4);
			}});
		}

			@Override
			public void setStats() {
				super.setStats();

				if(coolant != null){
					stats.remove(Stat.booster);
					coolant.display(stats);
				}
			}
		};
		trebuchet = new ItemTurret("trebuchet") {{
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

//		curve = new ConsumeTurret("curve") {{
//			requirements(Category.turret, BuildVisibility.hidden, with());
//			size = 3;
//			scaledHealth = 220f;
//			reload = 15f;
//			shootY = 12f;
//			range = 240f;
//
//			consumeItem(Items.silicon, 1);
//
//			drawer = new DrawTurret() {{
//				parts.add(
//					new RegionPart("-cannon") {{
//						mirror = false;
//						under = true;
//						moveX = 2f;
//						moveY = -1f;
//						layerOffset = -0.001f;
//						outlineLayerOffset = -0.03f;
//						progress = PartProgress.reload.curve(Interp.circle).inv();
//					}},
//					new RegionPart("-cannon") {{
//						mirror = false;
//						under = true;
//						x = 2f;
//						y = -1f;
//						moveX = -2f;
//						moveY = -1f;
//						layerOffset = -0.003f;
//						outlineLayerOffset = -0.03f;
//						progress = PartProgress.reload.curve(Interp.circle).inv();
//					}},
//					new RegionPart("-cannon") {{
//						mirror = false;
//						under = true;
//						y = -2f;
//						moveX = -2f;
//						moveY = 1f;
//						layerOffset = -0.002f;
//						outlineLayerOffset = -0.03f;
//						progress = PartProgress.reload.curve(Interp.circle).inv();
//					}},
//					new RegionPart("-cannon") {{
//						mirror = false;
//						under = true;
//						x = -2f;
//						y = -1f;
//						moveX = 2f;
//						moveY = 1f;
//						outlineLayerOffset = -0.03f;
//						progress = PartProgress.reload.curve(Interp.circle).inv();
//					}}
//				);
//			}};
//
//			shootType = new BasicBulletType(3f, 20) {{
//				shrinkY = 0f;
//				width = 8f;
//				height = 8f;
//				trailWidth = 4f;
//				trailLength = 5;
//				hitSound = Sounds.explosion;
//				lifetime = 80f;
//			}};
//
//			spinConfig.hasSpin = false;
//		}};
//		sonar = new ConsumeTurret("sonar") {{
//			requirements(Category.turret, BuildVisibility.hidden, with());
//			size = 2;
//			scaledHealth = 220;
//			reload = 90f;
//			range = 160f;
//
//			drawer = new DrawTurret() {{
//				parts.add(
//					new RegionPart("-back") {{
//						moveY = 2f;
//						under = true;
//					}},
//					new RegionPart("-side") {{
//						moveX = -1f;
//						moveY = 1f;
//						mirror = true;
//						under = true;
//					}}
//				);
//			}};
//
//			shootY = 0f;
//			shootType = new SoundLaserBulletType() {{
//				damage = 30f;
//				width = 16f;
//				length = 160f;
//				colors = new Color[]{Color.white};
//			}};
//
//			spinConfig.hasSpin = false;
//		}};

		push = new ConsumeTurret("push") {{
			requirements(Category.turret, with());
			size = 3;
			scaledHealth = 180f;
			range = 22.5f * 8f;
			reload = 60f;
			rotateSpeed = 5f;

			linearWarmup = true;
			minWarmup = 0.95f;

			outlineIcon = false;

			consumeItem(Items.silicon, 5);
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

			drawer = new DrawTurret() {{
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
		}

			@Override
			public void setStats() {
				super.setStats();

				if(coolant != null){
					stats.remove(Stat.booster);
					coolant.display(stats);
				}
			}
		};
//		swing = new ConsumeTurret("swing") {{
//			requirements(Category.turret, BuildVisibility.hidden, with());
//			size = 3;
//			scaledHealth = 220f;
//			range = 200f;
//			reload = 180f;
//			recoil = 4f;
//			rotateSpeed = 1f;
//			cooldownTime = 90f;
//
//			shootY = 6f;
//
//			shootSound = Sounds.shootAlt;
//
//			consumeItem(SWItems.compound, 3);
//
//			drawer = new DrawTurret() {{
//				parts.add(
//					new RegionPart("-floor") {{
//						under = true;
//						layerOffset = -0.001f;
//					}},
//					new RegionPart() {{
//						name = "sw-saw";
//
//						progress = PartProgress.heat;
//						colorTo = Color.white.cpy().a(0f);
//						color = Color.white;
//						outline = false;
//						under = true;
//
//						moves.add(new PartMove(PartProgress.charge.curve(Interp.circleOut), 0f, 0f, 720f));
//					}}
//				);
//			}};
//
//			shootType = new BasicBulletType(4f, 25f, "sw-saw") {{
//				width = height = 16f;
//				shrinkY = 0f;
//				lifetime = 50f;
//			}};
//		}};
	}
}

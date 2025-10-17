package sw.content.blocks;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import mindustry.content.*;
import mindustry.entities.bullet.*;
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
import sw.entities.bullet.*;
import sw.world.blocks.defense.*;

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
				SWItems.verdigris, 80,
				SWItems.iron, 60,
				Items.graphite, 20
			);
			size = 2;
			scaledHealth = 220;
			reload = 240f;
			range = 140f;

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

						progress = DrawPart.PartProgress.heat.curve(Interp.exp10Out);

						color = Color.white;
						colorTo = Color.clear;

						heatProgress = DrawPart.PartProgress.charge.compress(0.5f, 1f).curve(Interp.exp10);
					}},
					new RegionPart("-sparker") {{
						mirror = true;
						under = true;

						x = 3.5f;
						y = 3.75f;

						moves.add(
							new PartMove(DrawPart.PartProgress.charge.compress(0f, 0.5f).curve(Interp.exp10Out), 0f, -2f, 0f),
							new PartMove(DrawPart.PartProgress.charge.compress(0f, 0.5f).curve(Interp.pow10In), -0.75f, 0f, 0f),
							new PartMove(DrawPart.PartProgress.charge.compress(0.5f, 1f).curve(Interp.bounceOut), 0.75f, 3f, 0f),
							new PartMove(DrawPart.PartProgress.heat.curve(Interp.smooth), 0f, -2f, 0f)
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

			shootSound = Sounds.bigshot;
			ammo(
				SWItems.coke, new BasicBulletType(4, 50) {{
					width = 8f;
					height = 16f;
					shrinkY = 0f;

					lifetime = 60;
					drag = 0.02f;

					collidesGround = false;

					status = StatusEffects.burning;
					statusDuration = 60f;

					trailEffect = Fx.disperseTrail;
					trailInterval = 1f;
					trailRotation = true;

					hitSound = Sounds.flame2;
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

						hitSound = Sounds.flame2;
						hitEffect = Fx.hitBulletBig;
						despawnHit = true;
					}};
				}}
			);
		}};
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
					progress = DrawPart.PartProgress.reload.curve(Interp.swing);
				}});
			}};
			
			shootSound = Sounds.shootAlt;
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
//			shootSound = Sounds.shootBig;
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
//
//		push = new ConsumeTurret("push") {{
//			requirements(Category.turret, BuildVisibility.hidden, with());
//			size = 3;
//			scaledHealth = 220f;
//			range = 240f;
//			reload = 120f;
//			rotateSpeed = 1f;
//
//			drawer = new DrawTurret() {{
//				parts.addAll(
//					new RegionPart("-side") {{
//						mirror = true;
//						moveX = 2f;
//						progress = DrawPart.PartProgress.reload.inv().delay(0.25f).inv().curve(Interp.circle);
//					}},
//					new RegionPart("-support") {{
//						mirror = under = true;
//						moveY = 4f;
//						progress = DrawPart.PartProgress.reload.inv().mul(3).clamp().curve(Interp.circleIn);
//					}}
//				);
//			}};
//
//			shootSound = Sounds.shootSmite;
//			shootType = new BasicBulletType(3f, 30, "sw-sound-wave") {{
//				width = 16;
//				height = 10;
//				lifetime = 80f;
//				trailInterval = 10;
//				knockback = 8f;
//				pierceCap = 5;
//				smokeEffect = shootEffect = Fx.none;
//				hitEffect = despawnEffect = trailEffect = SWFx.soundDecay;
//				trailRotation = true;
//				pierce = pierceBuilding = true;
//			}};
//
//			spinConfig.hasSpin = false;
//		}};
//		thermikos = new ConsumeTurret("thermikos") {{
//			requirements(Category.turret, BuildVisibility.hidden, with());
//			size = 3;
//			scaledHealth = 220f;
//			range = 240f;
//			reload = 120f;
//			recoil = 0f;
//			rotateSpeed = 1f;
//			moveWhileCharging = false;
//
//			shootY = 9f;
//			shoot = new ShootPattern() {{
//				firstShotDelay = 30f;
//			}};
//
//			shootSound = Sounds.cannon;
//			chargeSound = Sounds.lasercharge2;
//
//			consumeItems(with(Items.graphite, 2, SWItems.thermite, 3));
//
//			drawer = new DrawTurret() {{
//				parts.add(
//					new RegionPart("-cannon") {{
//						under = true;
//						moveY = -4f;
//						progress = PartProgress.heat.curve(Interp.bounceIn);
//					}}
//				);
//			}};
//
//			shootType = new ArtilleryBulletType(4f, 200f) {{
//				splashDamage = 200f;
//				splashDamageRadius = 16f;
//				lifetime = 40f;
//				width = height = 20f;
//
//				collides = collidesAir = collidesGround = true;
//
//				shootEffect = SWFx.thermiteShoot;
//				chargeEffect = SWFx.thermiteCharge;
//			}};
//
//			spinConfig.hasSpin = false;
//		}};
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

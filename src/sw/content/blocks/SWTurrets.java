package sw.content.blocks;

import arc.graphics.*;
import arc.math.*;
import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.entities.part.*;
import mindustry.entities.pattern.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.draw.*;
import sw.audio.*;
import sw.content.*;
import sw.entities.bullet.*;
import sw.world.blocks.defense.*;
import sw.world.consumers.*;

import static mindustry.type.ItemStack.*;

public class SWTurrets {
	public static Block
		flow, trail, vniz, rozpad,
		curve, thermikos, sonar,
		push;

	public static void load() {
		flow = new ItemTurret("flow") {{
			requirements(Category.turret, with(
				SWItems.nickel, 20,
				Items.graphite, 25
			));
			researchCost = with(
				SWItems.nickel, 100,
				Items.graphite, 100
			);
			size = 1;
			scaledHealth = 200;
			reload = 60f;
			range = 140f;
			targetAir = false;

			shootSound = Sounds.shootAlt;

			shoot = new ShootPattern() {{
				shotDelay = 5f;
				shots = 2;
			}};

			ammo(
				SWItems.nickel, new BasicBulletType(2, 10) {{
					frontColor = trailColor = Color.valueOf("A1A7AB");
					backColor = Color.valueOf("595E61");
					hitEffect = despawnEffect = Fx.hitBulletColor;
					width = 8f;
					height = 10f;
					lifetime = 70f;
					trailWidth = 1.5f;
					trailLength = 5;
					homingRange = 35f;
					homingPower = 0.02f;
					collidesAir = false;
				}},
				SWItems.iron, new BasicBulletType(2, 15) {{
					frontColor = trailColor = Color.valueOf("A1A1B8");
					backColor = Color.valueOf("6F6F85");
					hitEffect = despawnEffect = Fx.hitBulletColor;
					width = 8f;
					height = 10f;
					lifetime = 70f;
					trailWidth = 1.5f;
					trailLength = 5;
					homingRange = 35f;
					homingPower = 0.03f;
					collidesAir = false;
				}}
			);
		}};
		trail = new ItemTurret("trail") {{
			requirements(Category.turret, with(
				SWItems.nickel, 40,
				SWItems.iron, 45,
				Items.graphite, 20
			));
			size = 2;
			scaledHealth = 200;
			reload = 30f;
			range = 180f;
			targetAir = false;

			shoot = new ShootSummon(0f, 0f, 0f, 5f) {{
				shots = 3;
				shotDelay = 1f;
			}};

			ammo(
				SWItems.nickel, new BasicBulletType(4, 15) {{
					frontColor = trailColor = Color.valueOf("A1A7AB");
					backColor = Color.valueOf("595E61");
					hitEffect = despawnEffect = Fx.hitBulletColor;
					width = 8f;
					height = 10f;
					lifetime = 45f;
					trailWidth = 1.5f;
					trailLength = 5;
					homingRange = 35f;
					homingPower = 0.04f;
					collidesAir = false;
				}},
				SWItems.iron, new BasicBulletType(4, 20) {{
					frontColor = trailColor = Color.valueOf("A1A1B8");
					backColor = Color.valueOf("6F6F85");
					hitEffect = despawnEffect = Fx.hitBulletColor;
					width = 8f;
					height = 10f;
					lifetime = 45f;
					trailWidth = 1.5f;
					trailLength = 5;
					homingRange = 35f;
					homingPower = 0.04f;
					collidesAir = false;
				}}
			);
		}};
		vniz = new ItemTurret("vniz") {{
			requirements(Category.turret, with(
				SWItems.nickel, 25,
				Items.graphite, 20
			));
			researchCost = with(
				SWItems.nickel, 100,
				Items.graphite, 100
			);
			size = 1;
			scaledHealth = 200;
			reload = 90f;
			range = 180f;
			targetGround = false;

			BulletType frag = new BasicBulletType(2f, 5) {{
				frontColor = trailColor = Color.valueOf("A1A7AB");
				backColor = Color.valueOf("595E61");
				lifetime = 30f;
				trailWidth = 1f;
				trailLength = 5;
				homingRange = 60f;
				homingPower = 0.5f;
				collidesGround = false;
			}};
			ammo(
				SWItems.nickel, new ArtilleryBulletType(4, 10) {{
					frontColor = trailColor = Color.valueOf("A1A7AB");
					backColor = Color.valueOf("595E61");
					hitEffect = despawnEffect = Fx.hitBulletColor;
					width = 8f;
					height = 10f;
					lifetime = 45f;
					trailWidth = 1.5f;
					trailLength = 5;
					collides = collidesAir = true;

					fragBullets = 3;
					fragBullet = frag;
				}},
				SWItems.iron, new ArtilleryBulletType(4, 15) {{
					frontColor = trailColor = Color.valueOf("A1A1B8");
					backColor = Color.valueOf("6F6F85");
					hitEffect = despawnEffect = Fx.hitBulletColor;
					width = 8f;
					height = 10f;
					lifetime = 45f;
					trailWidth = 1.5f;
					trailLength = 5;
					collides = collidesAir = true;

					fragBullets = 5;
					fragBullet = frag;
				}}
			);
		}};
		rozpad = new ItemTurret("rozpad") {{
			requirements(Category.turret, with(
				SWItems.nickel, 45,
				SWItems.iron, 40,
				Items.graphite, 20
			));
			size = 2;
			scaledHealth = 200;
			reload = 60f;
			range = 200f;
			targetGround = false;

			shoot = new ShootAlternate();

			BulletType frag = new BasicBulletType(2f, 10) {{
				frontColor = trailColor = Color.valueOf("A1A7AB");
				backColor = Color.valueOf("595E61");
				lifetime = 30f;
				trailWidth = 1f;
				trailLength = 5;
				homingRange = 60f;
				homingPower = 0.5f;
				collidesGround = false;
			}};
			ammo(
				SWItems.nickel, new ArtilleryBulletType(4, 20) {{
					frontColor = trailColor = Color.valueOf("A1A7AB");
					backColor = Color.valueOf("595E61");
					hitEffect = despawnEffect = Fx.hitBulletColor;
					width = 8f;
					height = 10f;
					lifetime = 50f;
					trailWidth = 1.5f;
					trailLength = 5;
					homingRange = 60f;
					homingPower = 0.2f;
					collides = collidesAir = true;

					fragBullets = 3;
					fragBullet = frag;
				}},
				SWItems.iron, new ArtilleryBulletType(4, 25) {{
					frontColor = trailColor = Color.valueOf("A1A1B8");
					backColor = Color.valueOf("6F6F85");
					hitEffect = despawnEffect = Fx.hitBulletColor;
					width = 8f;
					height = 10f;
					lifetime = 50f;
					trailWidth = 1.5f;
					trailLength = 5;
					homingRange = 60f;
					homingPower = 0.2f;
					collides = collidesAir = true;

					fragBullets = 5;
					fragBullet = frag;
				}}
			);
		}};

		curve = new SWConsumeTurret("curve") {{
			requirements(Category.turret, with());
			size = 3;
			scaledHealth = 220f;
			reload = 15f;
			shootY = 12f;
			range = 240f;
			shootSound = Sounds.shootBig;

			consumeItem(Items.silicon, 1);

			drawer = new DrawTurret() {{
				parts.add(
					new RegionPart("-cannon") {{
						mirror = false;
						under = true;
						moveX = 2f;
						moveY = -1f;
						layerOffset = -0.001f;
						outlineLayerOffset = -0.03f;
						progress = PartProgress.reload.curve(Interp.circle).inv();
					}},
					new RegionPart("-cannon") {{
						mirror = false;
						under = true;
						x = 2f;
						y = -1f;
						moveX = -2f;
						moveY = -1f;
						layerOffset = -0.003f;
						outlineLayerOffset = -0.03f;
						progress = PartProgress.reload.curve(Interp.circle).inv();
					}},
					new RegionPart("-cannon") {{
						mirror = false;
						under = true;
						y = -2f;
						moveX = -2f;
						moveY = 1f;
						layerOffset = -0.002f;
						outlineLayerOffset = -0.03f;
						progress = PartProgress.reload.curve(Interp.circle).inv();
					}},
					new RegionPart("-cannon") {{
						mirror = false;
						under = true;
						x = -2f;
						y = -1f;
						moveX = 2f;
						moveY = 1f;
						outlineLayerOffset = -0.03f;
						progress = PartProgress.reload.curve(Interp.circle).inv();
					}}
				);
			}};
			shootType = new BasicBulletType(3f, 20) {{
				shrinkY = 0f;
				width = 8f;
				height = 8f;
				trailWidth = 4f;
				trailLength = 5;
				hitSound = Sounds.explosion;
				lifetime = 80f;
			}};
		}};
		thermikos = new SWConsumeTurret("thermikos") {{
			requirements(Category.turret, with());
			size = 3;
			scaledHealth = 220f;
			range = 240f;
			reload = 120f;
			recoil = 0f;
			rotateSpeed = 1f;
			moveWhileCharging = true;

			shootY = 8f;
			shoot = new ShootPattern() {{
				firstShotDelay = 30f;
			}};

			shootSound = Sounds.cannon;
			chargeSound = Sounds.lasercharge2;

			consumeItems(with(Items.graphite, 2, SWItems.thermite, 3));

			drawer = new DrawTurret() {{
				parts.add(
					new RegionPart("-cannon") {{
						under = true;
						moveY = -4f;
						progress = PartProgress.heat.curve(Interp.bounceIn);
					}}
				);
			}};

			shootType = new ArtilleryBulletType(4f, 200f) {{
				splashDamage = 200f;
				splashDamageRadius = 16f;
				lifetime = 40f;
				width = height = 20f;

				collides = collidesAir = collidesGround = true;

				shootEffect = SWFx.thermiteShoot;
				chargeEffect = SWFx.thermiteCharge;
			}};
		}};
		sonar = new SWConsumeTurret("sonar") {{
			requirements(Category.turret, with());
			size = 2;
			scaledHealth = 220;
			reload = 90f;
			range = 160f;

			drawer = new DrawTurret() {{
				parts.add(
					new RegionPart("-back") {{
						moveY = 2f;
						under = true;
					}},
					new RegionPart("-side") {{
						moveX = -1f;
						moveY = 1f;
						mirror = true;
						under = true;
					}}
				);
			}};

			shootY = 0f;
			shootSound = ModSounds.sonarShoot;
			shootType = new SoundLaserBulletType() {{
				damage = 30f;
				width = 16f;
				length = 160f;
				colors = new Color[]{Color.white};
			}};
		}};

		push = new SWConsumeTurret("push") {{
			requirements(Category.turret, with());
			size = 3;
			scaledHealth = 220f;
			range = 240f;
			reload = 120f;

			drawer = new DrawTurret() {{
				parts.addAll(
					new RegionPart("-side") {{
						mirror = true;
						moveX = 2f;
						progress = DrawPart.PartProgress.reload.inv().delay(0.25f).inv().curve(Interp.circle);
					}},
					new RegionPart("-support") {{
						mirror = under = true;
						moveY = 4f;
						progress = DrawPart.PartProgress.reload.inv().mul(3).clamp().curve(Interp.circleIn);
					}}
				);
			}};

			consume(new ConsumeTension(10, 20));

			shootSound = Sounds.shootSmite;
			shootType = new BasicBulletType(3f, 30, "sw-sound-wave") {{
				width = 16;
				height = 10;
				lifetime = 80f;
				trailInterval = 10;
				knockback = 8f;
				pierceCap = 5;
				hitEffect = despawnEffect = trailEffect = SWFx.soundDecay;
				trailRotation = true;
				pierce = pierceBuilding = true;
			}};
		}};
	}
}

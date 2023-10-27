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
import mindustry.world.draw.*;
import sw.content.*;
import sw.world.blocks.defense.*;
import sw.world.blocks.production.*;
import sw.world.blocks.vibration.*;
import sw.world.consumers.*;
import sw.world.draw.*;
import sw.world.meta.*;

import static mindustry.type.ItemStack.*;

public class SWVibration {
	public static Block
		vibrationWire,
		thermiteMixer,
		springHammer,

		sonus, impacto;

	public static void load() {
		vibrationWire = new VibrationWire("vibration-wire") {{
			requirements(Category.power, with(
				SWItems.denseAlloy, 1
			));
			health = 40;
			vibrationConfig = new VibrationConfig() {{
				resistance = 0.1f;
				range = 40f;
			}};
		}};

		thermiteMixer = new SWGenericCrafter("thermite-mixer") {{
			requirements(Category.crafting, with(
				SWItems.denseAlloy, 100,
				Items.titanium, 120,
				Items.silicon, 80,
				Items.graphite, 100
			));
			size = 2;
			health = 360;
			craftTime = 120f;
			hasVibration = true;
			hasHeat = hasForce = false;

			consume(new ConsumeVibration(100f, 250f));
			consumeItems(with(
				Items.graphite, 2,
				Items.sand, 2,
				Items.sporePod, 1
			));
			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawBlurSpin("-rotator", 20) {{
					blurThresh = 123123f;
				}},
				new DrawMixing() {{
					circleRad = 6f;
					particleRad = 6f;
					alpha = 0.5f;
					particleColor = circleColor = Color.valueOf("6D6F7F");
				}},
				new DrawMixing() {{
					circleRad = 1f;
					particleRad = 6f;
					idOffset = 1;
					alpha = 0.5f;
					particleColor = circleColor = Color.valueOf("6C5252");
				}},
				new DrawDefault()
			);

			outputItems = with(SWItems.thermite, 2);

			vibrationConfig = new VibrationConfig() {{
				resistance = 0.2f;
				outputsVibration = false;
			}};
		}};

		springHammer = new SWGenericCrafter("spring-hammer") {{
			requirements(Category.power, with(
				SWItems.denseAlloy, 80,
				SWItems.compound, 120,
				Items.silicon, 100
			));
			size = 2;
			health = 160;
			craftTime = 120f;
			outputVibration = 480f;
			hasHeat = false;
			hasVibration = true;

			consume(new ConsumeSpeed(1f, 10f));
			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawHammer() {{
					offsetX = -5.25f;
					offsetY = -3.75f;
					moveX = 1.5f;
					moveProgress = Interp.circleIn;
				}},
				new DrawDefault()
			);
			vibrationConfig = new VibrationConfig() {{
				resistance = 0.2f;
				range = 80f;
				acceptsVibration = false;
			}};
			forceConfig = new ForceConfig() {{
				friction = 0.002f;
				beltSize = 4f;
				outputsForce = false;
				maxForce = 1f;
			}};
		}};

		sonus = new SWConsumeTurret("sonus") {{
			requirements(Category.turret, with(
				SWItems.scorch, 120,
				Items.silicon, 150,
				Items.titanium, 200
			));
			size = 3;
			scaledHealth = 220f;
			reload = 60f;
			range = 240f;
			cooldownTime = 5f;
			hasVibration = true;
			targetGround = false;
			shootSound = Sounds.shootSmite;
//			consume(new ConsumeVibration(0, 10000));

			drawer = new DrawTurret() {{
				parts.add(new RegionPart("-speaker") {{
					under = true;
					moveY = -0.5f;
					progress = PartProgress.heat;
				}});
			}};

			vibrationConfig = new VibrationConfig() {{
				outputsVibration = true;
			}};

			shootType = new BasicBulletType(6f, 60, "sw-sound-wave") {{
				width = height = 16f;
				lifetime = 40f;
				trailInterval = 10f;
				trailEffect = despawnEffect = SWFx.soundDecay;
				trailRotation = true;
				pierce = true;
				collidesGround = false;
				keepVelocity = false;
				hittable = false;
			}};
		}};
		impacto = new SWConsumeTurret("impacto") {{
			requirements(Category.turret, with(
				SWItems.denseAlloy, 100,
				Items.silicon, 120,
				Items.graphite, 150
			));
			size = 2;
			scaledHealth = 220f;
			rotateSpeed = 360f;
			reload = 60f;
			range = 120f;
			recoil = shootY = 0f;
			outlineRadius = 0;
			hasVibration = true;
			targetAir = false;
			shootSound = Sounds.titanExplosion;

			drawer = new DrawTurret() {{
				parts.add(new RegionPart("-hammer") {{
					growX = growY = 1f;
					outline = false;
					growProgress = PartProgress.charge.curve(Interp.circleOut);
				}});
			}};

			vibrationConfig = new VibrationConfig() {{
				outputsVibration = true;
			}};

			shoot = new ShootPattern() {{
				firstShotDelay = 30f;
			}};

			shootType = new ExplosionBulletType(150, 120) {{
				killShooter = false;
				collidesAir = false;
				shootEffect = SWFx.soundImpact;
			}};
		}};

	}
}

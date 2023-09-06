package sw.content.blocks;

import arc.graphics.*;
import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.entities.part.*;
import mindustry.entities.pattern.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.production.*;
import mindustry.world.consumers.*;
import mindustry.world.draw.*;
import sw.content.*;
import sw.world.blocks.heat.*;
import sw.world.blocks.production.*;
import sw.world.consumers.*;
import sw.world.draw.*;
import sw.world.graph.*;
import sw.world.meta.*;

import static mindustry.type.ItemStack.*;

public class SWHeat {
	public static Block
		heatPipe, heatBridge, heatRadiator,

		burner, resistance, frictionHeater,

		boiler, thermalBoiler,

		bolt, light;

	public static void load() {
		heatPipe = new HeatPipe("heat-pipe") {{
			requirements(Category.power, with(Items.silicon, 1, Items.metaglass, 1, SWItems.denseAlloy, 3));
			heatConfig.heatLoss = 0.01f;
		}};
		heatBridge = new HeatBridge("heat-bridge") {{
			requirements(Category.power, with(Items.silicon, 5, SWItems.denseAlloy, 8));
			heatConfig.heatLoss = 0.01f;
		}};
		heatRadiator = new HeatRadiator("heat-radiator") {{
			requirements(Category.power, with(Items.silicon, 3, SWItems.denseAlloy, 2, Items.graphite, 1));
			size = 2;
			heatConfig = new HeatConfig() {{
				maxHeat = 400f;
				heatLoss = 0.01f;
			}};
		}};

		burner = new SWGenericCrafter("burner") {{
			requirements(Category.power, with(
				Items.silicon, 20,
				Items.graphite, 30,
				SWItems.denseAlloy, 50,
				Items.lead, 25)
			);
			size = 2;
			health = 160;
			consume(new ConsumeItemFlammable());
			drawer = new DrawMulti(new DrawDefault(), new DrawFlame() {{
				flameRadius = 1.5f;
				flameRadiusIn = 0.75f;
				flameRadiusMag = 1f;
			}});
			hasForce = false;
			updateEffect = Fx.coalSmeltsmoke;
			craftTime = 120f;
			outputHeat = 500f;
			outputHeatSpeed = 3f;
			heatConfig.acceptHeat = false;
		}};
		resistance = new SWGenericCrafter("resistance") {{
			requirements(Category.power, with(
				Items.silicon, 100,
				SWItems.denseAlloy, 90,
				Items.titanium, 70,
				Items.graphite, 70
			));
			hasForce = false;
			size = 2;
			health = 160;
			outputHeatSpeed = 400f;
			outputHeat = 800f;
//			consume(new ConsumePowerMin(9f, 0f, 540f, false));
			consumePower(9f);
			heatConfig = new HeatConfig() {{
				maxHeat = 1000f;
				acceptHeat = false;
			}};
			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawTemperature(),
				new DrawDefault()
			);
		}};
		frictionHeater = new SWGenericCrafter("friction-heater") {{
			requirements(Category.power, with(
				Items.silicon, 150,
				SWItems.denseAlloy, 160,
				SWItems.nickel, 100,
				Items.titanium, 80,
				SWItems.compound, 200
			));
			clampRotation = true;
			size = 3;
			health = 200;
			craftTime = 5f;
			craftEffect = SWFx.sparks;
			forceConfig = new ForceConfig() {{
				friction = 0.18f;
				maxForce = 18f;
				beltSize = 6f;
				outputsForce = false;
			}};
			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawRegion("-bar"),
				new DrawSinSpin() {{
					sinScl = 1f;
					sinMag = 6.25f;
				}},
				new DrawDefault()
			);
			consume(new ConsumeSpeed(1f, 3f));
			consume(new ConsumeRatio(ForceGraph.ForceRatio.extreme));
			heatConfig.acceptHeat = false;
			outputHeat = 550f;
		}};

		boiler = new HeatGenericCrafter("boiler") {{
			requirements(Category.production, with(SWItems.nickel, 40, Items.metaglass, 35, Items.titanium, 30));
			heatConfig.outputHeat = false;
			size = 2;
			health = 160;
			hasLiquids = true;
			consumeLiquid(Liquids.water, 0.1f);
			consume(new ConsumeHeat(0.5f, 100f, false));
			updateEffect = Fx.smoke;
			outputLiquid = new LiquidStack(SWLiquids.steam, 0.15f);
		}};
		thermalBoiler = new AttributeCrafter("thermal-boiler") {{
			requirements(Category.production, with(SWItems.nickel, 40, Items.titanium, 50, Items.metaglass, 35, Items.titanium, 30));
			size = 2;
			health = 160;
			baseEfficiency = 0f;
			minEfficiency = 0.25f;
			maxBoost = 1.5f;
			boostScale = 0.5f;
			consumeLiquid(Liquids.water, 0.1f);
			updateEffect = Fx.smoke;
			outputLiquid = new LiquidStack(SWLiquids.steam, 0.2f);
		}};

		Color col = Pal.accent.cpy().a(0.3f);
		bolt = new HeatTurret("bolt") {{
			requirements(Category.turret, with(
				Items.silicon, 120,
				Items.copper, 70,
				SWItems.denseAlloy, 95
			));
			consumer = consume(new ConsumeHeat(1, 250, false));
			drawer = new DrawTurret() {{
				parts.add(
					new RegionPart("-barrel") {{
						moveY = -1f;
						under = true;
					}}
				);
			}};
			shootSound = Sounds.none;
			loopSound = Sounds.torch;
			loopSoundVolume = 1f;
			size = 2;
			health = 180 * 4;
			range = 92f;
			shootSound = Sounds.blaster;
			shootType = new ContinuousLaserBulletType(5) {{
				width = 4f;
				length = 92f;
				colors = new Color[]{col, col, col};
			}};
		}};
		light = new HeatTurret("light") {{
			requirements(Category.turret, with(
				Items.titanium, 200,
				Items.silicon, 200,
				Items.graphite, 220,
				SWItems.denseAlloy, 210
			));
			consumer = consume(new ConsumeHeat(2, 400, true));
			size = 3;
			health = 200 * 9;
			range = 180f;
			shootSound = Sounds.none;
			loopSound = Sounds.torch;
			loopSoundVolume = 1f;
			shoot = new ShootSpread(3, 15);
			shootType = new ContinuousLaserBulletType(15) {{
				length = 180f;
				colors = new Color[]{col, col, col};
			}};
		}};
	}
}

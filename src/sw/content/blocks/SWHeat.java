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
import mindustry.world.draw.*;
import sw.content.*;
import sw.world.blocks.heat.*;
import sw.world.consumers.*;
import sw.world.meta.*;

import static mindustry.type.ItemStack.*;

public class SWHeat {
	public static Block
		heatPipe, heatBridge, heatRadiator,

//		boiler, thermalBoiler,

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

//		boiler = new HeatGenericCrafter("boiler") {{
//			requirements(Category.production, with(SWItems.nickel, 40, Items.metaglass, 35, Items.titanium, 30));
//			heatConfig.outputHeat = false;
//			size = 2;
//			health = 160;
//			hasLiquids = true;
//			consumeLiquid(Liquids.water, 0.1f);
//			consume(new ConsumeHeat(0.5f, 100f, false));
//			updateEffect = Fx.smoke;
//			outputLiquid = new LiquidStack(SWLiquids.steam, 0.15f);
//		}};
//		thermalBoiler = new AttributeCrafter("thermal-boiler") {{
//			requirements(Category.production, with(SWItems.nickel, 40, Items.titanium, 50, Items.metaglass, 35, Items.titanium, 30));
//			size = 2;
//			health = 160;
//			baseEfficiency = 0f;
//			minEfficiency = 0.25f;
//			maxBoost = 1.5f;
//			boostScale = 0.5f;
//			consumeLiquid(Liquids.water, 0.1f);
//			updateEffect = Fx.smoke;
//			outputLiquid = new LiquidStack(SWLiquids.steam, 0.2f);
//		}};

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

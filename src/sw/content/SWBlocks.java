package sw.content;

import arc.graphics.Color;
import mindustry.content.*;
import mindustry.gen.Sounds;
import mindustry.world.blocks.storage.CoreBlock;
import pl.world.blocks.defense.turrets.PressureTurret;
import pl.world.blocks.distribution.PressureBridge;
import pl.world.blocks.distribution.PressurePipe;
import pl.world.blocks.distribution.PressureValve;
import pl.world.blocks.pressure.Pressure;
import pl.world.blocks.sandbox.PressureSource;
import pl.world.consumers.ConsumePressureTrigger;
import mindustry.entities.bullet.LaserBulletType;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.world.Block;
import mindustry.world.draw.*;
import sw.world.blocks.production.Heater;
import sw.world.blocks.production.StackCrafter;
import sw.world.recipes.GenericRecipe;
import sw.world.blocks.production.MultiCrafter;

import static mindustry.type.ItemStack.*;

public class SWBlocks {
	public static Block
		pressurePipe, pressureBridge, pressureValve,
		pressureSource,
		steamBurner, hydraulicCrafter, pressurePress,
		railgun,
		coreScaffold;

	public static void load() {
//		distribution
		pressurePipe = new PressurePipe("pressure-pipe") {{
			requirements(Category.liquid, with(
				Items.silicon, 1,
				Items.graphite, 2
			));
			health = 100;
			pressureC = new Pressure() {{
				overflows = true;
				lossRate = 1f;
			}};
		}};
		pressureBridge = new PressureBridge("pressure-bridge") {{
			requirements(Category.liquid, with(
				Items.silicon, 5,
				Items.graphite, 10,
				SWItems.silver, 7
			));
			pressureC = new Pressure() {{
				internalSize = 4;
				overflows = true;
				lossRate = 1f;
			}};
		}};
		pressureValve = new PressureValve("pressure-valve") {{
			requirements(Category.liquid, with(
				Items.silicon, 4,
				Items.graphite, 3,
				SWItems.tin, 3
			));
			health = 120;
			pressureC = new Pressure() {{
				internalSize = 4f;
				lossRate = 1f;
			}};
		}};
//		sandbox
		pressureSource = new PressureSource("pressure-source") {{
			category = Category.liquid;
			pressureC = new Pressure() {{
				maxPressure = 250f;
				lossRate = 1f;
			}};
		}};
//		crafting
		steamBurner = new Heater("steam-burner") {{
			requirements(Category.production, with(
				Items.metaglass, 120,
				Items.graphite, 100,
				Items.lead, 110
			));
			size = 2;
			consumeItem(Items.coal);
			consumeLiquid(Liquids.water, 0.1f);
			pressureSpeed = 2f;
			pressureC = new Pressure() {{
				maxPressure = 220f;
				internalSize = 12f;
				acceptsPressure = false;
			}};
		}};
		hydraulicCrafter = new MultiCrafter("hydraulic-crafter") {{
			requirements(Category.crafting, with(
				Items.graphite, 80,
				Items.copper, 60,
				Items.lead, 75
			));
			size = 3;
			health = 200;
			recipes.add(
				new GenericRecipe() {{
					craftTime = 45f;
					consumeItems = with(Items.graphite, 1, Items.lead, 2);
					outputItems = with(SWItems.tin, 1);
					drawer = new DrawMulti(
						new DrawDefault(),
						new DrawFlame()
					);
					craftEffect = SWFx.tinCraft;
					updateEffect = 	Fx.smeltsmoke;
				}},
				new GenericRecipe() {{
					craftTime = 60f;
					consumeItems = with(SWItems.tin, 2, Items.metaglass, 2);
					consumeLiquids = LiquidStack.with(Liquids.water, 0.2f);
					outputItems = with(SWItems.silver, 1);
					drawer = new DrawMulti(
						new DrawDefault(),
						new DrawRegion("-cap")
					);
					craftEffect = SWFx.silverCraft;
					updateEffect = Fx.smoke;
				}}
 			);
		}};
		pressurePress = new StackCrafter("pressure-press") {{
			requirements(Category.crafting, with(
							Items.plastanium, 120,
							Items.silicon, 140,
							Items.graphite, 115,
							SWItems.silver, 130
			));
			size = 4;
			health = 240;
			craftTime = 60;
			craftEffect = SWFx.quadPressureHoles;
			updateEffect = Fx.smoke;
			stackCraftEffect = Fx.plasticburn;
			stacks = 6;
			consume(new ConsumePressureTrigger(75f));
			consumePower(1f);
			consumeItems(with(
							Items.graphite, 2,
							SWItems.tin, 2
			));
			outputItem = new ItemStack(SWItems.denseAlloy, 1);
			pressureC = new Pressure() {{
				maxPressure = 250f;
				internalSize = 24f;
				lossRate = 0.995f;
				outputsPressure = false;
			}};
		}};
//		turrets
		railgun = new PressureTurret("railgun") {{
			requirements(Category.turret, with(
							Items.graphite, 150,
							Items.silicon, 130,
							Items.titanium, 160,
							SWItems.denseAlloy, 120
			));
			size = 3;
			reload = 120;
			range = 160f;
			consume(new ConsumePressureTrigger(40f));
			shootSound = Sounds.shotgun;
			shootType = new LaserBulletType(150f) {{
				lifetime = 60f;
				length = 160f;
				colors = new Color[]{Color.white, Color.lightGray, Color.gray};
			}};
			pressureC = new Pressure() {{
				maxPressure = 250f;
				internalSize = 20f;
				lossRate = 0.997f;
				outputsPressure = false;
			}};
		}};
//		storage
		coreScaffold = new CoreBlock("core-scaffold") {{
			requirements(Category.effect, with(
							Items.graphite, 1000,
							Items.lead, 1500,
							Items.silicon, 1200
			));
			size = 3;
			health = 2000;
			alwaysUnlocked = true;
			unitType = UnitTypes.beta;
			itemCapacity = 5000;
			unitCapModifier = 12;
		}};
	}
}

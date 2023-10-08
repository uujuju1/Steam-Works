package sw.content.blocks;

import arc.graphics.*;
import arc.math.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import sw.content.*;
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
		springHammer;

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
			size = 3;
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
				new DrawBlurSpin("-plate", 20),
				new DrawMixing() {{
					circleRad = 10f;
					particleRad = 10f;
					alpha = 0.5f;
					particleColor = circleColor = Color.valueOf("6D6F7F");
				}},
				new DrawMixing() {{
					circleRad = 1f;
					particleRad = 10f;
					idOffset = 1;
					alpha = 0.5f;
					particleColor = circleColor = Color.valueOf("6C5252");
				}},
				new DrawDefault()
			);

			outputItems = with(SWItems.neodymium, 2);

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

	}
}

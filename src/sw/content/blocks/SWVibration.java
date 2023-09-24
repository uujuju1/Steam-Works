package sw.content.blocks;

import arc.math.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import sw.content.*;
import sw.world.blocks.production.*;
import sw.world.blocks.vibration.*;
import sw.world.draw.*;
import sw.world.meta.*;

import static mindustry.type.ItemStack.*;

public class SWVibration {
	public static Block vibrationWire, springHammer;

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

		springHammer = new SWGenericCrafter("spring-hammer") {{
			requirements(Category.power, with(
				SWItems.denseAlloy, 80,
				SWItems.compound, 120,
				Items.silicon, 100
			));
			size = 2;
			health = 160;
			craftTime = 120f;
			outputVibration = 144f;
			hasHeat = false;

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
				resistance = 0.3f;
				range = 80f;
			}};
		}};
	}
}

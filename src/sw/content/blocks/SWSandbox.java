package sw.content.blocks;

import mindustry.entities.part.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;
import sw.world.blocks.power.*;
import sw.world.blocks.sandbox.*;
import sw.world.draw.*;
import sw.world.meta.*;

public class SWSandbox {
	public static Block allSource, carnotEngine, brake;

	public static void load() {
		allSource = new ResourceSource("all-source") {{
			health = 2147483647;
		}};

		carnotEngine = new SpinSource("carnot-engine") {{
			health = 2147483647;

			spinConfig = new SpinConfig();
		}};

		brake = new AxleBrake("brake") {{
			health = 2147483647;
			rotate = false;

			buildVisibility = BuildVisibility.sandboxOnly;
			category = Category.power;

			speedGradient = strength = -1;

			drawer = new DrawMulti(
				new DrawRegion(),
				new DrawParts() {{
					parts.add(
						new RegionPart("-wheel") {{
							outline = false;
							clampProgress = false;
							moveRot = 2f;

							progress = DrawParts.spin;
						}},
						new RegionPart("-brake-down") {{
							outline = false;
							moveY = -5/4f;

							progress = DrawParts.progress.inv();
						}},
						new RegionPart("-brake-up") {{
							outline = false;
							moveY = 5/4f;

							progress = DrawParts.progress.inv();
						}}
					);
				}}
			);

			spinConfig = new SpinConfig();
		}};
	}
}

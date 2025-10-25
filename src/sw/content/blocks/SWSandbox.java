package sw.content.blocks;

import mindustry.world.*;
import sw.world.blocks.sandbox.*;
import sw.world.meta.*;

public class SWSandbox {
	public static Block allSource, carnotEngine;

	public static void load() {
		allSource = new ResourceSource("all-source") {{
			health = 2147483647;
		}};

		carnotEngine = new SpinSource("carnot-engine") {{
			health = 2147483647;
			
			spinConfig = new SpinConfig();
		}};
	}
}

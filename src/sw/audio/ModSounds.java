package sw.audio;

import arc.audio.*;
import mindustry.*;

public class ModSounds {
	public static Sound hitMetal;
	public static Sound sonarShoot;
	public static Sound welding;

	public static void load() {
		hitMetal = Vars.tree.loadSound("hitMetal");
		sonarShoot = Vars.tree.loadSound("SonarShoot");
		welding = Vars.tree.loadSound("welding");
	}
}

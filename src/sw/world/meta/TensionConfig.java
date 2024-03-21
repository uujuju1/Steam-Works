package sw.world.meta;

import arc.*;
import arc.graphics.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import sw.world.interfaces.*;

public class TensionConfig {
	public int tier = 0;
	public float staticTension = 0f, maxTension = 100f;
	public boolean graphs = true;
	public Color fromColor = Pal.accent, toColor = Pal.turretHeat;

	public void addStats(Stats stats) {
		stats.add(SWStat.staticTension, staticTension, SWStat.tensionUnits);
		stats.add(SWStat.maxTension, maxTension, SWStat.tensionUnits);
	}
	public void addBars(Block block) {
		block.addBar("sw-tension", b -> {
			HasTension build = (HasTension) b;
			return new Bar(
				() -> Core.bundle.get("sw-tension", "sw-tension") + ": " + build.tensionGraph().getOverallTension(),
				() -> fromColor.cpy().lerp(toColor, build.tensionGraph().getOverallTension()/maxTension),
				() -> build.tensionGraph().getOverallTension()/maxTension
			);
		});
	}
}

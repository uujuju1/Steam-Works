package sw.world.meta;

import arc.util.*;
import mindustry.world.*;
import mindustry.world.meta.*;

public class FluidConfig {
	public int[][] allowedEdges;
	
	public @Nullable FluidGroup group;
	
	public void addBars(Block block) {
//		block.addBar("sw-spin", building -> {
//			HasSpin b = building.as();
//			return new Bar(
//				() -> Core.bundle.format("bar.sw-rotation", Strings.autoFixed(b.spinGraph().speed / b.spinGraph().ratios.get(b, 1f) * 10f, 2)),
//				() -> barColor,
//				() -> topSpeed > 0 ? Mathf.clamp((b.spinGraph().speed / b.spinGraph().ratios.get(b, 1f)) / topSpeed) : 1f
//			);
//		});
	}
	public void addStats(Stats stats) {
//		if (resistance > 0) stats.add(SWStat.spinResistance, StatValues.number(resistance * 600f, SWStat.spinMinuteSecond));
	}
	
	public enum FluidGroup {
		transportation
	}
}

package sw.world.blocks.liquid;

import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.meta.*;

public class LiquidPipeBridge extends Block {
	public final int timerFlow = timers++;

	public int range;

	public LiquidPipeBridge(String name) {
		super(name);
		rotate = true;
		update = true;
		destructible = true;
		hasLiquids = true;
		drawArrow = false;
		solid = false;
		floating = true;
		underBullets = true;
		noUpdateDisabled = true;
		canOverdrive = false;
		group = BlockGroup.liquids;
		buildVisibility = BuildVisibility.hidden;
		outputsLiquid = true;
		priority = TargetPriority.transport;
	}

	public class LiquidPipeBridgeBuild extends Building {
		@Override
		public void updateTile() {
		
		}
	}
}

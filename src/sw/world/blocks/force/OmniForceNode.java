package sw.world.blocks.force;

import arc.scene.ui.layout.*;
import arc.util.io.*;
import mindustry.gen.*;
import sw.world.meta.*;

public class OmniForceNode extends ForceNode {
	public int maxGearSize = 3;

	public OmniForceNode(String name) {
		super(name);
	}

	@Override
	public void setStats() {
		super.setStats();
		stats.remove(SWStat.beltSize);
	}

	public class OmniForceNodeBuild extends ForceNodeBuild {
		public int gear = 0;

		@Override public float beltSize() {
			return (gear + 1) * 2f;
		}

		@Override
		public void buildConfiguration(Table table) {
			table.button(Icon.settings, () -> {
				gear++;
				gear %= maxGearSize;
			});
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);
			gear = read.i();
		}

		@Override
		public void write(Writes write) {
			super.write(write);
			write.i(gear);
		}
	}
}

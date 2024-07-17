package sw.world.blocks.environment;

import arc.func.*;
import mindustry.world.blocks.environment.*;
import sw.world.MultiShape.*;
import sw.world.interfaces.*;

public class MultiOverlayFloor extends OverlayFloor implements MultiEnvI {
	public Prov<MultiShapeBuild> multiShapeBuild = MultiShapeBuild::new;

	public MultiOverlayFloor(String name) {
		super(name);
	}

	@Override public MultiShapeBuild createMultiShapeBuild() {
		return multiShapeBuild.get();
	}
}

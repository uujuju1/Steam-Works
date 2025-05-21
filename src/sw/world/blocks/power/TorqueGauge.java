package sw.world.blocks.power;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.math.*;
import arc.util.*;
import mindustry.graphics.*;
import sw.ui.elements.*;
import sw.world.interfaces.*;

public class TorqueGauge extends AxleBlock {
	public Color positiveForce = Pal.heal, negativeForce = Pal.breakInvalid, noForce = Color.white;
	
	public TorqueGauge(String name) {
		super(name);
	}
	
	@Override
	public void setBars() {
		super.setBars();
		
		addBar("sw-force", building -> {
			HasSpin b = building.as();
			
			Floatp forces = () -> b.spinGraph().force() - b.spinGraph().resistance();
			
			return new CenterBar(
				() -> Core.bundle.format(
					"bar.sw-force",
					forces.get() > 0 ? "+" : (forces.get() == 0 ? "" : "-"),
					Strings.autoFixed(Math.abs(forces.get() * 600f), 2)
				),
				() -> forces.get() > 0 ? positiveForce : (forces.get() == 0 ? noForce : negativeForce),
				() -> spinConfig.topSpeed > 0 ?
				        Mathf.clamp(forces.get() * 600 / spinConfig.topSpeed, -1f, 1f) :
				        forces.get() > 0 ? 1 : (forces.get() == 0 ? 0 : -1f)
			);
		});
	}
	
	public class TorqueGaugeBuild extends AxleBlockBuild {
	
	}
}

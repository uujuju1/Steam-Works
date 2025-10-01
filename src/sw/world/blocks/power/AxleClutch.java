package sw.world.blocks.power;

import arc.math.*;
import sw.world.graph.*;
import sw.world.interfaces.*;

public class AxleClutch extends AxleBlock {
	public float clutchStrength = 1f/600f;
	
	public AxleClutch(String name) {
		super(name);
	}
	
	public class AxleClutchBuild extends AxleBlockBuild {
		@Override public boolean connectTo(HasSpin other) {
			return false;
		}
		
		@Override
		public float getForceDisconnected(SpinGraph to) {
			boolean isBack = back() instanceof HasSpin build && build.spinGraph() == to;
			float scalar = isBack ? -((HasSpin) back()).getRatio() : (front() instanceof HasSpin build ? build.getRatio() : 0);
			float backSpeed = back() instanceof HasSpin build ? build.spinGraph().speed : 0f;
			float frontSpeed = front() instanceof HasSpin build ? build.spinGraph().speed : 0f;
			return Mathf.clamp(backSpeed - frontSpeed, -clutchStrength, clutchStrength) * scalar;
		}
		
		@Override
		public void updateTile() {
			if (back() instanceof HasSpin build && !build.spinGraph().disconnected.contains(this)) {
				build.spinGraph().disconnected.add(this);
			}
			if (front() instanceof HasSpin build && !build.spinGraph().disconnected.contains(this)) {
				build.spinGraph().disconnected.add(this);
			}
		}
	}
}

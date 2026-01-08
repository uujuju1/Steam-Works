package sw.world.blocks.power;

import arc.math.*;
import sw.world.graph.*;
import sw.world.interfaces.*;

public class AxleClutch extends AxleBlock {
	public float clutchStrength = 1f/600f;
	
	public AxleClutch(String name) {
		super(name);
	}
	
	@Override
	public void init() {
		super.init();
		
		if (spinConfig != null) spinConfig.checkSpeed = false;
	}
	
	public class AxleClutchBuild extends AxleBlockBuild {
		@Override public boolean connectTo(HasSpin other) {
			return false;
		}
		
		@Override
		public float getForce() {
			boolean isBack = back() instanceof HasSpin build && build.spinGraph() == SpinGraph.graphContext;
			float scalar = isBack ? -((HasSpin) back()).getRatio() : (front() instanceof HasSpin build ? build.getRatio() : 0);
			float backSpeed = back() instanceof HasSpin build ? build.getSpeed() : 0f;
			float frontSpeed = front() instanceof HasSpin build ? build.getSpeed() : 0f;
			return Mathf.clamp((backSpeed - frontSpeed)/60f, -clutchStrength, clutchStrength) / scalar;
		}
		@Override
		public float getTargetSpeed() {
			boolean isBack = back() instanceof HasSpin build && build.spinGraph() == SpinGraph.graphContext;
			float scalar = isBack ? ((HasSpin) back()).getRatio() : (front() instanceof HasSpin build ? build.getRatio() : 1);
			float backSpeed = back() instanceof HasSpin build ? build.getSpeed() : 0f;
			float frontSpeed = front() instanceof HasSpin build ? build.getSpeed() : 0f;
			return (isBack ? frontSpeed : backSpeed) / scalar;
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

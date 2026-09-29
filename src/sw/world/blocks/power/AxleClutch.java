package sw.world.blocks.power;

import arc.math.*;
import arc.util.*;
import sw.world.graph.*;
import sw.world.interfaces.*;

// TODO Fix minor friction issues when making the clutch have friction by itself
public class AxleClutch extends AxleBlock {
	public float clutchStrength = 1f/600f;
	
	public AxleClutch(String name) {
		super(name);
		update = true;
	}
	
	@Override
	public void init() {
		super.init();
		
		if (spinConfig != null) {
			spinConfig.disconnected = true;
		}
	}
	
	@Override
	public void setStats() {
		super.setStats();
	}
	
	public class AxleClutchBuild extends AxleBlockBuild {
		@Nullable public HasSpin front, back;

		public float frontTorque, backTorque;
		public float frontFriction, backFriction;

		public boolean shouldConnect;

		@Override
		public float getForce() {
			if (front == null || back == null || !shouldConnect) return 0f;
			return (front.spinGraph() == SpinGraph.graphContext ? backTorque * back.getRatio() / front.getRatio() : frontTorque * front.getRatio() / back.getRatio());
		}
		@Override
		public float getTargetSpeed() {
			if (front == null || back == null || !shouldConnect) return 0f;
			return front.spinGraph() == SpinGraph.graphContext ? back.spinGraph().targetSpeed / back.getRatio() * front.getRatio() : front.spinGraph().targetSpeed / front.getRatio() * back.getRatio();
		}

		@Override
		public float getResistance() {
			if (front == null || back == null || !shouldConnect) return spinConfig.resistance;
			return (front.spinGraph() == SpinGraph.graphContext ? backFriction * back.getRatio() / front.getRatio() : frontFriction * front.getRatio() / back.getRatio());
		}

		@Override
		public void update() {
			super.update();
			front = front() instanceof HasSpin build ? build : null;
			back = back() instanceof HasSpin build ? build : null;

			if (front != null && back != null) {
				frontTorque = front.spinGraph().staticTorque.sumf(forceEntry -> forceEntry.speed >= front.spinGraph().targetSpeed ? forceEntry.value : 0f) +
				front.spinGraph().dynamicTorque.sumf(forceEntry -> forceEntry.speed >= front.spinGraph().targetSpeed ? forceEntry.value : 0f);

				backTorque = back.spinGraph().staticTorque.sumf(forceEntry -> forceEntry.speed >= back.spinGraph().targetSpeed ? forceEntry.value : 0f) +
				back.spinGraph().dynamicTorque.sumf(forceEntry -> forceEntry.speed >= back.spinGraph().targetSpeed ? forceEntry.value : 0f);

				frontFriction = front.spinGraph().staticFriction + front.spinGraph().dynamicFriction;
				backFriction = back.spinGraph().staticFriction + back.spinGraph().dynamicFriction;

				float clutches = back.spinGraph().disconnected.sumf(build -> Mathf.num(build instanceof AxleClutchBuild && front.spinGraph().disconnected.contains(build)));
				frontFriction /= clutches;
				backFriction /= clutches;
				frontTorque /= clutches;
				backTorque /= clutches;

				shouldConnect = (frontTorque * front.getRatio() + backTorque * back.getRatio()) - (frontFriction * front.getRatio() + backFriction * back.getRatio()) > 0;

				if (shouldConnect) {

				}
			}
		}
	}
}

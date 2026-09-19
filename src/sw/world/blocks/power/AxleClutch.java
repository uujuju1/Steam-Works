package sw.world.blocks.power;

import arc.util.*;
import sw.world.graph.*;
import sw.world.interfaces.*;

// TODO Remake
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
//			spinConfig.checkSpeed = false;
			spinConfig.disconnected = true;
		}
	}
	
	@Override
	public void setStats() {
		super.setStats();
//		stats.add(SWStat.spinOutputForce, clutchStrength * 600f, SWStat.force);
	}
	
	public class AxleClutchBuild extends AxleBlockBuild {
		@Nullable public HasSpin front, back;

		public float frontTorque, backTorque;
		public float frontFriction, backFriction;

		public boolean shouldConnect;

		@Override
		public float getForce() {
			if (front == null || back == null || !shouldConnect) return 0f;
			return front.spinGraph() == SpinGraph.graphContext ? backTorque / back.getRatio() : frontTorque / front.getRatio();
		}
		@Override
		public float getTargetSpeed() {
			if (front == null || back == null || !shouldConnect) return 0f;
			return front.spinGraph() == SpinGraph.graphContext ? back.spinGraph().targetSpeed * back.getRatio() : front.spinGraph().targetSpeed * front.getRatio();
		}

		@Override
		public float getResistance() {
			if (front == null || back == null || !shouldConnect) return spinConfig.resistance;
			return front.spinGraph() == SpinGraph.graphContext ? (spinConfig.resistance + backFriction) / back.getRatio() : (spinConfig.resistance + frontFriction) / front.getRatio();
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

				shouldConnect = (frontTorque + backTorque) - (frontFriction + backFriction) > 0;

				if (shouldConnect) {

				}
			}
		}
	}
}

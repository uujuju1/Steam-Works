package sw.world.blocks.power;

import arc.struct.*;
import arc.util.*;
import sw.world.graph.*;
import sw.world.interfaces.*;

// TODO Fix clutch with one sided inertia
public class AxleClutch extends AxleBlock {
	public float clutchStrength = 1f/600f;

	private int updateTimer = timers++;
	
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
		public Seq<SpinGraph> connectedToFront = new Seq<>();

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
				if (timer.get(updateTimer, 60f)) {
					connectedToFront.clear();
					spinGraph().floodFill(front, b -> b != this && (!(b instanceof AxleClutchBuild clutchBuild) || clutchBuild.shouldConnect) ? b.nextBuilds() : Seq.with()).each(b -> connectedToFront.addUnique(b.spinGraph()));
				}

				frontTorque = front.spinGraph().staticTorque.sumf(forceEntry -> forceEntry.speed >= front.spinGraph().targetSpeed ? forceEntry.value : 0f) +
				front.spinGraph().dynamicTorque.sumf(forceEntry -> forceEntry.speed >= front.spinGraph().targetSpeed ? forceEntry.value : 0f) +
				front.spinGraph().disconnected.sumf(build -> {
					SpinGraph.graphContext = front.spinGraph();
					return back.spinGraph().disconnected.contains(build) ? 0 : build.getForce();
				});

				backTorque = back.spinGraph().staticTorque.sumf(forceEntry -> forceEntry.speed >= back.spinGraph().targetSpeed ? forceEntry.value : 0f) +
				back.spinGraph().dynamicTorque.sumf(forceEntry -> forceEntry.speed >= back.spinGraph().targetSpeed ? forceEntry.value : 0f) +
				back.spinGraph().disconnected.sumf(build -> {
					SpinGraph.graphContext = back.spinGraph();
					return front.spinGraph().disconnected.contains(build) ? 0 : build.getForce();
				});

				frontFriction = front.spinGraph().staticFriction + front.spinGraph().dynamicFriction +
				front.spinGraph().disconnected.sumf(build -> {
					SpinGraph.graphContext = front.spinGraph();
					return back.spinGraph().disconnected.contains(build) ? 0 : build.getResistance();
				});
				backFriction = back.spinGraph().staticFriction + back.spinGraph().dynamicFriction +
				back.spinGraph().disconnected.sumf(build -> {
					SpinGraph.graphContext = back.spinGraph();
					return front.spinGraph().disconnected.contains(build) ? 0 : build.getResistance();
				});

				shouldConnect = (frontTorque * front.getRatio() + backTorque * back.getRatio()) - (frontFriction * front.getRatio() + backFriction * back.getRatio()) > 0 && !connectedToFront.contains(back.spinGraph());

				if (shouldConnect) {

				}
			}
		}

		@Override
		public void onGraphUpdate() {
			super.onGraphUpdate();
		}
	}
}

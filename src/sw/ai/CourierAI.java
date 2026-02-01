package sw.ai;

import mindustry.entities.units.*;
import mindustry.gen.*;
import sw.world.blocks.payloads.*;

public class CourierAI extends AIController {
	@Override
	public void updateMovement() {
		if (
			!(unit instanceof BuildingTetherc tether) ||
			!(tether.building() instanceof PayloadCourierPort.PayloadCourierPortBuild port)
		) return;

		if (port.getLink() == null) {
			port.removeCourier();
			return;
		}

		moveTo(port.getLink(), 1f, 10f);

		if (
			unit.within(port.getLink(), 2f) &&
			port.getLink().payload == null &&
			unit instanceof Payloadc carrier
		) {
			port.getLink().handlePayload(port.getLink(), carrier.payloads().peek());
			port.getLink().launchers.remove(port);
//				Call.unitDespawn(unit);
			// TODO make a fix in case syncing issues appear with this thing
			port.removeCourier();
		}
//		} else Call.unitDespawn(unit);
	}
}

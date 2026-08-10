package sw.ai;

import mindustry.entities.units.*;
import mindustry.gen.*;
import sw.net.*;
import sw.world.blocks.payloads.*;

public class CourierAI extends AIController {
	public static float smooth = 100;

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

		moveTo(port.getLink(), 1f, smooth);

		if (
			unit.within(port.getLink(), 4f) &&
			port.getLink().payload == null &&
			unit instanceof Payloadc carrier &&
			!carrier.payloads().isEmpty()
		) {
			port.getLink().handlePayload(port.getLink(), carrier.payloads().peek());
			port.getLink().launchers.remove(port);
			CourierRemovePacket.call(port.getLink().tile, unit);
		}
	}
}

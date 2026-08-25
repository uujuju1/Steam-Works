package sw.net;

import mindustry.net.*;

public class SWPackets {
	public static void load() {
		Net.registerPacket(CourierSpawnPacket::new);
		Net.registerPacket(CourierRemovePacket::new);
	}
}

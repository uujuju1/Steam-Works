package sw.net;

import arc.util.io.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.io.*;
import mindustry.net.*;
import mindustry.world.*;
import sw.world.blocks.payloads.*;

public class CourierRemovePacket extends Packet {
	private byte[] DATA;
	public Tile tile;
	public Unit unit;

	public CourierRemovePacket() {
		this.DATA = NODATA;
	}

	public boolean allow(boolean server) {
		return !server;
	}

	public void handleClient() {
		PayloadCourierPort.removeCourier(tile, unit);
	}

	public void handled() {
		BAIS.setBytes(DATA);
		tile = TypeIO.readTile(READ);
		unit = TypeIO.readUnit(READ);
	}

	public void read(Reads READ, int LENGTH) {
		DATA = READ.b(LENGTH);
	}

	public void write(Writes WRITE) {
		TypeIO.writeTile(WRITE, tile);
		TypeIO.writeUnit(WRITE, unit);
	}

	public static void call(Tile tile, Unit unit) {
		if (Vars.net.server() || !Vars.net.active()) {
			PayloadCourierPort.removeCourier(tile, unit);
		}

		if (Vars.net.server()) {
			CourierRemovePacket packet = new CourierRemovePacket();
			packet.tile = tile;
			packet.unit = unit;
			Vars.net.send(packet, true);
		}
	}
}

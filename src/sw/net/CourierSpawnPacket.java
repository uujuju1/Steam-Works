package sw.net;

import arc.util.io.*;
import mindustry.*;
import mindustry.io.*;
import mindustry.net.*;
import mindustry.world.*;
import sw.world.blocks.payloads.*;

public class CourierSpawnPacket extends Packet {
	private byte[] DATA;
	public Tile tile;
	public int id;

	public CourierSpawnPacket() {
		this.DATA = NODATA;
	}

	public boolean allow(boolean server) {
		return !server;
	}

	public void handleClient() {
		PayloadCourierPort.createCourier(tile, id);
	}

	public void handled() {
		BAIS.setBytes(DATA);
		tile = TypeIO.readTile(READ);
		id = READ.i();
	}

	public void read(Reads READ, int LENGTH) {
		DATA = READ.b(LENGTH);
	}

	public void write(Writes WRITE) {
		TypeIO.writeTile(WRITE, tile);
		WRITE.i(id);
	}

	public static void call(Tile tile, int id) {
		if (Vars.net.server() || !Vars.net.active()) {
			PayloadCourierPort.createCourier(tile, id);
		}

		if (Vars.net.server()) {
			CourierSpawnPacket packet = new CourierSpawnPacket();
			packet.tile = tile;
			packet.id = id;
			Vars.net.send(packet, true);
		}
	}
}

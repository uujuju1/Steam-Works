package sw.world.blocks.payloads;

import mindustry.gen.*;
import mindustry.world.blocks.payloads.*;

public class SWPayloadConveyor extends PayloadConveyor {
	public SWPayloadConveyor(String name) {
		super(name);
	}

	public class SWPayloadConveyorBuild extends PayloadConveyorBuild {
		@Override
		public boolean acceptPayload(Building source, Payload payload){
			return this.item == null
				&& payload.fits(payloadLimit)
				&& (source == this || this.enabled);
		}
	}
}

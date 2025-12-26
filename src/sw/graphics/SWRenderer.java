package sw.graphics;

import arc.*;
import arc.graphics.*;
import arc.graphics.Texture.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import arc.struct.*;
import mindustry.*;
import mindustry.game.EventType.*;
import sw.world.blocks.environment.*;

public class SWRenderer {
	ObjectMap<String, FrameBuffer> buffers = new ObjectMap<>();

	public void init() {
		buffers.put("pitfall", new FrameBuffer());
		buffers.put("spinFragment", new FrameBuffer());
		
		Events.on(WorldLoadEvent.class, e -> {
			FrameBuffer frameBuffer = buffers.get("pitfall");
			frameBuffer.resize(Vars.world.unitWidth(), Vars.world.unitHeight());
			frameBuffer.begin(Color.clear);

			Draw.proj().setOrtho(0, 0, frameBuffer.getWidth(), frameBuffer.getHeight());

			Vars.world.tiles.eachTile(tile -> {
				if (tile.floor() instanceof Pitfall floor) {
					Draw.color(floor.maskColor);
					Fill.square(tile.worldx(), tile.worldy(), Vars.tilesize/2f);
				}
			});

			frameBuffer.end();
			frameBuffer.getTexture().setFilter(TextureFilter.nearest);
		});
		Events.on(ResizeEvent.class, e -> {
			buffers.get("spinFragment").resize(Core.graphics.getWidth(), Core.graphics.getHeight());
		});
		
		Events.on(DisposeEvent.class, e -> buffers.values().toSeq().each(FrameBuffer::dispose));
	}
	
	public FrameBuffer getBuffer(String name) {
		return buffers.get(name);
	}
	
	public Texture getPitfallTexture() {
		return buffers.get("pitfall").getTexture();
	}
}

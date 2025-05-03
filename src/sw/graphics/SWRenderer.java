package sw.graphics;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import mindustry.*;
import mindustry.game.*;
import sw.world.blocks.environment.*;

public class SWRenderer {
	public Texture pitfall;

	public FrameBuffer frameBuffer = new FrameBuffer();

	public void init() {
		Events.on(EventType.WorldLoadEvent.class, e -> {
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
			pitfall = frameBuffer.getTexture();
		});
	}
}

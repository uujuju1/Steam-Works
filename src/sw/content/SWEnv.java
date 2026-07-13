package sw.content;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import mindustry.*;
import mindustry.graphics.*;
import mindustry.type.*;

public class SWEnv {
	public static final int
	gasPocket = 1 << 8;

	public static void loadRenderers() {
		Vars.renderer.addEnvRenderer(gasPocket, () -> {
			Texture tex = Core.assets.get("sprites/distortAlpha.png", Texture.class);

			Draw.z(Layer.weather - 1);
			Weather.drawNoiseLayers(tex, Color.valueOf("E3D8B6"), 1000f, 0.24f, 0.4f, 1f, 1f, 0f, 4, -1.3f, 0.7f, 0.8f, 0.9f);
			Draw.reset();
		});
	}
}

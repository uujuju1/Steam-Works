package sw.graphics;

import arc.*;
import arc.graphics.gl.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.*;

public class SWShaders {
	public static UIShader sectorDialogBackground, techtreeBackground;

	public static void load() {
		sectorDialogBackground = new UIShader("sectorDialogBackground");
		techtreeBackground = new UIShader("techtreeBackground");
	}

	public static class UIShader extends Shader {
		public Vec2 pos = new Vec2();
		public float alpha = 1;

		public UIShader(String frag) {
			super(Core.files.internal("shaders/screenspace.vert"), Vars.tree.get("shaders/" + frag + ".frag"));
		}

		@Override
		public void apply() {
			setUniformf("u_resolution", Core.graphics.getWidth(), Core.graphics.getHeight());
			setUniformf("u_position", pos.x, pos.y);
			setUniformf("u_opacity", alpha);
			setUniformf("u_time", Time.globalTime);
		}
	}
}

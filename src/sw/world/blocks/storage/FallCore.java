package sw.world.blocks.storage;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.graphics.*;
import mindustry.world.blocks.storage.*;

public class FallCore extends CoreBlock {
	public FallCore(String name) {
		super(name);
	}

	public class FallCoreBuild extends CoreBuild {
		@Override
		public void drawLaunch() {
			if (Vars.renderer.isLaunching()) {
				return;
			}

			float fin = Vars.renderer.getLandTimeIn();
			float fout = 1f - fin;

			float scl = Scl.scl(4f) / Vars.renderer.getDisplayScale();
			float rotation = Interp.pow2In.apply(fout) * 135f;

			Draw.scl(scl);

			Drawf.spinSprite(region, x, y, rotation);

			Draw.blend();

			Draw.reset();
		}

		@Override
		public void endLaunch() {
			if (!Vars.renderer.isLaunching())tile.getLinkedTiles(t -> Fx.coreLandDust.at(t.worldx(), t.worldy(), angleTo(t.worldx(), t.worldy()) + Mathf.range(30f), Tmp.c1.set(t.floor().mapColor).mul(1.5f + Mathf.range(0.15f))));
		}

		@Override
		public void updateLaunch() {}

		@Override
		public float zoomLaunch() {
			return Vars.renderer.isLaunching() ? Vars.renderer.camerascale : super.zoomLaunch();
		}
	}
}

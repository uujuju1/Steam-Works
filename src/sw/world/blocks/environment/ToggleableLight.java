package sw.world.blocks.environment;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import sw.annotations.Annotations.*;

public class ToggleableLight extends Block {
	public @Load("@name$-top") TextureRegion topRegion;

	public ToggleableLight(String name) {
		super(name);
		buildVisibility = BuildVisibility.editorOnly;
		configurable = true;
		emitLight = true;
		update = true;

		config(Integer.class, (TogglableLightBuild build, Integer i) -> build.link = i);
	}

	public boolean accessible() {
		return buildVisibility.visible() || Vars.state.playtestingMap != null;
	}

	@Override public boolean canBreak(Tile tile) {
		return accessible();
	}

	@Override public boolean checkForceDark(Tile tile) {
		return accessible();
	}

	@Override
	public void init() {
		super.init();

		updateClipRadius(lightRadius);
	}

	public class TogglableLightBuild extends Building {
		public int link = -1;

		@Override public boolean collide(Bullet other) {
			return false;
		}

		@Override public Integer config() {
			return link;
		}

		@Override public void damage(float damage) {}

		@Override
		public void draw() {
			Draw.rect(region, x, y);

			if (getLink() != null) {
				Draw.alpha(getLink().warmup());
				Draw.rect(topRegion, x, y);
			}
		}

		@Override
		public void drawConfigure() {
			float sin = Mathf.absin(10, 4f);

			if (getLink() != null) {
				float angle = angleTo(getLink());
				Tmp.v1.trns(angle + 180f, size * Vars.tilesize * Mathf.sqrt2 / 2f + sin).add(x, y);
				Tmp.v2.trns(angle, getLink().block.size * Vars.tilesize * Mathf.sqrt2 / 2f + sin).add(getLink().x, getLink().y);
				Drawf.line(Pal.accent, Tmp.v1.x, Tmp.v1.y, Tmp.v2.x, Tmp.v2.y);
			}

			Drawf.circles(x, y, size * Vars.tilesize * Mathf.sqrt2 / 2f + sin);

			if (getLink() != null) {
				Drawf.circles(getLink().x, getLink().y, getLink().block.size * Vars.tilesize * Mathf.sqrt2 / 2f + sin);
			}
		}

		@Override
		public void drawLight() {
			if (getLink() != null) Drawf.light(this, lightRadius * getLink().warmup() * getLink().efficiency, lightColor, lightColor.a);
		}

		public @Nullable Building getLink() {
			return Vars.world.build(link);
		}

		@Override
		public boolean onConfigureBuildTapped(Building other) {
			if (other == getLink()) {
				configure(-1);
				return false;
			}
			if (other != this) {
				configure(other.pos());
				return false;
			}
			return true;
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);

			link = read.i();
		}

		@Override
		public void write(Writes write) {
			super.write(write);

			write.i(link);
		}
	}
}

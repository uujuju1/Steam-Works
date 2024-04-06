package sw.world.blocks.units;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;

public class WaveRadar extends Block {
	public TextureRegion top;

	public WaveRadar(String name) {
		super(name);
		solid = destructible = true;
		configurable = true;
	}

	@Override protected TextureRegion[] icons() {
		return new TextureRegion[]{region, top};
	}

	@Override
	public void load() {
		super.load();
		top = Core.atlas.find(name + "-top");
	}

	public class WaveRadarBuild extends Building {
		public float warmup = 0;

		@Override
		public void draw() {
			super.draw();
			Draw.rect(top, x, y, Time.time + id * 45f);
			drawRadar(Vars.state.wave - 1);
			warmup = Mathf.approachDelta(warmup, 0, 0.03f);
		}
		@Override public void drawConfigure() {
			warmup = Mathf.approachDelta(warmup, 1, 0.06f);
		}
		public void drawRadar(int wave) {
			Draw.z(Layer.blockOver);
			Lines.stroke(2f * warmup, Pal.accent);
			Lines.circle(x, y, size * 8f * warmup());

			Vars.state.rules.spawns.each(s -> s.getSpawned(wave) > 0, spawnGroup -> {
				if (spawnGroup.spawn != -1) {
					Point2 pos = Point2.unpack(spawnGroup.spawn);
					Tmp.v1.trns(Angles.angle(x, y, pos.x * 8f, pos.y * 8f), size * 8f * warmup());
					Drawf.tri(Tmp.v1.x + x, Tmp.v1.y + y, 4f * warmup(), 4f * spawnGroup.getSpawned(wave), Angles.angle(x, y, pos.x * 8, pos.y * 8));
				} else {
					Vars.spawner.getSpawns().each(spawn -> {
						Point2 pos = Point2.unpack(spawn.pos());
						Tmp.v1.trns(Angles.angle(x, y, pos.x * 8f, pos.y * 8f), size * 8f * warmup());
						Drawf.tri(Tmp.v1.x + x, Tmp.v1.y + y, 4f * warmup(), 4f * spawnGroup.getSpawned(wave), Angles.angle(x, y, pos.x * 8, pos.y * 8));
					});
				}
			});
		}

		@Override public float warmup() {
			return Interp.circle.apply(warmup);
		}
	}
}

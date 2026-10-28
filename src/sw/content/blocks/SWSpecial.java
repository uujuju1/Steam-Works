package sw.content.blocks;

import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import mindustry.entities.effect.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;
import sw.content.*;
import sw.entities.effect.*;
import sw.world.blocks.environment.*;
import sw.world.blocks.production.*;
import sw.world.draw.*;

import static mindustry.type.ItemStack.*;

public class SWSpecial {
	public static Block voidTile, voidTileWall, spikes;

	public static Block steamInlet;

	public static Block toggleableLight;

	public static void load() {
		voidTile = new Floor("void-tile", 0);
		voidTileWall =  new StaticWall("void-tile-wall") {{
			variants = 0;
		}};
		spikes = new Spike("spike") {{
			variants = 0;
			variantShapes = new ObjectMap[]{
				ObjectMap.of(
					new Polygon(new float[] {-3.5f, 2f, 3.5f, -1f, -4f, -4f, -4f, -4f}), Pal.shadow,
					new Polygon(new float[] {-3.5f, 2f, -1f, -3.5f, 3f, 9f, 3f, 9f}), Pal.gray,
					new Polygon(new float[] {-1f, -3.5f, 3.5f, -1f, 3f, 9f, 3f, 9f}), Color.gray
				),
				ObjectMap.of(
					new Polygon(new float[] {-4f, 0f, 3f, -3.5f, -8f, -8f, -8f, -8f}), Pal.shadow,
					new Polygon(new float[] {-4f, 0f, 3f, -3.5f, -12f, 12f, -12f, 12f}), Pal.gray,
					new Polygon(new float[] {4f, 1f, 3f, -3.5f, -12f, 12f, -12f, 12f}), Color.gray
				),
				ObjectMap.of(
					new Polygon(new float[] {-3.25f, 0f, 0f, -4f, -8f, -8f, -8f, -8f}), Pal.shadow,
					new Polygon(new float[] {-3.25f, 0f, 0f, 3.5f, 3f, -11f, 3f, -11f}), Pal.gray,
					new Polygon(new float[] {0f, 3.5f, 4f, 0f, 3f, -11f, 3f, -11f}), Color.gray
				),
				ObjectMap.of(
					new Polygon(new float[] {4f, -1f, -3f, 2.5f, -24f, -24f, -24f, -24f}), Pal.shadow,
					new Polygon(new float[] {4f, -1f, 2f, 4f, -12f, -15f, -12f, -15f}), Pal.gray,
					new Polygon(new float[] {-3f, 2.5f, 2f, 4f, -12f, -15f, -12f, -15f}), Color.gray
				)
			};
		}};

		steamInlet = new SWGenericCrafter("steam-inlet") {{
			requirements(Category.crafting, BuildVisibility.editorOnly, with());
			size = 3;
			rotate = true;
			quickRotate = false;
			targetable = false;

			consumeLiquid(SWLiquids.steam, 100 / 60f);

			updateEffectSpread = 0;
			updateEffect = new MultiEffect(
				new WrapEffect(SWFx.evaporate, Color.white, 8f),

				new ParticlePillarEffect() {{
					x = -8f;
					y = 5f;
					radius = 2f;

					alpha = 0.25f;

					sizeInterp = Interp.pow2Out;

					lifetime = 120;
				}},
				new ParticlePillarEffect() {{
					x = -8f;
					y = -5f;
					radius = 2f;

					alpha = 0.25f;

					sizeInterp = Interp.pow2Out;

					lifetime = 120;
				}}
			);

			drawer = new DrawMulti(
				new DrawRegion() {{
					name = "sw-bottom-3";
				}},
				new DrawRegion("-rotator", 5f),
				new DrawRotated(),
				new DrawLight(Color.valueOf("6367BF7f"), 20f * 8f)
			);
		}
			@Override
			public boolean canBreak(Tile tile) {
				return buildVisibility.visible();
			}
		};

		toggleableLight = new ToggleableLight("toggleable-light") {{
			lightColor = Pal.lancerLaser.cpy().a(0.5f);
			lightRadius = 40 * 8f;
		}};
	}
}

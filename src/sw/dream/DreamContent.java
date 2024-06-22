package sw.dream;

import arc.graphics.*;
import arc.math.geom.*;
import arc.struct.*;
import ent.anno.Annotations.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.blocks.storage.*;
import sw.gen.*;
import sw.world.blocks.environment.*;

public class DreamContent {
	public static @EntityDef (value = {Mechc.class, Unitc.class}, serialize = false) UnitType brutalistUnit;
	public static Block
		concrete, concreteWall,
		voidTile, voidTileWall,
		spikes,
		brutalistCore, lore;

	public static void load() {
		brutalistUnit = new UnitType("brutalist-unit") {{
			constructor = UnitMech::create;
			drawCell = false;
			speed = 0.55f;
			lightRadius = 20f;
			mineTier = 1;
			mineSpeed = 0.5f;
			mineRange = 20f;
			mechFrontSway = mechSideSway = 0;
		}};

		concrete = new Floor("concrete", 0);
		concreteWall = new StaticWall("concrete-wall") {{
			variants = 0;
		}};
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

		brutalistCore = new CoreBlock("brutalist-core") {{
			unitType = brutalistUnit;
			size = 2;
			lightRadius = 50f;
			drawTeamOverlay = solid = false;
		}};
	}
}

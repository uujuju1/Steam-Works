package sw.content.blocks;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import mindustry.content.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.meta.*;
import sw.content.*;
import sw.graphics.*;
import sw.world.blocks.environment.*;
import sw.world.meta.*;

import java.util.*;

public class SWEnvironment {
	public static Block
	oreVerdigris, oreIron, fissure, oreGraphite, collapsedSilicon,

	souesite, fissuredSouesite, souesiteCrater, largeSouesiteCrater, souesiteWall, souesiteBoulder,
	souesiteShallowerSolvent, souesiteShallowSolvent,
	agedSouesite, agedSouesiteWall,

	concrete, overrunConcrete, smoothConcrete, concreteStripes, concreteWall,
	slagPlug,
	pitfall, gratingPitfall, waterfall, chasm,

	tuff, shapedTuff, tuffWall, columnarTuff, tuffConcretion, tuffPile,
	tuffShallowerSolvent, tuffShallowSolvent,

	solvent,
	solventCrystal,

	plateWall, agedPlateWall,
	truss, beam,
	plate, waterloggedPlate, agedPlate, plateCross, plateVent;

	public static void load() {
		// region ores
		oreVerdigris = new OreBlock(SWItems.verdigris) {{
			variants = 4;
		}};
		oreIron = new OreBlock(SWItems.iron) {{
			variants = 4;
		}};
		fissure = new OreBlock("fissure", Items.sand) {{
			wallOre = true;
			variants = 4;
		}};
		oreGraphite = new OreBlock(Items.graphite) {{
			wallOre = true;
			variants = 4;
		}};

		collapsedSilicon = new StaticWall("collapsed-silicon") {{
			itemDrop = Items.silicon;
		}};
		// endregion

		//region souesite
		souesiteWall = new StaticWall("souesite-wall");
		souesiteBoulder = new Prop("souesite-boulder") {{
			variants = 2;
		}};
		souesiteCrater = new Floor("souesite-crater", 2) {{
			wall = souesiteWall;
			decoration = souesiteBoulder;

			attributes.set(Attribute.water, 2f);
		}};
		largeSouesiteCrater = new SteamVent("large-souesite-crater") {{
			variants = 0;
			effect = Fx.none;

			attributes.set(Attribute.water, 2f);
		}};
		souesite = new Floor("souesite", 4) {{
			wall = souesiteWall;
			decoration = souesiteBoulder;
			((SteamVent) largeSouesiteCrater).parent = this;
			((SteamVent) largeSouesiteCrater).blendGroup = this;

			attributes.set(Attribute.water, 1f);
		}};
		fissuredSouesite = new Floor("fissured-souesite", 4) {{
			wall = souesiteWall;
			decoration = souesiteBoulder;

			attributes.set(Attribute.water, 1.5f);
		}};

		agedSouesiteWall = new StaticWall("aged-souesite-wall");
		agedSouesite = new Floor("aged-souesite") {{
			wall = agedSouesiteWall;
		}};

		souesiteShallowerSolvent = new Floor("souesite-shallower-solvent", 2) {{
			wall = souesiteWall;
			cacheLayer = CacheLayer.water;
			isLiquid = true;
			liquidDrop = SWLiquids.solvent;
		}};
		souesiteShallowSolvent = new Floor("souesite-shallow-solvent", 2) {{
			wall = souesiteWall;
			cacheLayer = CacheLayer.water;
			isLiquid = true;
			liquidDrop = SWLiquids.solvent;
		}};
		//endregion

		//region concrete
		concreteWall = new StaticWall("concrete-wall");
		concrete = new Floor("concrete", 4) {{
			wall = concreteWall;
		}};
		overrunConcrete = new Floor("overrun-concrete", 4) {{
			wall = concreteWall;
		}};
		smoothConcrete = new Floor("smooth-concrete", 4) {{
			wall = concreteWall;
		}};
		concreteStripes = new Floor("concrete-stripes", 4) {{
			wall = concreteWall;
		}};

		slagPlug = new SteamVent("slag-plug") {{
			variants = 0;
			effect = Fx.none;
			
			blendGroup = concreteStripes;

			attributes.set(Attribute.heat, 1f);
		}};

		pitfall = new Pitfall("pitfall") {{
			cacheLayer = SWShaders.pitfallLayer;
			blendGroup = concrete;

			mapColor = Color.valueOf("71727d").lerp(Color.black, 0.5f);
		}};
		gratingPitfall = new Pitfall("pitfall-grating") {{
			maskColor = SWPal.pitfallGratingMask;
			cacheLayer = SWShaders.pitfallLayer;
			blendGroup = concrete;

			mapColor = Color.valueOf("71727d").lerp(Color.black, 0.5f);
		}};
		waterfall = new Pitfall("waterfall") {{
			maskColor = SWPal.waterfallMask;
			cacheLayer = SWShaders.pitfallLayer;
			blendGroup = concrete;
			
			attributes.set(SWAttribute.gravity, 1f);

			mapColor = Color.valueOf("71727d").lerp(Pal.water, 0.2f).lerp(Color.black, 0.5f);
		}};
		chasm = new Pitfall("chasm") {{
			maskColor = SWPal.chasmMask;
			cacheLayer = SWShaders.pitfallLayer;
			blendGroup = concrete;

			mapColor = Color.valueOf("71727d").lerp(Pal.turretHeat, 0.25f).lerp(Color.black, 0.5f);
		}};
		//endregion

		//region tuff
		tuffWall = new StaticWall("tuff-wall");
		tuffPile = new Prop("tuff-pile") {{
			variants = 2;
		}};
		columnarTuff = new TallBlock("columnar-tuff") {{
			variants = 2;
		}};
		tuffConcretion = new OverlayFloor("tuff-concretion") {{
			variants = 4;
		}};
		tuff = new Floor("tuff", 4) {{
			wall = tuffWall;
			decoration = tuffPile;
		}};
		shapedTuff = new Floor("shaped-tuff", 4) {{
			wall = tuffWall;
			decoration = tuffPile;
		}};

		tuffShallowerSolvent = new Floor("tuff-shallower-solvent", 2) {{
			wall = tuffWall;
			cacheLayer = CacheLayer.water;
			isLiquid = true;
			liquidDrop = SWLiquids.solvent;
		}};
		tuffShallowSolvent = new Floor("tuff-shallow-solvent", 2) {{
			wall = tuffWall;
			cacheLayer = CacheLayer.water;
			isLiquid = true;
			liquidDrop = SWLiquids.solvent;
		}};
		//endregion

		//region solvent
		solventCrystal = new TallBlock("solvent-crystal") {{
			variants = 2;
		}};
		solvent = new Floor("solvent-regular", 0) {{
			cacheLayer = CacheLayer.water;
			isLiquid = true;
			liquidDrop = SWLiquids.solvent;
		}};
		//endregion

		/*

		// region glacier
		glacierWall = new StaticWall("glacier-wall");
		glacier = new Floor("glacier", 4) {{
			wall = glacierWall;
		}};
		glacierCrater = new Floor("glacier-crater", 2) {{
			wall = glacierWall;
		}};
		weatheredGlacier = new Floor("weathered-glacier", 4) {{
			wall = glacierWall;
		}};
		// endregion

		// region blue ice
		blueSolventIceWall = new StaticWall("blue-solvent-ice-wall");
		blueSolventIce = new Floor("blue-solvent-ice", 4) {{
			wall = blueSolventIceWall;
		}};
		blueSolventIceCrater = new Floor("blue-solvent-ice-crater", 2) {{
			wall = blueSolventIceWall;
		}};
		crystallineBlue = new Floor("crystalline-blue", 4) {{
			wall = blueSolventIceWall;
		}};
		// endregion

		//region erosion
		erosionWall = new StaticWall("erosion-wall");
		erosionBoulder = new Prop("erosion-boulder") {{
			variants = 2;
		}};
		erosion = new Floor("erosion", 4) {{
			wall = erosionWall;
			decoration = erosionBoulder;
		}};
		erosionCrater = new Floor("erosion-crater", 2) {{
			wall = erosionWall;
			decoration = erosionCrater;
		}};
		//endregion

		//region melt stone
		meltStoneWall = new StaticWall("melt-stone-wall");
		meltStoneBoulder = new Prop("melt-stone-boulder") {{
			variants = 2;
		}};
		meltStone = new Floor("melt-stone", 4) {{
			wall = meltStoneWall;
			decoration = meltStoneBoulder;
		}};
		meltPebbles = new Floor("melt-pebbles", 4) {{
			wall = meltStoneWall;
			decoration = meltStoneBoulder;
		}};
		//endregion

		*/

		// region plate
		plateWall = new StaticWall("plate-wall");
		agedPlateWall = new StaticWall("aged-plate-wall");
		truss = new SwayingBeam("truss") {{
			baseRotation = -60f;
			rotationOffset = 30f;
			swayScale = 4 * Mathf.pi * 60 * 4;
			swayMagnitude = 30f;

			clipSize = 64f;
		}};
		beam = new SwayingBeam("beam") {{
			baseRotation = -67.5f;
			rotationOffset = 22.5f;
			swayScale = 4 * Mathf.pi * 60 * 6;
			swayMagnitude = 22.5f;

			clipSize = 64f;
		}};
		plate = new Floor("plate", 4) {{
			wall = plateWall;
		}};
		waterloggedPlate = new Floor("waterlogged-plate", 4) {{
			wall = plateWall;
			cacheLayer = CacheLayer.water;
			
			attributes.set(Attribute.water, 1f);
		}
			@Override
			public void drawBase(Tile tile) {
				Mathf.rand.setSeed(tile.pos());
				Draw.rect(variantRegions[variant(tile.x, tile.y)], tile.worldx(), tile.worldy());
				
				Draw.alpha(1f);
				drawOverlay(tile);
			}
			
			@Override
			public void drawNonLayer(Tile tile, CacheLayer layer) {
				Mathf.rand.setSeed(tile.pos());
				
				Arrays.fill(dirs, 0);
				blenders.clear();
				blended.clear();
				
				for(int i = 0; i < 8; i++){
					Point2 point = Geometry.d8[i];
					Tile other = tile.nearby(point);
					//special case: empty is, well, empty, so never draw emptiness on top, as that would just be an incorrect black texture
					if(other != null && other.floor().cacheLayer == layer && other.floor().cacheLayer != SWShaders.pitfallLayer && other.floor() != Blocks.empty){
						if(!blended.getAndSet(other.floor().id)){
							blenders.add(other.floor());
							dirs[i] = other.floorID();
						}
					}
				}
				
				drawBlended(tile, false);
			}
		};
		agedPlate = new Floor("aged-plate", 4) {{
			wall = agedPlateWall;
		}};
		plateVent = new Floor("plate-vent", 0) {{
			wall = plateWall;
			blendGroup = plate;
		}};
		plateCross = new Floor("plate-cross", 0) {{
			wall = plateWall;
			blendGroup = plate;
		}};
		// endregion
	}
}

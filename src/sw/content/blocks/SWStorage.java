package sw.content.blocks;

import arc.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.liquid.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.draw.*;
import sw.content.*;
import sw.entities.*;
import sw.graphics.*;
import sw.world.blocks.power.*;
import sw.world.blocks.storage.*;
import sw.world.draw.*;
import sw.world.meta.*;

import static mindustry.type.ItemStack.*;

public class SWStorage {
	public static Block
		compactContainer,
		liquidDistributor, liquidBasin,
		spring,
		coreScaffold, coreMole;

	public static void load() {
		compactContainer = new StorageBlock("compact-container") {{
			requirements(Category.effect, with(
				SWItems.verdigris, 40,
				SWItems.iron, 50,
				Items.silicon, 30
			));
			size = 2;
			health = 500;
			itemCapacity = 50;
			coreMerge = false;
		}};

		liquidDistributor = new LiquidRouter("liquid-distributor") {{
			requirements(Category.liquid, with(
				SWItems.iron, 15,
				SWItems.verdigris, 10
			));
			size = 2;
			health = 250;
			solid = true;
			liquidCapacity = 120 * 6f;
		}};
		liquidBasin = new LiquidRouter("liquid-basin") {{
			requirements(Category.liquid, with(
				SWItems.iron, 50,
				SWItems.verdigris, 30
			));
			size = 3;
			health = 500;
			solid = true;
			liquidCapacity = 120 * 12f;
		}};

		spring = new RotationBattery("spring") {{
			requirements(Category.power, with(
				SWItems.aluminium, 25,
				Items.silicon, 30,
				Items.graphite, 35
			));
			size = 2;
			rotate = false;

			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawRegion("-bar"),
				new DrawAxles() {{
					Axle base = new Axle("-axle") {{
						pixelWidth = 2;
						pixelHeight = 2;

						height = 14f;
						width = 2f;

						paletteLight = SWPal.axleLight;
						paletteMedium = SWPal.axleMedium;
						paletteDark = SWPal.axleDark;
					}};

					for (int i = 0; i < 4; i++) {
						axles.add(base.position(-4.5f + 3 * i, 0f, 0f, 1f/4f * (i + 1)));
					}
				}},
				new DrawRegion()
			);

			spinConfig = new SpinConfig() {{
				allowedEdges = new int[][] {
					new int[]{},
					new int[]{},
					new int[]{},
					new int[]{}
				};
			}};
		}};

		coreScaffold = new CoreBlock("core-scaffold") {{
			requirements(Category.effect, with(
				SWItems.verdigris, 500,
				Items.graphite, 400
			));
			size = 3;
			health = 2000;
			alwaysUnlocked = true;
			unitType = SWUnitTypes.lambda;
			itemCapacity = 2000;
			unitCapModifier = 12;
		}};
		coreMole = new CogCore("core-mole") {{
			requirements(Category.effect, with(
				SWItems.verdigris, 500,
				SWItems.aluminium, 300,
				Items.graphite, 200
			));
			researchCost = with(
				SWItems.verdigris, 2000,
				SWItems.aluminium, 1000,
				Items.graphite, 1500
			);
			size = 3;
			health = 2500;
			unitType = SWUnitTypes.rho;
			itemCapacity = 4500;
			unitCapModifier = 16;
			
			landDuration = 600f;
			
			cogShowInterp = a -> Interp.swing.apply(Mathf.clamp((1f - a) * 2f));
			cogMaxOffset = 14f;
			
			cogRotateInterp = a -> Interp.swingOut.apply(Mathf.clamp(2f * (1f - a) - 1f));
			minCogRotations = 1f;
			maxCogRotations = 5f;
			
			fadeOutInterp = a -> Interp.swingOut.apply(Mathf.clamp(2f * (1f - a) - 1f));
		}};
		
		Events.on(EventType.ClientLoadEvent.class, e -> {
			putSchem(coreScaffold, Schematics.readBase64("bXNjaAF4nGNgZmBmZmDJS8xNZeBNSizOTA5OTkxLy89JYeBOSS1OLsosKMnMz2NgYGDLSUxKzSlmYIqOZWQQKC7XTc4vStUthqlmYGAEISABAKHWFQU="));
			putSchem(coreMole, Schematics.readBase64("bXNjaAF4nBXLSwqAIBhF4euDiGorrqKxK4gGZv9A8IUKDaK9p5wz/SAgJGQ0gTDvqZBOnrDYFBvFpk0Gfz+sN1VbXG4uRQCTNxf5Cn6cDFt9lO1QhSEBNu79NxEWeA=="));
		});
	}
	
	public static void putSchem(Block core, Schematic schem) {
		try {
			((ObjectMap<CoreBlock, Schematic>) Reflect.get(Schematics.class, Vars.schematics, "defaultLoadouts")).put((CoreBlock) core, schem);
			((ObjectMap<CoreBlock, Seq<Schematic>>) Reflect.get(Schematics.class, Vars.schematics, "loadouts")).get((CoreBlock) core, Seq::new).add(schem);
		} catch(Exception e) {
			throw new RuntimeException("you messed up", e);
		}
	}
}

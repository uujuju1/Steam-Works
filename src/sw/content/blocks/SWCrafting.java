package sw.content.blocks;

import arc.graphics.*;
import arc.math.*;
import arc.struct.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.production.*;
import mindustry.world.draw.*;
import sw.content.*;
import sw.math.*;
import sw.world.blocks.production.*;
import sw.world.draw.*;
import sw.world.draw.DrawAxles.*;
import sw.world.interfaces.*;
import sw.world.meta.*;

import static mindustry.type.ItemStack.*;

public class SWCrafting {
	public static Block
		siliconBoiler,
		compoundSmelter, chalkSeparator,
		densePress, thermiteMixer;

	public static void load() {
		siliconBoiler = new GenericCrafter("silicon-boiler") {{
			requirements(Category.crafting, with(
				SWItems.nickel, 150,
				SWItems.iron, 120,
				Items.graphite, 80
			));
			size = 3;
			health = 240;

			craftTime = 180f;

			consumeItems(with(Items.graphite, 3, Items.sand, 3));
			outputItems = with(Items.silicon, 3);

			drawer = new DrawMulti(
				new DrawDefault(),
				new DrawFlame() {{
					flameRadius = 5f;
				}}
			);
		}};

		compoundSmelter = new SWGenericCrafter("compound-smelter") {{
			requirements(Category.crafting, with(
				SWItems.iron, 80,
				SWItems.nickel, 200,
				Items.silicon, 150,
				Items.graphite, 160
			));
			size = 3;
			health = 200;

			ambientSound = Sounds.torch;
			craftTime = 30f;
			craftEffect = SWFx.compoundCraft;

			drawer = new DrawMulti(
				new DrawDefault(),
				new DrawFlame(Color.valueOf("BEB5B2")) {{
					flameRadiusInMag = flameRadiusMag = 5f;
					flameRadius = 5;
					flameRadiusIn = 2.5f;
				}}
			);

			consumeItems(with(
				Items.silicon, 1,
				SWItems.nickel, 2
			));
			consumeLiquid(SWLiquids.solvent, 0.1f);

			outputItem = new ItemStack(SWItems.compound, 1);
		}};
		chalkSeparator = new SWGenericCrafter("chalk-separator") {{
			requirements(Category.crafting, with(
				SWItems.iron, 50,
				SWItems.compound, 35,
				Items.silicon, 30
			));
			size = 2;
			health = 160;
			
			rotate = true;

			spinConfig = new SpinConfig() {{
				connections = new Seq[]{
					BlockGeometry.sides21,
					BlockGeometry.sides22,
					BlockGeometry.sides23,
					BlockGeometry.sides24
				};
			}};

			drawer = new DrawMulti(
				new DrawAxles(
					new Axle("-shaft") {{
						pixelWidth = 64;
						pixelHeight = 7;

						y = 4;

						width = 16f;
						height = 3.5f;
					}},
					new Axle("-shaft") {{
						pixelWidth = 64;
						pixelHeight = 7;

						y = -4;

						width = 16f;
						height = 3.5f;
					}}
				) {{
					rotationOverride = b -> ((HasSpin) b).spinGraph().rotation * ((HasSpin) b).spinSection().ratio;
				}},
				new DrawRegion("-bottom"),
				new DrawBitmask("-tiles", b -> 0) {{
					tileWidth = tileHeight = 64;
				}}
			);

			craftTime = 120f;
			consumeItems(with(
				Items.sand, 1,
				SWItems.compound, 2
			));
			consumeSpin(2f, 4f, a -> Interp.sine.apply(Interp.slope.apply(a)));

			outputItem = new ItemStack(SWItems.chalk, 2);
		}};

		densePress = new SWGenericCrafter("dense-press") {{
			requirements(Category.crafting, with(
				SWItems.iron, 160,
				SWItems.nickel, 200,
				Items.silicon, 80,
				Items.graphite, 160
			));
			size = 3;
			health = 200;

			ambientSound = Sounds.grinding;
			craftTime = 60f;
			craftEffect = SWFx.denseAlloyCraft;
			craftSound = Sounds.dullExplosion;
			craftSoundVolume = 0.1f;

			consumeItems(with(
				Items.silicon, 2,
				SWItems.iron, 2
			));
			consumeLiquid(SWLiquids.solvent, 0.1f);

			outputItem = new ItemStack(SWItems.denseAlloy, 2);
		}};
		thermiteMixer = new SWGenericCrafter("thermite-mixer") {{
			requirements(Category.crafting, with(
				SWItems.iron, 50,
				SWItems.denseAlloy, 35,
				Items.silicon, 30
			));
			size = 2;
			health = 160;

			rotate = true;

			spinConfig = new SpinConfig() {{
				connections = new Seq[]{
					BlockGeometry.sides21,
					BlockGeometry.sides22,
					BlockGeometry.sides23,
					BlockGeometry.sides24
				};
			}};

			consumeItem(SWItems.denseAlloy, 1);
			consumeSpin(6f, 18f, a -> Interp.sine.apply(Interp.slope.apply(a)));

			drawer = new DrawMulti(
				new DrawAxles(
					new Axle("-shaft") {{
						pixelWidth = 64;
						pixelHeight = 7;

						y = 4;

						width = 16f;
						height = 3.5f;
					}},
					new Axle("-shaft") {{
						pixelWidth = 64;
						pixelHeight = 7;

						y = -4;

						width = 16f;
						height = 3.5f;
					}}
				) {{
					rotationOverride = b -> ((HasSpin) b).spinGraph().rotation * ((HasSpin) b).spinSection().ratio;
				}},
				new DrawRegion("-bottom"),
				new DrawRegion("-rotator") {{
					spinSprite = true;
					rotateSpeed = 3f;
				}},
				new DrawBitmask("-tiles", b -> 0) {{
					tileWidth = tileHeight = 64;
				}}
			);

			outputItems = with(SWItems.thermite, 1);
		}};
	}
}

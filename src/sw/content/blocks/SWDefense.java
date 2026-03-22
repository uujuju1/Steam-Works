package sw.content.blocks;

import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.*;
import mindustry.world.draw.*;
import sw.content.*;
import sw.world.blocks.defense.*;
import sw.world.blocks.power.*;
import sw.world.consumers.*;
import sw.world.draw.*;
import sw.world.interfaces.*;
import sw.world.meta.*;

import static mindustry.type.ItemStack.*;

public class SWDefense {
	public static Block
		repairStation,
		grindLamp, lavaLamp, lamparine,
		ironWall, ironWallLarge, bloomWall, bloomWallLarge;

	public static void load() {
		//region projectors
		repairStation = new PartialRegenProjector("repair-station") {{
			requirements(Category.effect, with(
				SWItems.iron, 30,
				SWItems.aluminium, 50,
				Items.silicon, 25,
				Items.graphite, 35
			));

			size = 2;
			rotate = true;
			drawArrow = false;

			regenAmount = 10f;
			regenPercentage = 0.01f;

			healEffect = SWFx.healEmber;

			consumeItem(SWItems.verdigris, 1);
			consume(new ConsumeSpin() {{
				minSpeed = 5f / 10f;
				maxSpeed = 15f / 10f;

				efficiencyScale = Interp.one;
			}});

			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawArcSmelt() {{
					flameColor = Pal.missileYellow;
					drawCenter = false;

					particles = 50;
					particleLife = 240;
					particleLen = 0.5f;
					particleStroke = 0.5f;

					blending = Blending.normal;
					alpha = 1;
				}},
				new DrawParticles() {{
					particleSizeInterp = a -> Interp.circle.apply(Interp.slope.apply(a));
					particleSize = 2;
					particleLife = 120f;

					blending = Blending.additive;
				}},
				new DrawAxles() {{
					for (Point2 offset : Geometry.d8edge) {
						axles.add(Axles.halfBlock.position(6f * offset.x, 4f * offset.y, 0, 1f));
					}
					rotationOverride = b -> ((HasSpin) b).getRotation();
				}},
				new DrawFacingLightRegion(),
				new DrawGlowRegion(true) {{
					color = Color.scarlet;
					glowIntensity = 0.2f;
					alpha = 1f;
				}}
			);

			spinConfig = new SpinConfig() {{
				resistance = 2.5f / 600f;

				allowedEdges = new int[][] {
					new int[] {0, 1, 4, 5},
					new int[] {2, 3, 6, 7},
					new int[] {4, 5, 0, 1},
					new int[] {6, 7, 2, 3}
				};
			}};
		}};
		//endregion

		// region lamps
		grindLamp = new SWLightBlock("grind-lamp") {{
			requirements(Category.effect, with(
				SWItems.aluminium, 20,
				SWItems.verdigris, 25,
				Items.silicon, 30
			));
			size = 2;
			
			lightRadius = 120f;
			lightOpacity = 0.5f;
			lightColor = Pal.accent;
			
			updateClipRadius(120 * 3f);
			
			consumeTime = 180f;
			consumeEffect = updateEffect = SWFx.cokeIlluminate;
			updateEffectChance = 0.1f;
			
			ambientSound = Sounds.loopCombustion;
			
			consume(new ConsumeSpin() {{
				minSpeed = 2.5f/10f;
				maxSpeed = 30f/10f;
				
				efficiencyScale = a -> Mathf.log2(a * 4f);
			}});
			consumeItem(SWItems.coke, 1);
			
			drawer = new DrawMulti(
				new DrawDefault(),
				new DrawRegion("-grinder", 2) {{
					layer = Layer.block + 0.1f;
				}},
				new DrawRegion("-top") {{
					layer = Layer.block + 0.1f;
				}}
			);
			
			spinConfig = new SpinConfig() {{
				resistance = 5f/600f;
			}};
		}};
		lavaLamp = new SWLightBlock("lava-lamp") {{
			requirements(Category.effect, with(
				SWItems.aluminium, 50,
				SWItems.iron, 30
			));
			size = 2;
			
			lightRadius = 200f;
			lightOpacity = 0.5f;
			lightColor = Pal.missileYellow.cpy().mul(0.3f);
			
			consumeLiquid(Liquids.slag, 9f/60f);
			
			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawLiquidTile(Liquids.slag, 1f),
				new DrawGlowRegion() {{
					suffix = "-light";
					color = Pal.missileYellow.cpy().mul(0.3f);
					layer = -1f;
					
					alpha = 1f;
					
					glowScale = 180f/Mathf.pi;
					glowIntensity = 0.2f;
				}},
				new DrawDefault(),
				new DrawRegion("-top")
			);
		}};
		lamparine = new SWLightBlock("lamparine") {{
			requirements(Category.effect, with(
				SWItems.aluminium, 30,
				SWItems.iron, 25,
				SWItems.verdigris, 20
			));
			size = 2;
			
			lightRadius = 200f;
			lightOpacity = 0.5f;
			
			consumeTime = 10f;
			consumeEffect = updateEffect = SWFx.cokeIlluminate;
			
			ambientSound = Sounds.loopFire;
			ambientSoundVolume = 0.1f;
			
			consumeLiquids(LiquidStack.with(
				Liquids.ozone, 0.1f / 60f,
				Liquids.hydrogen, 0.3f / 60f
			));
			
			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawArcSmelt() {{
					midColor = Pal.accent;
					flameColor = Pal.missileYellowBack;
					
					alpha = 0.9f;
					
					flameRadiusScl = 24f;
					flameRadiusMag = 2.5f;
					
					particleRad = 6f;
					particleStroke = 2f;
				}},
				new DrawParticles() {{
					blending = Blending.additive;
					color = Pal.missileYellowBack;
					reverse = true;
				}},
				new DrawDefault()
			);
		}};
		// endregion

		// region walls
		ironWall = new Wall("iron-wall") {{
			requirements(Category.defense, with(SWItems.iron, 6));
			scaledHealth = 400;
		}};
		ironWallLarge = new Wall("iron-wall-large") {{
			requirements(Category.defense, mult(ironWall.requirements, 4));
			size = 2;
			scaledHealth = 400;
		}};

		bloomWall = new Wall("bloom-wall") {{
			requirements(Category.defense, with(SWItems.bloom, 6));
			scaledHealth = 600;
		}};
		bloomWallLarge = new Wall("bloom-wall-large") {{
			requirements(Category.defense, with(SWItems.bloom, 6));
			size = 2;
			scaledHealth = 600;
		}};
		// endregion
	}
}

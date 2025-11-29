package sw.content.blocks;

import arc.graphics.*;
import arc.math.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.*;
import mindustry.world.draw.*;
import sw.content.*;
import sw.world.blocks.power.*;
import sw.world.consumers.*;
import sw.world.meta.*;

import static mindustry.type.ItemStack.*;

public class SWDefense {
	public static Block
		grindLamp, lavaLamp, lamparine,
		ironWall, ironWallLarge;

	public static void load() {
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
			
			ambientSound = Sounds.combustion;
			
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
			
			ambientSound = Sounds.fire;
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
			health = 100 * 4;
			absorbLasers = true;
		}};
		ironWallLarge = new Wall("iron-wall-large") {{
			requirements(Category.defense, mult(ironWall.requirements, 4));
			size = 2;
			health = 100 * 4 * 4;
			absorbLasers = true;
		}};
		// endregion
	}
}

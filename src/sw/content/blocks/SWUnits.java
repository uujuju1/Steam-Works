package sw.content.blocks;

import arc.math.*;
import arc.math.geom.*;
import mindustry.content.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import sw.audio.*;
import sw.content.*;
import sw.entities.*;
import sw.graphics.*;
import sw.world.blocks.units.*;
import sw.world.consumers.*;
import sw.world.draw.*;
import sw.world.meta.*;

import static mindustry.type.ItemStack.*;

public class SWUnits {
	public static Block mechanicalAssembler;

	public static void load() {
		mechanicalAssembler = new MechanicalAssembler("mechanical-assembler") {{
			requirements(Category.units, with());

			size = 2;

			itemCapacity = 30;

			stepSound = ModSounds.hitMetal;
			stepSoundPitchMax = 2f;
			stepSoundPitchMin = 0.5f;
			stepSoundVolume = 0.25f;

			progressSound = ModSounds.welding;
			progressEffect = SWFx.weld;
			progressEffectChance = 0.25f;

			armSpeed = 0.5f;
			armStartingOffset = 2f;

			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawAxles(new Axle("-axle") {{
					x = -4;
					y = 0;

					pixelWidth = 64;
					pixelHeight = 7;

					width = 16f;
					height = 3.5f;

					rotation = -90f;

					paletteLight = SWPal.axleLight;
					paletteMedium = SWPal.axleMedium;
					paletteDark = SWPal.axleDark;
				}}),
				new DrawBitmask("-tiles", u -> 0, 64),
				new DrawArm() {{
					layer = Layer.groundUnit + 1;

					armExtension = 8f;
					armOffset = (4f + 2f) * 8f/2f;
					armLength = 28f;
					armCurve = new Interp.SwingOut(1);
				}}
			);

			consume(new ConsumeRotation() {{
				startSpeed = 0.5f;
				endSpeed = 1f;
				curve = Interp.one;
			}});

			plans.add(
				new MechanicalAssemblerPlan() {{
					unit = SWUnitTypes.soar;
					requirements = new ItemStack[][]{
						with(Items.silicon, 10),
						with(SWItems.verdigris, 5),
						with(Items.silicon, 10),
						with(SWItems.verdigris, 10),
						with(Items.graphite, 15)
					};
					pos = new Vec2[]{
						new Vec2(-4, 8),
						new Vec2(4, 8),
						new Vec2(6, 0),
						new Vec2(-4, -8),
						new Vec2()
					};
					stepTime = 4f * 60f;
				}},
				new MechanicalAssemblerPlan() {{
					unit = SWUnitTypes.barrage;
					requirements = new ItemStack[][]{
						with(Items.silicon, 10),
						with(SWItems.verdigris, 5),
						with(Items.silicon, 5),
						with(SWItems.verdigris, 10),
						with(Items.graphite, 10)
					};
					pos = new Vec2[]{
						new Vec2(-4, 8),
						new Vec2(4, 8),
						new Vec2(6, 0),
						new Vec2(-4, -8),
						new Vec2()
					};
					stepTime = 6f * 60f;
				}}
			);

			spinConfig = new SpinConfig() {{
				resistance = 1f/60f;
				allowedEdges = new int[][]{
					new int[]{3, 6},
					new int[]{0, 5},
					new int[]{2, 7},
					new int[]{1, 4}
				};
			}};
		}};
	}
}

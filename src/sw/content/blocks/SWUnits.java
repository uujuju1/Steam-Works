package sw.content.blocks;

import arc.math.*;
import arc.math.geom.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import sw.audio.*;
import sw.content.*;
import sw.world.blocks.units.*;
import sw.world.consumers.*;
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

			armExtension = 8f;
			armLength = 28f;
			armBaseLength = 12f;
			armCurve = Interp.smooth;

			consume(new ConsumeRotation() {{
				startSpeed = 0.5f;
				endSpeed = 1f;
				curve = Interp.one;
			}});

			plans.add(
				new MechanicalAssembler.UnitPlan() {{
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
				new MechanicalAssembler.UnitPlan() {{
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

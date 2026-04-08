package sw.content.blocks;

import arc.math.*;
import arc.math.geom.*;
import mindustry.content.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import sw.content.*;
import sw.gen.*;
import sw.world.blocks.units.*;
import sw.world.consumers.*;
import sw.world.draw.*;
import sw.world.meta.*;

import static mindustry.type.ItemStack.*;

public class SWUnits {
	public static Block mechanicalAssembler, assemblyOutpost, assemblerArm;

	public static void load() {
		mechanicalAssembler = new MechanicalAssembler("mechanical-assembler") {{
			requirements(Category.units, with(
				SWItems.verdigris, 20,
				SWItems.iron, 30,
				Items.graphite, 25,
				Items.silicon, 35
			));

			size = 2;

			stepSound = SWSounds.hitMetal;
			stepSoundPitchMax = 2f;
			stepSoundPitchMin = 0.5f;
			stepSoundVolume = 0.25f;

			progressSound = SWSounds.welding;
			progressEffect = SWFx.weld;
			progressEffectChance = 0.25f;

			armSpeed = 0.5f;
			armStartingOffset = 2f;

			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawAxles() {{
					axles.add(Axles.doubleBlock.position(-4f, 0f, -90f, 1f));
				}},
				new DrawRotated(),
				new DrawArm() {{
					layer = Layer.groundUnit + 1;

					armExtension = 8f;
					armOffset = (4f + 2f) * 8f/2f;
					armLength = 28f;
					armCurve = new Interp.SwingOut(1);
				}}
			);

			consume(new ConsumeSpin() {{
				minSpeed = 0.4f;
				maxSpeed = 0.6f;
				efficiencyScale = Interp.one;
			}});

			plans.add(
				new MechanicalAssemblerPlan() {{
					unit = SWUnitTypes.soar;
					stepTime = 5f * 60f;
					steps.putAll(
						with(Items.silicon, 10), new Vec2(-4, 8),
						with(SWItems.verdigris, 5), new Vec2(4, 8),
						with(Items.silicon, 10), new Vec2(6, 0),
						with(SWItems.verdigris, 10), new Vec2(-4, -8),
						with(Items.graphite, 15), new Vec2()
					);
				}},
				new MechanicalAssemblerPlan() {{
					unit = SWUnitTypes.wisp;
					stepTime = 15f * 60f;
					steps.putAll(
						with(Items.silicon, 20), new Vec2(-2, 2),
						with(SWItems.aluminium, 25), new Vec2(6, 0),
						with(SWItems.coke, 15), new Vec2(0, -4)
					);
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

		assemblyOutpost = new MechanicalAssembler("assembly-outpost") {{
			requirements(Category.units, with(
				SWItems.verdigris, 50,
				SWItems.iron, 70,
				SWItems.bloom, 120,
				Items.graphite, 85,
				Items.silicon, 105
			));
			researchCost = with(
				SWItems.verdigris, 500,
				SWItems.iron, 700,
				SWItems.bloom, 120,
				Items.graphite, 850,
				Items.silicon, 1050
			);

			size = 3;

			areaSize = 7;

			stepSound = SWSounds.hitMetal;
			stepSoundPitchMax = 2f;
			stepSoundPitchMin = 0.5f;
			stepSoundVolume = 0.25f;

			progressSound = SWSounds.welding;
			progressEffect = SWFx.weld;
			progressEffectChance = 0.25f;

			armSpeed = 0.5f;
			armStartingOffset = 2f;

			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawAxles() {{
					axles.add(
						Axles.halfBlock.position(-8f, 10f, -90f, 1f),
						Axles.halfBlock.position(-8f, -10f, -90f, 1f),
						Axles.halfBlock.position(-10f, 0, 0f, 1f)
					);
				}},
				new DrawRotated(),
				new DrawArm() {{
					layer = Layer.groundUnit + 1;

					armExtension = 8f;
					armOffset = (7f + 3f) * 4f;
					armLength = 56f;
					armCurve = new Interp.SwingOut(1);
				}}
			);

			consume(new ConsumeSpin() {{
				minSpeed = 0.1f;
				maxSpeed = 0.6f;
				efficiencyScale = Interp.one;
			}});

			plans.add(
				new MechanicalAssemblerPlan() {{
					unit = SWUnitTypes.volare;
					stepTime = 20f * 60f;
					steps.putAll(
						with(SWItems.iron, 20), new Vec2(-4, 8),
						with(SWItems.verdigris, 25), new Vec2(4, 8),
						with(Items.silicon, 35), new Vec2(6, 0),
						with(SWItems.bloom, 30), new Vec2(-4, -8),
						with(Items.graphite, 25), new Vec2()
					);
				}},
				new MechanicalAssemblerPlan() {{
					unit = SWUnitTypes.ballistra;
					stepTime = 15f * 60f;
					steps.putAll(
						with(Items.silicon, 30), new Vec2(-2, 2),
						with(Items.graphite, 35), new Vec2(6, 0),
						with(SWItems.bloom, 25), new Vec2(0, -4),
						with(SWItems.aluminium, 25), new Vec2(6, 0),
						with(Items.silicon, 10), new Vec2(-2, 2)
					);
				}}
			);

			spinConfig = new SpinConfig() {{
				resistance = 50f/600f;
				allowedEdges = new int[][]{
					new int[]{6, 8, 4},
					new int[]{9, 11, 7},
					new int[]{0, 2, 10},
					new int[]{3, 5, 1}
				};
			}};
		}};

		assemblerArm = new AssemblerArm("assembler-arm") {{
			requirements(Category.units, with(
				SWItems.verdigris, 20,
				SWItems.iron, 30,
				Items.graphite, 15,
				Items.silicon, 20
			));

			size = 2;

			stepSound = SWSounds.hitMetal;
			stepSoundPitchMax = 2f;
			stepSoundPitchMin = 0.5f;
			stepSoundVolume = 0.25f;

			progressSound = SWSounds.welding;
			progressEffect = SWFx.weld;
			progressEffectChance = 0.25f;

			armSpeed = 0.5f;
			armStartingOffset = 2f;

			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawAxles() {{
					axles.add(Axles.doubleBlock.position(-4f, 0f, -90f, 1f));
				}},
				new DrawRotated(),
				new DrawArm() {{
					layer = Layer.groundUnit + 1;

					armExtension = 8f;
					armOffset = (4f + 2f) * 8f/2f;
					armLength = 28f;
					armCurve = new Interp.SwingOut(1);
				}}
			);

			consume(new ConsumeSpin() {{
				minSpeed = 0.1f;
				maxSpeed = 0.6f;
				efficiencyScale = Interp.one;
			}});

			spinConfig = new SpinConfig() {{
				resistance = 5f/600f;
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

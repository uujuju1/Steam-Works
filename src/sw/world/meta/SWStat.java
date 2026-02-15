package sw.world.meta;

import arc.math.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.meta.*;
import sw.world.blocks.units.MechanicalAssembler.*;

public class SWStat {
  // region Categories
  public static final StatCat spin = new StatCat("sw-spin");
  //endregion

  // region Stats
  public static final Stat
    spinRequirement = new Stat("sw-spin-requirement", spin),
    spinOutput = new Stat("sw-spin-output", spin),
    spinOutputForce = new Stat("sw-spin-output-force", spin),
    friction = new Stat("sw-friction", spin),
		maxFriction = new Stat("sw-max-friction", spin),
		weight = new Stat("sw-weight", spin);
  //endregion

  // region Units
	public static final StatUnit spinMinute = new StatUnit("sw-spin-minute");
	public static final StatUnit force = new StatUnit("sw-force");
	public static final StatUnit mass = new StatUnit("sw-mass");
  // endregion

	public static Table assemblerPlanRequirements(Table table, MechanicalAssemblerPlan plan, boolean detailed) {
		table.clear();

		if (detailed) {
			final int[] i2 = {0};
			plan.steps.each((req, pos) -> {
				table.table(Styles.flatOver, base -> {
					base.table(step -> {
						for(int index = 0; index < req.length; index++) {
							ItemStack stack = req[index];
							if (index % 3 == 0) {
								step.row();
								if (index + 2 > req.length - 1) {
									for(int i = 0; i < (index + 2 - (req.length - 1)); i++) table.add();
								}
							}
							step.add(StatValues.displayItem(stack.item, stack.amount, plan.stepTime, true)).left().pad(5);
						}
					}).pad(5).left();
					base.table(wreck -> {
						wreck.right();
						wreck.stack().with(stack -> {
							stack.addChild(new Image(Styles.black3));
							for(int i = 0; i <= Mathf.clamp(i2[0], 0, plan.unit.wreckRegions.length - 1); i++) {
								Image image = new Image(plan.unit.wreckRegions[i]);
								image.setScaling(Scaling.fit);
								stack.addChild(image);
							}
						}).size(40);
					}).grow().padRight(10);
				}).grow().pad(5);
				i2[0]++;
				table.row();
			});
		} else {
			ObjectIntMap<Item> res = new ObjectIntMap<>();
			plan.steps.each((req, pos) -> {
				for(ItemStack stack : req) res.increment(stack.item, 0, stack.amount);
			});
			final int[] index = {0};
			res.entries().toArray().each(stack -> {
				if (index[0] % 3 == 0) {
					table.row();
					if (index[0] + 2 > res.size - 1) for (int i = 0; i < (index[0] + 2 - (res.size - 1)); i++) table.add();
				}
				table.add(StatValues.displayItem(stack.key, stack.value, plan.stepTime * plan.steps.size, true)).left().pad(5);
				index[0]++;
			});
		}

		return table;
	}
}

package sw.world.consumers;

import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.core.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class ConsumeLiquidTrigger extends ConsumeLiquid {
	public boolean showName = true;

	public ConsumeLiquidTrigger(Liquid liquid, float amount) {
		super(liquid, amount);
	}

	@Override
	public void build(Building build, Table table){
		table.add(new ReqImage(liquid.uiIcon, () -> build.liquids.get(liquid) >= amount)).size(iconMed).top().left();
	}

	@Override
	public void display(Stats stats){
		stats.add(booster ? Stat.booster : Stat.input, stat -> {
			stat.table(t -> {
				var stack = t.stack(
					new Table(o -> {
						o.left();
						o.add(new Image(liquid.uiIcon)).size(32f).scaling(Scaling.fit);
					}),
					new Table(info -> {
						if (amount == 0) return;
						info.left().bottom();
						info.add(Strings.autoFixed(amount, 2)).style(Styles.outlineLabel);
						info.pack();
					})
				);
				if (!showName) stack.tooltip(liquid.localizedName);
				t.add((showName ? liquid.localizedName + "\n" : "") + "[lightgray]" + Strings.autoFixed(amount / (stats.timePeriod / 60f), 3) + StatUnit.perSecond.localized()).padLeft(2).padRight(5).style(Styles.outlineLabel);
			});
		});
	}

	@Override public float efficiency(Building build){return build.liquids.get(liquid) >= amount ? 1f : 0f;}

	@Override public void trigger(Building build) {build.liquids.remove(liquid, amount);}

	@Override public void update(Building build) {}
}

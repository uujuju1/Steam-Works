package sw.world.consumers;

import arc.scene.ui.layout.*;
import arc.struct.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class ConsumeLiquidBoosters extends ConsumeLiquidBase {
	public ObjectFloatMap<Liquid> boosters = new ObjectFloatMap<>();

	@Override
	public void apply(Block block){
		block.hasLiquids = true;
		content.liquids().each(liq -> boosters.containsKey(liq), item -> block.liquidFilter[item.id] = true);
	}

	@Override
	public void build(Building build, Table table){
		Seq<Liquid> list = content.liquids().select(l -> !l.isHidden() && boosters.containsKey(l));
		MultiReqImage image = new MultiReqImage();
		list.each(liquid -> image.add(new ReqImage(liquid.uiIcon, () -> getCurrentBooster(build) == liquid)));

		table.add(image).size(8 * 4);
	}

	@Override public boolean consumes(Liquid liquid) {
		return boosters.containsKey(liquid);
	}

	@Override
	public void display(Stats stats) {
		stats.add(Stat.booster, StatValues.liquidEffMultiplier(liquid -> boosters.get(liquid, 1f), amount * 60, liq -> boosters.containsKey(liq)));
	}

	public Liquid getCurrentBooster(Building build) {
		Liquid cur = boosters.keys().toSeq().max(liquid -> build.liquids.get(liquid));
		return build.liquids.get(cur) > 0.001f ? cur : null;
	}

	@Override
	public void update(Building build){
		if (getCurrentBooster(build) != null) build.liquids.remove(getCurrentBooster(build), amount * build.edelta() * multiplier.get(build));
	}

	@Override
	public float efficiency(Building build){
		float ed = build.edelta() * build.efficiencyScale();
		if(ed <= 0.00000001f || getCurrentBooster(build) == null) return 0f;
		//there can be more liquid than necessary, so cap at 1
		return boosters.get(getCurrentBooster(build), 1f) * Math.min(build.liquids.get(getCurrentBooster(build)) / (amount * ed * multiplier.get(build)), 1f);
	}
}

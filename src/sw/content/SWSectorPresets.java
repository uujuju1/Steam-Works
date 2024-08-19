package sw.content;

import arc.*;
import arc.struct.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.type.*;
import sw.dream.*;
import sw.dream.event.*;
import sw.ui.dialog.*;

public class SWSectorPresets {
	public static SectorPreset
		nowhere, anywhereTop, anywhereBottom,
		yggdrasil;

	public static void load() {
		nowhere = new SectorPreset("nowhere", SWPlanets.wendi, 0) {{
			rules = rule -> {
				rule.winWave = captureWave;
			};
		}};
		anywhereTop = new SectorPreset("anywhere-t", SWPlanets.wendi, 1) {{
			rules = rule -> {
				rule.winWave = captureWave;
				rule.loadout.set(ItemStack.with(SWItems.nickel, 10));
			};
		}};
		anywhereBottom = new SectorPreset("anywhere-b", SWPlanets.wendi, 2) {{
			rules = rule -> {
				rule.winWave = captureWave;
				rule.loadout.set(ItemStack.with(SWItems.nickel, 10, Items.graphite, 10));
			};
		}};

		yggdrasil = new SectorPreset("yggdrasil", SWPlanets.unknown, 0);
		Events.on(EventType.WorldLoadEvent.class, e -> {
			DreamCore.instance.event(null);
			if (Vars.state.getSector() == yggdrasil.sector) DreamCore.instance.event(new YggdrasilEvent());
		});
	}

	public static void init() {
		new SectorLaunchDialog.SectorNode(nowhere.sector, 0f, 0f, Seq.with(), () -> {
			new SectorLaunchDialog.SectorNode(anywhereTop.sector, 149f, 0f, Seq.with(), () -> {
				new SectorLaunchDialog.SectorNode(anywhereBottom.sector, 149f, 149f, Seq.with(nowhere.sector), () -> {});
			});
		});
	}
}

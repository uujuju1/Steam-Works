package sw.ui.dialog;

import arc.*;
import arc.scene.event.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.core.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;
import sw.*;
import sw.content.*;
import sw.type.*;
import sw.ui.*;
import sw.ui.elements.*;

public class SectorLaunchDialog extends BaseDialog {
	private Planet lastPlanet = Planets.serpulo;
	private @Nullable PositionSectorPreset selected;

	public SectorView view;
	public Table selectSector;
	public Table sectorList;

	public SectorLaunchDialog() {
		super("@sector.view");
		titleTable.remove();
		titleTable.button("@quit", Icon.left, new TextButton.TextButtonStyle() {{
			font = Fonts.def;
			up = Tex.buttonSideRightDown;
			down = Tex.buttonSideRightOver;
		}}, this::hide).size(210f, 48f);
		titleTable.setBackground(Styles.black6);
		titleTable.margin(10);

		buttons.remove();
		buttons.clear();
		buttons.button("@techtree", Icon.tree, () -> SWUI.techtreeDialog.show()).size(200f, 54f).pad(2).bottom();

		cont.clear();
		cont.stack(
			new Table(t -> t.add(view = new SectorView() {{
				selector = s -> {
					selected = s;
					rebuildSelector(selected);
				};
			}})),
			new Table(t -> t.add(selectSector = new Table(Styles.black6))).bottom(),
			new Table(t -> t.add(titleTable).growX()).top(),
			new Table(t -> t.add(sectorList = new Table(Styles.black6).margin(10))).left(),
			new Table(t -> t.add(buttons)).bottom().right()
		).grow();
		addCaptureListener(new ElementGestureListener() {
			@Override
			public void pan(InputEvent event, float x, float y, float deltaX, float deltaY){
				view.x += deltaX;
				view.y += deltaY;
			}
		});
		closeOnBack();
		shown(() -> {
			view.rebuild(SWPlanets.wendi.sectors.select(s -> s.preset instanceof PositionSectorPreset).map(s -> (PositionSectorPreset) s.preset).removeAll(s -> !s.unlocked()));
			rebuildSectorList(SWPlanets.wendi.sectors.select(s -> s.preset instanceof PositionSectorPreset).map(s -> (PositionSectorPreset) s.preset));
			Core.settings.put("lastplanet", lastPlanet.name);
			Vars.ui.planet.hide();
			if (selected != null) moveTo(selected);
		});
		hidden(() -> {
			Vars.ui.planet.state.planet = lastPlanet;
			if (Vars.ui.planet.state.planet == SWPlanets.wendi) Vars.ui.planet.state.planet = Planets.serpulo;
			if (Vars.state.getSector() == null) Vars.ui.planet.show();
		});
		addToMenu();

		shouldPause = true;
	}

	public void addToMenu() {
		Events.run(EventType.Trigger.update, () -> {
			if (SWVars.showSectorLaunchDialog && Vars.ui.planet.state.planet == SWPlanets.wendi && Vars.ui.planet.isShown() && !SWUI.sectorLaunchDialog.isShown()) {
				SWUI.sectorLaunchDialog.show();
			} else {
				if (Vars.ui.planet.state.planet != SWPlanets.wendi) lastPlanet = Vars.ui.planet.state.planet;
			}
		});
	}

	public boolean checkLoadout(PositionSectorPreset to) {
		if (to.sector.preset == null || to.sector.id == to.planet.startSector) return true;
		Rules rules = to.sector.preset.generator.map.rules();
		if (Vars.state.getSector() != null) {
			for (ItemStack stack : rules.loadout) {
				if (!Vars.state.rules.defaultTeam.items().has(stack.item, stack.amount)) return false;
			}
		} else {
			return rules.loadout.isEmpty();
		}
		return true;
	}

	public void moveTo(PositionSectorPreset to) {
		view.setPosition(
			cont.getWidth()/2f - to.x * to.width - to.width/2f,
			cont.getHeight()/2f - to.y * to.height - to.height/2f
		);
	}

	public void rebuildSelector(@Nullable PositionSectorPreset sector) {
		selectSector.clear();
		if (sector == null) {
			selectSector.margin(0);
			return;
		}
		selectSector.margin(10f);
		selectSector.table(Tex.underline, title -> {
			title.left();
			Image i = title.image(Icon.map).padRight(10f).get();
			i.touchable = Touchable.enabled;
			i.clicked(() -> Vars.ui.content.show(sector));
			i.addListener(new HandCursorListener());
			title.add(sector.sector.name(), Pal.accent);
		}).growX().padBottom(10f).row();
		var button = selectSector.button("@play", Icon.play, new TextButton.TextButtonStyle() {{
			font = Fonts.def;
			up = Tex.buttonEdge1;
			over = Tex.buttonEdgeOver1;
			down = Tex.buttonEdgeDown1;
		}}, () -> {
			if (sector.sector.isBeingPlayed()) {
				Vars.ui.planet.hide();
				hide();
			} else {
				if (checkLoadout(sector)){
					Vars.control.playSector(Vars.state.getSector(), sector.sector);
					hide();
				}
			}
		}).growX().size(200f, 50f);
		if (!(sector.planet.startSector == sector.sector.id || sector.sector.hasSave())) button.tooltip(t -> {
			t.margin(10f);
			t.setBackground(Tex.paneSolid);
			t.add("loadout:").row();
			t.table(req -> {
				Rules rules = sector.generator.map.rules();
				rules.loadout.each(stack -> {
					req.image(stack.item.uiIcon).padRight(5);
					req.add(
						(Vars.state.getSector() != null ?
							 UI.formatAmount(Math.min(stack.amount, Vars.state.rules.defaultTeam.items().get(stack.item))) :
							 UI.formatAmount(0)
						) +" / " + UI.formatAmount(stack.amount)).row();
				});
			}).padTop(5f).left();
		});
	}

	public void rebuildSectorList(Seq<PositionSectorPreset> sectors) {
		sectorList.clear();
		sectorList.pane(Styles.smallPane, t -> {
			sectors.each(s -> s.sector.hasSave(), s -> {
				var button = t.button(s.localizedName, s.sector.isCaptured() ? s.icon.get() : Icon.warning.tint(Pal.breakInvalid), () -> {
					moveTo(s);
					selected = s;
					rebuildSelector(selected);
				}).size(200, 50);
				t.row();
				if (!s.sector.isCaptured()) button.tooltip(tooltip -> tooltip.add("@sectors.underattack.nodamage"));
			});
		}).maxHeight(300f);
	}
}

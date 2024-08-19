package sw.ui.dialog;

import arc.*;
import arc.assets.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import arc.scene.*;
import arc.scene.event.*;
import arc.scene.style.*;
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
import mindustry.ui.layout.*;
import sw.*;
import sw.content.*;
import sw.ui.*;

public class SectorLaunchDialog extends BaseDialog {
	private Planet lastPlanet = Planets.serpulo;
	private @Nullable SectorNode selected;

	public SectorView view;
	public Table selectSector;

	public static Seq<SectorNode> sectors = new Seq<>();

	public static float nodeSize = 149f;

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
		buttons.button("@techtree", Icon.tree, () -> SWVars.techtreeDialog.show()).size(200f, 54f).pad(2).bottom();

		SWSectorPresets.init();
		sectors.each(sectorNode -> {
			sectorNode.requirements = sectors.select(a -> Seq.with(sectorNode.req).contains(a.sector));
		});

		cont.clear();
		cont.stack(
			new Table(t -> t.add(view = new SectorView())),
			new Table(t -> t.add(selectSector = new Table(Styles.black6))).bottom(),
			new Table(t -> t.add(titleTable).growX()).top(),
			new Table(t -> t.add(buttons)).bottom().right()
		).grow();

		view.rebuild();

		addCaptureListener(new ElementGestureListener() {
			@Override
			public void pan(InputEvent event, float x, float y, float deltaX, float deltaY){
				view.x += deltaX;
				view.y += deltaY;
			}
		});
		closeOnBack();
		shown(() -> {
			view.rebuild();
			Core.settings.put("lastplanet", lastPlanet.name);
		});
		hidden(() -> {
			Vars.ui.planet.state.planet = lastPlanet;
			if (Vars.ui.planet.state.planet == SWPlanets.wendi) Vars.ui.planet.state.planet = Planets.serpulo;
			if (Vars.state.getSector() != null) Vars.ui.planet.hide();
		});
		addToMenu();
	}

	public void addToMenu() {
		Events.run(EventType.Trigger.update, () -> {
			if (SWVars.showSectorLaunchDialog && Vars.ui.planet.state.planet == SWPlanets.wendi && Vars.ui.planet.isShown() && !SWVars.sectorLaunchDialog.isShown()) {
				SWVars.sectorLaunchDialog.show();
			} else {
				if (Vars.ui.planet.state.planet != SWPlanets.wendi) lastPlanet = Vars.ui.planet.state.planet;
			}
		});
	}

	public boolean checkLoadout(SectorNode to) {
		if (to.sector.preset == null || to.parent == null) return true;
		Rules rules = new Rules();
		to.sector.preset.rules.get(rules);
		for (ItemStack stack : rules.loadout) {
			if (!to.parent.sector.isBeingPlayed()) {
				if (!to.parent.sector.items().has(stack.item, stack.amount)) return false;
			} else {
				if (!Vars.state.rules.defaultTeam.items().has(stack.item, stack.amount)) return false;
			}
		}
		return true;
	}

	public void rebuildSelector(@Nullable SectorNode sector) {
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
			i.clicked(() -> {
				Log.info("uwu, pwease add sector descwiption hewe");
			});
			i.addListener(new IbeamCursorListener());
			title.add(sector.sector.name(), Pal.accent);
		}).growX().padBottom(10f).row();
		selectSector.button("@play", Icon.play, new TextButton.TextButtonStyle() {{
			font = Fonts.def;
			up = Tex.buttonEdge1;
			over = Tex.buttonEdgeOver1;
			down = Tex.buttonEdgeDown1;
		}}, () -> {
			if (sector.sector.isBeingPlayed()) {
				Vars.ui.planet.hide();
				hide();
			} else {
				if (sector.parent != null) {
					if (checkLoadout(sector) || sector.sector.hasSave()) {
						Vars.control.playSector(sector.parent.sector, sector.sector);
						hide();
					}
				} else {
					Vars.control.playSector(sector.sector);
					hide();
				}
			}
		}).growX().size(200f, 50f).tooltip(t -> {
			t.margin(10f);
			t.setBackground(Tex.paneSolid);
			t.add("loadout:").row();
			t.table(req -> {
				if (sector.parent == null || sector.sector.preset == null) {
					req.add("Free!");
				} else {
					Rules rules = new Rules();
					sector.sector.preset.rules.get(rules);
					rules.loadout.each(stack -> {
						req.image(stack.item.uiIcon).padRight(5);
						req.add(
							(sector.parent.sector.isBeingPlayed() ?
								 UI.formatAmount(Math.min(stack.amount, Vars.state.rules.defaultTeam.items().get(stack.item))) :
								 UI.formatAmount(Math.min(stack.amount, sector.parent.sector.items().get(stack.item)))
							) +" / " + UI.formatAmount(stack.amount)).row();
					});
					if (rules.loadout.isEmpty()) req.add("Free!").padTop(5f);
				}
			}).padTop(5f).left();
		});
	}

	public class SectorView extends Group {
		Shader background;
		SectorViewStyle style;
		public float margin = 10;

		float minx, miny, maxx, maxy;
		boolean empty;

		public SectorView() {
			background = new Shader(Core.files.internal("shaders/screenspace.vert"), Vars.tree.get("shaders/sectorDialogBackground.frag")) {
				@Override
				public void apply() {
					setUniformf("u_resolution", Core.graphics.getWidth(), Core.graphics.getHeight());
					setUniformf("u_position", -x, -y);
					setUniformf("u_opacity", parentAlpha);
					setUniformf("u_time", Time.time);
				}
			};
			style = new SectorViewStyle();
		}

		public void rebuild() {
			clear();
			minx = miny = Float.POSITIVE_INFINITY;
			maxx = maxy = Float.NEGATIVE_INFINITY;
			empty = true;
			sectors.each(sectorNode -> {
				boolean hasPreview = Core.assets.isLoaded(
					Vars.mapPreviewDirectory.child("save_slot_sector-" + sectorNode.sector.planet.name + "-" + sectorNode.sector.id + ".png").path()
				);

				if (!hasPreview && Vars.mapPreviewDirectory.child(
					"save_slot_sector-" + sectorNode.sector.planet.name + "-" + sectorNode.sector.id + ".png"
				).exists()) {
					Core.assets.load(new AssetDescriptor<>(
						Vars.mapPreviewDirectory.child("save_slot_sector-" + sectorNode.sector.planet.name + "-" + sectorNode.sector.id + ".png"),
						Texture.class
					));
					hasPreview = Core.assets.isLoaded(
						Vars.mapPreviewDirectory.child("save_slot_sector-" + sectorNode.sector.planet.name + "-" + sectorNode.sector.id + ".png").path()
					);
				}
				Table button = new Table();
				button.touchable = Touchable.enabled;
				button.addListener(new HandCursorListener());
				button.setSize(Scl.scl(nodeSize));
				button.clicked(() -> {
					if ((sectorNode.parent == null || sectorNode.parent.sector.hasSave())) {
						if (selected != sectorNode) {
							selected = sectorNode;
						} else {
							selected = null;
						}
						rebuildSelector(selected);
					}
				});
				button.setPosition(sectorNode.x - button.getWidth()/2f, sectorNode.y - button.getHeight()/2f);
				Image tex;
				if ((sectorNode.parent == null || sectorNode.parent.sector.hasSave())) {
					if (hasPreview) {
						tex = new Image((Texture) Core.assets.get(
							Vars.mapPreviewDirectory.child("save_slot_sector-" + sectorNode.sector.planet.name + "-" + sectorNode.sector.id + ".png").path()
						));
					} else {
						tex = new Image(Icon.map);
					}
				} else {
					tex = new Image(Icon.lock);
				}
				button.stack(tex, new Image(SWStyles.inventoryClear)).grow();
				minx = Math.min(minx, button.x);
				miny = Math.min(miny, button.y);
				maxx = Math.max(maxx, button.x + button.getWidth());
				maxy = Math.max(maxy, button.y + button.getHeight());
				empty = false;

				addChild(button);
			});
		}

		@Override
		public void draw() {
			Draw.blit(background);

			super.draw();

			Draw.color();
			if (!empty) {
				Draw.alpha(parentAlpha);
				style.under.draw(x + minx - Scl.scl(margin), y + miny - Scl.scl(margin), maxx - minx + Scl.scl(margin) * 2f, maxy - miny + Scl.scl(margin) * 2f);
			}

			if (selected != null) {
				Lines.stroke(Scl.scl(5));
				selected.requirements.each(req -> {
					Draw.color(req.sector.hasSave() ? style.selectedColorValid : style.selectedColorInvalid, parentAlpha);
					Fill.circle(selected.x + x, selected.y + y, Scl.scl(10f));
					Lines.line(selected.x + x, selected.y + y, req.x + x, req.y + y);
					Fill.circle(req.x + x, req.y + y, Scl.scl(10f));
				});
				if (selected.parent != null) {
					Draw.color(style.parentColor, parentAlpha);
					Fill.circle(selected.x + x, selected.y + y, Scl.scl(10f));
					Lines.line(selected.x + x, selected.y + y, selected.parent.x + x, selected.parent.y + y, false);
					Fill.circle(selected.parent.x + x, selected.parent.y + y, Scl.scl(10f));
				}
			}

			Draw.reset();
		}

		public class SectorViewStyle extends Style {
			Drawable under = Styles.black6;

			Color
				parentColor = Pal.accent,
				selectedColorValid = Pal.heal,
				selectedColorInvalid = Pal.breakInvalid;
		}
	}

	public static class SectorNode extends TreeLayout.TreeNode<SectorNode> {
		public static SectorNode last = null;

		public Sector sector;
		public Seq<Sector> req;
		public Seq<SectorNode> requirements;

		public SectorNode(Sector sector, float x, float y, Seq<Sector> requirements, Runnable run) {
			this.sector = sector;
			this.x = Scl.scl(x);
			this.y = Scl.scl(y);
			this.req = requirements;
			this.parent = last;

			SectorNode context = last;
			last = this;
			run.run();
			last = context;

			sectors.add(this);
		}
	}
}

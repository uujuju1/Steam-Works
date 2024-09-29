package sw.ui.dialog;

import arc.*;
import arc.graphics.gl.*;
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
	private float dx = 0, dy = 0;

	public SectorView view;
	public ShaderElement shader;
	public Table selectSector;
	public Table sectorList;

//	public static Seq<SectorNode> sectors = new Seq<>();

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
			new Table(t -> t.add(shader = new ShaderElement(new Shader(Core.files.internal("shaders/screenspace.vert"), Vars.tree.get("shaders/sectorDialogBackground.frag")) {
				@Override
				public void apply() {
					setUniformf("u_resolution", Core.graphics.getWidth(), Core.graphics.getHeight());
					setUniformf("u_position", dx, dy);
					setUniformf("u_opacity", parentAlpha);
					setUniformf("u_time", Time.globalTime);
				}
			}))),
			new Table(t -> t.add(view = new SectorView() {{
				selector = s -> rebuildSelector(s);
			}})),
			new Table(t -> t.add(selectSector = new Table(Styles.black6))).bottom(),
			new Table(t -> t.add(titleTable).growX()).top(),
			new Table(t -> t.add(sectorList = new Table(Styles.black6).margin(10))).left(),
			new Table(t -> t.add(buttons)).bottom().right()
		).grow();
		dx = shader.x;
		dy = shader.y;
		addCaptureListener(new ElementGestureListener() {
			@Override
			public void pan(InputEvent event, float x, float y, float deltaX, float deltaY){
				view.x += deltaX;
				view.y += deltaY;
				dx += deltaX;
				dy += deltaY;
			}
		});
		closeOnBack();
		shown(() -> {
			view.rebuild(SWPlanets.wendi.sectors.select(s -> s.preset instanceof PositionSectorPreset).map(s -> (PositionSectorPreset) s.preset));
			rebuildSectorList(SWPlanets.wendi.sectors.select(s -> s.preset instanceof PositionSectorPreset).map(s -> (PositionSectorPreset) s.preset));
			Core.settings.put("lastplanet", lastPlanet.name);
			Vars.ui.planet.hide();
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
		if (to.sector.preset == null) return true;
		Rules rules = new Rules();
		to.sector.preset.rules.get(rules);
		for (ItemStack stack : rules.loadout) {
			if (!Vars.state.rules.defaultTeam.items().has(stack.item, stack.amount)) return false;
		}
		return true;
	}

	public void moveTo(PositionSectorPreset to) {
		view.setPosition(cont.getWidth()/2f - to.x * view.sectorScale, cont.getHeight()/2f - to.y * view.sectorScale);
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
				if (Vars.state.getSector() != null && checkLoadout(sector)) {
					Vars.control.playSector(Vars.state.getSector(), sector.sector);
					hide();
				}
			}
		}).growX().size(200f, 50f).tooltip(t -> {
			t.margin(10f);
			t.setBackground(Tex.paneSolid);
			t.add("loadout:").row();
			t.table(req -> {
				Rules rules = new Rules();
				sector.sector.preset.rules.get(rules);
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
				t.button(s.localizedName, Icon.terrain, () -> {
					moveTo(s);
					selected = s;
					rebuildSelector(selected);
				}).size(200, 50).row();
			});
		}).maxHeight(300f);
	}

//	public class SectorView extends Group {
//		Shader background;
//		SectorViewStyle style;
//		public float margin = 10;
//
//		float minx, miny, maxx, maxy;
//		boolean empty;
//
//		public SectorView() {
//			background = new Shader(Core.files.internal("shaders/screenspace.vert"), Vars.tree.get("shaders/sectorDialogBackground.frag")) {
//				@Override
//				public void apply() {
//					setUniformf("u_resolution", Core.graphics.getWidth(), Core.graphics.getHeight());
//					setUniformf("u_position", -x, -y);
//					setUniformf("u_opacity", parentAlpha);
//					setUniformf("u_time", time);
//				}
//			};
//			style = new SectorViewStyle();
//		}
//
//		public void rebuild() {
//			clear();
//			minx = miny = Float.POSITIVE_INFINITY;
//			maxx = maxy = Float.NEGATIVE_INFINITY;
//			empty = true;
//			sectors.each(sectorNode -> {
//				Table button = new Table();
//				if (sectorNode.lock.get(sectorNode)) {
//					button.touchable = Touchable.enabled;
//					button.addListener(new HandCursorListener());
//					button.clicked(() -> {
//						if ((sectorNode.parent == null || sectorNode.parent.sector.hasSave())) {
//							if (selected != sectorNode) {
//								selected = sectorNode;
//							} else {
//								selected = null;
//							}
//							rebuildSelector(selected);
//						}
//					});
//				}
//				button.setSize(nodeSize);
//				button.setPosition(sectorNode.x - button.getWidth()/2f, sectorNode.y - button.getHeight()/2f);
//				Stack stack = button.stack(new Image(Styles.black)).size(nodeSize).get();
//				if (sectorNode.visible.get(sectorNode)) {
//					stack.add(new Image(sectorNode.getRegion()));
//				} else {
//					if (!sectorNode.lock.get(sectorNode)) {
//						stack.add(new Image(Icon.lock));
//					} else {
//						stack.add(new Image(Icon.map));
//					}
//				}
//				stack.add(new Image(SWStyles.inventoryClear));
//				if (sectorNode.visible.get(sectorNode) && sectorNode.top != null) {
//					stack.add(new Table(t -> {
//						t.table(Styles.black6, sticker -> sticker.image(sectorNode.top).size(nodeSize/4f).pad(5f));
//					}));
//				}
//				minx = Math.min(minx, button.x);
//				miny = Math.min(miny, button.y);
//				maxx = Math.max(maxx, button.x + button.getWidth());
//				maxy = Math.max(maxy, button.y + button.getHeight());
//				empty = false;
//
//				addChild(button);
//			});
//		}
//
//		@Override
//		public void draw() {
//			Draw.blit(background);
//
//			if (!empty) {
//				Draw.alpha(parentAlpha);
//				style.under.draw(x + minx - Scl.scl(margin), y + miny - Scl.scl(margin), maxx - minx + Scl.scl(margin) * 2f, maxy - miny + Scl.scl(margin) * 2f);
//			}
//
//			super.draw();
//
//			if (selected != null) {
//				Lines.stroke(Scl.scl(5));
//				selected.requirements.each(req -> {
//					Draw.color(req.sector.hasSave() ? style.selectedColorValid : style.selectedColorInvalid, parentAlpha);
//					Fill.circle(selected.x + x, selected.y + y, Scl.scl(10f));
//					Lines.line(selected.x + x, selected.y + y, req.x + x, req.y + y);
//					Fill.circle(req.x + x, req.y + y, Scl.scl(10f));
//				});
//				if (selected.parent != null) {
//					Draw.color(style.parentColor, parentAlpha);
//					Fill.circle(selected.x + x, selected.y + y, Scl.scl(10f));
//					Lines.line(selected.x + x, selected.y + y, selected.parent.x + x, selected.parent.y + y, false);
//					Fill.circle(selected.parent.x + x, selected.parent.y + y, Scl.scl(10f));
//				}
//			}
//
//			Draw.reset();
//		}
//
//		public static class SectorViewStyle extends Style {
//			Drawable under = Styles.black6;
//
//			Color
//				parentColor = Pal.accent,
//				selectedColorValid = Pal.heal,
//				selectedColorInvalid = Pal.breakInvalid;
//		}
//	}

//	public static class SectorNode extends TreeLayout.TreeNode<SectorNode> {
//		public static SectorNode last = null;
//
//		public @Nullable Drawable top;
//
//		public @Nullable String region;
//
//		public Boolf<SectorNode>
//		lock = self -> {
//			for (SectorNode req : self.requirements) {
//				if (!req.lock.get(req) || !req.visible.get(req)) return false;
//			}
//			return (self.parent == null || self.parent.sector.hasSave());
//		},
//		visible = self -> self.sector.hasSave();
//
//		public Sector sector;
//		public Seq<Sector> req;
//		public Seq<SectorNode> requirements;
//
//		public SectorNode(Sector sector, float x, float y, Seq<Sector> requirements, Cons<SectorNode> run) {
//			this.sector = sector;
//			this.x = Scl.scl(x);
//			this.y = Scl.scl(y);
//			this.req = requirements;
//			this.parent = last;
//
//			SectorNode context = last;
//			last = this;
//			run.get(this);
//			last = context;
//
//			sectors.add(this);
//		}
//
//		public TextureRegion getRegion() {
//			return region == null ? Core.atlas.find("sw-sector-" + sector.id, "nomap") : Core.atlas.find(region, "nomap");
//		}
//	}
}

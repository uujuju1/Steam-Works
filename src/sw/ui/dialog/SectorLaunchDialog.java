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
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;
import mindustry.ui.layout.*;
import sw.*;
import sw.content.*;

public class SectorLaunchDialog extends BaseDialog {
	private Planet lastPlanet = Planets.serpulo;
	private SectorNode selected;

	public SectorView view;
	public Table selectSector;

	public static Seq<SectorNode> sectors = new Seq<>();

	public static float nodeSize = 128f;

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

		SWSectorPresets.init();
		sectors.each(sectorNode -> {
			sectorNode.requirements = sectors.select(a -> Seq.with(sectorNode.req).contains(a.sector));
		});

		cont.clear();
		cont.stack(
			new Table(t -> t.add(view = new SectorView())),
			new Table(t -> t.add(selectSector = new Table(Styles.black6))).bottom(),
			new Table(t -> t.add(titleTable).growX()).top()
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
		});
		addToMenu();
	}

	public void addToMenu() {
		Events.run(EventType.Trigger.update, () -> {
			if (SWVars.showSectorLaunchDialog && Vars.ui.planet.state.planet == SWPlanets.wendi && Vars.ui.planet.isShown() && !SWVars.sectorLaunchDialog.isShown()) {
				SWVars.sectorLaunchDialog.show();
			} else {
				lastPlanet = Vars.ui.planet.state.planet;
			}
		});
	}

	public void rebuildSelector(SectorNode sector) {
		selectSector.clear();
		selectSector.margin(10f);
		selectSector.table(Tex.underline, title -> {
			title.image(Icon.map).left();
			title.add(sector.sector.name(), Pal.accent).padRight(10f);
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
				Vars.control.playSector(sector.sector);
				hide();
			}
		}).growX().size(200f, 50f);
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
				ImageButton button = new ImageButton();
				button.setSize(Scl.scl(nodeSize));
				button.clicked(() -> {
					selected = sectorNode;
					rebuildSelector(sectorNode);
				});
				button.setPosition(sectorNode.x - button.getWidth()/2f, sectorNode.y - button.getHeight()/2f);
				if (hasPreview) {
					button.replaceImage(new Image((Texture) Core.assets.get(
						Vars.mapPreviewDirectory.child("save_slot_sector-" + sectorNode.sector.planet.name + "-" + sectorNode.sector.id + ".png").path()
					)).setScaling(Scaling.fit));
				} else {
					button.replaceImage(new Image(Icon.map).setScaling(Scaling.fit));
				}
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

			Draw.color();
			if (!empty) {
				Draw.alpha(parentAlpha);
				style.under.draw(x + minx - Scl.scl(margin), y + miny - Scl.scl(margin), maxx - minx + Scl.scl(margin) * 2f, maxy - miny + Scl.scl(margin) * 2f);
			}

			sectors.each(sector -> {
				Lines.stroke(Scl.scl(10), style.parentColor);
				Draw.alpha(parentAlpha);
				if (sector.parent != null) {
					Lines.line(sector.x + x, sector.y + y, sector.parent.x + x, sector.parent.y + y);
				}
			});
			sectors.each(sector -> {
				Lines.stroke(Scl.scl(10), style.requirementColor);
				Draw.alpha(parentAlpha);
				sector.requirements.each(req -> {
					Lines.line(sector.x + x, sector.y + y, req.x + x, req.y + y);
				});
			});

			Lines.stroke(Scl.scl(10), style.selectedColorValid);
			Draw.alpha(parentAlpha);
			if (selected != null) selected.requirements.each(req -> {
				Lines.line(selected.x + x, selected.y + y, req.x + x, req.y + y);
			});

			Draw.reset();
			super.draw();
		}

		public class SectorViewStyle extends Style {
			Drawable under = Styles.black6;

			Color
				parentColor = Pal.accent,
				requirementColor = Pal.gray,
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

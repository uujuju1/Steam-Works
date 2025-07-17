package sw.ui.dialog;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.scene.*;
import arc.scene.event.*;
import arc.scene.style.*;
import arc.scene.ui.*;
import arc.scene.ui.Button.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.core.*;
import mindustry.ctype.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;
import mindustry.ui.fragments.MenuFragment.*;
import sw.*;
import sw.content.*;
import sw.gen.*;
import sw.graphics.*;
import sw.graphics.SWShaders.*;
import sw.type.*;
import sw.ui.*;

import java.util.*;

import static arc.Core.*;
import static mindustry.Vars.*;
import static mindustry.ui.dialogs.PlanetDialog.*;

public class SectorLaunchDialog extends BaseDialog {
	Planet planet;
	public static Planet lastPlanet;
	public static float minX, minY, maxX, maxY;
	
	PositionSectorPreset currentSector;
	
	public SectorView view;
	public Table sectorTable;
	
	public SectorLaunchDialog() {
		super("");
		shouldPause = true;
		
		closeOnBack();
		addToMenu();
		
		buildTitle();
		buildButtons();
		
		cont.clear();
		cont.stack(
			new Table(t -> t.add(view = new SectorView())),
			new Table(t -> t.add(titleTable).growX()).top(),
//			new Table(t -> t.add(buttons).growX()).bottom()
			buttons
		).grow();
		
		shown(() -> {
			view.offsetX = view.offsetY = 0;
			view.rebuild(SWPlanets.wendi);
			view.move((minX - maxX) / 2f, (minY - maxY) / 2f);
		});
		
		addCaptureListener(new ElementGestureListener(){
			@Override
			public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
				view.move(deltaX, deltaY);
			}
		});
	}
	
	public void addToMenu() {
		Events.run(EventType.Trigger.update, () -> {
			planet = SWPlanets.wendi;
			if (Vars.ui.planet.state.planet == planet && SWVars.showSectorLaunchDialog) {
				show();
				Vars.ui.planet.hide();
				Vars.ui.planet.state.planet = lastPlanet == null ? Planets.serpulo : lastPlanet;
				Core.settings.put("lastplanet", lastPlanet == null ? "serpulo" : lastPlanet.name);
			}
			lastPlanet = Vars.ui.planet.state.planet;
		});
		if (Vars.mobile || !Core.settings.getBool("sw-menu-override", true)) {
			Vars.ui.menufrag.addButton(new MenuButton("@planet.sw-wendi.name", Icon.terrain, () -> {
				if (Vars.mods.hasContentErrors()) {
					Vars.ui.showInfo("@mod.noerrorplay");
				} else show();
			}));
		} else {
			MenuButton old = Vars.ui.menufrag.desktopButtons.first().submenu.first();
			MenuButton newButton = new MenuButton(
				"@ui.sw-main-game",
				Icon.play, () -> {},
				old, new MenuButton("@planet.sw-wendi.name", Icon.terrain, () -> {
					if (Vars.mods.hasContentErrors()) {
						Vars.ui.showInfo("@mod.noerrorplay");
					} else show();
				})
			);
			Vars.ui.menufrag.desktopButtons.first().submenu.replace(old, newButton);
		}
	}
	
	public void buildButtons() {
		buttons.remove();
		buttons.stack(
			new Table(selector -> {
				selector.add(sectorTable = new Table()).minWidth(200);
			}).bottom(),
			new Table(tech -> {
				tech.button(button -> {
					button.margin(10);
					button.left();
					button.image(Icon.tree).size(Vars.iconMed).scaling(Scaling.fit).padRight(10).padLeft(2);
					button.add("@techtree");
				}, () -> Vars.ui.research.show()).margin(10);
			}).bottom().right()
		).grow();
	}
	
	public void buildTitle() {
		titleTable.remove();
		titleTable.clear();
		titleTable.table(titleBottom -> {
			titleBottom.image(Tex.whiteui, Pal.accent).growX();
			titleBottom.button("@quit", Icon.left, new TextButton.TextButtonStyle() {{
				font = Fonts.def;
				up = Tex.buttonSideRightDown;
				down = Tex.buttonSideRightOver;
			}}, this::hide).size(210f, 48f);
		}).growX();
		titleTable.margin(10);
	}
	
	// yoinked from planetDialog
	void displayItems(Table c, float scl, ObjectMap<Item, SectorInfo.ExportStat> stats, String name){
		displayItems(c, scl, stats, name, t -> {});
	}
	void displayItems(Table c, float scl, ObjectMap<Item, SectorInfo.ExportStat> stats, String name, Cons<Table> builder){
		Table t = new Table().left();
		
		int i = 0;
		for(var item : content.items()){
			var stat = stats.get(item);
			if(stat == null) continue;
			int total = (int)(stat.mean * 60 * scl);
			if(total > 1){
				t.image(item.uiIcon).padRight(3);
				t.add(UI.formatAmount(total) + " " + Core.bundle.get("unit.perminute")).color(Color.lightGray).padRight(3);
				if(++i % 3 == 0){
					t.row();
				}
			}
		}
		
		if(t.getChildren().any()){
			c.defaults().left();
			c.add(name).row();
			builder.get(c);
			c.add(t).padLeft(10f).row();
		}
	}
	
	public void playSector(PositionSectorPreset sector) {
		if (sector.sector.isBeingPlayed()) {
			hide();
			return;
		}
		
		if(!planet.allowWaveSimulation && !debugSelect) {
			//if there are two or more attacked sectors... something went wrong, don't show the dialog to prevent softlock
			Sector attacked = planet.sectors.find(s -> s.isAttacked() && s != sector.sector);
			if(attacked != null && planet.sectors.count(Sector::isAttacked) < 2 && attacked.preset instanceof PositionSectorPreset preset) {
				Vars.ui.showInfoOnHidden(Core.bundle.format("sector.noswitch", attacked.name(), attacked.planet.localizedName), () -> {
					view.move(-view.offsetX + minX - preset.x - preset.width / 2f, -view.offsetY + minY - preset.y - preset.height / 2f);
				});
				return;
			}
		}
		
		if(control.saves.getCurrent() != null && Vars.state.isGame()) {
			try {
				control.saves.getCurrent().save();
			} catch(Throwable e) {
				Log.err(e);
				ui.showException("[accent]" + Core.bundle.get("savefail"), e);
			}
		}
		
		if (!sector.planet.sectors.contains(Sector::hasBase) || sector.sector.hasBase()) {
			Vars.control.playSector(sector.sector);
			hide();
		} else {
			PositionSectorPreset launcher = sector.launcher;
			if (launcher == null && Vars.state.getSector() != null && Vars.state.getSector().preset instanceof PositionSectorPreset p ) launcher = p;
			if (launcher == null) {
				Vars.ui.showInfo("Enter a Sector from which to launch to");
				return;
			}
			
			PositionSectorPreset finalLauncher = launcher;
			ui.planet.loadouts.show(sector.core, launcher.sector, sector.sector, () -> {
				if (settings.getBool("skipcoreanimation")) {
					Vars.control.playSector(finalLauncher.sector, sector.sector);
					
					Time.runTask(8f, this::hide);
				} else {
					hide();
					
					Time.runTask(5f, () -> {
						Vars.renderer.showLaunch(Vars.player.core());
						Time.runTask(Vars.player.core().launchDuration() - 8, () -> Vars.control.playSector(finalLauncher.sector, sector.sector));
					});
				}
			});
		}
	}
	
	public void selectSector(PositionSectorPreset sector) {
		currentSector = sector;
		sectorTable.clear();
		sectorTable.setBackground(SWTex.buttonSideUpOver);
		
		sectorTable.add(sector.localizedName).color(Pal.accent).row();
		sectorTable.image(((TextureRegionDrawable) Tex.whiteui).tint(Pal.accent)).padTop(10).padBottom(10).growX().row();
		if (sector.sector.hasSave() && sector.sector.info.resources.any()) sectorTable.table(res -> {
			res.add("@sectors.resources").padRight(4);
			sector.sector.info.resources.each(Objects::nonNull, r -> {
				res.image(r.uiIcon).padRight(3).scaling(Scaling.fit).size(iconSmall);
			});
		}).padBottom(10).row();
		if (sector.sector.hasBase()) sectorTable.button("@stats", Icon.info, Styles.cleart, () -> showStats(sector.sector)).height(40f).fillX().padBottom(10).row();
		sectorTable.button("@play", () -> playSector(sector)).growX();
	}
	
	// yoinked from planetDialog
	void showStats(Sector sector){
		BaseDialog dialog = new BaseDialog(sector.name());
		
		dialog.cont.pane(c -> {
			c.defaults().padBottom(5);
			
			if(sector.preset != null && sector.preset.description != null){
				c.add(sector.preset.displayDescription()).width(420f).wrap().left().row();
			}
			
			c.add(Core.bundle.get("sectors.time") + " [accent]" + sector.save.getPlayTime()).left().row();
			
			if(sector.info.waves && sector.hasBase()){
				c.add(Core.bundle.get("sectors.wave") + " [accent]" + (sector.info.wave + sector.info.wavesPassed)).left().row();
			}
			
			if(sector.isAttacked() || !sector.hasBase()){
				c.add(Core.bundle.get("sectors.threat") + " [accent]" + sector.displayThreat()).left().row();
			}
			
			if(sector.save != null && sector.info.resources.any()){
				c.add("@sectors.resources").left().row();
				c.table(t -> {
					for(UnlockableContent uc : sector.info.resources){
						if(uc == null) continue;
						t.image(uc.uiIcon).scaling(Scaling.fit).padRight(3).size(iconSmall);
					}
				}).padLeft(10f).left().row();
			}
			
			//production
			displayItems(c, sector.getProductionScale(), sector.info.production, "@sectors.production");
			
			//export
			displayItems(c, sector.getProductionScale(), sector.info.export, "@sectors.export", t -> {
				if(sector.info.destination != null && sector.info.destination.hasBase()){
					String ic = sector.info.destination.iconChar();
					t.add(Iconc.rightOpen + " " + (ic == null || ic.isEmpty() ? "" : ic + " ") + sector.info.destination.name()).padLeft(10f).row();
				}
			});
			
			//import
			if(sector.hasBase()){
				displayItems(c, 1f, sector.info.imports, "@sectors.import", t -> {
					sector.info.eachImport(sector.planet, other -> {
						String ic = other.iconChar();
						t.add(Iconc.rightOpen + " " + (ic == null || ic.isEmpty() ? "" : ic + " ") + other.name()).padLeft(10f).row();
					});
				});
			}
			
			ItemSeq items = sector.items();
			
			//stored resources
			if(sector.hasBase() && items.total > 0){
				
				c.add("@sectors.stored").left().row();
				c.table(t -> {
					t.left();
					
					t.table(res -> {
						
						int i = 0;
						for(ItemStack stack : items){
							res.image(stack.item.uiIcon).padRight(3);
							res.add(UI.formatAmount(Math.max(stack.amount, 0))).color(Color.lightGray);
							if(++i % 4 == 0){
								res.row();
							}
						}
					}).padLeft(10f);
				}).left().row();
			}
		});
		
		dialog.addCloseButton();
		
		if(sector.hasBase()){
			dialog.buttons.button("@sector.abandon", Icon.cancel, () -> Vars.ui.planet.abandonSectorConfirm(sector, dialog::hide));
		}
		
		dialog.show();
	}
	
	public void unSelectSector() {
		currentSector = null;
		sectorTable.clear();
		sectorTable.setBackground(null);
	}
	
	public static class SectorView extends Group {
		public SectorLaunchShader shader;
		
		public float offsetX, offsetY;
		
		public Cons<PositionSectorPreset> clickSector = sector -> {
			if (SWUI.sectorLaunchDialog.currentSector == sector) {
				SWUI.sectorLaunchDialog.unSelectSector();
			} else {
				SWUI.sectorLaunchDialog.selectSector(sector);
			}
		};
		public Boolf<PositionSectorPreset> sectorChecked = sector -> SWUI.sectorLaunchDialog.currentSector == sector;
		
		public ObjectMap<Element, PositionSectorPreset> sectors = new ObjectMap<>();
		
		public SectorView() {
			shader = SWShaders.sectorLaunchShader;
		}
		
		@Override
		public void draw() {
			shader.pos.set(offsetX, offsetY);
			shader.opacity = parentAlpha * color.a;
			shader.lights.clear();
			children.each(child -> {
				if (sectors.get(child).clearFog.get(sectors.get(child).sector)) {
					shader.lights.add(child.x + x + child.getWidth() / 2f);
					shader.lights.add(child.y + y + child.getHeight() / 2f);
					shader.lights.add(child.getWidth());
					shader.lights.add(child.getHeight());
				}
			});
			
			shader.boxes.clear();
			children.each(child -> {
				if (sectors.get(child).hasOverlay.get(sectors.get(child).sector)) {
					shader.boxes.add(child.x + x + child.getWidth() / 2f);
					shader.boxes.add(child.y + y + child.getHeight() / 2f);
					shader.boxes.add(child.getWidth());
					shader.boxes.add(child.getHeight());
				}
			});
			Draw.blit(shader);
			
			super.draw();
		}
		
		public void rebuild(Planet planet) {
			minX = minY = Float.POSITIVE_INFINITY;
			maxX = maxY = Float.NEGATIVE_INFINITY;
			clear();
			sectors.clear();
			planet.sectors.each(sector -> {
				if (sector.preset instanceof PositionSectorPreset preset) {
					ButtonStyle style = new ButtonStyle();
					style.down = style.checked = ((ScaledNinePatchDrawable) Tex.whitePane).tint(Pal.accent);
					
					Button button = new Button(style);
					button.setPosition(preset.x + offsetX, preset.y + offsetY);
					button.setSize(preset.width, preset.height);
					addChild(button);
					sectors.put(button, preset);
					
					button.clicked(() -> clickSector.get(preset));
					button.update(() -> button.setChecked(sectorChecked.get(preset)));
					
					button.touchable = Touchable.disabled;
					if (preset.visible.get(preset.sector)) {
						button.table(Styles.black6, icon -> {
							icon.image(preset.icon.get()).grow().scaling(Scaling.fit);
						}).size(preset.width / 3f, preset.height / 3f);
						button.touchable = Touchable.enabled;
					}
					
					minX = Math.min(minX, preset.x);
					minY = Math.min(minY, preset.y);
					maxX = Math.max(maxX, preset.x + preset.width);
					maxY = Math.max(maxY, preset.y + preset.height);
				}
			});
			children.each(e -> e.moveBy(-minX, -minY));
		}
		
		public void move(float deltaX, float deltaY) {
			offsetX += deltaX;
			offsetY += deltaY;
			children.each(e -> e.moveBy(deltaX, deltaY));
		}
	}
}

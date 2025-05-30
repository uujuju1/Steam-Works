package sw.ui.dialog;

import arc.*;
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
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;
import mindustry.ui.fragments.MenuFragment.*;
import sw.content.*;
import sw.graphics.*;
import sw.graphics.SWShaders.*;
import sw.type.*;
import sw.ui.*;

public class SectorLaunchDialog extends BaseDialog {
	public static float minX, minY;
	public SectorView view;
	
	public SectorLaunchDialog() {
		super("");
		shouldPause = true;
		
		closeOnBack();
		addToMenu();
		
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
		
		buttons.remove();
		buttons.right();
		buttons.button(button -> {
			button.margin(10);
			button.left();
			button.image(Icon.tree).size(Vars.iconMed).scaling(Scaling.fit).padRight(10);
			button.add("@techtree");
		}, () -> SWUI.techtreeDialog.show()).size(200, 54);
		
		cont.clear();
		cont.stack(
			new Table(t -> t.add(view = new SectorView())),
			new Table(t -> t.add(titleTable).growX()).top(),
			new Table(t -> t.add(buttons).growX()).bottom()
		).grow();
		
		shown(() -> {
			view.rebuild(SWPlanets.wendi);
		});
		
		addCaptureListener(new ElementGestureListener(){
			@Override
			public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
				view.move(deltaX, deltaY);
			}
		});
	}
	
	public void addToMenu() {
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
	
	public static class SectorView extends Group {
		public SectorLaunchShader shader;
		
		public float offsetX, offsetY;
		
		public ObjectMap<Element, PositionSectorPreset> sectors = new ObjectMap<>();
		
		public SectorView() {
			shader = SWShaders.sectorLaunchShader;
		}
		
		@Override
		public void draw() {
			shader.pos.set(offsetX, offsetY);
			shader.opacity = parentAlpha * color.a;
			shader.points.clear();
			children.each(child -> {
				if (sectors.get(child).clearFog.get(sectors.get(child).sector)) {
					shader.points.add(child.x + x + child.getWidth() / 2f);
					shader.points.add(child.y + y + child.getHeight() / 2f);
					shader.points.add(child.getWidth());
					shader.points.add(child.getHeight());
				}
			});
			Draw.blit(shader);
			
			super.draw();
		}
		
		public void rebuild(Planet planet) {
			clear();
			sectors.clear();
			planet.sectors.each(sector -> {
				if (sector.preset instanceof PositionSectorPreset preset) {
					ButtonStyle style = new ButtonStyle();
					Button button = new Button(style);
					if (preset.unlocked() && !preset.sector.hasSave()) {
						style.up = Tex.whitePane;
						style.down = style.checked = ((ScaledNinePatchDrawable) Tex.whitePane).tint(Pal.accent);
					}
					button.setPosition(preset.x + offsetX, preset.y + offsetY);
					button.setSize(preset.width, preset.height);
					addChild(button);
					sectors.put(button, preset);
					
					if (preset.unlocked()) button.table(Styles.black6, icon -> {
						icon.image(preset.icon.get()).grow().pad(10).scaling(Scaling.fit);
					}).size(preset.width / 3f, preset.height / 3f);
					
					minX = Math.min(minX, preset.x);
					minY = Math.min(minY, preset.y);
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

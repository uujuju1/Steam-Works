package sw.ui.elements;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.scene.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.*;
import sw.graphics.*;
import sw.type.*;

public class CampaignView extends Element {
	public static boolean debugSelect = false;

	private Seq<PositionSectorPreset> sectors = new Seq<>();

	private float offsetX, offsetY, scale = 1;

	private PositionSectorPreset selected;

	public void build(Planet planet) {
		sectors.set(planet.sectors.select(s -> s.preset instanceof PositionSectorPreset).map(s -> (PositionSectorPreset) s.preset));
	}

	@Override
	public void draw() {
		validate();
		float alpha = parentAlpha * color.a;

		float scale = Scl.scl(this.scale);
		sectors.each(sector -> {
			float centerX = Core.graphics.getWidth() / 2f + (sector.x - offsetX / scale + sector.width / 2f) * scale;
			float centerY = Core.graphics.getHeight() / 2f + (sector.y - offsetY / scale + sector.height / 2f) * scale;
			Draw.alpha(alpha);
			Draw.rect(
				sector.viewRegion,
				centerX,
				centerY,
				sector.viewRegion.width * scale,
				sector.viewRegion.height * scale
			);
		});

		drawShader();

		sectors.each(sector -> sector.visible.get(sector.sector), sector -> {
			float centerX = Core.graphics.getWidth() / 2f + (sector.x - offsetX / scale + sector.width / 2f) * scale;
			float centerY = Core.graphics.getHeight() / 2f + (sector.y - offsetY / scale + sector.height / 2f) * scale;
			Draw.alpha(alpha);
			Styles.black6.draw(
				centerX - sector.width / 3f / 2f * scale,
				centerY - sector.height / 3f / 2f * scale,
				sector.width / 3f * scale,
				sector.height / 3f * scale
			);
			Draw.reset();
			Draw.alpha(alpha);
			Draw.rect(
				sector.icon.get().getRegion(),
				centerX, centerY,
				30 * scale, 30 * scale
			);
			Draw.reset();
		});

		if (selected != null) {
			float baseX = Core.graphics.getWidth() / 2f + (selected.x - offsetX / scale) * scale;
			float baseY = Core.graphics.getHeight() / 2f + (selected.y - offsetY / scale) * scale;

			Draw.alpha(alpha);
			Tex.buttonSelect.draw(
				baseX,
				baseY,
				selected.width * scale,
				selected.height * scale
			);
		}
	}

	public void drawShader() {
		float scale = Scl.scl(this.scale);

		Draw.flush();
		SWShaders.SectorLaunchShader shader = SWShaders.sectorLaunchShader;
		shader.opacity = parentAlpha * color.a;
		shader.pos.set(-offsetX, -offsetY);
		shader.scale = scale;
		shader.lights.clear();
		shader.boxes.clear();
		sectors.each(sector -> {
			float centerX = Core.graphics.getWidth() / 2f + (sector.x - offsetX / scale + sector.width / 2f) * scale;
			float centerY = Core.graphics.getHeight() / 2f + (sector.y - offsetY / scale + sector.height / 2f) * scale;
			if (sector.clearFog.get(sector.sector) || debugSelect) {
				shader.lights.add(centerX);
				shader.lights.add(centerY);
				shader.lights.add(sector.width * scale);
				shader.lights.add(sector.height * scale);
			}
			if (sector.hasOverlay.get(sector.sector) && !debugSelect) {
				shader.boxes.add(centerX);
				shader.boxes.add(centerY);
				shader.boxes.add(sector.width * scale);
				shader.boxes.add(sector.height * scale);
			}
		});
		Camera c = Core.camera;

		Core.camera = Core.scene.getViewport().getCamera();
		Draw.blit(shader);
		Core.camera = c;
		Draw.flush();
	}

	public PositionSectorPreset getAt(float x, float y) {
		float scale = Scl.scl(this.scale);
		return sectors.find(sector -> Tmp.r1.setCentered(
			Core.graphics.getWidth() / 2f + (sector.x - offsetX / scale + sector.width / 2f) * scale,
			Core.graphics.getHeight() / 2f + (sector.y - offsetY / scale + sector.height / 2f) * scale,
			sector.width * scale,
			sector.height * scale
		).contains(x, y));
	}

	public PositionSectorPreset getSelected() {
		return selected;
	}

	public void move(float x, float y) {
		offsetX += x;
		offsetY += y;
	}

	public float scl() {
		return scale;
	}

	public void select(@Nullable PositionSectorPreset sector) {
		selected = sector;
	}

	public void setPos(float x, float y) {
		offsetX = x;
		offsetY = y;
	}

	public void setScl(float nscl) {
		offsetX *= (nscl / scale);
		offsetY *= (nscl / scale);
		scale = nscl;
	}
}

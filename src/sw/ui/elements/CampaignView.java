package sw.ui.elements;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.scene.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import sw.graphics.*;
import sw.math.*;
import sw.type.*;

public class CampaignView extends Element {
	public static boolean debugSelect = false;

	private final Seq<PositionSectorPreset> sectors = new Seq<>();

	private float offsetX, offsetY, scale = 1;
	private PositionSectorPreset selected;

	public void build(Planet planet) {
		sectors.set(planet.sectors.select(s -> s.preset instanceof PositionSectorPreset).map(s -> (PositionSectorPreset) s.preset));
	}

	@Override
	public void draw() {
		PositionSectorPreset hover = getAt(Core.input.mouseX(), Core.input.mouseY());
		if (hover != null && hover.visible.get(hover.sector)) {
			Core.graphics.cursor(Graphics.Cursor.SystemCursor.hand);
		} else Core.graphics.restoreCursor();

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

			drawTails();
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

	// assumes selected is not null
	public void drawTails() {
		float scale = Scl.scl(this.scale);
		if (selected.launcher != null) {
			Tmp.v2.set(
				Core.graphics.getWidth() / 2f - offsetX + (selected.x + selected.width / 2f) * scale,
				Core.graphics.getHeight() / 2f - offsetY + (selected.y + selected.width / 2f) * scale
			);
			Tmp.v4.set(
				Core.graphics.getWidth() / 2f - offsetX + (selected.launcher.x + selected.launcher.width / 2f) * scale,
				Core.graphics.getHeight() / 2f - offsetY + (selected.launcher.y + selected.launcher.width / 2f) * scale
			);
			Parallax.getParallaxFrom(Tmp.v3.set(Tmp.v2).lerp(Tmp.v4, 0.5f), Core.scene.getViewport().getCamera().position, 10f, scale / 10f);
			Tmp.v6.set(Tmp.v2);
			Lines.stroke(5f, Pal.accent);
			Fill.circle(Tmp.v2.x, Tmp.v2.y, 2.5f);
			Fill.circle(Tmp.v4.x, Tmp.v4.y, 2.5f);
			for (int i = 0; i < 10; i++) {
				Bezier.quadratic(
					Tmp.v1,
					i / 9f,
					Tmp.v2, Tmp.v3, Tmp.v4,
					Tmp.v5
				);

				Lines.line(Tmp.v6.x, Tmp.v6.y, Tmp.v1.x, Tmp.v1.y, false);
				Tmp.v6.set(Tmp.v1);
			}
			Bezier.quadratic(
				Tmp.v1,
				1f - Time.globalTime / 240f % 1f,
				Tmp.v2, Tmp.v3, Tmp.v4,
				Tmp.v5
			);
			Fill.circle(Tmp.v1.x, Tmp.v1.y, 5f);
		}
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

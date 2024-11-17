package sw.ui.elements;

import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.scene.*;
import arc.scene.event.*;
import arc.scene.style.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import mindustry.gen.*;
import mindustry.ui.*;
import sw.graphics.*;
import sw.graphics.SWShaders.*;
import sw.type.*;
import sw.ui.*;

public class SectorView extends Group {
	public float margin = 15;
	public Cons<PositionSectorPreset> selector = s -> {};

	public SectorViewStyle style = new SectorViewStyle();
	public UIShader shader = SWShaders.sectorDialogBackground;

	float minx, miny, maxx, maxy;
	boolean hasSectors = false;

	@Override
	public void draw() {
		shader.alpha = parentAlpha;
		shader.pos.set(-x, -y);
		Draw.blit(shader);

		if (hasSectors) {
			Draw.alpha(parentAlpha);
			style.under.draw(minx + x, miny + y, maxx - minx, maxy - miny);
		}

		super.draw();
	}

	public void rebuild(Seq<PositionSectorPreset> sectors) {
		clear();

		minx = miny = Float.POSITIVE_INFINITY;
		maxx = maxy = Float.NEGATIVE_INFINITY;

		hasSectors = !sectors.isEmpty();

		for(PositionSectorPreset sector : sectors) {
			Table button = new Table();

			button.touchable = Touchable.enabled;
			button.addListener(new HandCursorListener());
			button.clicked(() -> selector.get(sector));
			button.setSize(sector.size);
			button.setPosition(sector.x * sector.size, sector.y * sector.size);
			button.visible = sector.unlocked();

			if (sector.sector.hasSave() || (!sector.accessible && !sector.visibleReq.contains(s -> !s.sector.hasSave()))) {
				Stack stack = button.stack(new Image(sector.mapRegion)).get();
				if (sector.accessible) {
					stack.add(new Image(SWStyles.inventoryClear));
					stack.add(new Table(t -> t.table(Styles.black6, img -> img.image(sector.icon.get())).margin(10f)));
				}
			} else {
				button.stack(new Image(Styles.black), new Image(SWStyles.inventoryClear), new Image(Icon.map)).size(sector.size);
			}

			addChild(button);

			minx = Math.min(minx, button.x);
			miny = Math.min(miny, button.y);
			maxx = Math.max(maxx, button.x + sector.size);
			maxy = Math.max(maxy, button.y + sector.size);
		}

		if (sectors.isEmpty()) return;

		minx -= margin;
		miny -= margin;
		maxx += margin;
		maxy += margin;
	}

	public static class SectorViewStyle extends Style {
		public Drawable under = ((ScaledNinePatchDrawable) SWStyles.treeBorder).tint(Color.black.cpy().a(0.6f));
	}
}

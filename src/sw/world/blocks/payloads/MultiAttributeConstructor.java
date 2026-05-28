package sw.world.blocks.payloads;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.meta.*;

public class MultiAttributeConstructor extends DrawerConstructor {
	public Rect attributeArea;
	public ObjectMap<Attribute, Block> recipes = new ObjectMap<>();

	public boolean displayEfficiency = false;
	public float baseAttribute = 1f;
	public float minAttribute = 0f;
	public float maxAttribute = 1f;
	public float attributeScl = 1f;

	public boolean cutShadow = false;

	public MultiAttributeConstructor(String name) {
		super(name);
		buildSpeed = 1;
	}

	public void addRecipes(Object... values) {
		recipes.putAll(values);
	}

	@Override
	public boolean canPlaceOn(Tile tile, Team team, int rotation) {
		for (Attribute a : recipes.keys()) {
			if (tile != null && baseAttribute + countAttribute(tile, a) >= minAttribute) return super.canPlaceOn(tile, team, rotation);
		}
		return false;
	}

	public float countAttribute(Tile tile, Attribute attribute) {
		float sum = 0;
		for (int dx = Mathf.floor(attributeArea.x); dx < Mathf.floor(attributeArea.x) + attributeArea.width; dx++) {
			for (int dy = Mathf.floor(attributeArea.y); dy < Mathf.floor(attributeArea.y) + attributeArea.height; dy++) {
				Tile cur = tile.nearby(dx, dy);
				if (cur == null) continue;

				sum += cur.floor().attributes.get(attribute);
			}
		}
		return baseAttribute + sum;
	}

	@Override
	public void drawOverlay(float x, float y, int rotation) {
		Drawf.dashRect(Pal.accent, Tmp.r1.set(attributeArea).move(x - Vars.tilesize, y - Vars.tilesize).setSize(Tmp.r1.width * Vars.tilesize, Tmp.r1.height * Vars.tilesize));
	}

	@Override
	public void drawPlace(int x, int y, int rotation, boolean valid) {
		super.drawPlace(x, y, rotation, valid);

		Attribute currentAttribute = null;
		float currentAttributeValue = 0;

		if (Vars.world.tile(x, y) != null) for (Attribute a : recipes.keys()) {
			float sum = countAttribute(Vars.world.tile(x, y), a);
			if (sum > currentAttributeValue) {
				currentAttribute = a;
				currentAttributeValue = sum;
			}
		}

		if (currentAttribute != null) {
			if (displayEfficiency) drawPlaceText(
				Core.bundle.format("bar.efficiency", (baseAttribute + Math.min(maxAttribute, attributeScl * currentAttributeValue)) * 100f),
				x, y, valid
			);

			float dx = x * Vars.tilesize + offset * Vars.tilesize - size * Vars.tilesize/2f, dy = y * Vars.tilesize + offset * Vars.tilesize + size * Vars.tilesize/2f;
			TextureRegion icon = recipes.get(currentAttribute).uiIcon;
			Draw.mixcol(Color.darkGray, 1f);
			//Fixes size because modded content icons are not scaled
			Draw.rect(icon, dx - 0.7f, dy - 1f, Draw.scl * Draw.xscl * 24f, Draw.scl * Draw.yscl * 24f);
			Draw.reset();
			Draw.rect(icon, dx, dy, Draw.scl * Draw.xscl * 24f, Draw.scl * Draw.yscl * 24f);
		}
	}

	@Override
	public boolean displayShadow(Tile tile) {
		if (!cutShadow || tile.build == null) return hasShadow;

		Tmp.r1.set(attributeArea).move(tile.build.x - Vars.tilesize, tile.build.y - Vars.tilesize).setSize(Tmp.r1.width * Vars.tilesize, Tmp.r1.height * Vars.tilesize);

		return hasShadow && !Tmp.r1.contains(tile.worldx(), tile.worldy());
	}

	@Override
	public void init() {
		super.init();

		if (attributeArea == null) attributeArea = new Rect().setCentered(1, 1, size);
	}

	@Override
	public void setStats() {
		super.setStats();
		stats.remove(Stat.output);
		stats.add(Stat.output, t -> recipes.each((attribute, block) -> {
			t.row();
			t.table(Styles.grayPanel, table -> {
				table.table(res -> {
					res.image(block.uiIcon).size(40f).padBottom(5f).row();
					res.add(Strings.autoFixed(60f / block.buildTime, 3) + StatUnit.perSecond.localized()).left().color(Color.lightGray);
				}).pad(10f).padRight(0f);
				table.image(Icon.right).color(Pal.darkishGray).pad(10f).padRight(0f).size(40f);
				table.table(tiles -> StatValues.blocks(attribute, floating, attributeScl, !displayEfficiency).display(tiles)).pad(10f);
			});
		}));
	}

	public class MultiAttributeConstructorBuild extends DrawerConstructorBuild {
		public Attribute currentAttribute;
		public float currentAttributeValue;

		public void calculateAttribute() {
			currentAttribute = null;
			currentAttributeValue = 0;
			for (Attribute a : recipes.keys()) {
				float sum = countAttribute(tile, a);
				if (sum > currentAttributeValue) {
					currentAttribute = a;
					currentAttributeValue = sum;
				}
			}
		}

		@Override
		public void drawSelect() {
			super.drawSelect();

			if (currentAttribute != null) {
				float dx = x - size * Vars.tilesize / 2f, dy = y + size * Vars.tilesize / 2f;
				TextureRegion icon = recipes.get(currentAttribute).uiIcon;
				Draw.mixcol(Color.darkGray, 1f);
				//Fixes size because modded content icons are not scaled
				Draw.rect(icon, dx - 0.7f, dy - 1f, Draw.scl * Draw.xscl * 24f, Draw.scl * Draw.yscl * 24f);
				Draw.reset();
				Draw.rect(icon, dx, dy, Draw.scl * Draw.xscl * 24f, Draw.scl * Draw.yscl * 24f);
			}
		}

		@Override
		public float efficiencyScale() {
			return super.efficiencyScale() * (baseAttribute + Math.min(currentAttributeValue * attributeScl, maxAttribute));
		}

		@Override
		public Block recipe() {
			return currentAttribute == null ? null : recipes.get(currentAttribute);
		}

		@Override
		public void onProximityUpdate() {
			super.onProximityUpdate();
			calculateAttribute();
		}
	}
}

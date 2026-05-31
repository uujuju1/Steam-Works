package sw.ui.elements;

import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.scene.*;
import arc.scene.style.*;
import arc.util.*;
import arc.util.pooling.*;
import mindustry.graphics.*;
import mindustry.ui.*;

public class GraphElement extends Element {
	public Drawable background, innerBackground;
	public float horizontalPadding;
	public float verticalPadding;

	public Color labelColor = Color.white;
	public String rowLabel, columnLabel;
	public float horizontalLabelPadding;
	public float verticalLabelPadding;

	public Func<Integer, String> rowTextProv = index -> index + "", columnTextProv = index -> index + "";
	public int rows = 10, columns = 10;
	public Color textColor = Color.white;
	public float horizontalTextPadding;
	public float verticalTextPadding;

	public Color lineColor = Color.white, startLineColor = Color.white;
	public float lineStroke = 1;
	public float horizontalLineTextPadding;
	public float verticalLineTextPadding;

	public Color progressColor = Pal.heal;
	public float progressStroke = 1f;
	public FloatFloatf progressFunc = a -> a;
	public float stepMul = 1;

	@Override
	public void draw() {
		if (background != null) {
			Draw.color(color, color.a * parentAlpha);
			background.draw(x, y, width, height);
		}
		if (innerBackground != null) {
			Draw.color(color, color.a * parentAlpha);
			innerBackground.draw(x + horizontalPadding, y + verticalPadding, width - horizontalPadding * 2f - horizontalLabelPadding, height - verticalPadding * 2f - verticalLabelPadding);
		}

		float totalWidth = width - 2f * (horizontalPadding + horizontalTextPadding + horizontalLineTextPadding) - horizontalLabelPadding;
		float totalHeight = height - 2f * (verticalPadding + verticalTextPadding + verticalLineTextPadding) - verticalLabelPadding;

		Draw.color(Tmp.c1.set(labelColor).mul(color).mul(1f, 1f, 1f, parentAlpha));
		Lines.line(
			x + horizontalPadding + horizontalTextPadding,
			y + verticalPadding + verticalTextPadding,
			x + horizontalPadding + horizontalTextPadding,
			y + verticalPadding + verticalTextPadding + height - 2 * (verticalPadding + verticalTextPadding) - verticalLabelPadding
		);
		Draw.color(Tmp.c1.set(labelColor).mul(color).mul(1f, 1f, 1f, parentAlpha));
		Lines.line(
			x + horizontalPadding + horizontalTextPadding,
			y + verticalPadding + verticalTextPadding,
			x + horizontalPadding + horizontalTextPadding + width - 2 * (verticalPadding + horizontalTextPadding) - horizontalLabelPadding,
			y + verticalPadding + verticalTextPadding
		);

		for (int i = 0; i < rows + 1; i++) {
			drawText(
				rowTextProv.get(i),
				x + horizontalPadding + horizontalTextPadding,
				y + verticalPadding + verticalTextPadding + verticalLineTextPadding + totalHeight / rows * i
			);
			Lines.stroke(lineStroke, Tmp.c1.set(lineColor).mul(color).mul(1f, 1f, 1f, parentAlpha));
			Lines.line(
				x + horizontalPadding + horizontalTextPadding * 2,
				y + verticalPadding + verticalTextPadding + verticalLineTextPadding + totalHeight / rows * i,
				x + horizontalPadding + horizontalTextPadding + horizontalLineTextPadding * 2f + totalWidth,
				y + verticalPadding + verticalTextPadding + verticalLineTextPadding + totalHeight / rows * i
			);
		}
		for (int i = 0; i < columns + 1; i++) {
			drawText(
				columnTextProv.get(i),
				x + horizontalPadding + horizontalTextPadding + horizontalLineTextPadding + totalWidth / columns * i,
				y + verticalPadding + verticalTextPadding);
			Lines.stroke(lineStroke, Tmp.c1.set(lineColor).mul(color).mul(1f, 1f, 1f, parentAlpha));
			Lines.line(
				x + horizontalPadding + horizontalTextPadding + horizontalLineTextPadding + totalWidth / columns * i,
				y + verticalPadding + verticalTextPadding * 2,
				x + horizontalPadding + horizontalTextPadding + horizontalLineTextPadding + totalWidth / columns * i,
				y + verticalPadding + verticalTextPadding + verticalLineTextPadding * 2 + totalHeight
			);
		}
		Lines.stroke(lineStroke, Tmp.c1.set(startLineColor).mul(color).mul(1f, 1f, 1f, parentAlpha));
		Lines.line(
			x + horizontalPadding + horizontalTextPadding * 2,
			y + verticalPadding + verticalTextPadding + verticalLineTextPadding,
			x + horizontalPadding + horizontalTextPadding + horizontalLineTextPadding * 2f + totalWidth,
			y + verticalPadding + verticalTextPadding + verticalLineTextPadding
		);
		Lines.line(
			x + horizontalPadding + horizontalTextPadding + horizontalLineTextPadding,
			y + verticalPadding + verticalTextPadding * 2,
			x + horizontalPadding + horizontalTextPadding + horizontalLineTextPadding,
			y + verticalPadding + verticalTextPadding + verticalLineTextPadding * 2f + totalHeight
		);

		if (rowLabel != null) drawText(rowLabel, x + horizontalPadding + horizontalTextPadding, y + height - verticalPadding - verticalLabelPadding / 2f);
		if (columnLabel != null) drawText(columnLabel, x + width - horizontalPadding - horizontalLabelPadding / 2f, y + verticalPadding + verticalTextPadding);

		Lines.stroke(progressStroke, Tmp.c1.set(progressColor).mul(color).mul(1f, 1f, 1f, parentAlpha));
		for (float i = 0; i < columns + 1; i+= 1 / stepMul) {
			if (i == columns) break;
			float cur = progressFunc.get(1f / columns * i);
			float next = progressFunc.get(1f / columns * (i + 1f / stepMul));
			Lines.line(
				x + horizontalPadding + horizontalTextPadding + horizontalLineTextPadding + totalWidth / columns * i,
				y + verticalPadding + verticalTextPadding + verticalLineTextPadding + (totalHeight) * cur,
				x + horizontalPadding + horizontalTextPadding + horizontalLineTextPadding + totalWidth / columns * (i + 1f / stepMul),
				y + verticalPadding + verticalTextPadding + verticalLineTextPadding + (totalHeight) * next
			);
		}
	}

	public void drawText(String text, float offsetX, float offsetY) {
		Draw.color(color, color.a * parentAlpha);

		Font font = Fonts.def;
		GlyphLayout lay = Pools.obtain(GlyphLayout.class, GlyphLayout::new);
		lay.setText(font, text);

		font.setColor(color.r * textColor.r, color.g * textColor.g, color.b * textColor.b, color.a * textColor.a);
		font.getCache().clear();
		font.getCache().addText(text, offsetX - lay.width / 2f, offsetY + lay.height / 2f + 1);
		font.getCache().draw(parentAlpha);

		Pools.free(lay);
	}
}

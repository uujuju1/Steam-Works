package sw.ui.elements;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.scene.*;
import arc.scene.style.*;
import arc.util.pooling.*;
import mindustry.gen.*;
import mindustry.ui.*;
import sw.gen.*;

public class SplitBar extends Element {
	private Floatp fractionLeft, fractionRight;
	private Prov<Color> colorLeft, colorRight;
	private Prov<CharSequence> nameLeft, nameRight;
	
	private float valueLeft, valueRight;
	
	@Override
	public void draw() {
		Draw.colorl(0.1f);
		Draw.alpha(parentAlpha);
		Tex.bar.draw(x, y, width, height);
		
		valueLeft = Mathf.lerpDelta(valueLeft, getTarget(true), 0.15f);
		valueRight = Mathf.lerpDelta(valueRight, getTarget(false), 0.15f);
		
		Drawable bar = Tex.barTop;
		
		Draw.color(colorLeft.get(), colorLeft.get().a * parentAlpha);
		bar.draw(
			x + Math.min(width / 2f * (1f - valueLeft), width / 2f - Core.atlas.find("bar-top").width),
			y,
			Math.max(width / 2f * valueLeft, Core.atlas.find("bar-top").width),
			height
		);
		
		Draw.color(colorRight.get(), colorRight.get().a * parentAlpha);
		bar.draw(
			x + width / 2f,
			y,
			Math.max(width / 2f * valueRight, Core.atlas.find("bar-top").width),
			height
		);
		
		Draw.color();
		Draw.alpha(parentAlpha);
		SWTex.barCenter.draw(
			x + width/2f - Core.atlas.find("bar-top").width / 2f, y,
			Core.atlas.find("bar-top").width, height
		);
		
		Draw.color();
		
		Font font = Fonts.outline;
		GlyphLayout lay = Pools.obtain(GlyphLayout.class, GlyphLayout::new);
		lay.setText(font, nameLeft.get());
		
		font.setColor(1f, 1f, 1f, 1f);
		font.getCache().clear();
		font.getCache().addText(nameLeft.get(), x + width / 4f - lay.width / 2f, y + height / 2f + lay.height / 2f);
		font.getCache().draw(parentAlpha);
		
		lay.setText(font, nameRight.get());
		
		font.setColor(1f, 1f, 1f, 1f);
		font.getCache().clear();
		font.getCache().addText(nameRight.get(), x + width * 3f / 4f - lay.width / 2f, y + height / 2f + lay.height / 2f);
		font.getCache().draw(parentAlpha);
		
		Pools.free(lay);
	}
	
	public float getTarget(boolean left) {
		float a = left ? fractionLeft.get() : fractionRight.get();
		
		if(Float.isNaN(a)) a = 0;
		if(Float.isInfinite(a)) a = 1f;
		
		return Mathf.clamp(a);
	}
	
	public SplitBar setBar(Floatp fraction, Prov<Color> color, Prov<CharSequence> name, boolean left) {
		if (left) {
			fractionLeft = fraction;
			colorLeft = color;
			nameLeft = name;
		} else {
			fractionRight = fraction;
			colorRight = color;
			nameRight = name;
		}
		
		return this;
	}
}

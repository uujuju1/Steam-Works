package sw.ui.elements;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.Texture.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.scene.style.*;
import arc.util.*;
import arc.util.pooling.*;
import mindustry.gen.*;
import mindustry.ui.*;
import sw.gen.*;
import sw.graphics.*;

public class RotationBar extends Bar {
	private Floatp angleProv;
	private CharSequence label;
	private RotationBarStyle style = new RotationBarStyle();
	
	public RotationBar(CharSequence label, Floatp rotationProv) {
		this.label = label;
		this.angleProv = rotationProv;
	}
	
	public RotationBar(Prov<CharSequence> label, Floatp rotationProv) {
		this(label.get(), rotationProv);
		update(() -> this.label = label.get());
	}
	
	@Override
	public void draw() {
		float angle = angleProv.get();
		
		if (style.outlineRadius > 0) {
			Draw.color(style.outlineColor, style.outlineColor.a * parentAlpha);
			style.outline.draw(x, y - style.outlineRadius, width, height + style.outlineRadius * 2f);
		}
		
		Draw.color(style.backgroundColor, style.backgroundColor.a * parentAlpha);
		style.background.draw(x, y, width, height);
		
		Draw.color();
		Draw.alpha(parentAlpha);
		int sides = style.axle.length;
		for (int i = 0; i < sides; i++) {
			float mod1 = Mathf.mod(angle + 360f/sides * i, 360f);
			float mod2 = Mathf.mod(angle + 360f/sides * (i + 1), 360f);
			
			float cos1 = Mathf.cos(Mathf.degreesToRadians * mod1, 1f, 1f);
			float cos2 = Mathf.cos(Mathf.degreesToRadians * mod2, 1f, 1);
			float cos3 = Mathf.cos(Mathf.degreesToRadians * (angle + 360f/sides * (i + 0.5f)), 1, 1);
			
			if (cos3 > 0f) {
				Tmp.c1.set(style.axleMedium).lerp(style.axleLight, Mathf.clamp(cos3));
			} else {
				Tmp.c1.set(style.axleDark).lerp(style.axleMedium, Mathf.clamp(cos3 + 1f));
			}
			
			Draw.mixcol(Tmp.c1, Tmp.c1.a);
			
			if (cos1 - cos2 >= 0) {
				Tmp.v1.trns(rotation, 0f, height * (cos1 + cos2)/4f);
				
				TextureFilter req = Core.settings.getBool("sw-ui-filter", true) ? TextureFilter.linear : TextureFilter.nearest;
				if (((TextureRegionDrawable) style.axle[i]).getRegion().texture.getMagFilter() != req) ((TextureRegionDrawable) style.axle[i]).getRegion().texture.setFilter(req);
				
				Draw.rect(
					((TextureRegionDrawable) style.axle[i]).getRegion(),
					x + Tmp.v1.x + width/2f,
					y + Tmp.v1.y + height/2f,
					width,
					height * (cos1 - cos2)/2f,
					rotation
				);
			}
		}
		Draw.reset();
		
		Draw.color();
		Draw.alpha(parentAlpha);
		style.top.draw(x, y - style.outlineRadius, width, height + style.outlineRadius * 2);
		
		Draw.color();
		
		Font font = Fonts.outline;
		GlyphLayout lay = Pools.obtain(GlyphLayout.class, GlyphLayout::new);
		lay.setText(font, label);
		
		font.setColor(style.fontColor);
		font.getCache().clear();
		font.getCache().addText(label, x + width / 2f - lay.width / 2f, y + height / 2f + lay.height / 2f);
		font.getCache().draw(parentAlpha);
		
		Pools.free(lay);
	}
	
	public void setAngle(Floatp angleProv) {
		this.angleProv = angleProv;
	}
	
	public void setStyle(RotationBarStyle style) {
		this.style = style;
	}
	
	public static class RotationBarStyle {
		public Drawable background = Tex.whiteui;
		public Drawable outline = Tex.whiteui;
		public Drawable[] axle = new Drawable[]{SWTex.barAxle1, SWTex.barAxle2, SWTex.barAxle1, SWTex.barAxle2};
		public Drawable top = SWTex.barEdge;
		
		public float outlineRadius = 0;
		
		public Color backgroundColor = Color.grays(0.1f);
		public Color outlineColor = new Color();
		public Color fontColor = Color.white;
		
		public Color axleLight = SWPal.axleLight;
		public Color axleMedium = SWPal.axleMedium;
		public Color axleDark = SWPal.axleDark;
	}
}

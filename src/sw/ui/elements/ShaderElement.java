package sw.ui.elements;

import arc.graphics.g2d.*;
import arc.scene.*;
import sw.graphics.*;

public class ShaderElement extends Element {
	private SWShaders.UIShader shader;

	public ShaderElement(SWShaders.UIShader shader) {
		setShader(shader);
	}

	public SWShaders.UIShader getShader() {
		return shader;
	}
	public void setShader(SWShaders.UIShader shader) {
		this.shader = shader;
	}

	@Override
	public void draw() {
		shader.pos.set(x, y);
		shader.alpha = parentAlpha;
		Draw.blit(shader);
	}
}

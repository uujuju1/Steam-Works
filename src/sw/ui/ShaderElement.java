package sw.ui;

import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import arc.scene.*;

public class ShaderElement extends Element {
	private Shader shader;

	public ShaderElement(Shader shader) {
		setShader(shader);
	}

	public Shader getShader() {
		return shader;
	}
	public void setShader(Shader shader) {
		this.shader = shader;
	}

	@Override
	public void draw() {
		Draw.blit(shader);
	}
}

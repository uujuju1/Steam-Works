package sw.graphics.menus;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.graphics.*;

import static mindustry.Vars.*;

public class MenuBackgroundShader extends MenuBackground {
	Shader shader;
	Texture noiseTex, baseTex;

	public MenuBackgroundShader() {
		init = menu -> {
			load();
		};
	}

	public void apply() {
		Color color = Pal.accent;
		shader.setUniformf("u_campos", Core.camera.position.x - Core.camera.width / 2, Core.camera.position.y - Core.camera.height / 2);
		shader.setUniformf("u_resolution", Core.camera.width, Core.camera.height);
		shader.setUniformf("u_time", Time.time);
		shader.setUniformf("u_baseColor", color.r, color.g, color.b);

		if(shader.hasUniform("u_noise")){
			if(noiseTex == null) {
				noiseTex = Core.assets.get("sprites/noise.png", Texture.class);
			}

			noiseTex.bind(1);
			renderer.effectBuffer.getTexture().bind(0);

			shader.setUniformi("u_noise", 1);
		}
	}

	public void load() {
		shader = new Shader(Core.files.internal("shaders/screenspace.vert"), Vars.tree.get("shaders/menuBackground.frag"));
		Core.assets.load("sprites/noise.png", Texture.class).loaded = t -> {
			t.setFilter(Texture.TextureFilter.linear);
			t.setWrap(Texture.TextureWrap.repeat);
		};
	}

	@Override
	public void render() {
		Draw.draw(0f, () -> {
			Draw.flush();
			shader.bind();
			apply();
			Draw.blit(Blocks.darkPanel3.region.texture, shader);
			Draw.flush();
		});
	}
}

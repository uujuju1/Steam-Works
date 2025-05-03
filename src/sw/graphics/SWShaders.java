package sw.graphics;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.*;
import mindustry.graphics.*;
import sw.*;

public class SWShaders {
	public static PitfallShader pitfall;

	public static UIShader sectorDialogBackground, techtreeBackground;

	public static CacheLayer pitfallLayer;

	public static void load() {
		sectorDialogBackground = new UIShader("sectorDialogBackground");
		techtreeBackground = new UIShader("techtreeBackground");

		pitfall = new PitfallShader("pitfall");
		pitfallLayer = new CacheLayer.ShaderLayer(pitfall);
		CacheLayer.add(pitfallLayer);
	}

	public static class UIShader extends Shader {
		public Vec2 pos = new Vec2();
		public float alpha = 1;

		public UIShader(String frag) {
			super(Core.files.internal("shaders/screenspace.vert"), Vars.tree.get("shaders/" + frag + ".frag"));
		}

		@Override
		public void apply() {
			setUniformf("u_resolution", Core.graphics.getWidth(), Core.graphics.getHeight());
			setUniformf("u_position", pos.x, pos.y);
			setUniformf("u_opacity", alpha);
			setUniformf("u_time", Time.globalTime);
		}
	}

	public static class SWSurfaceShader extends Shader {
		public Texture noise;
		public String noiseName = "noise";

		public SWSurfaceShader(String frag) {
			super(Core.files.internal("shaders/screenspace.vert"), Vars.tree.get("shaders/" + frag + ".frag"));
			loadNoise();
		}

		@Override
		public void apply(){
			setUniformf("u_campos", Core.camera.position.x - Core.camera.width / 2, Core.camera.position.y - Core.camera.height / 2);
			setUniformf("u_resolution", Core.camera.width, Core.camera.height);
			setUniformf("u_time", Time.time);

			if(hasUniform("u_noise")){
				if(noise == null){
					noise = Core.assets.get("sprites/" + noiseName + ".png", Texture.class);
				}

				noise.bind(1);
				Vars.renderer.effectBuffer.getTexture().bind(0);

				setUniformi("u_noise", 1);
			}
		}

		public void loadNoise(){
			Core.assets.load("sprites/" + noiseName + ".png", Texture.class).loaded = t -> {
				t.setFilter(Texture.TextureFilter.linear);
				t.setWrap(Texture.TextureWrap.repeat);
			};
		}
	}

	public static class PitfallShader extends SWSurfaceShader {
		public String wallTilesName = "sw-pitfall-wall-tiles";
		public float wallTilesWidth = 32f;
		public TextureRegion wallTiles;

		public String gratingName = "sw-pitfall-grating-beam";
		public TextureRegion grating;

		public PitfallShader(String frag) {
			super(frag);
		}

		@Override
		public void apply(){
			super.apply();

			Core.camera.project(Tmp.v1.setZero());
			setUniformf(
				"u_maskprojectionuv",
				Tmp.v1.x / Core.graphics.getWidth(),
				Tmp.v1.y / Core.graphics.getHeight()
			);
			Core.camera.project(Tmp.v1.set(Vars.world.unitWidth(), Vars.world.unitWidth()));
			setUniformf(
				"u_maskprojectionuv2",
				Tmp.v1.x / Core.graphics.getWidth(),
				Tmp.v1.y / Core.graphics.getHeight()
			);

			setUniformf("u_masksize", SWVars.renderer.pitfall.width, SWVars.renderer.pitfall.height);

			setUniformf("u_scale", 64 / Mathf.log(2, 2 * Vars.renderer.getDisplayScale()));

			Vars.renderer.effectBuffer.getTexture().bind(0);
			SWVars.renderer.pitfall.bind(1);
			setUniformi("u_mask", 1);

			applyWall();
			applyGrating();
		}

		public void applyWall() {
			if (wallTiles == null) {
				wallTiles = Core.atlas.find(wallTilesName);
			}

			wallTiles.texture.bind(2);
			setUniformi("u_wall", 2);
			setUniformf("u_walluv", wallTiles.u, wallTiles.v, wallTiles.u2, wallTiles.v2);
			setUniformf("u_wallsize", wallTiles.width, wallTiles.height);
			setUniformf("u_walltilesize", wallTilesWidth);
		}

		public void applyGrating() {
			if (grating == null) {
				grating = Core.atlas.find(gratingName);
			}

			grating.texture.bind(3);
			setUniformi("u_grating", 3);
			setUniformf("u_gratinguv", grating.u, grating.v, grating.u2, grating.v2);
			setUniformf("u_gratingsize", grating.width, grating.height);
		}
	}
}

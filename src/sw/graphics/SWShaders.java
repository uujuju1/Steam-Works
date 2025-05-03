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
	public static ChasmShader chasm;

	public static UIShader sectorDialogBackground, techtreeBackground;

	public static CacheLayer pitfallLayer, chasmLayer;

	public static void load() {
		sectorDialogBackground = new UIShader("sectorDialogBackground");
		techtreeBackground = new UIShader("techtreeBackground");

		pitfall = new PitfallShader("pitfall");
		pitfallLayer = new CacheLayer.ShaderLayer(pitfall);
		CacheLayer.add(pitfallLayer);

		chasm = new ChasmShader("chasm");
		chasmLayer = new CacheLayer.ShaderLayer(chasm);
		CacheLayer.add(chasmLayer);
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

	public static class ChasmShader extends Shader {
		TextureRegion toplayer;
		TextureRegion bottomlayer;
		TextureRegion truss;

		Texture noiseTex;

		String toplayerName = "sw-concrete-stripes1";
		String bottomlayerName = "sw-concrete1";
		String trussName = "sw-void-tile-edge";
		String noiseTexName = "noise";

		public float sampleLen = 20;
		public float epsilonp1 = 1.01f;

		public ChasmShader(String frag) {
			super(Core.files.internal("shaders/screenspace.vert"), Vars.tree.get("shaders/" + frag + ".frag"));
			loadNoise();
		}

		@Override
		public void apply(){
			var texture = Core.atlas.find("grass1").texture;
			if(toplayer == null){
				toplayer = Core.atlas.find(toplayerName);
			}
			if(bottomlayer == null){
				bottomlayer = Core.atlas.find(bottomlayerName);
			}
			if(truss == null){
				truss = Core.atlas.find(trussName);
			}
			if(noiseTex == null){
				noiseTex = Core.assets.get("sprites/" + textureName() + ".png", Texture.class);
			}
			setUniformf("u_campos", Core.camera.position.x - Core.camera.width / 2, Core.camera.position.y - Core.camera.height / 2);
			setUniformf("u_resolution", Core.camera.width, Core.camera.height);
			setUniformf("u_time", Time.time);
//tvariants
			setUniformf("u_toplayer", toplayer.u, toplayer.v, toplayer.u2, toplayer.v2);
			setUniformf("u_bottomlayer", bottomlayer.u, bottomlayer.v, bottomlayer.u2, bottomlayer.v2);
			setUniformf("bvariants", bottomlayer.width/32f);
			setUniformf("u_truss", truss.u, truss.v, truss.u2, truss.v2);

			setUniformf("u_maskSize", SWVars.renderer.pitfall.width, SWVars.renderer.pitfall.height);

			setUniformf("samplelen", sampleLen);
			setUniformf("epsilonp1", epsilonp1);

			texture.bind(2);
			noiseTex.bind(1);
			SWVars.renderer.pitfall.bind(0);
			setUniformi("u_noise", 1);
			setUniformi("u_texture2", 2);
		}

		public void loadNoise(){
			Core.assets.load("sprites/" + textureName() + ".png", Texture.class).loaded = t -> {
				t.setFilter(Texture.TextureFilter.linear);
				t.setWrap(Texture.TextureWrap.repeat);
			};
		}

		public String textureName(){
			return noiseTexName;
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

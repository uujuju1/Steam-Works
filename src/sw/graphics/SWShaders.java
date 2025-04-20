package sw.graphics;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
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

	public static class PitfallShader extends Shader {
		public float spacing = 1;
		public Vec2 scl = new Vec2(3, 1);
		public Vec2 clip = new Vec2();

		public PitfallShader(String frag) {
			super(Core.files.internal("shaders/screenspace.vert"), Vars.tree.get("shaders/" + frag + ".frag"));
		}

		@Override
		public void apply(){
			setUniformf("u_campos", Core.camera.position.x, Core.camera.position.y);
			setUniformf("u_resolution", Core.camera.width, Core.camera.height);
			setUniformf("u_time", Time.time);

			setUniformf("u_maskSize", SWVars.renderer.pitfall.width, SWVars.renderer.pitfall.height);

			setUniformf("u_spacing", spacing);
			setUniformf("u_scl", scl.x / Vars.renderer.getDisplayScale(), scl.y / Vars.renderer.getDisplayScale());
			setUniformf("u_clip", clip.x, clip.y);

			Vars.renderer.effectBuffer.getTexture().bind(0);
			SWVars.renderer.pitfall.bind(1);

			setUniformi("u_mask", 1);
		}
	}
}

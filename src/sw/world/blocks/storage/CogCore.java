package sw.world.blocks.storage;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.graphics.*;
import mindustry.world.blocks.storage.*;
import sw.annotations.Annotations.*;

public class CogCore extends CoreBlock {
	public Interp cogShowInterp = Interp.linear;
	public int cogs = 4;
	public float cogMaxOffset = -1f;
	
	public Interp cogRotateInterp = Interp.linear;
	public float minCogRotations = 3.5f;
	public float maxCogRotations = 10f;
	
	public float particleTime = 0.5f;
	public Effect particleEffect = Fx.coreLandDust;
	
	public Interp fadeOutInterp = Interp.linear;
	
	public @Load("@name$-cog") TextureRegion cogRegion;
	public @Load("@name$-cog-small") TextureRegion smallCogRegion;
	
	private final Rand rand = new Rand();
	
	public CogCore(String name) {
		super(name);
	}
	
	@Override
	public void init() {
		super.init();
		
		if (cogMaxOffset < 0) cogMaxOffset = size * 4f;
	}
	
	public class CogCoreBuild extends CoreBuild {
		@Override
		public void beginLaunch(boolean launching) {
		
		}
		
		@Override
		public void drawLaunch() {
			rand.setSeed(id);
			for(int i = 0; i < cogs; i++) {
				TextureRegion cogSprite = rand.nextBoolean() ? cogRegion : smallCogRegion;
				Tmp.v1.trns(rand.range(360f), cogMaxOffset * cogShowInterp.apply(Vars.renderer.getLandTimeIn()));
				
				Draw.rect(
					cogSprite,
					x + Tmp.v1.x,
					y + Tmp.v1.y,
					rand.random(360f) + rand.random(minCogRotations, maxCogRotations) * 360f * cogRotateInterp.apply(Vars.renderer.getLandTimeIn())
				);
			}
			Draw.rect(region, x, y);
			
			Draw.z(Layer.max);
			Draw.color(Color.black);
			Draw.alpha(fadeOutInterp.apply(Vars.renderer.getLandTimeIn()));
			Draw.rect();
			Draw.reset();
		}
		
		@Override
		public void updateLaunch() {
			if (Vars.renderer.getLandTimeIn() > particleTime) return;
			landParticleTimer += Time.delta;
			if(landParticleTimer >= 1f){
				tile.getLinkedTiles(t -> {
					if(Mathf.chance(0.4f)){
						particleEffect.at(t.worldx(), t.worldy(), angleTo(t.worldx(), t.worldy()) + Mathf.range(30f), Tmp.c1.set(t.floor().mapColor).mul(1.5f + Mathf.range(0.15f)));
					}
				});
				
				landParticleTimer = 0f;
			}
		}
		
		@Override
		public float zoomLaunch() {
			Core.camera.position.set(this);
			return 4f;
		}
	}
}

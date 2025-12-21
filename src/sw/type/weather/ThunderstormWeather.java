package sw.type.weather;

import arc.*;
import arc.audio.*;
import arc.graphics.*;
import arc.math.*;
import arc.util.*;
import mindustry.*;
import mindustry.entities.bullet.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.type.weather.*;

public class ThunderstormWeather extends RainWeather {
	public String texturePath = "clouds";
	public @Nullable Texture texture;
	public Color textureColor = Color.black;
	public int layers = 3;
	public float
		alphaMin = 0.4f, alphaMax = 0.1f,
		sclMin = 2000f, sclMax = 5000f,
		speedMin = 1f, speedMax = 3f;

	// bullet related fields
	public Sound bulletSound = Sounds.none;
	public float bulletSoundPitch = 1f;
	public float bulletSoundVolume = 1f;
	public @Nullable BulletType lightningBullet;
	public float bulletChance = 0.001f;

	public ThunderstormWeather(String name) {
		super(name);
	}

	@Override
	public void drawOver(WeatherState state) {
		super.drawOver(state);
		if(texture == null){
			texture = Core.assets.get("sprites/" + texturePath + ".png", Texture.class);
			texture.setWrap(Texture.TextureWrap.repeat);
			texture.setFilter(Texture.TextureFilter.linear);
		}
		float offset = 0f;
		for(int i = 0; i < layers; i++){
			drawNoise(
				texture,
				textureColor,
				Mathf.map(i, 0f, layers, sclMin, sclMax),
				Mathf.map(i, 0f, layers, alphaMin, alphaMax) * state.opacity,
				Mathf.map(i, 0, layers, speedMin, speedMax),
				state.intensity,
				state.windVector.x,
				state.windVector.y,
				offset
			);
			offset += 0.29f;
		}
	}

	@Override
	public void update(WeatherState state) {
		if (Mathf.chance(bulletChance) && lightningBullet != null) {
			float x = Mathf.random((float) Vars.world.unitWidth());
			float y = Mathf.random((float) Vars.world.unitHeight());
			lightningBullet.create(state, Team.derelict, x, y, state.windVector.angle());
			lightningBullet.shootEffect.at(x, y, state.windVector.angle(), lightningBullet.hitColor);
			bulletSound.at(x, y, bulletSoundPitch, bulletSoundVolume);
		}
	}
}

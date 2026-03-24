package sw.type.weather;

import arc.math.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.type.weather.*;

public class ThunderstormWeather extends RainWeather {
	public int spawns = 0;
	public float spawnChance = 0.5f;
	public float spawnRotation = -1f;
	public BulletType spawnBullet = Bullets.placeholder;
	
	public ThunderstormWeather(String name) {
		super(name);
	}

	public boolean shouldSpawn(WeatherState state){
		return Mathf.chance(spawnChance);
	}

	public void spawn(WeatherState state, float x, float y){
		spawnBullet.create(null, Team.derelict, x, y, spawnRotation < 0 ? rand.random(360f) : spawnRotation);
	}

	@Override
	public void update(WeatherState state){
		if(Vars.net.client()) return;
		rand.setSeed((long) Time.time * Mathf.random(Vars.world.unitHeight(), Vars.world.unitWidth()));

		for(int spawn = 0; spawn < spawns; spawn++){
			if(shouldSpawn(state)){
				float rx = rand.random(0f, Vars.world.unitWidth());
				float ry = rand.random(0f, Vars.world.unitHeight());
				spawn(state, rx, ry);
			}
		}
	}
}

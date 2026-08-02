package sw.type.weather;

import arc.math.*;
import arc.util.*;
import arc.util.noise.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.game.*;
import mindustry.gen.*;

import static mindustry.Vars.*;

public class FireStormWeather extends DustStormWeather {
	public int spawns;
	public float maxScale = 100f * 60f;
	public float radius, radiusIncrease;
	public float damage, damageIncrease;
	public float incend, incendIncrease;

	public FireStormWeather(String name) {
		super(name);
	}

	@Override
	public void updateEffect(WeatherState state) {
		if(!Vars.net.client()) {
			if (state.effectTimer < maxScale) state.effectTimer += Time.delta;
			rand.setSeed((long) Time.time * Mathf.random(Vars.world.unitHeight(), Vars.world.unitWidth()));

			for (int spawn = 0; spawn < spawns; spawn++) {
				float rx = rand.random(0f, Vars.world.unitWidth());
				float ry = rand.random(0f, Vars.world.unitHeight());

				Damage.damage(Team.derelict, rx, ry, radius + radiusIncrease * state.effectTimer, damage + damageIncrease * state.effectTimer, true, true, true, true, null, 1f);

				if (status != StatusEffects.none) Damage.status(Team.derelict, rx, ry, radius + radiusIncrease * state.effectTimer, status, statusDuration, true, true);

				Damage.createIncend(rx, ry, radius + radiusIncrease * state.effectTimer, Mathf.round(incend + incendIncrease * state.effectTimer));
			}
		}

		if(!headless && sound != Sounds.none){
			float noise = soundVolOscMag > 0 ? (float)Math.abs(Noise.rawNoise(Time.time / soundVolOscScl)) * soundVolOscMag : 0;
			control.sound.loop(sound, Math.max((soundVol + noise) * state.opacity, soundVolMin));
		}
	}
}

package sw.maps.generators;


import arc.math.*;
import arc.struct.*;
import mindustry.game.*;
import mindustry.type.*;

import static sw.content.SWUnitTypes.*;

public class SWWaveGeneration {
	public static UnitType[][] ground = {
		{focus, precision, target},
		{sentry, tower, castle},
		{existence, remembered, presence}
	};
	public static UnitType[][] naval = {
		{recluse, retreat, evade}
	};
	public static UnitType[][] air = {
		{fly, spin, gyro}
	};

	public static Seq<SpawnGroup> navalGen(float difficulty, int maxWave, Rand rand) {
		Seq<SpawnGroup> out = new Seq<>();

		for (int i = 0; i < maxWave; i++) {
			if(rand.random(10f) > 7.5f && i > maxWave/3) continue;

			int id = Mathf.clamp(Mathf.round(difficulty/10f) + rand.range(naval.length - 1) + (i < maxWave/2 ? 0 : 1), 0, naval.length);
			final int wave = i;
			out.add(new SpawnGroup(naval[rand.random(naval.length - 1)][id]) {{
				begin = wave;
				if (wave < maxWave - 5) end = wave + rand.random(5);

				unitAmount = Math.max(1, Math.round((rand.random(difficulty)/2f) - id));
				float scaling = rand.random(7.5f);
				if (scaling < 4f) unitScaling = scaling;
				max = unitAmount * 2;

				shields = Mathf.maxZero(difficulty * 3f - wave);
				shieldScaling = shields > 0 ? difficulty : 0f;

				spacing = scaling < 2.5f ? 1 : rand.random(1, 2);
			}});
		}
		return out;
	}
}

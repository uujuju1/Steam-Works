package sw.maps.generators;

import arc.graphics.*;
import arc.math.geom.*;
import mindustry.game.*;
import mindustry.maps.generators.*;

public class UnknownPlanetGenerator extends PlanetGenerator {

	public UnknownPlanetGenerator() {
		defaultLoadout = Schematics.readBase64("bXNjaAF4nGNgYmBiZmDJS8xNZeB1zi9KjUxPTylKLM7MYeBOSS1OLsosKMnMz2NgYGDLSUxKzSlmYIqOZWQQLC7XTSoqLUnMySwu0U0G6gOqYGQAA0YAwcYVpg==");
	}

	@Override public float getHeight(Vec3 position) {
		return -2;
	}
	@Override public Color getColor(Vec3 position) {
		return Color.clear;
	}
}

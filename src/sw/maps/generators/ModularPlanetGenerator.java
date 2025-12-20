package sw.maps.generators;

import arc.math.geom.*;
import mindustry.game.*;
import mindustry.maps.generators.*;
import mindustry.type.*;

public class ModularPlanetGenerator extends PlanetGenerator {
	@Override
	public void addWeather(Sector sector, Rules rules) {
		if (sector.preset != null) {
			sector.preset.rules.get(rules);
		}
	}

	@Override
	public void generateSector(Sector sector) {}

	@Override public float getHeight(Vec3 position) {
		return 0;
	}
}

package sw.maps.generators;

import arc.graphics.*;
import arc.math.geom.*;
import arc.struct.*;
import mindustry.game.*;
import mindustry.maps.generators.*;
import mindustry.type.*;
import sw.maps.*;

public class ModularPlanetGenerator extends PlanetGenerator {
	public Seq<HeightPass> heights = new Seq<>();
	public Seq<ColorPass> colors = new Seq<>();
	public float baseHeight = 0;
	public Color baseColor = Color.white;

	@Override
	public void addWeather(Sector sector, Rules rules) {
		if (sector.preset != null) {
			sector.preset.rules.get(rules);
		}
	}

	public float rawHeight(Vec3 position) {
		float height = baseHeight;
		for (HeightPass h : heights) {
			height = h.height(position, height);
		}
		return height;
	}

	@Override
	public void generateSector(Sector sector) {

	}

	@Override
	public float getHeight(Vec3 position) {
		return rawHeight(position);
	}
}

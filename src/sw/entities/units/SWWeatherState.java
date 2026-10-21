package sw.entities.units;

import arc.*;
import arc.graphics.g2d.*;
import mindustry.*;
import mindustry.gen.*;
import sw.type.weather.*;

public class SWWeatherState extends WeatherState {
	@Override public int classId() {
		return EntityMaps.ids.get(getClass().getName());
	}

	@Override
	public void draw() {
		if (Vars.renderer.weatherAlpha > 0.0001f && Vars.renderer.drawWeather && Core.settings.getBool("showweather")) {
			Draw.draw(((SWWeather) weather).layerOver, () -> {
				Draw.alpha(Vars.renderer.weatherAlpha * opacity * weather.opacityMultiplier);
				weather.drawOver(this);
				Draw.reset();
			});
			Draw.draw(((SWWeather) weather).layerUnder, () -> {
				Draw.alpha(Vars.renderer.weatherAlpha * opacity * weather.opacityMultiplier);
				weather.drawUnder(this);
				Draw.reset();
			});
		}
	}
}

package sw.type.weather;

import mindustry.type.*;
import sw.entities.units.*;

public class SWWeather extends Weather {
	public float layerUnder = 20, layerOver = 130f;

	public SWWeather(String name) {
		super(name, SWWeatherState::new);
	}
}

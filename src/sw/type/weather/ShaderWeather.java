package sw.type.weather;

import arc.graphics.g2d.*;
import mindustry.gen.*;
import sw.graphics.SWShaders.*;

public class ShaderWeather extends SWWeather {
	public WeatherShader shader;

	public ShaderWeather(String name, WeatherShader shader) {
		super(name);

		this.shader = shader;
	}

	@Override
	public void drawOver(WeatherState state) {
		super.drawOver(state);

		shader.intensity = state.opacity;
		Draw.blit(shader);
	}
}

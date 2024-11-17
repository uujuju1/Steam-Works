package sw.content;

import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.meta.*;
import sw.type.weather.*;

public class SWWeathers {
	public static Weather thunder, souesiteDust;

	public static void load() {
		thunder = new ThunderstormWeather("thunder") {{
			lightningBullet = new BulletType() {{
				collides = false;
				collidesAir = collidesGround = true;
				instantDisappear = true;

				despawnEffect = SWFx.lightning;

				splashDamage = 30;
				splashDamageRadius = 24f;
			}};

			bulletChance = 0.01f;

			attrs.set(Attribute.light, -0.5f);
			attrs.set(Attribute.water, 0.5f);
			status = StatusEffects.wet;
			sound = Sounds.rain;
			soundVol = 0.25f;
		}};
		souesiteDust = new DustStormWeather("souesite-dust");
	}
}

package sw.content;

import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import sw.type.weather.*;

public class SWWeathers {
	public static Weather
		embers,
		souesiteDust;

	public static void load() {
//		thunder = new ThunderstormWeather("thunder") {{
//			lightningBullet = new BulletType() {{
//				collides = false;
//				collidesAir = collidesGround = true;
//				instantDisappear = true;
//
//				despawnEffect = SWFx.lightning;
//
//				splashDamage = 30;
//				splashDamageRadius = 24f;
//			}};
//
//			bulletChance = 0.01f;
//
//			attrs.set(Attribute.light, -0.5f);
//			attrs.set(Attribute.water, 0.5f);
//			status = StatusEffects.wet;
//			sound = Sounds.rain;
//			soundVol = 0.25f;
//		}};
		souesiteDust = new DustStormWeather("souesite-dust") {{
			sound = Sounds.windHowl;
		}};
		embers = new EmberParticleWeather("embers") {{
			particleRegion = "sw-ember";
			
			density = 4800f;
			
			xspeed = -9f / 12f;
			yspeed = 2f / 12f;
			sizeMin = 5f;
			sizeMax = 7f;
			
			color = Pal.darkPyraFlame;
			lightColor = Pal.darkFlame;
			lightOpacity = 0.3f;
			lightRadiusMin = 20f;
			lightRadiusMax = 40f;
			
			sound = Sounds.windHowl;
			soundVol = 0f;
			soundVolOscMag = 1.5f;
			soundVolOscScl = 1100f;
			soundVolMin = 0.02f;
		}};
	}
}

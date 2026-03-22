package sw.content;

import arc.math.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import sw.content.blocks.*;
import sw.type.weather.*;

public class SWWeathers {
	public static Weather
		thunderstorm,
		embers,
		souesiteDust;

	public static void load() {
		thunderstorm = new ThunderstormWeather("thunderstorm") {{
			spawns = 3;
			spawnChance = 0.05f;
			spawnBullet = new BulletType() {{
				collides = false;
				collidesAir = collidesGround = true;
				instantDisappear = true;

				despawnEffect = SWFx.lightning;
			}

				// maybe a separate class for this? i dunno there's only one of it
				@Override
				public void despawned(Bullet b) {
					super.despawned(b);

					for (int x = -5; x < 5; x++) {
						for (int y = -5; y < 5; y++) {
							Tile current = Vars.world.tile(b.tileX() + x, b.tileY() + y);

							if (current != null && current.block() == SWEnvironment.lightningRod && Mathf.chance(0.5f)) {
								SWUnitTypes.ballLightning.spawn(Team.crux, current.worldx(), current.worldy(), Mathf.random(360f), u -> u.vel.trns(u.rotation, 4f));
							}
						}
					}
				}
			};

			xspeed = 6f;
			yspeed = 20f;
			density = 600f;

			attrs.set(Attribute.light, -0.5f);
			attrs.set(Attribute.water, 0.5f);
			status = StatusEffects.wet;
			sound = Sounds.rain;
			soundVol = 0.25f;
		}};
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
			lightOpacity = 0.15f;
			lightRadiusMin = 20f;
			lightRadiusMax = 40f;
			
			sound = Sounds.wind2;
			soundVol = 0f;
			soundVolOscMag = 1.5f;
			soundVolOscScl = 1100f;
			soundVolMin = 0.02f;
		}};
	}
}

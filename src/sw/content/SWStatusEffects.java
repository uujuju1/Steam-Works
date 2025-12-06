package sw.content;

import arc.graphics.*;
import arc.math.*;
import mindustry.content.*;
import mindustry.type.*;
import sw.type.*;

public class SWStatusEffects {
	public static StatusEffect flammable;
	
	public static void load() {
		flammable = new PhotosensitiveStatusEffect("flammable", 0.85f) {{
			outline = true;
			
			color = Color.valueOf("DFF381");
			
			damage = 50f / 60f;
			effect = Fx.burning;
			transitionDamage = 8f;
			
			init(() -> {
				opposite(StatusEffects.wet, StatusEffects.freezing);
				affinity(StatusEffects.tarred, (unit, result, time) -> {
					unit.damagePierce(transitionDamage);
					Fx.burning.at(unit.x + Mathf.range(unit.bounds() / 2f), unit.y + Mathf.range(unit.bounds() / 2f));
					result.set(StatusEffects.burning, Math.min(time + result.time, 300f));
				});
			});
		}};
	}
}

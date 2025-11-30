package sw.type;

import mindustry.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.type.*;

public class PhotosensitiveStatusEffect extends StatusEffect {
	public float minLight;
	
	public PhotosensitiveStatusEffect(String name, float minLight) {
		super(name);
		this.minLight = minLight;
		generateIcons = false;
	}
	
	@Override
	public void update(Unit unit, StatusEntry entry) {
		boolean hasLight = Groups.build.contains(b -> b.block.emitLight && (b.efficiency > 0 ? unit.dst(b) / b.block.lightRadius * b.efficiency : 2f) < minLight);
		hasLight |= Vars.state.rules.ambientLight.a <= minLight && Vars.state.rules.lighting;
		
		if (hasLight) super.update(unit, entry);
	}
}

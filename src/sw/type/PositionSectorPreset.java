package sw.type;

import arc.*;
import arc.func.*;
import arc.graphics.g2d.*;
import arc.scene.style.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.type.*;

public class PositionSectorPreset extends SectorPreset {
	public @Nullable String regionOverride;
	
	public Prov<Drawable> icon = () -> Icon.waves;
	
	public Boolf<Sector> clearFog = Sector::hasSave;

	public float x = 0, y = 0;
	public float width = 100, height = 100;

	public TextureRegion mapRegion;

	public PositionSectorPreset(String name, Planet planet, int sector) {
		super(name, planet, sector);
	}

	@Override
	public void load() {
		super.load();

		if (regionOverride != null) {
			mapRegion = Core.atlas.find(regionOverride);
		} else {
			mapRegion = Core.atlas.find(planet.name + "-preview-" + sector.id);
		}
	}
}

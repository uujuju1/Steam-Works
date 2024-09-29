package sw.type;

import arc.*;
import arc.graphics.g2d.*;
import arc.scene.style.*;
import arc.struct.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.type.*;

public class PositionSectorPreset extends SectorPreset {
	public @Nullable String regionOverride;

	/**
	 * Icon shown when the sector has a save. Disabled with accessible = false
	 */
	public Drawable icon = Icon.waves;

	/**
	 * require those sectors to have a save for it to be visible. Only works with accessible = false
	 */
	public Seq<PositionSectorPreset> visibleReq = new Seq<>();

	public int x = 0, y = 0;
	public boolean accessible = true;

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

package sw.type;

import arc.func.*;
import arc.scene.style.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.blocks.storage.*;

public class PositionSectorPreset extends SectorPreset {
	public Prov<Drawable> icon = () -> Icon.waves;
	
	public Boolf<Sector>
		clearFog = Sector::hasSave,
		hasOverlay = sector -> unlocked() && !sector.hasSave(),
		visible = sector -> unlocked();
	
	public @Nullable PositionSectorPreset launcher;
	public CoreBlock core = (CoreBlock) Blocks.coreShard;

	public float x = 0, y = 0;
	public float width = 100, height = 100;

	public PositionSectorPreset(String name, Planet planet, int sector) {
		super(name, planet, sector);
	}
}

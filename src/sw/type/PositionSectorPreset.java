package sw.type;

import arc.*;
import arc.func.*;
import arc.graphics.g2d.*;
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
	
	public TextureRegion viewRegion;

	public PositionSectorPreset(String name, Planet planet, int sector) {
		super(name, planet, sector);
	}
	
	@Override
	public void loadIcon() {
		uiIcon = fullIcon = Core.atlas.find(name);
		viewRegion = Core.atlas.find(name + "-view");
		if (!uiIcon.found()) super.loadIcon();
	}
}

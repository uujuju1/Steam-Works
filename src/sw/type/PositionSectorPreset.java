package sw.type;

import arc.func.*;
import arc.scene.style.*;
import mindustry.gen.*;
import mindustry.type.*;

public class PositionSectorPreset extends SectorPreset {
	public Prov<Drawable> icon = () -> Icon.waves;
	
	public Boolf<Sector> clearFog = Sector::hasSave;

	public float x = 0, y = 0;
	public float width = 100, height = 100;

	public PositionSectorPreset(String name, Planet planet, int sector) {
		super(name, planet, sector);
	}
}

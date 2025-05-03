package sw.world.blocks.environment;

import arc.graphics.*;
import mindustry.graphics.*;
import mindustry.world.blocks.environment.*;
import sw.graphics.*;

public class Pitfall extends Floor {
	public Color maskColor = SWPal.pitfallMask;

	public int lightLevel = 4;

	public Pitfall(String name) {
		super(name);
		solid = true;
		placeableOn = false;
		canShadow = false;
		variants = 0;
		mapColor = null;
	}

	@Override
	public void createIcons(MultiPacker packer) {
		Color mapCol = mapColor;
		mapColor = new Color(0, 0, 0, 1);
		super.createIcons(packer);
		if (mapCol != null) mapColor.set(mapCol);
	}
}

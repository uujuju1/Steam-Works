package sw.world.blocks.production;

import arc.audio.*;
import mindustry.gen.*;
import mindustry.world.blocks.production.*;

public class SWGenericCrafter extends GenericCrafter {
	public Sound craftSound = Sounds.none;
	public float craftSoundVolume = 1f;

	public SWGenericCrafter(String name) {
		super(name);
	}

	public class SWGenericCrafterBuild extends GenericCrafterBuild {
		@Override
		public void craft() {
			super.craft();
			craftSound.at(x, y, 1f, craftSoundVolume);
		}
	}
}

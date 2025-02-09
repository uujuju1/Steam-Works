package sw.world.draw;

import arc.graphics.g2d.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import sw.world.blocks.distribution.BeltConveyor.*;

public class DrawConveyor extends DrawBlock {
	public float layer = -1f;

	public BeltConveyorBuild cast(Building build) {
		try {
			return (BeltConveyorBuild) build;
		} catch(ClassCastException e) {
			throw new RuntimeException("This drawer needs a BeltConveyorBuild building. Uses a different drawer");
		}
	}

	@Override
	public void draw(Building build) {
		float z = Draw.z();
		if (layer > 0) Draw.z(layer);
		cast(build).beltItems.each(beltItem -> Draw.rect(beltItem.item.uiIcon, build.x + beltItem.x, build.y + beltItem.y, Vars.itemSize, Vars.itemSize));
		Draw.z(z);
	}

	@Override
	public TextureRegion[] icons(Block block) {
		return new TextureRegion[]{};
	}
}

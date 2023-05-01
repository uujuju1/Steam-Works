package sw.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import sw.world.blocks.production.*;
import sw.world.blocks.production.SWGenericCrafter.*;

public class DrawSinSpin extends DrawBlock {
	public TextureRegion barT, barB;
	public float sinScl = 1, sinMag = 1;

	@Override
	public void load(Block block) {
		barT = Core.atlas.find(block.name + "-bar-t");
		barB = Core.atlas.find(block.name + "-bar-b");
	}

	@Override
	public void draw(Building b) {
		SWGenericCrafterBuild self = (SWGenericCrafterBuild) b;
		Draw.rect(barT, b.x + Mathf.sinDeg(Mathf.map(self.rotation, 0f, expectCrafter(b.block).maxRotation, 0f, 90f)/sinScl) * sinMag, b.y);
		Draw.rect(barB, b.x - Mathf.sinDeg(Mathf.map(self.rotation, 0f, expectCrafter(b.block).maxRotation, 0f, 90f)/sinScl) * sinMag, b.y);
	}

	@Override
	public TextureRegion[] icons(Block block) {
		return new TextureRegion[] {barB, barT};
	}

	@Override
	public SWGenericCrafter expectCrafter(Block block) {
		if(!(block instanceof SWGenericCrafter crafter)) throw new ClassCastException("This drawer requires the block to be a SWGenericCrafter. Use a different drawer.");
		return crafter;
	}
}

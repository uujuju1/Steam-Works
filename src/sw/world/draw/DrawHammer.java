package sw.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.draw.*;

public class DrawHammer extends DrawBlock {
	public TextureRegion hammerRegion;
	public int hammers = 4;
	public float offsetX = 0f, offsetY = 0f;
	public float moveX = 0f, moveY = 0f;
	public Interp moveProgress = Interp.linear;

	@Override
	public void draw(Building build) {
		float progress = moveProgress.apply(1f - build.progress());
		float mx = moveX * progress, my = moveY * progress;
		for (int i = 0; i < hammers; i++) {
			Draw.rect(
				hammerRegion,
				build.x + Angles.trnsx(360f/hammers * i, offsetX + mx, offsetY + my),
				build.y + Angles.trnsy(360f/hammers * i, offsetX + mx, offsetY + my),
				360f/hammers * i
			);
		}
	}

	@Override
	public void load(Block block) {
		hammerRegion = Core.atlas.find(block.name + "-hammer");
	}

}

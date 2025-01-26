package sw.tools.processors;

import arc.graphics.g2d.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.entities.part.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.draw.*;
import sw.tools.*;
import sw.tools.GeneratedAtlas.*;

public class BlockProcessor implements SpriteProcessor {
	final Seq<TextureRegion> tmp = new Seq<>();

	@Override
	public void process() {
		Vars.content.blocks().each(u -> u.minfo != null && u.minfo.mod == Tools.mod, block -> {
			try {
				block.init();
				block.load();
				block.loadIcon();
				if (block instanceof Turret turret) {
					processTurret(turret);
				}
			} catch (Exception e) {
				Log.err(e);
				Log.err("Falied to process sprites for @", block);
			}
		});
	}

	private void processTurret(Turret turret) {
		turret.drawer.getRegionsToOutline(turret, tmp);
		// make sure to only generate sprites to those drawers that i hardcode to not generate outline because anuke.
		if (turret.region.found() && turret.drawer instanceof DrawTurret drawer && tmp.isEmpty()) {
			drawer.outline = new GeneratedRegion(
				turret.name + "-outline",
				outline(Tools.atlas.castRegion(turret.region).pixmap(), turret.outlineRadius, turret.outlineColor),
				Tools.atlas.castRegion(turret.region).file.sibling(turret.name.substring("sw-".length()) + "-outline.png")
			).save(true);

			if (drawer.preview.found()) {
				Tools.atlas.castRegion(drawer.preview).pixmap().draw(outline(Tools.atlas.castRegion(drawer.preview).pixmap(), turret.outlineRadius, turret.outlineColor), 0, 0, true);
				Tools.atlas.castRegion(drawer.preview).save(false);
			}

			Seq<DrawPart> ptmp = Seq.with(drawer.parts);
			while (!ptmp.isEmpty()) {
				DrawPart part = ptmp.pop();
				if (
					part instanceof RegionPart regionPart &&
					regionPart.outline && regionPart.drawRegion
				) {
					for (int i = 0; i < regionPart.regions.length; i++) {
						GeneratedRegion cast = Tools.atlas.castRegion(regionPart.regions[i]);
						regionPart.outlines[i] = new GeneratedRegion(
							cast.name,
							outline(cast.pixmap(), turret.outlineRadius, turret.outlineColor),
							cast.file.sibling(cast.name.substring("sw-".length()) + "-outline.png")
						).save(true);
					}
					ptmp.addAll(regionPart.children);
				}
			}
		}
	}
}

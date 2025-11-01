package sw.tools.processors;

import arc.graphics.*;
import arc.util.*;
import mindustry.*;
import mindustry.graphics.*;
import sw.tools.GeneratedAtlas.*;
import sw.tools.*;

public class StatusProcessor implements SpriteProcessor {
	
	@Override
	public void process() {
		Vars.content.statusEffects().select(s -> s.minfo != null && s.minfo.mod == Tools.mod).each(status -> {
			try {
				status.init();
				status.load();
				status.loadIcon();
				
				GeneratedRegion icon = Tools.atlas.castRegion(status.uiIcon);
				
				icon.pixmap().each((x, y) -> icon.pixmap().set(x, y, new Color(icon.pixmap().get(x, y)).mul(status.color)));
				
				if (status.outline) {
					icon.pixmap().draw(outline(icon.pixmap(), 3, Pal.gray, true));
				}
				
				icon.save(false);
				
			} catch (Exception e) {
				Log.err(e);
				Log.warn("Error processing sprites for status effect @. Skipping", status);
			}
		});
	}
}

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
				
				icon.pixmap().draw(mapColors(outline(icon.pixmap(), status.outline ? 3 : 0, Pal.gray, true), old -> {
					if (old.equals(Color.white)) return status.color;
					return old;
				}));
				
				icon.save(false);
			} catch (Exception e) {
				Log.err(e);
				Log.warn("Error processing sprites for status effect @. Skipping", status);
			}
		});
	}
}

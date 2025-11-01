package sw.tools;

import arc.*;
import arc.assets.*;
import arc.files.*;
import arc.graphics.*;
import arc.mock.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.core.*;
import mindustry.game.*;
import mindustry.mod.*;
import sw.*;
import sw.tools.processors.*;

public class Tools {
	protected static final SpriteProcessor[] processes = new SpriteProcessor[]{
		new BlockProcessor(),
		new StatusProcessor(),
		new UnitProcessor()
	};

	public static ModLoader main;
	public static Mods.ModMeta meta;
	public static Mods.LoadedMod mod;

	public static Fi assets;

	public static GeneratedAtlas atlas;

	private Tools() {}

	public static void main(String[] args) {
		Vars.loadLogger();

		Log.info("Sprite processor init");

		var logger = Log.logger;
		Log.logger = new Log.NoopLogHandler();

		long counter = Time.millis(), total = counter;

		assets = Fi.get(args[0]);

		try {
			ArcNativesLoader.load();
		} catch(Throwable ignored) {}

		// create a mockup mindustry to load the mod
		Vars.headless = true;
		SWVars.isMod = false;
		Core.app = new MockApplication();
		Core.files = new MockFiles();
		Core.assets = new AssetManager(Vars.tree = new FileTree() {
			@Override
			public Fi get(String path) {
				Fi file = assets.child(path);
				return file.exists() ? file : Core.files.internal(path);
			}
		});
		Core.atlas = atlas = new GeneratedAtlas();
		Vars.mods = new Mods();

		Vars.content = new ContentLoader();
		Vars.content.createBaseContent();

		// creates a mockup of the mod
		main = new ModLoader();
		meta = new Mods.ModMeta() {{
			name = "sw";
		}};
		mod = new Mods.LoadedMod(null, null, main, Tools.class.getClassLoader(), meta);

		// puts the created mockup into the mods list
		Reflect.<Seq<Mods.LoadedMod>>get(Mods.class, Vars.mods, "mods").add(mod);
		Reflect.<ObjectMap<Class<?>, Mods.ModMeta>>get(Mods.class, Vars.mods, "metas").put(ModLoader.class, meta);

		Events.fire(new EventType.FileTreeInitEvent());

		// loads the mockup's content
		Vars.content.setCurrentMod(mod);
		main.loadContent();
		Vars.content.setCurrentMod(null);
		main.init();
		Log.logger = logger;

		Log.info("Mod initialization finished in @ms.", Time.timeSinceMillis(counter));
		counter = Time.millis();

		Fi.get("sprites").walk(file -> {
			if (file.extEquals("png")) {
				GeneratedAtlas.GeneratedRegion region = new GeneratedAtlas.GeneratedRegion(
					(file.path().contains("vanilla/") ? "" : "sw-") + file.nameWithoutExtension(),
					new Pixmap(file), file
				);
				atlas.add(region);
			}
		});
		atlas.setErrorRegion("sw-compound");

		Log.info("Atlas initialization finished in @ms.", Time.timeSinceMillis(counter));
		counter = Time.millis();

		for (SpriteProcessor process : processes) process.process();

		Log.info("Sprite processing finished in @ms.", Time.timeSinceMillis(counter));
		atlas.dispose();

		Log.info("Sprite processor finished in @ms.", Time.timeSinceMillis(total));
	}
}

package sw.tools;

import arc.files.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.struct.*;
import arc.util.*;

public class GeneratedAtlas extends TextureAtlas implements Eachable<GeneratedAtlas.GeneratedRegion> {
	private final ObjectMap<String, GeneratedRegion> regions = new ObjectMap<>();

	public void add(GeneratedRegion region) {
		regions.put(region.name, region);
	}

	public GeneratedRegion castRegion(TextureRegion region) {
		if (!(region instanceof GeneratedRegion cast)) throw new IllegalArgumentException(region + " is not an instance of GeneratedRegion");
		return cast;
	}

	@Override
	public void each(Cons<? super GeneratedRegion> cons) {
		regions.each((name, region) -> cons.get(region));
	}

	@Override
	public boolean has(String name) {
		return regions.containsKey(name);
	}

	@Override
	public GeneratedRegion find(String name) {
		return regions.get(name, (GeneratedRegion) error);
	}

	@Override
	public GeneratedRegion find(String name, TextureRegion def) {
		return regions.get(name, castRegion(def));
	}

	public static class GeneratedRegion extends AtlasRegion {
		protected final @Nullable Pixmap pixmap;
		public final @Nullable Fi file;

		public GeneratedRegion(String name, @Nullable Pixmap pixmap, @Nullable Fi file) {
			this.name = name;
			this.pixmap = pixmap;
			this.file = file;

			u = v = 0f;
			u2 = v2 = 1f;

			if (pixmap != null) {
				width = pixmap.width;
				height = pixmap.height;
			}
		}

		@Override public boolean found() {
			return pixmap != null && this != Tools.atlas.error;
		}

		public Pixmap pixmap() {
			if(pixmap == null) throw new IllegalArgumentException("Region '" + name + "' not found.");
			return pixmap;
		}

		public GeneratedRegion save(boolean add) {
			if(file == null) throw new IllegalStateException("Missing file for region '" + name + "'.");

			file.writePng(pixmap());
			if(add) Tools.atlas.add(this);
			return this;
		}
	}
}

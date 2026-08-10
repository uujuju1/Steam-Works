package sw.matryoshka.ui;

import arc.files.*;
import arc.struct.*;
import arc.util.serialization.*;
import mindustry.content.*;
import sw.matryoshka.actions.*;
import sw.matryoshka.world.*;

/**
 * Class representing a tutorial with multiple layers. Can be loaded from json or created dynamically if needed.
 * <p> Each layer can be created via {@link #buildLayer(int)} which will return the layer with its own nesting world.
 */
public class TutorialSequence {
	public JsonValue tutorialJson;

	public int maxLayers;

	public static TutorialSequence loadFromFile(Fi file) {
		return new TutorialSequence() {{
			tutorialJson = new JsonReader().parse(file);
			maxLayers = tutorialJson.getInt("maxLayers", 0);
		}};
	}

	/**
	 * Creates a layer of the tutorial, with its own Nesting and associated actions.
	 * @param layerNumber finds the specific layer named "layer" + layerNumber. If this number is less than 0 or the layer cannot be found, it will return a blank layer.
	 */
	public SequenceLayer buildLayer(int layerNumber) {
		JsonValue layer = tutorialJson.get("layer" + layerNumber);

		if (layer == null || layerNumber < 0) {
			return new SequenceLayer() {{
				nesting = new Nesting(5, 5);
			}};
		}

		return new SequenceLayer() {{
			nesting = new Nesting(layer.getInt("width", 5), layer.getInt("height", 5));

			nesting.world.tiles.eachTile(tile -> {
				tile.setFloor(Blocks.metalTiles7.asFloor());
				if (tile.x % 4 == 0 || tile.y % 4 == 0) tile.setFloor(Blocks.metalTiles11.asFloor());
			});
		}};
	}

	public static class SequenceLayer {
		public Nesting nesting;

		public Seq<TutorialAction> actions = new Seq<>();

		public FloatSeq timestamp = new FloatSeq();
		public float maxTime;
	}
}

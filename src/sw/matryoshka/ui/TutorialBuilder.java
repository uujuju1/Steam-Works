package sw.matryoshka.ui;

import arc.func.*;
import arc.struct.*;

public class TutorialBuilder extends TutorialSequence {
	public Seq<Prov<SequenceLayer>> layers = new Seq<>();

	public void addLayer(Prov<SequenceLayer> layer) {
		layers.add(layer);
		maxLayers = layers.size;
	}

	@Override
	public SequenceLayer buildLayer(int layerNumber) {
		return layers.get(layerNumber).get();
	}
}

package sw.world.interfaces;

import arc.struct.*;
import mindustry.gen.*;
import sw.world.graph.*;
import sw.world.meta.*;
import sw.world.modules.*;

public interface HasTension extends Buildingc {
	default boolean connects(HasTension to) {
		return to.tensionConfig().tier == tensionConfig().tier || to.tensionConfig().tier == -1 || tensionConfig().tier == -1;
	}

	default HasTension getTensionDestination(HasTension from) {
		return this;
	}
	default Seq<HasTension> nextBuilds() {
		return proximity().select(b -> b instanceof HasTension other && connects(other)).map(b -> ((HasTension) b).getTensionDestination(this)).removeAll(b -> !connects(b) && !b.connects(this) && b.tensionConfig().graphs);
	}

	TensionModule tension();
	TensionConfig tensionConfig();
	default TensionGraph tensionGraph() {
		return tension().graph;
	}

	default float tensionMobile() {
		return 0f;
	}
	default float tensionStatic() {
		return tensionConfig().staticTension;
	}
}

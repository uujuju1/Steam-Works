package sw.world.graph;

import arc.struct.*;
import sw.world.interfaces.*;

public class SpinSection {
	public final Seq<HasSpin> builds = new Seq<>();
	public float ratio = 1;

	public void addBuild(HasSpin build) {
		builds.addUnique(build);
		build.spin().section = this;
	}

	public void merge(SpinSection other) {
		other.builds.each(this::addBuild);
	}
}

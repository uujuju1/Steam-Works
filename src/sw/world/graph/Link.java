package sw.world.graph;

import mindustry.*;
import mindustry.gen.*;
import sw.world.interfaces.*;

public class Link {
	public HasForce l1, l2;

	public Link(HasForce l1, HasForce l2) {
		this.l1 = l1;
		this.l2 = l2;
		l1.graph().addGraph(l2.graph());
		l1.graph().links.addUnique(this);
		l1.force().links.addUnique(this);
		l2.force().links.addUnique(this);
	}
	public Link(int l1, int l2) {
		this((HasForce) Vars.world.build(l1), (HasForce) Vars.world.build(l2));
	}

	public HasForce other(HasForce build) {
		return build == l1 ? l2 : l1;
	}
	public boolean has(HasForce build) {
		return build == l1 || build == l2;
	}
	public float ratio(HasForce start, boolean reverse) {
		return reverse ?
			       start.forceConfig().beltSizeOut/other(start).forceConfig().beltSizeIn:
			       start.forceConfig().beltSizeIn/other(start).forceConfig().beltSizeOut;
	}

	public void removeS() {
		l2.force().link = -1;
		l1.force().links.remove(this);
		l2.force().links.remove(this);
		l1.graph().links.remove(this);
		l1.graph().remove((Building) l2);
		l1.graph().removeBuild((Building) l2);
		l2.graph().floodFill((Building) l2).each(b -> l2.graph().add(b));
	}

	@Override public boolean equals(Object obj) {
		return obj instanceof Link link && (link.l1 == l1 || link.l1 == l2) && (link.l2 == l2 || link.l2 == l1);
	}
	@Override public String toString() {
		return "link1: " + l1 + "; link2: " + l2;
	}
}

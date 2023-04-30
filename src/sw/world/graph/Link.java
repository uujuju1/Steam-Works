package sw.world.graph;

import mindustry.gen.*;
import sw.world.interfaces.*;

public class Link {
	public Building l1, l2;

	public Link(Building l1, Building l2) {
		this.l1 = l1;
		this.l2 = l2;
	}

	public Building other(Building build) {
		return build == l1 ? l2 : l1;
	}
	public boolean has(Building build) {
		return build == l1 || build == l2;
	}

	public Link addS() {
		((HasForce) l1).force().links.addUnique(this);
		((HasForce) l2).force().links.addUnique(this);
		return this;
	}
	public Link removeS() {
		((HasForce) l1).force().links.remove(this);
		((HasForce) l2).force().links.remove(this);
		((HasForce) l1).graph().floodFill(l1).each(b -> ((HasForce) l1).graph().add(b));
		((HasForce) l2).graph().floodFill(l2).each(b -> ((HasForce) l2).graph().add(b));
		return this;
	}

	@Override public boolean equals(Object obj) {
		return obj instanceof Link link && (link.l1 == l1 || link.l1 == l2) && (link.l2 == l2 || link.l2 == l1);
	}
	@Override public String toString() {
		return "link1: " + l1 + "; link2: " + l2;
	}
}

package sw.world.graph;

import mindustry.*;
import mindustry.gen.*;
import sw.world.interfaces.*;

public class Link {
	public int l1, l2;

	public Link(HasForce l1, HasForce l2) {
		this(((Building) l1).pos(), ((Building) l2).pos());
	}
	public Link(int l1, int l2) {
		this.l1 = l1;
		this.l2 = l2;
	}

	public HasForce l1() {
		return (HasForce) Vars.world.build(l1);
	}
	public HasForce l2() {
		return (HasForce) Vars.world.build(l2);
	}

	public HasForce other(HasForce build) {
		return build == l1() ? l2() : l1();
	}
	public boolean has(HasForce build) {
		return build == l1() || build == l2();
	}
	public float ratio(HasForce from, boolean reverse) {
		return !reverse ?
			       from.forceConfig().beltSizeOut/other(from).forceConfig().beltSizeIn:
			       other(from).forceConfig().beltSizeIn/from.forceConfig().beltSizeOut;
	}

	@Override public boolean equals(Object obj) {
		return obj instanceof Link link && (link.l1 == l1 || link.l1 == l2) && (link.l2 == l2 || link.l2 == l1);
	}
	@Override public String toString() {
		return "link1: " + l1 + "; link2: " + l2;
	}
}

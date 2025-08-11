package sw.world.interfaces;

import arc.struct.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import sw.world.graph.*;
import sw.world.meta.*;
import sw.world.modules.*;

public interface HasFluid {
	default Building asBuilding() {
		return (Building) this;
	}
	
	static boolean connectsFluids(@Nullable HasFluid from, @Nullable HasFluid to) {
		if (from == null || to == null) return false;
		return from.connectToFluid(to) && to.connectToFluid(from);
	}
	
	default void addLiquid(Liquid liquid, float amount) {
	
	}
	
	default boolean connectToFluid(HasFluid other) {
		boolean hasSpin = fluidConfig() != null;
		boolean sameTeam = other.asBuilding().team == asBuilding().team;
		boolean isEdge = !asBuilding().proximity.contains((Building) other);
		if (fluidConfig().allowedEdges != null) {
			for(int i : fluidConfig().allowedEdges[asBuilding().rotation]) {
				isEdge |= asBuilding().nearby(
					Edges.getEdges(asBuilding().block.size)[i].x,
					Edges.getEdges(asBuilding().block.size)[i].y
				) == other;
			}
		} else isEdge = true;
		return hasSpin && sameTeam && isEdge;
	}
	
	default float getFluid(Liquid liquid) {
		return fluid().liquids[liquid.id];
	}
	
	default HasFluid getFluidDestination(HasFluid from) {
		return this;
	}
	
	default FluidModule fluid() {
		try {
			return (FluidModule) asBuilding().getClass().getField("fluid").get(this);
		} catch (Exception ignored) {
			return null;
		}
	}
	default FluidConfig fluidConfig() {
		try {
			return (FluidConfig) asBuilding().block.getClass().getField("fluidConfig").get(asBuilding().block);
		} catch (Exception ignored) {
			return null;
		}
	}
	default FluidGraph fluidGraph() {
		return fluid().graph;
	}
	
	default Seq<HasFluid> nextFluidBuilds() {
		return asBuilding().proximity
			       .select(b -> b instanceof HasFluid a && connectsFluids(this, a.getFluidDestination(this)))
			       .map(a -> ((HasFluid) a).getFluidDestination(this));
	}
	
	default void removeLiquid(Liquid liquid, float amount) {
	
	}
}

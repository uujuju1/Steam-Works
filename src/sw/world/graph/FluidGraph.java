package sw.world.graph;

import arc.struct.*;
import mindustry.*;
import mindustry.type.*;
import sw.world.interfaces.*;

public class FluidGraph extends Graph<HasFluid> {
	public ObjectMap<HasFluid, HasFluid> connections = new ObjectMap<>();
	
	@Override
	public void graphChanged() {
	
	}
	
	public static class FluidTank {
		public Seq<HasFluid> builds = new Seq<>(false);
		
		public void addBuild(HasFluid fluid) {
			builds.add(fluid);
			fluid.fluid().tank = this;
		}
		
		public void equalizeFluids() {
			for (Liquid liquid : Vars.content.liquids()) {
				float total = 0;
				
				for (HasFluid build : builds) {
					total += build.getFluid(liquid);
				}
				
				total /= builds.size;
				
				for (HasFluid build : builds) {
					build.fluid().setAmount(liquid.id, total);
				}
			}
		}
	}
}

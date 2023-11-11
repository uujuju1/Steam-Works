package sw.world;

import arc.math.*;
import mindustry.type.*;

public class FluidArea {
	public int x, y;
	public int radius;
	public Liquid fluid;

	public FluidArea(int x, int y, int radius, Liquid fluid) {
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.fluid = fluid;
	}

	public float get(int x, int y) {
		return Mathf.maxZero(1f - Mathf.dst(this.x, this.y, x, y)/(float)radius);
	}

	@Override public boolean equals(Object obj) {
		return obj instanceof FluidArea area && area.radius == radius && area.fluid == fluid;
	}
}

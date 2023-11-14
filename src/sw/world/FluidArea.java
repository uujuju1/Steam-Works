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

	public float get(float x, float y) {
		return Mathf.maxZero(1f - (dst(x, y)/(radius * 8f)));
	}
	public float dst(float x, float y) {
		return Mathf.dst(this.x * 8f, this.y * 8f, x, y);
	}

	@Override public boolean equals(Object obj) {
		return obj instanceof FluidArea area && area.radius == radius && area.fluid == fluid;
	}
}

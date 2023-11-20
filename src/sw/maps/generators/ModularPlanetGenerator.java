package sw.maps.generators;

import arc.func.*;
import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.noise.*;
import mindustry.content.*;
import mindustry.maps.generators.*;
import mindustry.type.*;
import mindustry.world.*;

public class ModularPlanetGenerator extends PlanetGenerator {
	public float minHeight = 0f;
	public Block defaultBlock = Blocks.grass;
	public Seq<Noise3DSettings> heights = new Seq<>();
	public Seq<ColorPatch> colors = new Seq<>();
	public Cons<ModularPlanetGenerator> gen = g -> {};

	public class Noise3DSettings {
		public Vec3 offset = Vec3.Zero.cpy();
		public float min = 0f, max = 1f, mag = 1f, heightOffset = 0;
		public double oct = 7, per = 0.5f, scl = 1f/3f;

		public float noise(Vec3 pos) {
			Tmp.v31.set(pos).add(offset);
			return Mathf.clamp(Simplex.noise3d(seed, oct, per, scl, Tmp.v31.x, Tmp.v31.y, Tmp.v31.z) * mag + heightOffset, min, max);
		}
	}
	public class ColorPatch {
		public Block block = Blocks.grass;
		public Noise3DSettings noise;
		public float minT = 0f, maxT = 1f;

		public boolean canColor(Vec3 pos, float height) {
			float noise1 = noise.noise(pos);
			return noise1 >= minT && noise1 <= maxT && noise1 >= height;
		}
	}

	public class Room {
		public int x, y;
		public float rad;
		public @Nullable Room child;

		public Room(int x, int y, float rad, Room child) {
			this(x, y, rad);
			this.child = child;
		}

		public Room(int x, int y, float rad) {
			this.x = x;
			this.y = y;
			this.rad = rad;
		}
	}

	float height(Vec3 pos) {
		return heights.sumf(s -> s.noise(pos))/heights.size;
	}
	@Override public float getHeight(Vec3 p) {
		return Math.max(minHeight, height(p));
	}

	@Override
	public Color getColor(Vec3 p) {
		return getBlock(p).mapColor;
	}
	
	public Block getBlock(Vec3 p) {
		Block out = defaultBlock;
		for (ColorPatch color : colors) {
			if (color.canColor(p, getHeight(p))) out = color.block;
		}
		return out;
	}

	@Override
	public void generateSector(Sector sector) {
		sector.generateEnemyBase = false;
	}

	@Override
	protected void generate() {
		gen.get(this);
	}

	@Override
	protected float noise(float x, float y, double octaves, double falloff, double scl, double mag){
		Vec3 v = sector.rect.project(x, y).scl(5f);
		return Simplex.noise3d(seed, octaves, falloff, 1f / scl, v.x, v.y, v.z) * (float)mag;
	}

	public Seq<Room> rooms(int rooms, float angle, float randomAngle, float distance, float randomDistance, float minRadius, float randomRadius, int x, int y) {
		Seq<Room> output = new Seq<>();
		Room lastRoom = null;
		for (int i = 0; i < rooms; i++) {
			lastRoom = new Room(x, y, minRadius + rand.range(randomRadius), lastRoom);
			output.add(lastRoom);
			int newx = -1, newy = -1;
			int max = 0;

			while (!tiles.in(newx, newy)) {
				float newAngle = angle + rand.range(randomAngle);
				Tmp.v1.trns(newAngle, distance + rand.range(randomDistance));
				newx = x + Math.round(Tmp.v1.x);
				newy = y + Math.round(Tmp.v1.y);
			}
			x = newx;
			y = newy;
		}
		return output;
	}
	public boolean nearFloor(int cx, int cy, int rad, Block block){
		for(int x = -rad; x <= rad; x++){
			for(int y = -rad; y <= rad; y++){
				int wx = cx + x, wy = cy + y;
				if(Structs.inBounds(wx, wy, width, height) && Mathf.within(x, y, rad)){
					Tile other = tiles.getn(wx, wy);
					if(other.floor() == block){
						return true;
					}
				}
			}
		}
		return false;
	}

	public void replace(int cx, int cy, int rad, Block floor, Block overlay, Block block){
		for(int x = -rad; x <= rad; x++){
			for(int y = -rad; y <= rad; y++){
				int wx = cx + x, wy = cy + y;
				if(Structs.inBounds(wx, wy, width, height) && Mathf.within(x, y, rad)){
					Tile other = tiles.getn(wx, wy);
					if (floor != null) other.setFloor(floor.asFloor());
					if (overlay != null) other.setOverlay(overlay);
					if (block != null) other.setBlock(block);
				}
			}
		}
	}
}

package sw.world.draw;

import arc.func.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.gen.*;
import sw.annotations.Annotations.*;

public class DrawFlaps extends BlockDrawer {
	public Floatf<Building> rotationFunc = Building::totalProgress;
	
	public float radius;
	public float width;
	public float height;
	public float zScale;
	public float rotation;
	public float layer;
	public float layerOffset;
	
	public int blades;
	
	public @Load("@loadBlock.name$-flap") TextureRegion flapRegion;
	
	@Override
	public void draw(Building build) {
		float z = Draw.z();
		for (int i = 0; i < blades; i++) {
			float c = rotationFunc.get(build) * Mathf.degreesToRadians + Mathf.PI * 2f / blades * i;
			
			float scl = Mathf.cos(c) * zScale;
			Tmp.v1.trns(rotation + build.rotdeg(), Mathf.sin(c) * (radius - zScale/4f));
			Draw.z((layer < 0 ? z : layer) + layerOffset * Mathf.cos(c));
			Draw.rect(
				flapRegion,
				build.x + Tmp.v1.x,
				build.y + Tmp.v1.y,
				Mathf.sin(c) * (width - zScale/2f + scl/2f),
				height - zScale/2f + scl/2f,
				rotation + build.rotdeg()
			);
		}
		Draw.z(z);
	}
}

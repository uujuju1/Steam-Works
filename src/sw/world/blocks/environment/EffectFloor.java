package sw.world.blocks.environment;

import arc.graphics.*;
import arc.math.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;

import static mindustry.Vars.*;

public class EffectFloor extends Floor {
	public float effectTime = 60;
	public float effectTimeRand = 0;
	public Effect effect = Fx.none;
	public Color effectColor = Color.white;

	public EffectFloor(String name) {
		super(name);
	}

	@Override
	public boolean updateRender(Tile tile) {
		return true;
	}

	@Override
	public void renderUpdate(UpdateRenderState state){
		if(state.tile.nearby(-1, -1) != null && state.tile.nearby(-1, -1).block() == Blocks.air && (state.data += (Time.delta + Mathf.range(effectTimeRand))) >= effectTime){
			effect.at(state.tile.x * tilesize, state.tile.y * tilesize, effectColor);
			state.data = 0f;
		}
	}
}

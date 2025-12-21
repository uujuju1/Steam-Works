package sw.world.blocks.production;

import arc.*;
import arc.audio.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.consumers.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;
import sw.content.*;
import sw.world.graph.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

import static mindustry.Vars.*;

public class RangedDrill extends Block {
	public SpinConfig spinConfig;
	
	public int
		range = 5,
		tier = 1;
	public float
		optionalBoostIntensity = 1f,
		drillTime = 200f;
	
	public Sound drillSound = Sounds.drillImpact;
	public float drillSoundVolume = 1;
	public Effect drillEffect = SWFx.boreMine;
	
	public boolean useAllowList = false;
	
	public ObjectFloatMap<Item> drillMultipliers = new ObjectFloatMap<>();
	
	public DrawBlock drawer = new DrawDefault();
	
	public RangedDrill(String name) {
		super(name);
		
		hasItems = true;
		rotate = true;
		update = true;
		solid = true;
		regionRotated1 = 1;
		ambientSoundVolume = 0.05f;
		ambientSound = Sounds.loopMineBeam;
		
		envEnabled |= Env.space;
		flags = EnumSet.of(BlockFlag.drill);
	}
	
	public boolean canMine(Item item) {
		if (
			getDrillTime(item) <= 0 ||
				useAllowList && !drillMultipliers.containsKey(item) ||
				item.hardness > tier
		) return false;
		return true;
	}
	
	@Override
	public boolean canPlaceOn(Tile tile, Team team, int rotation){
		for(int i = 0; i < size; i++){
			nearbySide(tile.x, tile.y, rotation, i, Tmp.p1);
			for(int j = 0; j < range; j++){
				Tile other = world.tile(Tmp.p1.x + Geometry.d4x(rotation)*j, Tmp.p1.y + Geometry.d4y(rotation)*j);
				if(other != null && other.solid()){
					Item drop = other.wallDrop();
					if(drop != null && canMine(drop)){
						return true;
					}
					break;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public void drawPlace(int x, int y, int rotation, boolean valid) {
		Item item = null, invalidItem = null;
		boolean multiple = false;
		int count = 0;
		
		for(int i = 0; i < size; i++){
			nearbySide(x, y, rotation, i, Tmp.p1);
			
			int j = 0;
			Item found = null;
			for(; j < range; j++){
				int rx = Tmp.p1.x + Geometry.d4x(rotation)*j, ry = Tmp.p1.y + Geometry.d4y(rotation)*j;
				Tile other = world.tile(rx, ry);
				if(other != null && other.solid()){
					Item drop = other.wallDrop();
					if(drop != null){
						if(canMine(drop)){
							found = drop;
							count++;
						}else{
							invalidItem = drop;
						}
					}
					break;
				}
			}
			
			if(found != null){
				//check if multiple items will be drilled
				if(item != found && item != null){
					multiple = true;
				}
				item = found;
			}
			
			int len = Math.min(j, range - 1);
			Drawf.dashLine(found == null ? Pal.remove : Pal.placing,
				Tmp.p1.x * tilesize,
				Tmp.p1.y *tilesize,
				(Tmp.p1.x + Geometry.d4x(rotation)*len) * tilesize,
				(Tmp.p1.y + Geometry.d4y(rotation)*len) * tilesize
			);
		}
		
		if(item != null){
			float width = drawPlaceText(Core.bundle.formatFloat("bar.drillspeed", 60f / getDrillTime(item) * count, 2), x, y, valid);
			if(!multiple){
				float dx = x * tilesize + offset - width/2f - 4f, dy = y * tilesize + offset + size * tilesize / 2f + 5, s = iconSmall / 4f;
				Draw.mixcol(Color.darkGray, 1f);
				Draw.rect(item.fullIcon, dx, dy - 1, s, s);
				Draw.reset();
				Draw.rect(item.fullIcon, dx, dy, s, s);
			}
		}else if(invalidItem != null){
			drawPlaceText(Core.bundle.get("bar.drilltierreq"), x, y, false);
		}
	}
	
	@Override
	public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
		if (spinConfig != null) spinConfig.drawPlace(this, plan.x, plan.y, plan.rotation, true);
		drawer.drawPlan(this, plan, list);
	}
	
	@Override
	public void drawPlanConfigTop(BuildPlan plan, Eachable<BuildPlan> list) {
		drawer.drawPlan(this, plan, list);
	}
	
	public float getDrillTime(Item item){
		return drillTime / drillMultipliers.get(item, 1f);
	}
	
	@Override protected TextureRegion[] icons() {
		return drawer.finalIcons(this);
	}
	
	@Override
	public void init(){
		updateClipRadius((range + 2) * tilesize);
		super.init();
	}
	
	@Override
	public void load() {
		super.load();
		drawer.load(this);
	}
	
	@Override
	public boolean outputsItems(){
		return true;
	}
	
	@Override
	public void setBars() {
		super.setBars();
		
		if (spinConfig != null) spinConfig.addBars(this);
		
		addBar("drillspeed", (sw.world.blocks.production.RangedDrill.RangedDrillBuild e) ->
			                     new Bar(
				                     () -> Core.bundle.format("bar.drillspeed", Strings.fixed(e.lastDrillSpeed * 60, 2)),
				                     () -> Pal.ammo,
				                     () -> e.warmup)
		);
	}
	
	@Override
	public void setStats() {
		super.setStats();
		
		if (spinConfig != null) spinConfig.addStats(stats);
		
		stats.add(Stat.drillTier, StatValues.drillables(drillTime, 0f, size, drillMultipliers, b -> (b instanceof Floor f && f.wallOre && f.itemDrop != null && canMine(f.itemDrop)) || (b instanceof StaticWall w && w.itemDrop != null && canMine(w.itemDrop))));
		
		stats.add(Stat.drillSpeed, 60f / drillTime * size, StatUnit.itemsSecond);
		
		if(optionalBoostIntensity != 1 && findConsumer(f -> f instanceof ConsumeLiquidBase && f.booster) instanceof ConsumeLiquidBase consBase){
			stats.remove(Stat.booster);
			stats.add(Stat.booster,
				StatValues.speedBoosters("{0}" + StatUnit.timesSpeed.localized(),
					consBase.amount, optionalBoostIntensity, false,
					l -> (consumesLiquid(l) && (findConsumer(f -> f instanceof ConsumeLiquid).booster || ((ConsumeLiquid)findConsumer(f -> f instanceof ConsumeLiquid)).liquid != l)))
			);
		}
	}
	
	@Override
	public boolean rotatedOutput(int x, int y){
		return false;
	}
	
	public class RangedDrillBuild extends Building implements HasSpin {
		public SpinModule spin;
		
		public Tile[] facing = new Tile[size];
		public Point2[] lasers = new Point2[size];
		public @Nullable Item lastItem;
		
		public float time, totalTime;
		public float warmup, boostWarmup;
		public float lastDrillSpeed;
		public int facingAmount;
		
		@Override
		public Building create(Block block, Team team) {
			if (spinConfig != null) spin = new SpinModule();
			return super.create(block, team);
		}
		
		@Override public void draw() {
			drawer.draw(this);
		}
		
		@Override
		public void drawSelect(){
			if (spin != null) spinConfig.drawPlace(block, tileX(), tileY(), rotation, true);
			if(lastItem != null){
				float dx = x - size * tilesize/2f, dy = y + size * tilesize/2f, s = iconSmall / 4f;
				Draw.mixcol(Color.darkGray, 1f);
				Draw.rect(lastItem.fullIcon, dx, dy - 1, s, s);
				Draw.reset();
				Draw.rect(lastItem.fullIcon, dx, dy, s, s);
			}
		}
		
		@Override
		public void onProximityUpdate() {
			updateLasers();
			updateFacing();
			
			if (spin != null) new SpinGraph().mergeFlood(this);
		}
		
		@Override
		public void onProximityRemoved() {
			super.onProximityRemoved();
			
			if (spin != null) spinGraph().removeBuild(this);
		}
		
		@Override public float progress() {
			return time/drillTime;
		}
		
		@Override
		public void read(Reads read, byte revision){
			super.read(read, revision);
			
			if (revision > 0 && spin != null) spin.read(read);
			
			time = read.f();
			warmup = read.f();
		}
		
		@Override
		public boolean shouldConsume(){
			return items.total() < itemCapacity && lastItem != null && enabled;
		}
		
		@Override public float totalProgress() {
			return totalTime;
		}
		
		protected void updateFacing(){
			lastItem = null;
			boolean multiple = false;
			int dx = Geometry.d4x(rotation), dy = Geometry.d4y(rotation);
			facingAmount = 0;
			
			//update facing tiles
			for(int p = 0; p < size; p++){
				Point2 l = lasers[p];
				Tile dest = null;
				for(int i = 0; i < range; i++){
					int rx = l.x + dx*i, ry = l.y + dy*i;
					Tile other = world.tile(rx, ry);
					if(other != null){
						if(other.solid()){
							Item drop = other.wallDrop();
							if(drop != null && canMine(drop)){
								facingAmount ++;
								if(lastItem != drop && lastItem != null){
									multiple = true;
								}
								lastItem = drop;
								dest = other;
							}
							break;
						}
					}
				}
				
				facing[p] = dest;
			}
			
			//when multiple items are present, count that as no item
			if(multiple){
				lastItem = null;
			}
		}
		
		protected void updateLasers(){
			for(int i = 0; i < size; i++){
				if(lasers[i] == null) lasers[i] = new Point2();
				nearbySide(tileX(), tileY(), rotation, i, lasers[i]);
			}
		}
		
		@Override
		public void updateTile(){
			super.updateTile();
			
			if(lasers[0] == null) updateLasers();
			
			warmup = Mathf.approachDelta(warmup, Mathf.num(efficiency > 0), 1f / 60f);
			totalTime += Time.delta * warmup;
			
			updateFacing();
			
			float multiplier = Mathf.lerp(1f, optionalBoostIntensity, optionalEfficiency);
			float drillTime = getDrillTime(lastItem);
			boostWarmup = Mathf.lerpDelta(boostWarmup, optionalEfficiency, 0.1f);
			lastDrillSpeed = (facingAmount * multiplier * timeScale) / drillTime;
			
			time += edelta() * multiplier;
			
			if(time >= drillTime){
				for(Tile tile : facing){
					Item drop = tile == null ? null : tile.wallDrop();
					if(items.total() < itemCapacity && drop != null){
						items.add(drop, 1);
						produced(drop);
						drillEffect.at(tile.worldx(), tile.worldy(), rotdeg(), drop.color);
					}
				}
				time %= drillTime;
				drillSound.at(x, y, 1f, drillSoundVolume);
			}
			
			if(timer(timerDump, dumpTime)){
				dump();
			}
		}
		
		@Override
		public byte version() {
			return 1;
		}
		
		@Override public float warmup() {
			return warmup;
		}
		
		@Override
		public void write(Writes write){
			super.write(write);
			
			if (spin != null) spin.write(write);
			
			write.f(time);
			write.f(warmup);
		}
	}
}

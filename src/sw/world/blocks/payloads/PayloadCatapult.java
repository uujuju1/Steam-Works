package sw.world.blocks.payloads;

import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.blocks.*;
import mindustry.world.blocks.payloads.*;
import sw.annotations.Annotations.*;

public class PayloadCatapult extends PayloadBlock {
	public float range = 80f;
	public float maxPayloadSize = 3f;
	
	public @Load("@name$-catapult") TextureRegion catapultRegion;
	public @Load("@name$-catapult-edge") TextureRegion catapultEdgeRegion;
	
	public PayloadCatapult(String name) {
		super(name);
		
		solid = true;
		rotate = true;
		configurable = true;
		clearOnDoubleTap = true;
		outputsPayload = true;
		
		config(Point2.class, (PayloadCatapultBuild build, Point2 offset) -> {
			build.link = Point2.unpack(build.pos()).add(offset).pack();
		});
		config(Integer.class, (PayloadCatapultBuild build, Integer link) -> {
			build.link = link;
		});
		configClear((PayloadCatapultBuild build) -> build.link = -1);
	}
	
	@Override
	public void init() {
		super.init();
		
		updateClipRadius(range);
	}
	
	// TODO make a proper way of knowing what the catapult is supposed to do
	// TODO make a proper way of knowing which catapult should launch to this
	// TODO make a proper "jumping" animation of the payload
	// TODO make a proper ejecting animation during payload launch
	public class PayloadCatapultBuild extends PayloadBlockBuild<Payload> implements RotBlock {
		public int link = -1;
		
		public float angle;
		
		public Queue<PayloadCatapultBuild> launchers = new Queue<>();
		
		public boolean acceptLauncher(PayloadCatapultBuild source, Payload payload) {
			return
				acceptPayload(source, payload) &&
				this.payload == null &&
				launchers.last() == source;
		}
		
		@Override public boolean acceptPayload(Building source, Payload payload) {
			return super.acceptPayload(source, payload) && payload.size() <= maxPayloadSize * Vars.tilesize;
		}
		
		@Override public float buildRotation() {
			return rotation;
		}
		
		@Override
		public Point2 config() {
			if (getLink() == null) return null;
			return Point2.unpack(link).sub(tile.x, tile.y);
		}
		
		@Override
		public void draw() {
			Draw.rect(region, x, y);
			
			for (int i = 0; i < 4; i++) {
				if (blends(i) && i != rotation) {
					Draw.rect(inRegion, x, y, i * 90f - 180);
				}
			}
			
			Draw.rect(outRegion, x, y, rotdeg());
			
			if(payload != null) {
				Draw.z(Layer.blockOver);
				
				updatePayload();
				payload.draw();
			}
			
			Draw.z(Layer.blockOver + 0.1f);
			Draw.rect(topRegion, x, y);
		}
		
		@Override
		public void drawConfigure() {
			Drawf.poly(x, y, 4, size * Vars.tilesize / 2f, 0, Pal.accent);
			
			if (getLink() != null) {
				Drawf.poly(getLink().x, getLink().y, 4, getLink().block.size * Vars.tilesize / 2f, 0, Pal.place);
			}
		}
		
		public @Nullable PayloadCatapultBuild getLink() {
			if (Vars.world.build(link) instanceof PayloadCatapultBuild build) return build;
			return null;
		}
		
		public void launchPayload(PayloadCatapultBuild to) {
			to.payload = takePayload();
			to.payVector.set(to).sub(this);
			to.launchers.removeLast();
			
			to.updatePayload();
		}
		
		@Override
		public boolean onConfigureBuildTapped(Building other) {
			if (other instanceof PayloadCatapultBuild build) {
				if (other == getLink()) {
					configure(-1);
					return false;
				}
				
				if (build.getLink() == this) {
					build.configure(-1);
					configure(build.pos());
					return true;
				}
				
				if (validLink(other)) configure(build.pos());
			}
			return false;
		}
		
		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);
			
			link = read.i();
			
			angle = read.f();
		}
		
		@Override
		public void updateTile() {
			if (!launchers.isEmpty() && launchers.last().getLink() != this) getLink().launchers.removeLast();
			
			if (payload != null) {
				if (payVector.isZero() && getLink() != null) {
					if (!getLink().launchers.contains(this)) getLink().launchers.addFirst(this);
					if (getLink().acceptLauncher(this, payload)) launchPayload(getLink());
				} else {
					if (getLink() != null) {
						payVector.approachDelta(Vec2.ZERO, payloadSpeed);
					} else {
						moveOutPayload();
					}
				}
			}
		}
		
		public boolean validLink(Building other) {
			return
				other != this &&
				other instanceof PayloadCatapultBuild &&
				other.dst(this) <= range &&
				other.isValid() &&
				other.team == team;
		}
		
		@Override
		public void write(Writes write) {
			super.write(write);
			
			write.i(link);
			
			write.f(angle);
		}
	}
}

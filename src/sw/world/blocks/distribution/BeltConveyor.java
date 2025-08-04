package sw.world.blocks.distribution;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import sw.world.graph.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

public class BeltConveyor extends Block {
	public SpinConfig spinConfig = new SpinConfig();

	public DrawBlock drawer = new DrawDefault();

	public float movementScale = 10f;

	public BeltConveyor(String name) {
		super(name);
		rotate = true;
		hasItems = true;
		update = true;
	}

	@Override
	public void drawPlace(int x, int y, int rotation, boolean valid) {
		super.drawPlace(x, y, rotation, valid);
		spinConfig.drawPlace(this, x, y, rotation, valid);
	}

	@Override
	public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
		drawer.drawPlan(this, plan, list);
	}

	@Override
	public void getRegionsToOutline(Seq<TextureRegion> out){
		drawer.getRegionsToOutline(this, out);
	}

	@Override
	public TextureRegion[] icons(){
		return drawer.finalIcons(this);
	}

	@Override
	public void load() {
		super.load();
		drawer.load(this);
	}

	@Override
	public void setBars() {
		super.setBars();
		spinConfig.addBars(this);
	}

	@Override
	public void setStats() {
		super.setStats();
		spinConfig.addStats(stats);
	}

	public class BeltConveyorBuild extends Building implements HasSpin {
		public SpinModule spin = new SpinModule();

		public Seq<ConveyorItem> beltItems = new Seq<>();

		@Override
		public boolean acceptItem(Building source, Item item) {
			return source != front() && (source == back() || !nextBuilds().contains(other -> !(other instanceof BeltConveyorBuild))) && !beltItems.contains(b -> Mathf.dst(
				b.x, b.y, Angles.trnsx(relativeTo(source) * 90f, 4f), Angles.trnsy(relativeTo(source) * 90f, 4f)
			) < Vars.itemSize);
		}

		@Override
		public boolean connectTo(HasSpin other) {
			boolean hasSpin = spinConfig.hasSpin;
			boolean sameTeam = other.asBuilding().team == team;
			boolean isEdge = !proximity.contains((Building) other);
			if (spinConfig().allowedEdges != null) {
				for(int i : spinConfig().allowedEdges[rotation]) {
					isEdge |= nearby(Edges.getEdges(block.size)[i].x, Edges.getEdges(block.size)[i].y) == other;
				}
			} else isEdge = true;
			return hasSpin && sameTeam && (isEdge || (other instanceof BeltConveyorBuild && other.asBuilding().rotation == rotation));
		}

		@Override public void draw() {
			drawer.draw(this);
		}
		@Override public void drawLight() {
			drawer.drawLight(this);
		}

		@Override
		public void handleItem(Building source, Item i) {
			int relativeTo = relativeTo(source);
			beltItems.add(new ConveyorItem() {{
				item = i;
				x = Angles.trnsx(relativeTo * 90f, 4f);
				y = Angles.trnsy(relativeTo * 90f, 4f);
			}});
			items.add(i, 1);
		}

		@Override
		public void handleStack(Item ite, int amount, Teamc source) {
			for(int i = 0; i < amount; i++) {
				beltItems.add(new ConveyorItem() {{
					item = ite;
					x = 0;
					y = 0;
				}});
			}
			items.add(ite, amount);
		}

		public boolean pass(Item item){
			if(item != null && front() != null && front().team == team && front().acceptItem(this, item)){
				front().handleItem(this, item);
				return true;
			}
			return false;
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);
			spin.read(read);

			byte size = read.b();
			for(int i = 0; i < size; i++) {
				beltItems.add(new ConveyorItem() {{
					item = Vars.content.item(read.i());
					x = read.f();
					y = read.f();
				}});
			}
		}

		@Override
		public int removeStack(Item item, int amount) {
			int removed = 0;

			for (int i = 0; i < amount; i++) {
				for (ConveyorItem cItem : beltItems) {
					if (cItem.item == item) {
						beltItems.remove(cItem);
						removed++;
						break;
					}
				}
			}

			items.remove(item, removed);
			return removed;
		}

		public float conveyorSpeed() {
			return spinGraph().speed / (spinGraph().ratios.get(this, 1) * 360f) * movementScale;
		}

		@Override
		public float totalProgress() {
			return getRotation();
		}

		@Override
		public void updateTile() {
			float tx = Angles.trnsx(rotdeg(), 4f);
			float ty = Angles.trnsy(rotdeg(), 4f);
			beltItems.sort(
				item -> -Mathf.dst(item.x, item.y, tx, ty)
			).each(item -> {
				ConveyorItem next = beltItems.peek() == item ? null : beltItems.get(beltItems.indexOf(item) + 1);
				return next == null || Mathf.dst(next.x, next.y, item.x, item.y) > Vars.itemSize;
			}, item -> {
				item.x = Mathf.approachDelta(item.x, tx, conveyorSpeed());
				item.y = Mathf.approachDelta(item.y, ty, conveyorSpeed());

				if (item.x == Angles.trnsx(rotdeg(), 4f) && item.y == Angles.trnsy(rotdeg(), 4f) && pass(item.item)) {
					beltItems.remove(item);
					items.remove(item.item, 1);
				}
			});
		}

		@Override
		public void write(Writes write) {
			super.write(write);
			spin.write(write);

			write.b(beltItems.size);
			beltItems.each(beltItem -> {
				write.i(beltItem.item.id);
				write.f(beltItem.x);
				write.f(beltItem.y);
			});
		}
	}

	public static class ConveyorItem {
		public Item item;
		public float x, y;
	}
}

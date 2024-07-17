package sw.core;

import arc.*;
import arc.math.geom.*;
import arc.struct.*;
import mindustry.*;
import mindustry.async.*;
import mindustry.game.*;
import mindustry.world.*;
import sw.world.*;
import sw.world.interfaces.*;

/**
 * process to handle env tiles that span multiple tiles
 */
public class EnvProcess implements AsyncProcess {
	public Seq<MultiShape> shapes = new Seq<>();

	private final Seq<Tile> floorChecked = new Seq<>();
	private final Seq<Tile> overlayChecked = new Seq<>();
	private final Seq<Tile> blockChecked = new Seq<>();

	public EnvProcess() {
		Vars.asyncCore.processes.add(this);
		Events.run(EventType.Trigger.draw, () -> {
			shapes.each(shape -> shape.build.draw(shape));
		});
	}

	@Override
	public void begin() {
		shapes.each(shape -> shape.build.update(shape));
	}

	@Override
	public void init() {
		shapes.clear();
		floorChecked.clear();
		overlayChecked.clear();
		blockChecked.clear();
		Vars.world.tiles.eachTile(tile -> {
			if (tile.floor() instanceof MultiEnvI base && !floorChecked.contains(tile)) createShape(base, tile, 0);
			if (tile.overlay() instanceof MultiEnvI base && !overlayChecked.contains(tile)) createShape(base, tile, 1);
			if (tile.block() instanceof MultiEnvI base && !blockChecked.contains(tile)) createShape(base, tile, 2);
		});
	}

	public void createShape(MultiEnvI base, Tile start, int layer) {
		MultiShape shape = new MultiShape();

		shape.layer = layer;
		shape.build = base.createMultiShapeBuild();

		Seq<Tile> tmp = Seq.with(start);
		shape.tiles.add(start);

		while (!tmp.isEmpty()) {
			Tile current = tmp.pop();
			switch (layer) {
				default -> floorChecked.add(current);
				case 1 -> overlayChecked.add(current);
				case 2 -> blockChecked.add(current);
			}
			for (Point2 next : Geometry.d4) {
				Tile nearby = current.nearby(next);
				if (nearby == null) continue;
				switch (layer) {
					default -> {
						if (nearby.floor() instanceof MultiEnvI b && ((MultiEnvI) start.floor()).multiShapeGroup().contains(b.asBlock())) {
							if (shape.tiles.addUnique(nearby)) tmp.addUnique(nearby);
						}
					}
					case 1 -> {
						if (nearby.overlay() instanceof MultiEnvI b && ((MultiEnvI) start.overlay()).multiShapeGroup().contains(b.asBlock())) {
							if (shape.tiles.addUnique(nearby)) tmp.addUnique(nearby);
						}
					}
					case 2 -> {
						if (nearby.block() instanceof MultiEnvI b && ((MultiEnvI) start.block()).multiShapeGroup().contains(b.asBlock())) {
							if (shape.tiles.addUnique(nearby)) tmp.addUnique(nearby);
						}
					}
				}
			}
		}

		shapes.add(shape);
	}
}

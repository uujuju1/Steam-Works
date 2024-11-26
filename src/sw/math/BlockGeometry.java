package sw.math;

import arc.math.geom.*;
import arc.struct.*;

public class BlockGeometry {
	public static Seq<Point2>

	half2 = Seq.with(
		new Point2(2, 0),
		new Point2(1, 2),
		new Point2(-1, 1),
		new Point2(0, -1)
	),

	full1 = Seq.with(
		new Point2(1, 0),
		new Point2(0, 1),
		new Point2(-1, 0),
		new Point2(0, -1)
	),

	side21 = Seq.with(new Point2(2, 0), new Point2(2, 1)),
	side22 = Seq.with(new Point2(1, 2), new Point2(0, 2)),
	side23 = Seq.with(new Point2(-1, 1), new Point2(-1, 0)),
	side24 = Seq.with(new Point2(0, -1), new Point2(1, -1)),

	sides1 = Seq.with(new Point2(1, 0), new Point2(-1, 0)),
	sides2 = Seq.with(new Point2(0, 1), new Point2(0, -1)),
	sides3 = Seq.with(new Point2(-1, 0), new Point2(1, 0)),
	sides4 = Seq.with(new Point2(0, -1), new Point2(0, 1)),

	sides21 = Seq.with(
		new Point2(2, 0),
		new Point2(-1, 0),
		new Point2(2, 1),
		new Point2(-1, 1)
	),
	sides22 = Seq.with(
		new Point2(1, 2),
		new Point2(1, -1),
		new Point2(0, 2),
		new Point2(0, -1)
	),
	sides23 = Seq.with(
		new Point2(2, 1),
		new Point2(-1, 1),
		new Point2(2, 0),
		new Point2(-1, 0)
	),
	sides24 = Seq.with(
		new Point2(0, 2),
		new Point2(0, -1),
		new Point2(1, 2),
		new Point2(1, -1)
	);
}

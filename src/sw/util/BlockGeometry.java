package sw.util;

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

	sides2 = Seq.with(
		new Point2(2, 0),
		new Point2(2, 1),
		new Point2(-1, 1),
		new Point2(-1, 0)
	);
}

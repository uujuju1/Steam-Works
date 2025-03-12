package sw.world.blocks.power;

import mindustry.world.*;
import sw.world.interfaces.*;

public class ShaftTransmission extends AxleBlock {
	public float multiplier = 2;

	// TODO make a way to generate the lowEdges array
	public int[] highEdges;
	public int[] lowEdges;

	public ShaftTransmission(String name) {
		super(name);
	}

	public class ShaftTransmissionBuild extends AxleBlockBuild {
		@Override
		public boolean invalidConnection(HasSpin other, float ratio, float lastRatio) {
			return false;
//			boolean otherHigh = false;
//			for(int i : highEdges) {
//				if (nearby(
//					Edges.getEdges(size)[spinConfig.allowedEdges[rotation][i]].x,
//					Edges.getEdges(size)[spinConfig.allowedEdges[rotation][i]].y
//				) == other) otherHigh = true;
//			}
//			boolean otherLow = false;
//			for(int i : lowEdges) {
//				if (nearby(
//					Edges.getEdges(size)[spinConfig.allowedEdges[rotation][i]].x,
//					Edges.getEdges(size)[spinConfig.allowedEdges[rotation][i]].y
//				) == other) otherLow = true;
//			}
//
//			return
//				other instanceof ShaftTransmissionBuild ?
//				(lastRatio * multiplier < ratio || ratio < lastRatio / multiplier || other.rotation() != rotation) :
//				(ratio != lastRatio || (otherHigh && otherLow));

		}

		@Override
		public float ratioOf(HasSpin other, HasSpin last, float startRatio, float lastRatio) {
			boolean otherHigh = false;
			for(int i : highEdges) {
				if (nearby(
					Edges.getEdges(size)[spinConfig.allowedEdges[rotation][i]].x,
					Edges.getEdges(size)[spinConfig.allowedEdges[rotation][i]].y
				) == other) otherHigh = true;
			}
			boolean otherLow = false;
			for(int i : lowEdges) {
				if (nearby(
					Edges.getEdges(size)[spinConfig.allowedEdges[rotation][i]].x,
					Edges.getEdges(size)[spinConfig.allowedEdges[rotation][i]].y
				) == other) otherLow = true;
			}
			boolean lastHigh = false;
			for(int i : highEdges) {
				if (nearby(
					Edges.getEdges(size)[spinConfig.allowedEdges[rotation][i]].x,
					Edges.getEdges(size)[spinConfig.allowedEdges[rotation][i]].y
				) == last) lastHigh = true;
			}

			float h = startRatio * (otherHigh ? multiplier : 1f / multiplier);

			if (startRatio == 1 && lastRatio != 1) return 1;

			return otherHigh ^ lastHigh && !(otherHigh && otherLow) ? h : startRatio;
		}
	}
}

package sw.world.blocks.power;

public class ShaftTransmission extends AxleBlock {
	public float multiplier = 2;

	public int[] scaledEdges;

	public ShaftTransmission(String name) {
		super(name);
	}

	public class ShaftTransmissionBuild extends AxleBlockBuild {
	}
}

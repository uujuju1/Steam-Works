package sw.world.blocks.power;

public class AxleBrake extends AxleBlock {
	public float targetSpeed = 10;
	
	public float brakeTarget = 10;
	
	public AxleBrake(String name) {
		super(name);
	}
	
	public class AxleBrakeBuild extends AxleBlockBuild {
		@Override public float getResistance() {
			return super.getResistance() + (getRotation() > targetSpeed && efficiency > 0 ? brakeTarget : 0f);
		}
	}
}

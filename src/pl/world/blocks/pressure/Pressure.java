package pl.world.blocks.pressure;

public class Pressure {
  public float minPressure = 0f;
  public float maxPressure = 200f;
  public float internalSize = 6f;
  public float transferRate = 0.4f;
  public float lossRate = 0.997f;
  public boolean acceptsPressure = true;
  public boolean outputsPressure = true;
  public boolean overflows = true;
}

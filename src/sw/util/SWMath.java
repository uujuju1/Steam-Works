package sw.util;

import arc.math.Mathf;
import arc.util.Time;
import sw.SWVars;

/**
 * @author uujuju
 */
public class SWMath {
/**
 * @param cap disables delta time if the game lags too much
 */
  public static float heatTransferDelta(float conductivity, float temp1, float temp2, boolean cap) {
    if (cap && Time.delta > 1.5f) return conductivity * (temp1 - temp2);
    return conductivity * (temp1 - temp2) * Time.delta;
  }
  public static float heatMap(float heat, float min, float max) {
    return Mathf.map(heat, min, max, 0f, 1f);
  }
  public static float heatGlow(float heat) {
    return Mathf.clamp(Mathf.map(heat, 0, SWVars.maxHeatGlow, 0f, 1f));
  }
}

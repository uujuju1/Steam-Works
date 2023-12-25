package sw.world.modules;

import arc.struct.*;
import arc.util.io.*;
import mindustry.world.modules.*;
import sw.world.graph.*;
import sw.world.graph.HeatGraphDeprecated.*;

public class HeatModule extends BlockModule {
  public float heat;
	public HeatGraphDeprecated graph = new HeatGraphDeprecated();

  public final Seq<HeatLink> links = new Seq<>();

	public void addHeat(float amount) {
    heat+=amount;
  }
  public void setHeat(float amount) {
    heat = amount;
  }

  @Override
  public void write(Writes write) {
    write.f(heat);
    write.s(links.size);
    for (HeatLink link: links) {
      write.i(link.l1);
      write.i(link.l2);
    }
  }
  @Override
  public void read(Reads read) {
    heat = read.f();
    short size = read.s();
    for (int i = 0; i < size; i++) {
      HeatLink link = new HeatLink(read.i(), read.i());
      links.add(link);
      graph.links.add(link);
    }
  }
}

package sw.entities;

import arc.func.Prov;
import arc.struct.ObjectIntMap;
import arc.struct.ObjectMap.Entry;
import mindustry.gen.EntityMapping;
import mindustry.gen.Entityc;
import sw.entities.comp.CrafterUnit;
import sw.entities.comp.SubmarineUnit;


public class SWEntityMapping {
  public static int customUnits;
  public static ObjectIntMap<Class<? extends Entityc>> idMap = new ObjectIntMap<>();
  public static Entry<Class<? extends Entityc>, Prov<? extends Entityc>>[] entities = new Entry[]{
    entry(SubmarineUnit.class, SubmarineUnit::new),
    entry(CrafterUnit.class, CrafterUnit::new)
  };

  private static <T extends Entityc> Entry<Class<T>, Prov<T>> entry(Class<T> name, Prov<T> prov) {
    Entry<Class<T>, Prov<T>> out = new Entry<>();
    out.key = name;
    out.value = prov;
    return out;
  }

  public static void load() {
    for (Entry<Class<? extends Entityc>, Prov<? extends Entityc>> entry : entities) {
      customUnits++;
      idMap.put(entry.key, EntityMapping.register("CustomUnit:" + customUnits, entry.value));
    }
  }
}

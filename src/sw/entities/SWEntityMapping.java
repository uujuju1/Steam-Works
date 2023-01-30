package sw.entities;

import arc.func.Prov;
import arc.struct.ObjectIntMap;
import arc.struct.ObjectMap.Entry;
import arc.util.Structs;
import mindustry.gen.EntityMapping;
import mindustry.gen.Entityc;
import sw.entities.comp.SubmarineUnit;


public class SWEntityMapping {
  public static ObjectIntMap<String> idMap = new ObjectIntMap<>();
  public static Entry<String, Prov<? extends Entityc>>[] entities = new Entry[]{
    entry("SubmarineUnit", SubmarineUnit::new)
  };

  private static <T extends Entityc> Entry<String, Prov<T>> entry(String name, Prov<T> prov) {
    Entry<String, Prov<T>> out = new Entry<>();
    out.key = name;
    out.value = prov;
    return out;
  }

  public static void load() {
    for (Entry<String, Prov<? extends Entityc>> entry : entities) {
      EntityMapping.register(entry.key, entry.value);
      idMap.put(entry.key, Structs.indexOf(EntityMapping.idMap, entry.value));
    }
  }
}

package sw.entities.units;

import arc.func.*;
import arc.struct.*;
import mindustry.gen.*;

public class EntityMaps {
	public static ObjectIntMap<String> ids = new ObjectIntMap<>();

	public static void load() {
		registerEntity(CopterUnit.class, CopterUnit::new);
		registerUnit("sw-soar", CopterUnit::new);
		registerUnit("sw-volare", CopterUnit::new);
		registerUnit("sw-wisp", UnitEntity::create);
		registerEntity(CollisionlessLegsUnit.class, CollisionlessLegsUnit::new);
		registerUnit("sw-lambda", CollisionlessLegsUnit::new);
		registerUnit("sw-rho", UnitEntity::create);
	}

	public static <T extends Entityc> void registerEntity(Class<T> type, Prov<T> provider) {
		ids.put(type.getName(), EntityMapping.register(type.getName(), provider));
	}

	public static void registerUnit(String name, Prov<Unit> constructor) {
		EntityMapping.nameMap.put(name, constructor);
	}
}

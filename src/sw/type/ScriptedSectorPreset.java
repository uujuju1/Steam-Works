package sw.type;

import arc.struct.*;
import mindustry.type.*;

public class ScriptedSectorPreset extends SectorPreset {
	public static ObjectMap<String, Runnable> scripts = new ObjectMap<>();

	public Runnable script;

	public ScriptedSectorPreset(String name, Planet planet, int id, Runnable script) {
		super(name, planet, id);
		this.script = script;
		addScript(generator.map.name(), script);
	}

	public static void addScript(String name, Runnable script) {
		scripts.put(name, script);
	}
}

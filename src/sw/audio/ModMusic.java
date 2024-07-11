package sw.audio;

import arc.audio.*;
import arc.struct.*;

import static mindustry.Vars.*;

public class ModMusic {
	public static Music adventurers, steelSoul, descend, cigarra;

	public static void load() {
		adventurers = loadMusic(tree.loadMusic("adventurers"), control.sound.ambientMusic);
		steelSoul = loadMusic(tree.loadMusic("steelsoul"), control.sound.darkMusic);
		descend = loadMusic(tree.loadMusic("descend"), control.sound.darkMusic);
		cigarra = loadMusic(tree.loadMusic("cigarra"), control.sound.ambientMusic);
	}

	public static Music loadMusic(Music music, Seq<Music> to) {
		to.add(music);
		return music;
	}
}

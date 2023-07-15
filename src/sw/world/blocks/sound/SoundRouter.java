package sw.world.blocks.sound;

import arc.util.io.*;
import mindustry.gen.*;
import mindustry.world.*;
import sw.world.interfaces.*;
import sw.world.modules.*;

public class SoundRouter extends Block {
	public SoundRouter(String name) {
		super(name);
		solid = destructible = true;
		update = true;
	}

	public class SoundRouterBuild extends Building implements HasSound {
		SoundModule sound = new SoundModule();

		@Override public SoundModule sound() {
			return sound;
		}

		@Override
		public void onProximityUpdate() {
			soundGraph().addBuild(this);
			for(HasSound build : soundProximity()) soundGraph().mergeGraph(build.soundGraph());
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);
			sound.read(read);
		}
		@Override
		public void write(Writes write) {
			super.write(write);
			sound.write(write);
		}
	}
}

package sw.core;

import arc.*;
import arc.audio.*;
import arc.util.*;
import mindustry.*;
import mindustry.audio.*;
import mindustry.game.*;
import sw.type.*;

import static mindustry.Vars.*;

public class MusicPlayer extends SoundControl {
	public void init() {
		Events.on(EventType.ClientLoadEvent.class, e -> {
			if (!Vars.headless && Core.settings.getBool("sw-replace-music", true)) Vars.control.sound = this;
		});
		Events.on(EventType.SectorLaunchEvent.class, e -> {
			if (!Vars.headless && e.sector.preset instanceof PositionSectorPreset modPreset && modPreset.landMusic != null) {
				Time.run(modPreset.core.landDuration, () -> playOnce(modPreset.landMusic));
			}
		});
	}

	@Override
	public void update() {
		updateBus();
		updateLoops();
	}

	public void updateBus() {
		boolean paused = state.isGame() && Core.scene.hasDialog();
		boolean playing = state.isGame();

		//check if current track is finished
		if(current != null && !current.isPlaying()){
			current = null;
			fade = 0f;
		}

		//fade the lowpass filter in/out, poll every 30 ticks just in case performance is an issue
		if(timer.get(1, 30f)){
			Core.audio.soundBus.fadeFilterParam(0, Filters.paramWet, paused ? 1f : 0f, 0.4f);
		}

		//play/stop ordinary effects
		if(playing != wasPlaying){
			wasPlaying = playing;

			if(playing){
				Core.audio.soundBus.play();
				setupFilters();
			}else{
				//stopping a single audio bus stops everything else, yay!
				Core.audio.soundBus.stop();
				//play music bus again, as it was stopped above
				Core.audio.musicBus.play();

				Core.audio.soundBus.play();
			}
		}

		Core.audio.setPaused(Core.audio.soundBus.id, state.isPaused());
	}
}

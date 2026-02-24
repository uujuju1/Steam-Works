package sw.core;

import arc.*;
import arc.audio.*;
import arc.math.*;
import arc.scene.actions.*;
import arc.scene.event.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.audio.*;
import mindustry.game.*;
import mindustry.ui.*;
import sw.gen.*;
import sw.type.*;

import static mindustry.Vars.*;

public class MusicPlayer extends SoundControl {
	public final Seq<Music> casual = Seq.with();
	public final ObjectMap<Music, String[]> titles = new ObjectMap<>();

	public MusicPlayer() {
		musicInterval = 10 * Time.toMinutes;
	}

	public void musicNotification(Music music){
		Table t = new Table(Styles.black3);
		t.touchable = Touchable.disabled;
		t.margin(8f).add(Core.bundle.format(
			"ui.sw-music-player-notification",
			(Object[]) titles.get(music, () -> new String[]{"unknown", "unknown"})
		)).style(Styles.outlineLabel).labelAlign(Align.center);
		t.update(() -> {
			t.toFront();

			if(state.isMenu() || !ui.hudfrag.shown) t.remove();
		});
		t.pack();
		t.setPosition(0f, Core.graphics.getHeight() / 2f, Align.left);
		t.actions(
			Actions.fadeOut(0f),
			Actions.fadeIn(1f, Interp.pow4Out),
			Actions.delay( 3f),
			Actions.parallel(
				Actions.moveBy(0f, -t.getHeight() * 3f, 1f, Interp.pow4In),
				Actions.fadeOut(1f, Interp.pow4In)
			),
			Actions.remove()
		);
		t.act(0.1f);
		Core.scene.add(t);
	}

	public void init() {
		Events.on(EventType.ClientLoadEvent.class, e -> {
			if (!Vars.headless && Core.settings.getBool("sw-replace-music", true)) {
				Vars.control.sound = this;
				reload();
			}
		});
		Events.on(EventType.SectorLaunchEvent.class, e -> {
			if (!Vars.headless && e.sector.preset instanceof PositionSectorPreset modPreset && modPreset.landMusic != null) {
				Time.run(modPreset.core.landDuration, () -> playOnce(modPreset.landMusic));
			}
		});
	}

	@Override
	protected void playOnce(Music music) {
		if(current != null || music == null || !shouldPlay()) return;
		super.playOnce(music);
		musicNotification(music);
	}

	@Override
	protected void reload() {
		super.reload();
		casual.add(SWMusics.fog, SWMusics.create, SWMusics.complex);
		titles.put(SWMusics.fog, new String[]{"Fog", "12Three7"});
		titles.put(SWMusics.create, new String[]{"Create", "12Three7"});
		titles.put(SWMusics.complex, new String[]{"Complex", "12Three7"});

		titles.put(SWMusics.dust, new String[]{"Dust", "Liz"});
		titles.put(SWMusics.scorchedBay, new String[]{"Scorched Bay", "Liz"});
		titles.put(SWMusics.mountainWisp, new String[]{"Mountain Wisp", "Liz"});
	}

	@Override
	public void update() {
		updateBus();

		if(Time.timeSinceMillis(lastPlayed) > 1000 * musicInterval / 60f && !state.isMenu()){
			//chance to play it per interval
			if(Mathf.chance(musicChance)){
				lastPlayed = Time.millis();
				playOnce(casual.random(lastRandomPlayed));
			}
		}

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

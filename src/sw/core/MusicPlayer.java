package sw.core;

import arc.*;
import arc.audio.*;
import arc.math.*;
import arc.scene.actions.*;
import arc.scene.event.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import arc.util.serialization.*;
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

		setupFilters();
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
				control.sound.bossMusic.clear();
				control.sound.darkMusic.clear();
				control.sound.ambientMusic.clear();
				Vars.control.sound = this;
				reload();
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
		casual.addAll(
			SWMusics.asTerras,
			SWMusics.pendahuluan,
			SWMusics.terlahirKembali,
			SWMusics.pendaratanPertama,
			SWMusics.passadoEsquecido
		);

		JsonValue r = new JsonReader().parse(Vars.tree.get("music/names.json")).get("titles");
		for(JsonValue value = r.child; value != null; value = value.next){
			try {
				Music music = (Music) SWMusics.class.getField(Strings.kebabToCamel(value.getString("music"))).get(null);
				String name = value.getString("name"), author = value.getString("author");

				titles.put(music, new String[]{name, author});
			} catch (NoSuchFieldException | IllegalAccessException e) {
				Log.err("Failed to parse music title for @. Does it exist?", value.get("music"));
			}
		}

		Events.on(EventType.SectorLaunchEvent.class, e -> {
			if (!Vars.headless && e.sector.preset instanceof PositionSectorPreset modPreset && modPreset.landMusic != null) {
				Time.run(modPreset.core.landDuration, () -> playOnce(modPreset.landMusic));
			}
		});
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

		if (current != null) {
			current.setVolume(Core.settings.getInt("musicvol", 100) / 100f);

			if (Core.settings.getInt("musicvol", 100) == 0) stop();
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

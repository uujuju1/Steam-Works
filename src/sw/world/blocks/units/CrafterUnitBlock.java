package sw.world.blocks.units;

import arc.math.geom.*;
import arc.scene.style.*;
import arc.scene.ui.TextField.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.ui.*;
import sw.util.*;

public class CrafterUnitBlock extends SingleUnitFactory {
	public CrafterUnitBlock(String name) {
		super(name);
	}

	public class CrafterUnitBlockBuild extends SingleUnitFactoryBuild {
		public Point2 start = new Point2(0, 0), end = new Point2(0, 0);
		public int waitTime1 = 0, waitTime2 = 0, counter = 0;
		public float time = 0;

		@Override
		public boolean canBuild() {
			return efficiency > 0 && !(unit instanceof BuildingTetherc);
		}

		@Override
		public void buildConfiguration(Table cont) {
			var whiteui = (TextureRegionDrawable) Tex.whiteui;

			cont.table(Styles.black6, table -> {
				table.table(whiteui.tint(SWDraw.compoundBase), box -> {
					box.add("move:").left().padBottom(5).row();
					box.table(Styles.black, move -> {
						move.add("x");
						move.field(start.x + "", TextFieldFilter.digitsOnly, text -> {
							Log.info(text);
							start.x = Strings.parseInt(text, 0);
						});
						move.add("y").padLeft(5);
						move.field(start.y + "", TextFieldFilter.digitsOnly, text -> {
							Log.info(text);
							start.y = Strings.parseInt(text, 0);
						});
	        }).margin(5).left();
			  }).left().margin(5).pad(5).row();
				table.table(whiteui.tint(SWDraw.compoundBase), box -> {
					box.add("wait:").left().padBottom(5).row();
					box.table(Styles.black, wait -> {
						wait.field(waitTime1 + "", TextFieldFilter.digitsOnly, text -> {
							Log.info(text);
							waitTime1 = Strings.parseInt(text, 0);
						});
						wait.add("seconds").padLeft(5);
					}).margin(5).left();
				}).left().margin(5).pad(5).row();
				table.table(whiteui.tint(SWDraw.compoundBase), box -> {
					box.add("move:").left().padBottom(5).row();
					box.table(Styles.black, move -> {
						move.add("x");
						move.field(end.x + "", TextFieldFilter.digitsOnly, text -> {
							Log.info(text);
							end.x = Strings.parseInt(text, 0);
						});
						move.add("y").padLeft(5);
						move.field(end.y + "", TextFieldFilter.digitsOnly, text -> {
							Log.info(text);
							end.y = Strings.parseInt(text, 0);
						});
					}).margin(5).left();
				}).left().margin(5).pad(5).row();
				table.table(whiteui.tint(SWDraw.compoundBase), box -> {
				box.add("wait:").left().padBottom(5).row();
				box.table(Styles.black, wait -> {
					wait.field(waitTime2 + "", TextFieldFilter.digitsOnly, text -> {
						Log.info(text);
						waitTime2 = Strings.parseInt(text, 0);
					});
					wait.add("seconds").padLeft(5);
				}).margin(5).left();
			}).left().margin(5).pad(5).row();
			}).margin(10);
		}
	}
}

package sw.matryoshka;

import arc.scene.ui.layout.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.ui.*;
import sw.*;
import sw.content.*;
import sw.content.blocks.*;
import sw.matryoshka.actions.*;
import sw.matryoshka.ui.*;
import sw.matryoshka.world.*;
import sw.world.blocks.power.*;
import sw.world.blocks.sandbox.*;

public class Tutorials {
	public static TutorialSequence demo = new TutorialBuilder() {{
		addLayer((() -> new SequenceLayer() {{
			nesting = new Nesting(5, 5);

			actions.add(new UIElementAction(new Table(t -> {
				t.setFillParent(true);
				t.table(Styles.black6, e -> e.labelWrap("This is a text that will appear for a moment ooooo").width(100).pad(10f));
			})) {{
				startTime = 350f;
				endTime = 450f;
			}});

			timestamp.add(60f);
			timestamp.add(300f);
			maxTime = 600f;
		}}));
	}};

	public static TutorialSequence transmission = new TutorialBuilder() {{
		addLayer(() -> new SequenceLayer() {{
			nesting = new Nesting(9, 9);

			SWVars.matryoshka.run(nesting, () -> {
				nesting.world.tiles.eachTile(tile -> {
					tile.setFloor(Blocks.metalTiles7.asFloor());
					if (tile.x % 4 == 0 || tile.y % 4 == 0) tile.setFloor(Blocks.metalTiles11.asFloor());
				});

				nesting.world.tile(3, 4).setBlock(SWSandbox.carnotEngine, Team.sharded, 0);
				nesting.world.tile(4, 4).setBlock(SWPower.wireShaft, Team.sharded, 0);
				nesting.world.tile(5, 4).setBlock(SWPower.wireShaftRouter, Team.sharded, 2);
				nesting.world.tile(5, 5).setBlock(SWPower.wireShaftRouter, Team.sharded, 3);
				nesting.world.tile(6, 5).setBlock(SWPower.shaftGearbox, Team.sharded, 0);
				nesting.world.tile(4, 2).setBlock(SWPower.shaftGearbox, Team.sharded, 1);
				nesting.world.tile(4, 5).setBlock(SWPower.overheadBelt, Team.sharded, 0);
				nesting.world.tile(1, 5).setBlock(SWPower.overheadBelt, Team.sharded, 3);
				nesting.world.tile(1, 4).setBlock(SWPower.wireShaft, Team.sharded, 1);
				nesting.world.tile(1, 3).setBlock(SWPower.wireShaft, Team.sharded, 1);
				nesting.world.tile(1, 2).setBlock(SWPower.wireShaft, Team.sharded, 1);

				var source = ((SpinSource.SpinSourceBuild) nesting.world.build(3, 4));
				source.targetSpeed = 2f;
				source.force = 1f;

				var belt1 = ((AxleBridge.AxleBridgeBuild) nesting.world.build(4, 5));
				var belt2 = ((AxleBridge.AxleBridgeBuild) nesting.world.build(1, 5));
				belt1.link = belt2.pos();
				belt2.incoming.add(belt1.pos());
			});

			maxTime = 2000f;

			timestamp.add(100f);
			actions.add(new UIElementAction(new Table(t -> {
				t.setFillParent(true);
//				t.left();
				t.table(Styles.black6, table -> table.add("@tutorial.sw-spin-transmission.spin-blocks").pad(10f));
			})) {{
				fadeSeconds = 0.5f;
				startTime = 120f;
				endTime = 360f;
			}});

			timestamp.add(400f);
			nesting.actions.add(new RunnablesAction() {{
				startTime = 420f;
				endTime = 450f;
				start = (ignored, ignoredToo) -> {
					nesting.world.tiles.eachTile(tile -> {
						if (tile.build != null) {
							SWFx.changeEffect.at(tile.build.x, tile.build.y, 0, tile.build.block);
							tile.setAir();
						}
					});
				};
				end = (ignored, ignoredToo) -> {
					nesting.world.tile(4, 7).setBlock(Blocks.metalWall3);
					nesting.world.tile(5, 7).setBlock(Blocks.metalWall3);
					nesting.world.tile(4, 7).setOverlay(SWEnvironment.fissure);
					nesting.world.tile(5, 7).setOverlay(SWEnvironment.fissure);
					nesting.world.tile(4, 3).setBlock(SWCrafting.engineSmelter, Team.sharded, 0);
					nesting.world.tile(4, 5).setBlock(SWProduction.mechanicalFracker, Team.sharded, 1);
					nesting.world.tile(4, 1).setBlock(SWSandbox.carnotEngine, Team.sharded, 1);
					SWFx.changeEffect.at(32, 56, 0, SWCrafting.engineSmelter);
					SWFx.changeEffect.at(40, 56, 0, SWCrafting.engineSmelter);
					SWFx.changeEffect.at(32, 24, 0, SWCrafting.engineSmelter);
					SWFx.changeEffect.at(4.5f * 8f, 5.5f * 8, 0, SWProduction.mechanicalFracker);
					SWFx.changeEffect.at(32f, 8f, 0, SWSandbox.carnotEngine);

					var source = ((SpinSource.SpinSourceBuild) nesting.world.build(4, 1));
					source.targetSpeed = 2f;
					source.force = 1f;
				};
			}});
			actions.add(new UIElementAction(new Table(t -> {
				t.setFillParent(true);
//				t.left();
				t.table(Styles.black6, table -> table.add("@tutorial.sw-spin-transmission.spin-buildings").pad(10f));
			})) {{
				fadeSeconds = 0.5f;
				startTime = 450f;
				endTime = 660f;
			}});
		}});
	}};
}

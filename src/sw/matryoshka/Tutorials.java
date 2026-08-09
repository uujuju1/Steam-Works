package sw.matryoshka;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.scene.ui.layout.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.game.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import sw.*;
import sw.content.*;
import sw.content.blocks.*;
import sw.matryoshka.actions.*;
import sw.matryoshka.ui.*;
import sw.matryoshka.world.*;
import sw.world.blocks.power.*;
import sw.world.blocks.sandbox.*;
import sw.world.interfaces.*;

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

			maxTime = 2500f;

			// region spin blocks
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
			// endregion

			// region spin buildings
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
					SWFx.changeEffect.at(32, 56, 0, Blocks.router);
					SWFx.changeEffect.at(40, 56, 0, Blocks.router);
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
			// endregion

			// region spin edges
			timestamp.add(660f);
			nesting.actions.add(new RunnablesAction() {{
				startTime = 690f;
				endTime = 720f;
				start = (ignored, ignoredToo) -> {
					nesting.world.tiles.eachTile(tile -> {
						if (tile.block() != Blocks.air) {
							SWFx.changeEffect.at(tile.build == null ? tile.drawx() : tile.build.x, tile.build == null ? tile.drawy() : tile.build.y, 0, tile.block());
							tile.setAir();
						}
						if (tile.overlay() != Blocks.air) tile.setOverlay(Blocks.air);
					});
				};
				end = (ignored, ignoredToo) -> {
					nesting.world.tile(2, 2).setBlock(SWPower.wireShaftRouter, Team.sharded, 0);
					nesting.world.tile(2, 6).setBlock(SWPower.wireShaft, Team.sharded, 0);
					nesting.world.tile(6, 2).setBlock(SWPower.handWheel, Team.sharded, 0);
					nesting.world.tile(6, 6).setBlock(SWCrafting.engineSmelter, Team.sharded, 0);
					SWFx.changeEffect.at(16, 16, 0, SWPower.wireShaftRouter);
					SWFx.changeEffect.at(16, 48, 0, SWPower.wireShaft);
					SWFx.changeEffect.at(48, 16, 0, SWPower.handWheel);
					SWFx.changeEffect.at(48, 48, 0, SWCrafting.engineSmelter);
				};
			}});
			actions.add(new UIElementAction(new Table(t -> {
				t.setFillParent(true);
//				t.left();
				t.table(Styles.black6, table -> table.add("@tutorial.sw-spin-transmission.spin-edges").pad(10f));
			})) {{
				fadeSeconds = 0.5f;
				startTime = 720f;
				endTime = 1020f;
			}});
			nesting.actions.add(new RunnablesAction() {{
				startTime = endTime = 840f;
				start = (ignored, ignoredToo) -> {
					Effect effect = new Effect(180f, e -> {
						if (e.data instanceof HasSpin a) {
							Draw.alpha(e.foutpowdown());
							a.getConnectingOuterEdges().each(p -> Draw.rect(
								"sw-icon-spin-edge",
								(a.asBuilding().tileX() + p.x + (p.x == 0 ? 0 : -Mathf.sign(p.x) / 2f)) * Vars.tilesize,
								(a.asBuilding().tileY() + p.y + (p.y == 0 ? 0 : -Mathf.sign(p.y) / 2f)) * Vars.tilesize,
								4, 4
							));
						}
					}).layer(Layer.effect + 1f);
					effect.at(0, 0, 0, nesting.world.build(2, 2));
					effect.at(0, 0, 0, nesting.world.build(2, 6));
					effect.at(0, 0, 0, nesting.world.build(6, 2));
					effect.at(0, 0, 0, nesting.world.build(6, 6));
				};
			}});
			// endregion

			// region edge pair
			timestamp.add(1080f);
			nesting.actions.add(new RunnablesAction() {{
				startTime = 1110f;
				endTime = 1140f;
				start = (ignored, ignoredToo) -> {
					nesting.world.tiles.eachTile(tile -> {
						if (tile.build != null) {
							SWFx.changeEffect.at(tile.build.x, tile.build.y, 0, tile.build.block);
							tile.setAir();
						}
					});
				};
				end = (ignored, ignoredToo) -> {
					for (int i = 1; i < 8; i++) {
						nesting.world.tile(i, 4).setBlock(SWPower.wireShaft, Team.sharded, 0);
						SWFx.changeEffect.at(i * Vars.tilesize, 32, 0, SWPower.wireShaft);
					}
					nesting.world.tile(1, 4).setBlock(SWSandbox.carnotEngine, Team.sharded, 0);

					var source = ((SpinSource.SpinSourceBuild) nesting.world.build(1, 4));
					source.targetSpeed = 2f;
					source.force = 1f;
				};
			}});
			actions.add(new UIElementAction(new Table(t -> {
				t.setFillParent(true);
//				t.left();
				t.table(Styles.black6, table -> table.add("@tutorial.sw-spin-transmission.edge-pair").pad(10f));
			})) {{
				fadeSeconds = 0.5f;
				startTime = 1200f;
				endTime = 1380f;
			}});
			nesting.actions.add(new RunnablesAction() {{
				startTime = endTime = 1440f;
				start = (ignored, ignoredToo) -> {
					new Effect(120f, e -> {
						if (e.data instanceof HasSpin a) {
							Draw.color(Pal.heal);
							a.getConnectingOuterEdges().each(p -> Draw.rect(
								"sw-icon-spin-edge",
								(a.asBuilding().tileX() + p.x + (p.x == 0 ? 0 : -Mathf.sign(p.x) / 2f)) * Vars.tilesize,
								(a.asBuilding().tileY() + p.y + (p.y == 0 ? 0 : -Mathf.sign(p.y) / 2f)) * Vars.tilesize,
								4, 4
							));
						}
					}).layer(Layer.effect + 1f).at(0, 0, 0, nesting.world.build(4, 4));
				};
			}});
			nesting.actions.add(new RunnablesAction() {{
				startTime = endTime = 1560f;
				start = (ignored, ignoredToo) -> {
					nesting.world.tile(4, 4).setBlock(SWPower.wireShaft, Team.sharded, 1);
					SWFx.changeEffect.at(32, 32, 0, SWPower.wireShaft);
					new Effect(120f, e -> {
						if (e.data instanceof HasSpin a) {
							Draw.color(Pal.remove);
							Draw.alpha(e.foutpowdown());
							a.getConnectingOuterEdges().each(p -> Draw.rect(
								"sw-icon-spin-edge",
								(a.asBuilding().tileX() + p.x + (p.x == 0 ? 0 : -Mathf.sign(p.x) / 2f)) * Vars.tilesize,
								(a.asBuilding().tileY() + p.y + (p.y == 0 ? 0 : -Mathf.sign(p.y) / 2f)) * Vars.tilesize,
								4, 4
							));
						}
					}).layer(Layer.effect + 1f).at(0, 0, 0, nesting.world.build(4, 4));
				};
			}});
			// endregion

			// region exceptions

			timestamp.add(1860f);
			nesting.actions.add(new RunnablesAction() {{
				startTime = 1890f;
				endTime = 1920f;
				start = (ignored, ignoredToo) -> {
					nesting.world.tiles.eachTile(tile -> {
						if (tile.build != null) {
							SWFx.changeEffect.at(tile.build.x, tile.build.y, 0, tile.build.block);
							tile.setAir();
						}
					});
				};
				end = (ignored, ignoredToo) -> {
					nesting.world.tile(3, 5).setBlock(SWSandbox.carnotEngine, Team.sharded, 0);
					nesting.world.tile(4, 5).setBlock(SWPower.wireShaft, Team.sharded, 1);
					nesting.world.tile(3, 3).setBlock(SWPower.evaporator, Team.sharded, 0);
					SWFx.changeEffect.at(24, 40, 0, SWSandbox.carnotEngine);
					SWFx.changeEffect.at(32, 40, 0, SWPower.wireShaft);
					SWFx.changeEffect.at(28, 28, 0, SWPower.evaporator);

					var source = ((SpinSource.SpinSourceBuild) nesting.world.build(3, 5));
					source.targetSpeed = 2f;
					source.force = 1f;
				};
			}});
			actions.add(new UIElementAction(new Table(t -> {
				t.setFillParent(true);
//				t.left();
				t.table(Styles.black6, table -> table.add("@tutorial.sw-spin-transmission.exceptions").pad(10f));
			})) {{
				fadeSeconds = 0.5f;
				startTime = 1920f;
				endTime = 2100f;
			}});
			nesting.actions.add(new RunnablesAction() {{
				startTime = endTime = 2130f;
				start = (ignored, ignoredToo) -> {
					nesting.world.tiles.eachTile(tile -> {
						for (int i = 0; i < 3; i++) {
							nesting.world.tile(i * 2 + 1, 5).setBlock(SWSandbox.carnotEngine, Team.sharded, 0);
							nesting.world.tile(i * 2 + 2, 5).setBlock(SWPower.wireShaft, Team.sharded, 1);
							nesting.world.tile(i * 2 + 1, 3).setBlock(SWPower.evaporator, Team.sharded, 0);
							SWFx.changeEffect.at((i * 2 + 1) * Vars.tilesize, 40, 0, SWSandbox.carnotEngine);
							SWFx.changeEffect.at((i * 2 + 2) * Vars.tilesize, 40, 0, SWPower.wireShaft);
							SWFx.changeEffect.at((i * 2 + 1.5f) * Vars.tilesize, 28, 0, SWPower.evaporator);

							var source = ((SpinSource.SpinSourceBuild) nesting.world.build(i * 2 + 1, 5));
							source.targetSpeed = 2f + i * 2;
							source.force = 1f;
						}
					});
				};
			}});
			actions.add(new UIElementAction(new Table(t -> {
				t.setFillParent(true);
//				t.left();
				t.table(Styles.black6, table -> table.add("@tutorial.sw-spin-transmission.exception-pair").pad(10f));
			})) {{
				fadeSeconds = 0.5f;
				startTime = 2130f;
				endTime = 2370f;
			}});

			// endregion
		}});
	}};
}

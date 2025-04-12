package sw.ui.elements;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.scene.*;
import arc.scene.actions.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.core.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.ui.layout.*;
import sw.ui.*;
import sw.ui.dialog.*;

import java.util.*;

public class TreeView extends Group {
	Table tooltip;

	private TechTreeDialog.TechTreeNode lastTooltip;
	private TechTreeDialog.TechTreeNode layout;
	private Button hoverButton;

	public TreeView() {
		tooltip = new Table(Tex.button);
		rebuild(null);
	}

	public void addChildCentered(Element actor) {
		actor.x -= actor.getWidth()/2f;
		actor.y -= actor.getHeight()/2f;
		addChild(actor);
	}

	public void addRoot(TechTreeDialog.TechTreeNode root) {
		if (root.visible) {
			Button button = new Button(new Button.ButtonStyle() {{
				up = Tex.button;
				if (!root.selectable) up = Tex.buttonRed;
				if (root.node.content.unlocked()) up = Tex.buttonOver;
			}});
			button.setSize(48f);

			button.setPosition(root.x, root.y);
			if (root.selectable) {
				button.image(root.node.content.uiIcon).size(32f);
			} else {
				button.image(Icon.lock.tint(Pal.gray)).size(32f);
			}
			button.clicked(() -> {
				if (Vars.mobile) {
					hoverButton = button;
					tooltip(root);
				} else SWUI.techtreeDialog.spend(root.node);
			});
			button.hovered(() -> {
				if (!Vars.mobile) {
					hoverButton = button;
					tooltip(root);
				}
			});
			button.exited(() -> {
				hoverButton = null;
				lastTooltip = null;
				if (!tooltip.hasMouse() && !button.hasMouse() && !tooltip.hasActions()) {
					tooltip.actions(Actions.fadeOut(0.5f), Actions.remove());
				}
			});
			addChildCentered(button);
		}
		for (TechTreeDialog.TechTreeNode next : root.children) addRoot(next);
	}

	@Override
	public void draw() {
		drawConnections(layout);

		super.draw();
	}

	public void drawConnections(@Nullable TechTreeDialog.TechTreeNode from) {
		if (from == null) return;
		for(TechTreeDialog.TechTreeNode next : from.children) {
			if (!next.visible) continue;

			Lines.stroke(5, Pal.accent);
			Draw.alpha(parentAlpha * color.a);

			float fromx = from.x;
			float fromy = from.y;

			float nextx = next.x;
			float nexty = next.y;

			Lines.line(
				fromx + x,
				fromy + y,
				nextx + x,
				nexty + y
			);
			drawConnections(next);
		}
	}

	public void rebuild(@Nullable TechTree.TechNode root) {
		layout = null;
		clear();
		setSize(0f);
		if (root == null) return;
		layout = new TechTreeDialog.TechTreeNode(root, null);

		TechTreeDialog.TechTreeNode[] backup = layout.children;
		TechTreeDialog.TechTreeNode[] half = Arrays.copyOfRange(backup, 0, Mathf.ceil(backup.length/2f));
		TechTreeDialog.TechTreeNode[] half2 = Arrays.copyOfRange(backup, Mathf.ceil(backup.length/2f), backup.length);

		layout.children = half;
		new BranchTreeLayout() {{
			gapBetweenLevels = gapBetweenNodes = 10f;
			rootLocation = TreeLocation.top;
		}}.layout(layout);

		float lastY = layout.y;
		if (half2.length > 0) {
			layout.children = half2;
			new BranchTreeLayout() {{
				gapBetweenLevels = gapBetweenNodes = 10f;
				rootLocation = TreeLocation.bottom;
			}}.layout(layout);
			shiftY(new TechTreeDialog.TechTreeNode[]{layout}, lastY - layout.y);
		}

		layout.children = backup;
		addRoot(layout);
		updateSize();
	}

	public void shiftY(TechTreeDialog.TechTreeNode[] list, float amount) {
		for(TechTreeDialog.TechTreeNode node : list) {
			node.y += amount;
			shiftY(node.children, amount);
		}
	}

	public void tooltip(TechTreeDialog.TechTreeNode node) {
		if (node == lastTooltip) return;
		if (tooltip.parent != this) addChild(tooltip);

		Button button = hoverButton;

		tooltip.clear();
		tooltip.clearActions();
		tooltip.actions(Actions.fadeIn(0));
		tooltip.exited(() -> {
			if (!tooltip.hasMouse() && !button.hasMouse() && !tooltip.hasActions()) {
				tooltip.actions(Actions.fadeOut(0.5f), Actions.remove());
			}
		});
		tooltip.table(info -> {
			if (node.selectable) info.button(Icon.info, Styles.cleari, () -> Vars.ui.content.show(node.node.content)).growY().width(50f);
			info.table(desc -> {
				desc.add(node.selectable ? node.node.content.localizedName : "[accent] ???").row();
				if (node.selectable) {
					for(int i = 0; i < node.node.requirements.length; i++) {
						int finalI = i;
						desc.table(req -> {
							ItemStack required = node.node.requirements[finalI];
							ItemStack completed = node.node.finishedRequirements[finalI];

							req.image(required.item.uiIcon).size(24f).padRight(5f).scaling(Scaling.fit);
							req.add(required.item.localizedName).padRight(5f);
							req.add(
								UI.formatAmount(completed.amount) + " / " + UI.formatAmount(required.amount)
							).color(
								Tmp.c1.set(Color.scarlet).lerp(Pal.heal, Mathf.clamp((float) completed.amount / required.amount))
							);
						}).left().row();
					}
				} else {
					node.node.objectives.each(objective -> {
						desc.add(objective.display()).color(objective.complete() ? Pal.heal : Color.scarlet).row();
					});
				}
			}).padLeft(5f).left();
		}).row();
		if (node.selectable) {
			tooltip.labelWrap(node.node.content.description).growX().row();
			if (Vars.mobile) tooltip.button("@research", () -> SWUI.techtreeDialog.spend(node.node)).height(50f).minWidth(200f).growX();
		}

		lastTooltip = node;

		tooltip.pack();
		tooltip.setPosition(node.x, node.y, Align.bottomLeft);
		tooltip.act(Core.graphics.getDeltaTime());
	}

	public void updateSize() {
		float minx = getChildren().min(element -> element.x).x;
		float miny = getChildren().min(element -> element.y).y;
		setSize(
			getChildren().max(element -> element.x).x - minx,
			getChildren().max(element -> element.y).y - miny
		);
	}
}
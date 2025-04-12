package sw.ui.dialog;

import arc.*;
import arc.input.*;
import arc.math.*;
import arc.scene.actions.*;
import arc.scene.event.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;
import mindustry.ui.layout.*;
import sw.*;
import sw.content.*;
import sw.ui.*;
import sw.ui.elements.*;

import static mindustry.content.TechTree.*;

public class TechTreeDialog extends BaseDialog {
	float time = 0.5f;
	float groupSize = 400;
	float catSize = 64;
	float catScl = 5;
	Interp interp = Interp.circle;

	public WidgetGroup categories;
	public TreeView view;
	public ResourceDisplay resources;

	public int selectedCat = -1;

	public TechTreeDialog() {
		super("");
		title.remove();
		titleTable.remove();
		titleTable.button("@quit", Icon.left, new TextButton.TextButtonStyle() {{
			font = Fonts.def;
			up = Tex.buttonSideRightDown;
			down = Tex.buttonSideRightOver;
		}}, this::close).size(210f, 48f);
		titleTable.top();

		buttons.remove();

		cont.stack(
			new Table(t -> t.add(view = new TreeView())),
			new Table(t -> t.add(categories = new WidgetGroup()).touchable(Touchable.childrenOnly).size(400)),
			new Table(t -> t.add(resources = new ResourceDisplay(SWPlanets.wendi))).bottom().left(),
			titleTable
		).grow();
		buildCategorySelector(SWPlanets.wendi);
		keyDown(key -> {
			if(key == KeyCode.escape || key == KeyCode.back) close();
		});
		Vars.ui.research.fill(t -> t.update(() -> {
			if (Vars.ui.research.root.node == SWPlanets.wendi.techTree && SWVars.showTechTreeDialog) {
				Vars.ui.research.hide(Actions.fadeOut(0f));
				SWUI.techtreeDialog.show();
			}
		}));
	}

	public void buildCategorySelector(Planet planet) {
		categories.clear();
		categories.setSize(groupSize);

		int count = planet.techTree.children.size;
		for(int i = 0; i < count; i++) {
			Button button = new Button(Tex.buttonOver);
			button.setSize(catSize);
			button.setPosition(
				groupSize/2f + Angles.trnsx(360f/count * i, groupSize/2.6f),
				groupSize/2f + Angles.trnsy(360f/count * i, groupSize/2.6f)
			);
			button.image(planet.techTree.children.get(i).icon()).grow().scaling(Scaling.fit);

			int finalI = i;
			button.clicked(() -> {
				selectCategory(finalI);
				view.rebuild(planet.techTree.children.get(finalI));
				view.actions(Actions.alpha(1f, time, interp));
			});
			categories.addChild(button);
		}
	}

	public static void clearTree() {
		clearBranch(SWPlanets.wendi.techTree);
	}
	public static void clearBranch(TechNode root) {
		root.reset();
		root.content.clearUnlock();
		root.children.each(TechTreeDialog::clearBranch);
	}

	public void close() {
		view.actions(Actions.alpha(0f, time, interp));
		if (selectedCat != -1) {
			selectCategory(-1);
		} else hide();
	}

	// TODO do it after removing the whole planet thing
	public void spend(TechNode node) {
		boolean complete = true;

		if (node.objectives.contains(o -> !o.complete())) return;

		for(int i = 0; i < node.requirements.length; i++){
			ItemStack req = node.requirements[i];
			ItemStack completed = node.finishedRequirements[i];

			//amount actually taken from inventory
			int used = Math.max(Math.min(req.amount - completed.amount, resources.items.get(req.item)), 0);
			completed.amount += used;

			//disable completion if the completed amount has not reached requirements
			if(completed.amount < req.amount){
				complete = false;
			}
		}
		complete &= !node.objectives.contains(o -> !o.complete());

		if(complete){
			node.content.unlock();
		}

		node.save();

		Core.scene.act();
		resources.rebuild();
		view.rebuild(SWPlanets.wendi.techTree.children.get(selectedCat));
	}

	public void selectCategory(int category) {
		selectedCat = category;

		var categoryChildren = categories.getChildren();

		if (category != -1) {
			categoryChildren.each(child -> child.actions(
				Actions.touchable(Touchable.disabled),
				Actions.parallel(
					Actions.moveTo(
						(groupSize - (child == categoryChildren.get(category) ? catSize * catScl : catSize))/2f,
						(groupSize - (child == categoryChildren.get(category) ? catSize * catScl : catSize))/2f,
						time, interp
					),
					Actions.alpha(0f, child == categoryChildren.get(category) ? time : time/2f, interp),
					Actions.sizeTo(
						child == categoryChildren.get(category) ? catSize * catScl : catSize,
						child == categoryChildren.get(category) ? catSize * catScl : catSize,
						time, interp
					)
				)
			));
		} else {
			categoryChildren.each(child -> child.actions(
				Actions.parallel(
					Actions.moveTo(
						groupSize / 2f + Angles.trnsx(360f / categoryChildren.size * categoryChildren.indexOf(child), groupSize / 2.6f),
						groupSize / 2f + Angles.trnsy(360f / categoryChildren.size * categoryChildren.indexOf(child), groupSize / 2.6f),
						time, interp
					),
					Actions.alpha(1f, time / 2, interp),
					Actions.sizeTo(catSize, catSize, time, interp)
				),
				Actions.touchable(Touchable.enabled)
			));
		}
	}

	public static class TechTreeNode extends TreeLayout.TreeNode<TechTreeNode> {
		public final TechNode node;
		public boolean visible = true, selectable = true;

		public TechTreeNode(TechNode node, TechTreeNode parent){
			this.node = node;
			this.parent = parent;
			width = height = 48f;
			children = new TechTreeNode[node.children.size];
			for(int i = 0; i < children.length; i++){
				children[i] = new TechTreeNode(node.children.get(i), this);
			}

			if (node.objectives.contains(objective -> !objective.complete())) selectable = false;
			if (parent != null && (!parent.selectable || !parent.node.content.unlocked())) visible = false;
		}
	}
}

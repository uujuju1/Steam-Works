package sw.ui.dialog;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import arc.scene.*;
import arc.scene.actions.*;
import arc.scene.style.*;
import arc.scene.ui.*;
import arc.scene.ui.ImageButton.*;
import arc.scene.ui.TextButton.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.core.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;
import mindustry.ui.layout.*;
import sw.*;
import sw.content.*;
import sw.ui.*;

import static mindustry.Vars.*;
import static mindustry.content.TechTree.*;

public class TechtreeDialog extends BaseDialog {
	private float time;

	public ShaderElement shader;
	public TreeView view;
	public ResourceDisplay resources;
	public NodeView info;
	public int currentRoot = -1;

	public Seq<TechNode> roots = new Seq<>();

	public TechtreeDialog() {
		super("");
		title.remove();
		titleTable.remove();
		titleTable.button("@quit", Icon.left, new TextButtonStyle() {{
			font = Fonts.def;
			up = Tex.buttonSideRightDown;
			down = Tex.buttonSideRightOver;
		}}, this::hide).size(210f, 48f);
		titleTable.top();

		SWTechTree.init(roots);

		buttons.remove();
		buttons.pane(cat -> {
			roots.each(root -> {
				ImageButtonStyle style = new ImageButtonStyle() {{
					up = Tex.pane;
					down = checked = Tex.buttonSelectTrans;
				}};
				if (roots.first() == root) {
					style.up = Tex.buttonEdge1;
					style.down = style.checked = Tex.buttonEdgeOver1;
				}
				if (roots.peek() == root) {
					style.up = Tex.buttonEdge3;
					style.down = style.checked = Tex.buttonEdgeOver3;
				}
				ImageButton button = buttons.button(root.icon(), style, 32f, () -> {
					currentRoot = roots.indexOf(root);
					Core.settings.put("settings-sw-techtree-category", currentRoot);
					view.rebuild(root);
				}).size(64f).tooltip(root.localizedName()).get();
				button.update(() -> button.setChecked(currentRoot == roots.indexOf(root)));
			});
		}).maxWidth(320f).height(64f).get().setOverscroll(false, false);
		buttons.center().bottom();

		cont.stack(
			shader = new ShaderElement(new Shader(Core.files.internal("shaders/screenspace.vert"), Vars.tree.get("shaders/techtreeBackground.frag")) {
				@Override
				public void apply() {
					setUniformf("u_resolution", Core.camera.width, Core.camera.height);
					setUniformf("u_opacity", parentAlpha);
					setUniformf("u_time", time);
				}
			}),
			new Table(t -> t.addChild(view = new TreeView(Styles.black6))),
			new Table(t -> t.add(resources = new ResourceDisplay(SWPlanets.wendi)).bottom().left()).bottom().left(),
			new Table(t -> t.add(info = new NodeView())).right(),
			titleTable,
			buttons
		).grow();

		addCloseListener();
		Vars.ui.research.fill(t -> t.update(() -> {
			if (Vars.ui.research.root.node == SWPlanets.wendi.techTree) {
				Vars.ui.research.hide(Actions.fadeOut(0f));
				SWVars.techtreeDialog.show();
			}
		}));

		shown(() -> {
			resources.rebuild();
			currentRoot = Core.settings.getInt("settings-sw-techtree-category", -1);
			if (currentRoot != -1) view.rebuild(roots.get(currentRoot));
		});

		update(() -> {
			time += Time.delta;
		});
	}

	public void spend(TechNode node) {
		boolean complete = true;

		for(int i = 0; i < node.requirements.length; i++){
			ItemStack req = node.requirements[i];
			ItemStack completed = node.finishedRequirements[i];

			//amount actually taken from inventory
			int used = Math.max(Math.min(req.amount - completed.amount, resources.items.get(req.item)), 0);
			resources.items.remove(req.item, used);
			completed.amount += used;

			//disable completion if the completed amount has not reached requirements
			if(completed.amount < req.amount){
				complete = false;
			}
		}

		if(complete){
			node.content.unlock();
		}

		node.save();

		//??????
		Core.scene.act();
		resources.rebuild();
	}

	public void clearTree() {
		roots.each(this::clearBranch);
	}
	public void clearBranch(TechNode root) {
		root.reset();
		root.content.clearUnlock();
		root.children.each(this::clearBranch);
	}

	public class TreeView extends Group {
		Drawable background;
		TechTreeNode layout;
		public TechNode root;
		public float margin = 10;
		float start;

		public TreeView(Drawable background) {
			this.background = background;
			rebuild(null);
		}

		public void addChildCentered(Element actor) {
			actor.x -= actor.getWidth()/2f;
			actor.y -= actor.getHeight()/2f;
			addChild(actor);
		}

		public void addRoot(TechTreeNode root) {
			ImageButton button = new ImageButton(new ImageButtonStyle() {{
				up = Tex.button;
				down = Tex.buttonDown;
				disabled = Tex.buttonDisabled;
			}});
			button.setSize(48f);
			button.setPosition(root.x + width/2f, root.y + height/2f);
			button.update(() -> {
				if (root.parent != null) button.setDisabled(!root.parent.node.content.unlocked());
				button.replaceImage(new Image(button.isDisabled() ? Icon.lock.getRegion() : (root.node.content.unlocked() ? root.node.content.uiIcon : Icon.cancel.getRegion())));
				button.getImage().setScaling(Scaling.fit);
			});
			button.hovered(() -> {
				if (!button.isDisabled()) info.setNode(root);
			});
			button.clicked(() -> {
				if (!button.isDisabled() && !root.node.content.unlocked() && root.node.objectives.find(o -> !o.complete()) == null) spend(root.node);
			});
			addChildCentered(button);
			for (TechTreeNode next : root.children) {
				addRoot(next);
			}
		}

		@Override
		public void draw() {
			Color color = this.color;
			Draw.color(color, color.a * parentAlpha);
			background.draw(x, y, width, height);

			drawConnections(layout);

			super.draw();
		}

		public void drawConnections(@Nullable TechTreeNode from) {
			if (from == null) return;
			for(TechTreeNode next : from.children) {
				Lines.stroke(4, Pal.accent);
				Draw.alpha(parentAlpha);
				Lines.line(
					from.x + x + margin - start,
					from.y + y + margin,
					next.x + x + margin - start,
					next.y + y + margin
				);
				drawConnections(next);
			}
		}

		public void rebuild(@Nullable TechNode root) {
			this.root = root;
			layout = null;
			clear();
			setPosition(0f, 0f);
			setSize(0f);
			start = 0f;
			if (root == null) return;
			layout = new TechTreeNode(root, null);
			new BranchTreeLayout(){{
				gapBetweenLevels = gapBetweenNodes = 10f;
				rootLocation = TreeLocation.top;
			}}.layout(layout);
			addRoot(layout);
			updateSize();
		}

		public void updateSize() {
			float minx = Float.POSITIVE_INFINITY, miny = Float.POSITIVE_INFINITY, maxx = Float.NEGATIVE_INFINITY, maxy = Float.NEGATIVE_INFINITY;
			start = Math.min(0, getChildren().min(child -> child.x).x);
			for(Element actor : getChildren()) {
				actor.x -= start;
				actor.x += margin;
				actor.y += margin;
				if (actor.x < minx) minx = actor.x;
				if (actor.y < miny) miny = actor.y;
				if (actor.x + actor.getWidth() > maxx) maxx = actor.x + actor.getWidth();
				if (actor.y + actor.getHeight() > maxy) maxy = actor.y + actor.getHeight();
			}
			setSize(maxx - minx + margin * 2f, maxy - miny + margin * 2f);
			x = Core.graphics.getWidth()/2f - width/2f - margin;
			y = Core.graphics.getHeight()/2f - height/2f - margin * 1.5f;
		}
	}

	private class NodeView extends Table {
		public TechTreeNode node;

		public void setNode(TechTreeNode node) {
			this.node = node;
			clear();
			setBackground(Styles.black6);
			table(Tex.underline, label -> {
				label.button(new TextureRegionDrawable(node.node.content.uiIcon), new ImageButtonStyle(), 48f, () -> ui.content.show(node.node.content)).padRight(5f).left();
				label.add(node.node.content.localizedName);
			}).padBottom(10f).left().growX().row();
			add("@complete").left().row();
			pane(Styles.smallPane, objectives -> {
				for(Objectives.Objective o : node.node.objectives) {
					objectives.add("> " + o.display()).left();
					objectives.image(o.complete() ? Icon.ok : Icon.cancel, o.complete() ? Pal.heal : Pal.remove).padLeft(5f);
					objectives.row();
				}
			}).maxHeight(200f).left().row();
			if (node.node.requirements != ItemStack.empty) {
				add(Core.bundle.get("stat.buildcost") + ":").color(Color.gray).padTop(10f).left().row();
				pane(Styles.smallPane, t -> {
					for (int i = 0; i < node.node.requirements.length; i++) {
						ItemStack req = node.node.requirements[i];
						ItemStack completed = node.node.finishedRequirements[i];

						t.table(list -> {
							list.left();
							list.image(req.item.uiIcon).size(24f).padRight(5f);
							list.add(req.item.localizedName).color(Color.lightGray);
							list.label(() -> " " + UI.formatAmount(completed.amount) + " / " + UI.formatAmount(req.amount));

						}).fillX().left();
						t.row();
					}
				}).left().maxHeight(200f);
			}
			margin(10f);
		}
	}

	public class TechTreeNode extends TreeLayout.TreeNode<TechTreeNode> {
		public final TechNode node;
		public boolean visible = true, selectable = true;

		public TechTreeNode(TechNode node, TechTreeNode parent){
			this.node = node;
			this.parent = parent;
			this.width = this.height = 48f;
			children = new TechTreeNode[node.children.size];
			for(int i = 0; i < children.length; i++){
				children[i] = new TechTreeNode(node.children.get(i), this);
			}
		}
	}
}

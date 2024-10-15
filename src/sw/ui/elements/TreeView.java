package sw.ui.elements;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.scene.*;
import arc.scene.style.*;
import arc.scene.ui.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.layout.*;
import sw.ui.dialog.*;

public class TreeView extends Group {
	Drawable background;
	TechtreeDialog.TechTreeNode layout;
	public TechTree.TechNode root;
	public float margin = 10;
	float start;

	public Cons<TechtreeDialog.TechTreeNode>
		clicked = node -> {},
		hovered = node -> {};

	public TreeView(Drawable background) {
		this.background = background;
		rebuild(null);
	}

	public void addChildCentered(Element actor) {
		actor.x -= actor.getWidth()/2f;
		actor.y -= actor.getHeight()/2f;
		addChild(actor);
	}

	public void addRoot(TechtreeDialog.TechTreeNode root) {
		ImageButton button = new ImageButton(new ImageButton.ImageButtonStyle() {{
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
		button.hovered(() -> hovered.get(root));
		button.clicked(() -> clicked.get(root));
		addChildCentered(button);
		for (TechtreeDialog.TechTreeNode next : root.children) addRoot(next);
	}

	@Override
	public void draw() {
		Color color = this.color;
		Draw.color(color, color.a * parentAlpha);
		background.draw(x, y, width, height);

		drawConnections(layout);

		super.draw();
	}

	public void drawConnections(@Nullable TechtreeDialog.TechTreeNode from) {
		if (from == null) return;
		for(TechtreeDialog.TechTreeNode next : from.children) {
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

	public void rebuild(@Nullable TechTree.TechNode root) {
		this.root = root;
		layout = null;
		clear();
		setPosition(0f, 0f);
		setSize(0f);
		start = 0f;
		if (root == null) return;
		layout = new TechtreeDialog.TechTreeNode(root, null);
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
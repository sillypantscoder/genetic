package com.sillypantscoder.geometrydash.tile;

import com.sillypantscoder.geometrydash.View;
import com.sillypantscoder.geometrydash.SceneItem;
import com.sillypantscoder.geometrydash.Rect;
import com.sillypantscoder.geometrydash.Player;

public class Tile extends SceneItem {
	public Tile(View view, double x, double y, double dw, double dh, double rotation) {
		super(view, x, y);
		this.rotation = rotation;
	}
	public Rect getRect() {
		return new Rect(this.x, this.y, 1, 1);
	}
	public void tick(double amount) {
		this.collide(this.view.player);
		super.tick(amount);
	}
	public void collide(Player player) {}
}

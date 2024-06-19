package com.sillypantscoder.geometrydash.tile;

import com.sillypantscoder.geometrydash.View;
import com.sillypantscoder.geometrydash.SceneItem;
import com.sillypantscoder.geometrydash.Rect;
import com.sillypantscoder.gdgenetic.Surface;
import com.sillypantscoder.geometrydash.Player;

public abstract class Tile extends SceneItem {
	public static final int RENDER_TILE_SIZE = 20;
	public Tile(View view, double x, double y, double rotation) {
		super(view, x, y);
		this.rotation = rotation;
	}
	public Rect getRect() {
		return new Rect(this.x, this.y, 1, 1);
	}
	public Rect getRotatedRect() {
		return getRect().rotate(this.rotation, this.x + 0.5, this.y + 0.5);
	}
	public void tick(double amount) {
		this.collide(this.view.player);
		super.tick(amount);
	}
	public void collide(Player player) {}
	public int[][] drawForNetwork() {
		return new int[][] {
			new int[] { 0, 0, 0, 0 },
			new int[] { 0, 0, 0, 0 },
			new int[] { 0, 0, 0, 0 },
			new int[] { 0, 0, 0, 0 }
		};
	}
	public abstract void drawForHuman(Surface surface, Rect pxRect);
}

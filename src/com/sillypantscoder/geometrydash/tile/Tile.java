package com.sillypantscoder.geometrydash.tile;

import com.sillypantscoder.geometrydash.SceneItem;
import com.sillypantscoder.windowlib.Surface;
import com.sillypantscoder.geometrydash.DoubleRect;
import com.sillypantscoder.geometrydash.IntRect;
import com.sillypantscoder.geometrydash.Player;

public abstract class Tile extends SceneItem {
	public static final int RENDER_TILE_SIZE = 20;
	public Tile(double x, double y) {
		super(x, y);
	}
	public DoubleRect getRect() {
		return new DoubleRect(this.x, this.y, 1, 1);
	}
	public void flip() {}
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
	public abstract void drawForHuman(Surface surface, IntRect pxRect);
}

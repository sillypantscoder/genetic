package com.sillypantscoder.geometrydash.tile;

import com.sillypantscoder.geometrydash.Player;
import com.sillypantscoder.geometrydash.Rect;

public abstract class TileDeath extends Tile {
	public TileDeath(double x, double y) {
		super(x, y);
	}
	public void collide(Player player) {
		Rect playerRect = player.getDeathRect().relative(0.1, 0.1, 0.8, 0.8);
		Rect thisRect = this.getRect();
		if (playerRect.colliderect(thisRect)) {
			// Player dies!
			// It's that simple.
			player.destroy();
		}
	}
}

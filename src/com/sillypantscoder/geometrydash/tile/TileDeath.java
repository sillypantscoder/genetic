package com.sillypantscoder.geometrydash.tile;

import com.sillypantscoder.geometrydash.Player;
import com.sillypantscoder.geometrydash.DoubleRect;

public abstract class TileDeath extends Tile {
	public TileDeath(double x, double y) {
		super(x, y);
	}
	public void collide(Player player) {
		DoubleRect playerRect = player.getDeathRect().relative(0.1, 0.1, 0.8, 0.8);
		DoubleRect thisRect = this.getRect();
		if (playerRect.colliderect(thisRect)) {
			// Player dies!
			// It's that simple.
			player.destroy();
		}
	}
}

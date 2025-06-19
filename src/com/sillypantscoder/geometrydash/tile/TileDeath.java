package com.sillypantscoder.geometrydash.tile;

import com.sillypantscoder.geometrydash.Player;
import com.sillypantscoder.geometrydash.DoubleRect;

public abstract class TileDeath extends FlippableTile {
	public TileDeath(double x, double y, boolean flipped) {
		super(x, y, flipped);
	}
	public void collide(Player player) {
		DoubleRect playerRect = player.getDeathRect();
		DoubleRect thisRect = this.getRect();
		if (playerRect.colliderect(thisRect)) {
			// Player dies!
			// It's that simple.
			player.destroy();
		}
	}
}

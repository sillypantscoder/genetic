package com.sillypantscoder.geometrydash.tile;

import com.sillypantscoder.geometrydash.View;
import com.sillypantscoder.geometrydash.Player;
import com.sillypantscoder.geometrydash.Rect;

public class TileDeath extends Tile {
	/**
	 * @param {View} view
	 * @param {number} x
	 * @param {number} y
	 * @param {number} rotation
	 * @param {string[]} groups
	 */
	public TileDeath(View view, double x, double y, double rotation) {
		super(view, x, y, 1, 1, rotation);
	}
	public void collide(Player player) {
		Rect playerRect = player.getDeathRect().relative(0.1, 0.1, 0.8, 0.8);
		Rect thisRect = this.getRect().rotate(this.rotation, this.x + 0.5, this.y + 0.5);
		if (playerRect.colliderect(thisRect)) {
			// Player dies!
			player.destroy();
		}
	}
}

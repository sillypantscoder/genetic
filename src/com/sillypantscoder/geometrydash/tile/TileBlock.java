package com.sillypantscoder.geometrydash.tile;

import com.sillypantscoder.geometrydash.View;

import java.util.Optional;

import com.sillypantscoder.geometrydash.Player;
import com.sillypantscoder.geometrydash.Rect;

public abstract class TileBlock extends Tile {
	public TileBlock(View view, double x, double y, double rotation) {
		super(view, x, y, rotation);
	}
	public void collide(Player player) {
		Player.PlayerBlockRects playerRects = new Player.PlayerBlockRects(player);
		Rect thisRect = this.getRect().rotate(this.rotation, this.x + 0.5, this.y + 0.5);
		if (playerRects.collideRect.colliderect(thisRect)) {
			// The player has collided with the side of this block.
			player.destroy(); // :(
		} else if (playerRects.topRect.colliderect(thisRect)) {
			// If the player is hitting the ceiling, first find out if they can die.
			if (player.canDieFromCeiling()) {
				// The player will die a moment later when the "collide" rect is activated.
				// No need to kill the player manually here.
			} else {
				if (player.gravity > 0) {
					// Move the top of the player down to the bottom of this block.
					player.y = thisRect.y - player.getGeneralRect().h;
				} else {
					// Move the bottom of the player up to the top of this block.
					player.y = thisRect.y + thisRect.h ;// - (player.mode instanceof WaveMode ? 0.1 : 0)
					// why does there need to be an exception for wave
				}
				player.vy = 0;
			}
		} else if (playerRects.bottomRect.colliderect(thisRect)) {
			// If the player is almost on top of this block, push them.
			if (player.gravity > 0) {
				player.groundHeight = Optional.of(thisRect.y + thisRect.h);
			} else {
				player.groundHeight = Optional.of(thisRect.y);
			}
		}
	}
}

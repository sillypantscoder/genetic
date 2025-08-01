package com.sillypantscoder.geometrydash.tile;

import java.util.Optional;

import com.sillypantscoder.geometrydash.Player;
import com.sillypantscoder.geometrydash.DoubleRect;

public abstract class TileBlock extends FlippableTile {
	public TileBlock(double x, double y, boolean flipped) {
		super(x, y, flipped);
	}
	public void collide(Player player) {
		Player.PlayerBlockRects playerRects = new Player.PlayerBlockRects(player);
		DoubleRect thisRect = this.getRect();
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
				if (player.vy > 0) this.view.agentScore -= 30;
			} else {
				player.groundHeight = Optional.of(thisRect.y);
			}
		}
	}
}

package com.sillypantscoder.geometrydash.tile;

import java.util.Optional;

import com.sillypantscoder.geometrydash.Player;
import com.sillypantscoder.geometrydash.DoubleRect;

public abstract class Orb extends Tile {
	public boolean activated;
	public Orb(double x, double y) {
		super(x, y);
	}
	public void collide(Player player) {
		if (this.activated) return;
		DoubleRect playerRect = player.getGeneralRect();
		DoubleRect thisRect = this.getRect();
		if (playerRect.colliderect(thisRect)) {
			// Jumpy jumpy
			Orb target = this;
			player.specialJump = Optional.ofNullable(() -> {
				target.activated = true;
				target.activate(player);
				// boing
				player.view.agentScore += 10;
			});
		}
	}
	public DoubleRect getRect() {
		double padding = 0.3;
		return new DoubleRect(this.x - padding, this.y - padding, 1 + padding + padding, 1 + padding + padding);
	}
	public abstract void activate(Player player);
}

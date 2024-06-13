package com.sillypantscoder.geometrydash.tile;

import java.awt.Color;
import java.util.Optional;

import com.sillypantscoder.geometrydash.Player;
import com.sillypantscoder.geometrydash.Rect;
import com.sillypantscoder.geometrydash.View;

public abstract class Orb extends Tile {
	public boolean activated = false;
	public Orb(View view, double x, double y, double rotation) {
		super(view, x, y, rotation);
	}
	public abstract Color getColor();
	public void collide(Player player) {
		if (this.activated) return;
		Rect playerRect = player.getGeneralRect();
		Rect thisRect = this.getRect().rotate(this.rotation, this.x + 0.5, this.y + 0.5);
		if (playerRect.colliderect(thisRect)) {
			// Jumpy jumpy
			Orb target = this;
			player.specialJump = Optional.ofNullable(() -> {
				target.activated = true;
				target.activate(player);
			});
		}
	}
	public Rect getRect() {
		double padding = 0.3;
		return new Rect(this.x - padding, this.y - padding, 1 + padding + padding, 1 + padding + padding);
	}
	public abstract void activate(Player player);
}

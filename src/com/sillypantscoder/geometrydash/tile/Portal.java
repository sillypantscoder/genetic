package com.sillypantscoder.geometrydash.tile;

import com.sillypantscoder.geometrydash.Player;
import com.sillypantscoder.geometrydash.Rect;

public abstract class Portal extends Tile {
	public boolean activated;
	public double realheight;
	public Portal(int x, int y, double realheight) {
		super(x, y);
		this.realheight = realheight;
	}
	public Rect getRect() {
		return super.getRect().relative(0, (this.realheight * -0.5) + 0.5, 1, this.realheight);
	}
	public void collide(Player player) {
		if (activated) return;
		var playerRect = player.getGeneralRect();
		var thisRect = this.getRect();
		if (playerRect.colliderect(thisRect)) {
			this.activated = true;
			this.activate(player);
			// vwoop
			view.agentScore += 5;
		}
	}
	public abstract void activate(Player player);
}

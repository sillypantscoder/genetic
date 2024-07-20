package com.sillypantscoder.geometrydash.tile;

import com.sillypantscoder.geometrydash.Player;
import com.sillypantscoder.geometrydash.Rect;
import com.sillypantscoder.geometrydash.View;

public abstract class Portal extends Tile {
	public boolean activated;
	public double realheight;
	public Portal(View view, int x, int y, double realheight) {
		super(view, x, y);
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
			view.agentScore += 100000;
			this.activate(player);
		}
	}
	public abstract void activate(Player player);
}

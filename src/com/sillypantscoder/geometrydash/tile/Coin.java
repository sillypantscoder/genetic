package com.sillypantscoder.geometrydash.tile;

import java.awt.Color;

import com.sillypantscoder.gdgenetic.Surface;
import com.sillypantscoder.geometrydash.DoubleRect;
import com.sillypantscoder.geometrydash.IntRect;
import com.sillypantscoder.geometrydash.Player;

public class Coin extends Tile {
	public boolean activated;
	public int value = 15;
	public Coin(double x, double y) {
		super(x, y);
	}
	public void collide(Player player) {
		if (this.activated) return;
		DoubleRect playerRect = player.getGeneralRect();
		DoubleRect thisRect = this.getRect();
		if (playerRect.colliderect(thisRect)) {
			// Get the coin
			this.activated = true;
			this.view.agentScore += this.value;
		}
	}
	public DoubleRect getRect() {
		double padding = 0.0;
		return new DoubleRect(this.x - padding, this.y - padding, 1 + padding + padding, 1 + padding + padding);
	}
	public void drawForHuman(Surface surface, IntRect pxRect) {
		// default positions
		float y = (float)(pxRect.centerY());
		float a = 1;
		// time
		float t = (float)(this.view.player.x - this.x) * 2;
		if (t > 0 && this.activated) {
			y -= (10 * t) - (t * t);
			a = 1 - (t / 10);
		}
		if (a > 1) a = 1;
		if (a <= 0) return;
		// find color
		float g = 0.5f;
		if (this.value <= 0) g = 0;
		// draw
		surface.drawCircle(new Color(1.0f, g, 0f, a), (int)(pxRect.centerX()), (int)(y), (int)(pxRect.w * 0.4));
	}
}

package com.sillypantscoder.geometrydash;

import com.sillypantscoder.gdgenetic.Surface;

public abstract class GameMode {
	protected Player player;
	public GameMode(Player player) {
		this.player = player;
	}
	public abstract Surface getIcon();
	public void gravity(double amount) {
		this.player.vy -= 0.028 * this.player.gravity * amount;
	}
	public void checkJump(double _amount) {}
	public DoubleRect getRect() {
		return new DoubleRect(this.player.x, this.player.y, 1, 1);
	}
	public abstract boolean canDieFromCeiling();
	public abstract boolean jumpingHasEffect();
}

package com.sillypantscoder.geometrydash;

abstract class GameMode {
	protected Player player;
	public GameMode(Player player) {
		this.player = player;
	}
	public static String getIcon() {
		throw new UnsupportedOperationException("Aaaaaa! You're not supposed to do that!");
	}
	public void gravity(double amount) {
		this.player.vy -= 0.028 * this.player.gravity * amount;
	}
	public void checkJump(double _amount) {}
	public void getMax() {
		if (this.player.y > this.player.view.getStageHeight()) {
			this.player.destroy();
		}
	}
	public Rect getRect() {
		return new Rect(this.player.x, this.player.y, 1, 1);
	}
	public boolean canDieFromCeiling() {
		return false;
	}
}

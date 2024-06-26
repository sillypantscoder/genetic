package com.sillypantscoder.geometrydash;

import java.util.Optional;

public class Player extends SceneItem {
	public double vy = 0;
	public Optional<Double> groundHeight = Optional.empty();
	public Optional<Runnable> specialJump = Optional.empty();
	public double gravity = 1;
	public GameMode mode;
	public Player(View view) {
		super(view, -3, 0);
		this.mode = new CubeMode(this);
		this.setStartMode();
	}
	public Rect getGeneralRect() {
		return this.mode.getRect();
	}
	public Rect getDeathRect() {
		return this.getGeneralRect().relative(0.1, 0.1, 0.8, 0.8);
	}
	public static class PlayerBlockRects {
		public Rect collideRect;
		public Rect topRect;
		public Rect bottomRect;
		public PlayerBlockRects(Player player) {
			double margin = 0.3;
			Rect general = player.getGeneralRect();
			Rect maxY = general.relative(0, 1 - margin, 1, margin);
			Rect minY = general.relative(0, 0, 1, margin);
			Rect top = player.gravity > 0 ? maxY : minY;
			Rect bottom = player.gravity > 0 ? minY : maxY;
			// Set rects
			this.collideRect = general.relative(0, margin, 1, 1 - (margin * 2));
			this.topRect = top;
			this.bottomRect = bottom;
		}
	}
	public void tick(double amount) {
		// Move forwards
		this.x += 0.2 * amount;
		// Fall
		this.mode.gravity(amount);
		// Update Y
		this.y += this.vy * amount;
		// Setup collision detection
		this.groundHeight = Optional.empty();
		this.specialJump = Optional.empty();
		// Check for collision with stage
		if (this.y < 0) {
			this.y = 0;
			if (this.gravity > 0) {
				this.groundHeight = Optional.of(0.0);
			}
		}
		this.mode.getMax();
		// // Update styles
		// this.extraStyles = new String[] {
		// 	"background: url(data:image/svg+xml;base64," + Base64.getEncoder().encodeToString(this.mode.getIcon().getBytes()) + ");",
		// 	"transform: rotate(" + this.rotation + "deg) scaleY(" + this.gravity + ");"
		// };
		super.tick(amount);
	}
	public void finishTick(double amount) {
		if (this.groundHeight.isPresent()) {
			if (this.gravity < 0) {
				if (this.vy > 0) this.vy = 0;
			} else {
				if (this.vy < 0) this.vy = 0;
			}
		}
		this.mode.checkJump(amount);
		if (this.x > this.view.getStageWidth()) this.view.hasWon = true;
	}
	public void destroy() {
		super.destroy();
		this.view.hasDied = true;
	}
	public void setStartMode() {
		this.mode = new CubeMode(this);
	}
	public boolean canDieFromCeiling() {
		return this.mode.canDieFromCeiling();
	}
}

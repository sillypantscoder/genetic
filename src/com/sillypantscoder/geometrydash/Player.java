package com.sillypantscoder.geometrydash;

import java.util.Optional;

public class Player extends SceneItem {
	public double vy = 0;
	public double rotation = 0;
	public Optional<Double> groundHeight = Optional.empty();
	public Optional<Runnable> specialJump = Optional.empty();
	public double gravity = 1;
	public GameMode mode;
	public Player(View view) {
		super(-3, 0);
		this.view = view;
		this.mode = new CubeMode(this);
		this.setStartMode();
	}
	public DoubleRect getGeneralRect() {
		return this.mode.getRect();
	}
	public DoubleRect getDeathRect() {
		return this.getGeneralRect().relative(0.1, 0.1, 0.8, 0.8);
	}
	public static class PlayerBlockRects {
		public DoubleRect collideRect;
		public DoubleRect topRect;
		public DoubleRect bottomRect;
		public PlayerBlockRects(Player player) {
			double margin = 0.3;
			DoubleRect general = player.getGeneralRect();
			DoubleRect maxY = general.relative(0, 1 - margin, 1, margin);
			DoubleRect minY = general.relative(0, 0, 1, margin);
			DoubleRect top = player.gravity > 0 ? maxY : minY;
			DoubleRect bottom = player.gravity > 0 ? minY : maxY;
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
			} else {
				this.destroy();
			}
		}
		// Kill player if they are too high up
		if (this.y > this.view.getStageHeight()) {
			this.destroy();
		}
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

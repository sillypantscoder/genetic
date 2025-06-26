package com.sillypantscoder.geometrydash;

import java.awt.Color;

import com.sillypantscoder.gdgenetic.Surface;
import com.sillypantscoder.geometrydash.tile.Tile;

public class CubeMode extends GameMode {
	public static final double JUMP_STRENGH = 0.35;
	public CubeMode(Player player) {
		super(player);
	}
	public Surface getIcon() {
		Surface s = new Surface(Tile.RENDER_TILE_SIZE, Tile.RENDER_TILE_SIZE, new Color(0, 0, 0, 0));
		Color[] bands = new Color[] {
			new Color(0, 0, 0, 255),
			new Color(0, 255, 33, 255),
			new Color(0, 255, 33, 255),
			new Color(0, 255, 33, 255),
			new Color(0, 0, 0, 255),
			new Color(0, 0, 0, 0),
			new Color(0, 0, 0, 0),
			new Color(0, 0, 0, 255),
			new Color(0, 242, 255, 255),
			new Color(0, 242, 255, 255)
		};
		for (int i = 0; i < bands.length; i++) {
			int j = (s.get_width() - 1) - i;
			s.drawLine(bands[i], i, i, i, j, 1);
			s.drawLine(bands[i], i, i, j, i, 1);
			s.drawLine(bands[i], j, j, i, j, 1);
			s.drawLine(bands[i], j, j, j, i, 1);
		}
		return s;
	}
	public void handleGround(double groundHeight) {
		double targetRotation = (Math.floor((this.player.rotation - 45) / 90) * 90) + 90;
		this.player.rotation = (targetRotation + (this.player.rotation * 2)) / 3;
		if (this.player.gravity < 0) {
			double ph = this.player.getGeneralRect().h;
			if (this.player.y + ph > groundHeight) {
				this.player.y -= 0.1;
				if (this.player.y + ph < groundHeight) {
					this.player.y = groundHeight - ph;
				}
			}
		} else {
			if (this.player.y < groundHeight) {
				this.player.y += 0.1;
				if (this.player.y > groundHeight) {
					this.player.y = groundHeight;
				}
			}
		}
	}
	public void checkJump(double amount) {
		this.player.groundHeight.ifPresent(this::handleGround);
		if (this.player.groundHeight.isEmpty()) {
			this.player.rotation += 5 * amount * this.player.gravity;
		}
		if (this.player.view.isPressing) {
			if (this.player.specialJump.isPresent() && this.player.view.hasStartedPressing) {
				this.player.specialJump.ifPresent(e -> e.run());
				this.player.view.hasStartedPressing = false;
			} else if (this.player.groundHeight.isPresent()) {
				this.player.vy = JUMP_STRENGH * this.player.gravity;
				this.player.view.hasStartedPressing = false;
			}
		}
	}
	public boolean canDieFromCeiling() {
		return true;
	}
	public boolean jumpingHasEffect() {
		return this.player.groundHeight.isPresent() || this.player.specialJump.isPresent();
	}
}

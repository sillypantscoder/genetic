package com.sillypantscoder.geometrydash;

public class CubeMode extends GameMode {
	public CubeMode(Player player) {
		super(player);
	}
	public static String getIcon() {
		return "<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 18 18\">\n" +
				"\t<style>\n" +
				"\t\t.outline {\n" +
				"\t\t\tfill: black;\n" +
				"\t\t}\n" +
				"\t\t.middle {\n" +
				"\t\t\tfill: #00ff21;\n" +
				"\t\t}\n" +
				"\t\t.inside {\n" +
				"\t\t\tfill: #00f2ff;\n" +
				"\t\t}\n" +
				"\t</style>\n" +
				"\t<path class=\"outline\" d=\"M 0 0 L 18 0 L 18 18 L 0 18 Z M 1 1 L 1 17 L 17 17 L 17 1 Z\" />\n" +
				"\t<path class=\"middle\" d=\"M 1 1 L 17 1 L 17 17 L 1 17 Z M 5 5 L 5 13 L 13 13 L 13 5 Z\" />\n" +
				"\t<path class=\"outline\" d=\"M 6 6 L 12 6 L 12 12 L 6 12 Z M 7 7 L 7 11 L 11 11 L 11 7 Z\" />\n" +
				"\t<path class=\"outline\" d=\"M 4 4 L 14 4 L 14 14 L 4 14 Z M 5 5 L 5 13 L 13 13 L 13 5 Z\" />\n" +
				"\t<rect class=\"inside\" x=\"7\" y=\"7\" width=\"4\" height=\"4\" />\n" +
				"</svg>";
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
				this.player.vy = 0.33 * this.player.gravity;
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

package com.sillypantscoder.geometrydash.tile;

import java.awt.Color;

public class ReverseGravityPortal extends GravityPortal {
	public ReverseGravityPortal(int x, int y) {
		super(x, y);
	}
	public int getGravity() {
		return -1;
	}
	public Color getColor() {
		return new Color(255, 233, 84);
	}
}

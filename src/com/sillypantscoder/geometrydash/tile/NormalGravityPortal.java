package com.sillypantscoder.geometrydash.tile;

import java.awt.Color;

public class NormalGravityPortal extends GravityPortal {
	public NormalGravityPortal(int x, int y) {
		super(x, y);
	}
	public int getGravity() {
		return 1;
	}
	public Color getColor() {
		return new Color(0, 180, 255);
	}
}

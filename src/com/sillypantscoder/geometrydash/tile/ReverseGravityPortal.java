package com.sillypantscoder.geometrydash.tile;

import java.awt.Color;

import com.sillypantscoder.geometrydash.View;

public class ReverseGravityPortal extends GravityPortal {
	public ReverseGravityPortal(View view, int x, int y) {
		super(view, x, y);
	}
	public int getGravity() {
		return -1;
	}
	public Color getColor() {
		return new Color(255, 233, 84);
	}
}

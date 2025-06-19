package com.sillypantscoder.geometrydash.tile;

public abstract class FlippableTile extends Tile {
	public boolean flipped;
	public FlippableTile(double x, double y, boolean flipped) {
		super(x, y);
		this.flipped = flipped;
	}
	public void flip() { this.flipped = !this.flipped; }
}

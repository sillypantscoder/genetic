package com.sillypantscoder.geometrydash;

public class IntRect extends Rect {
	public int x;
	public int y;
	public int w;
	public int h;
	/**
	 * @param x The x-coordinate of the rectangle.
	 * @param y The y-coordinate of the rectangle.
	 * @param w The width of the rectangle.
	 * @param h The height of the rectangle.
	 */
	public IntRect(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	public double top() { return this.y; }
	public double left() { return this.x; }
	public double width() { return this.w; }
	public double height() { return this.h; }
	public int topInt() { return this.y; }
	public int leftInt() { return this.x; }
	public int widthInt() { return this.w; }
	public int heightInt() { return this.h; }
	/**
	 * @param x The x-coordinate to move to.
	 * @param y The y-coordinate to move to.
	 * @return A new Rect with the updated position.
	 */
	public IntRect move(int x, int y) {
		return new IntRect(this.x + x, this.y + y, this.w, this.h);
	}
	/**
	 * @param x The relative x-coordinate.
	 * @param y The relative y-coordinate.
	 * @param w The relative width.
	 * @param h The relative height.
	 * @return A new Rect with the updated position and size.
	 */
	public IntRect relative(int x, int y, int w, int h) {
		return new IntRect(
			this.x + (this.w * x),
			this.y + (this.h * y),
			this.w * w,
			this.h * h
		);
	}
	/**
	 * @param x The relative x-coordinate.
	 * @param y The relative y-coordinate.
	 * @param w The relative width.
	 * @param h The relative height.
	 * @return A new Rect with the updated position and size.
	 */
	public DoubleRect relative(double x, double y, double w, double h) {
		return new DoubleRect(
			this.x + (this.w * x),
			this.y + (this.h * y),
			this.w * w,
			this.h * h
		);
	}
	/**
	 * @return true if any of the rectangle's properties are NaN or undefined, false otherwise.
	 */
	public boolean hasInvalid() {
		return Double.isNaN(this.x) || this.x == Double.NaN
			|| Double.isNaN(this.y) || this.y == Double.NaN
			|| Double.isNaN(this.w) || this.w == Double.NaN
			|| Double.isNaN(this.h) || this.h == Double.NaN;
	}
}

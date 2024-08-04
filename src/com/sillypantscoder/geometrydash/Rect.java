package com.sillypantscoder.geometrydash;

public abstract class Rect {
	public abstract double top();
	public abstract double left();
	public abstract double width();
	public abstract double height();
	public double bottom() { return top() + height(); }
	public double right() { return left() + width(); }
	/**
	 * @return The y-coordinate of the center of the rectangle.
	 */
	public double centerY() {
		return this.top() + (this.height() / 2);
	}
	/**
	 * @return The x-coordinate of the center of the rectangle.
	 */
	public double centerX() {
		return this.left() + (this.width() / 2);
	}
	/**
	 * Determine whether this Rect collides with another Rect.
	 * @param other The rect to check.
	 * @return true if the rectangles collide, false otherwise.
	 */
	public boolean colliderect(Rect other) {
		return this.left() < other.right()
			&& right() > other.left()
			&& top() < other.bottom()
			&& bottom() > other.top();
	}
}

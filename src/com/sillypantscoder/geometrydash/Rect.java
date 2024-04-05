package com.sillypantscoder.geometrydash;

public class Rect {
    public double x;
    public double y;
    public double w;
    public double h;
    /**
     * @param x The x-coordinate of the rectangle.
     * @param y The y-coordinate of the rectangle.
     * @param w The width of the rectangle.
     * @param h The height of the rectangle.
     */
    public Rect(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
    /**
     * Determine whether this Rect collides with another Rect.
     * @param other The rect to check.
     * @return true if the rectangles collide, false otherwise.
     */
    public boolean colliderect(Rect other) {
        return this.x < other.x + other.w
            && this.x + this.w > other.x
            && this.y < other.y + other.h
            && this.y + this.h > other.y;
    }
    /**
     * @param x The x-coordinate to move to.
     * @param y The y-coordinate to move to.
     * @return A new Rect with the updated position.
     */
    public Rect move(double x, double y) {
        return new Rect(this.x + x, this.y + y, this.w, this.h);
    }
    /**
     * @return The y-coordinate of the center of the rectangle.
     */
    public double centerY() {
        return this.y + (this.h / 2);
    }
    /**
     * @return The x-coordinate of the center of the rectangle.
     */
    public double centerX() {
        return this.x + (this.w / 2);
    }
    /**
     * @param x The relative x-coordinate.
     * @param y The relative y-coordinate.
     * @param w The relative width.
     * @param h The relative height.
     * @return A new Rect with the updated position and size.
     */
    public Rect relative(double x, double y, double w, double h) {
        return new Rect(
            this.x + (this.w * x),
            this.y + (this.h * y),
            this.w * w,
            this.h * h
        );
    }
    /**
     * @param x1 The x-coordinate of the first point.
     * @param y1 The y-coordinate of the first point.
     * @param x2 The x-coordinate of the second point.
     * @param y2 The y-coordinate of the second point.
     * @return A new Rect that encloses the two points.
     */
    public static Rect fromPoints(double x1, double y1, double x2, double y2) {
        return new Rect(
            Math.min(x1, x2),
            Math.min(y1, y2),
            Math.abs(x1 - x2),
            Math.abs(y1 - y2)
        );
    }
    /**
     * @param amount The amount to rotate the rectangle.
     * @param centerX The x-coordinate of the rotation center.
     * @param centerY The y-coordinate of the rotation center.
     * @return A new Rect with the updated position and size after rotation.
     */
    public Rect rotate(double amount, double centerX, double centerY) {
        double[] a = rotatePoint(centerX, centerY, this.x, this.y, amount);
        double[] b = rotatePoint(centerX, centerY, this.x + this.w, this.y + this.h, amount);
        return Rect.fromPoints(a[0], a[1], b[0], b[1]);
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
    private static double[] rotatePoint(double centerX, double centerY, double x, double y, double angle) {
        double radians = Math.toRadians(angle);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        double tx = x - centerX;
        double ty = y - centerY;
        return new double[] {
            centerX + (tx * cos - ty * sin),
            centerY + (tx * sin + ty * cos)
        };
    }
}

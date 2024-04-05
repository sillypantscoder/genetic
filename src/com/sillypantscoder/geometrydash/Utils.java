package com.sillypantscoder.geometrydash;

public class Utils {
	/**
	 * @param n The input value.
	 * @param in_min The minimum input value.
	 * @param in_max The maximum input value.
	 * @param out_min The minimum output value.
	 * @param out_max The maximum output value.
	 * @return The mapped value.
	 */
	public static double map(double n, double in_min, double in_max, double out_min, double out_max) {
		return (n - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}
	/**
	 * @param cx The center X coordinate.
	 * @param cy The center Y coordinate.
	 * @param x The X coordinate.
	 * @param y The Y coordinate.
	 * @param angle The angle in degrees.
	 * @return The rotated coordinates.
	 */
	public static double[] rotatePoint(double cx, double cy, double x, double y, double angle) {
		double radians = (Math.PI / 180) * angle;
		double cos = Math.cos(radians);
		double sin = Math.sin(radians);
		double nx = (cos * (x - cx)) + (sin * (y - cy)) + cx;
		double ny = (cos * (y - cy)) - (sin * (x - cx)) + cy;
		return new double[] { nx, ny };
	}
}

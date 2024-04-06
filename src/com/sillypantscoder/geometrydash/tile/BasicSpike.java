package com.sillypantscoder.geometrydash.tile;

import com.sillypantscoder.geometrydash.Rect;
import com.sillypantscoder.geometrydash.View;

public class BasicSpike extends TileDeath {
	/**
	 * @param {View} view
	 * @param {number} x
	 * @param {number} y
	 * @param {number} rotation
	 */
	public BasicSpike(View view, double x, double y, double rotation) {
		super(view, x, y, rotation);
	}
	/* static getImage() {
		return `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 10 10">
	<defs>
		<linearGradient id="mainGradient" gradientTransform="rotate(90)">
			<stop offset="0%" stop-color="#000F" />
			<stop offset="100%" stop-color="#0000" />
		</linearGradient>
	</defs>
	<path d="M 5 0 L 10 10 L 0 10 Z M 5 1.5 L 1 9.4 L 9 9.4 Z" fill="white" />
	<path d="M 5 1.5 L 1 9.4 L 9 9.4 Z" fill="url(#mainGradient)" />
</svg>`
	} */
	public Rect getRect() {
		return super.getRect().relative(0.2, 0, 0.6, 0.8);
	}
	public int drawForNetwork() {
		return 2;
	}
}

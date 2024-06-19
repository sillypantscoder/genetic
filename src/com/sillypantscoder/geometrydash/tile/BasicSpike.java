package com.sillypantscoder.geometrydash.tile;

import java.awt.Color;

import com.sillypantscoder.gdgenetic.Surface;
import com.sillypantscoder.geometrydash.Rect;
import com.sillypantscoder.geometrydash.Utils;
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
		return super.getRect().relative(0.4, 0.3, 0.2, 0.5);
	}
	public int[][] drawForNetwork() {
		return new int[][] {
			new int[] { 0, 0, 0, 0 },
			new int[] { 0, 2, 2, 0 },
			new int[] { 0, 2, 2, 0 },
			new int[] { 0, 2, 2, 0 }
		};
	}
	public void drawForHuman(Surface surface, Rect pxRect) {
		Surface s = new Surface((int)(pxRect.w), (int)(pxRect.h), Color.WHITE);
		int maxY = (int)(pxRect.h - 1);
		for (int y = 0; y < maxY; y++) {
			int sidePadding = (int)(Utils.map(y, 0, maxY, pxRect.w / 2, 0));
			int rightSide = ((int)(pxRect.w) - sidePadding) - 1;
			for (int x = 0; x < pxRect.w; x++) {
				s.set_at(x, y, new Color(0f, 0f, 0f, 1f - ((float)(y) / maxY)));
				if (x < sidePadding) s.set_at(x, y, new Color(0, 0, 0, 0));
				if (x == sidePadding) s.set_at(x, y, Color.WHITE);
				if (x == rightSide) s.set_at(x, y, Color.WHITE);
				if (x > rightSide) s.set_at(x, y, new Color(0, 0, 0, 0));
			}
		}
		surface.blit(s, (int)(pxRect.x), (int)(pxRect.y));
	}
}

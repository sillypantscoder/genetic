package com.sillypantscoder.geometrydash.tile;

import java.awt.Color;

import com.sillypantscoder.gdgenetic.Surface;
import com.sillypantscoder.geometrydash.Rect;
import com.sillypantscoder.geometrydash.View;

public class BasicBlock extends TileBlock {
	public BasicBlock(View view, double x, double y, double rotation) {
		super(view, x, y, rotation);
	}
	/* static getImage() {
		return `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20">
	<defs>
		<linearGradient id="mainGradient" gradientTransform="rotate(90)">
			<stop offset="0%" stop-color="#000F" />
			<stop offset="100%" stop-color="#0000" />
		</linearGradient>
	</defs>
	<path d="M 0 0 L 20 0 L 20 20 L 0 20 Z M 1 1 L 1 19 L 19 19 L 19 1 Z" fill="white" />
	<rect x="1" y="1" width="18" height="18" fill="url(#mainGradient)" />
</svg>`
	} */
	public int[][] drawForNetwork() {
		return new int[][] {
			new int[] { 1, 1, 1, 1 },
			new int[] { 1, 1, 1, 1 },
			new int[] { 1, 1, 1, 1 },
			new int[] { 1, 1, 1, 1 }
		};
	}
	public void drawForHuman(Surface surface, Rect pxRect) {
		Surface s = new Surface((int)(pxRect.w), (int)(pxRect.h), Color.WHITE);
		int maxY = (int)(pxRect.h - 2);
		for (int x = 1; x < pxRect.w - 1; x++) {
			for (int y = 0; y < maxY; y++) {
				s.set_at(
					/* x */ x,
					/* y */ 1 + y,
					/* color */
					new Color(0f, 0f, 0f, 1f - ((float)(y) / maxY))
				);
			}
		}
		surface.blit(s, (int)(pxRect.x), (int)(pxRect.y));
	}
}

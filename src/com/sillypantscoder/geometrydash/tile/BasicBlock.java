package com.sillypantscoder.geometrydash.tile;

import java.awt.Color;

import com.sillypantscoder.geometrydash.IntRect;
import com.sillypantscoder.windowlib.Surface;

public class BasicBlock extends TileBlock {
	public BasicBlock(double x, double y, boolean flipped) {
		super(x, y, flipped);
	}
	public int[][] drawForNetwork() {
		return new int[][] {
			new int[] { 1, 1, 1, 1 },
			new int[] { 1, 1, 1, 1 },
			new int[] { 1, 1, 1, 1 },
			new int[] { 1, 1, 1, 1 }
		};
	}
	public void drawForHuman(Surface surface, IntRect pxRect) {
		Surface s = new Surface(pxRect.w, pxRect.h, Color.WHITE);
		int maxY = pxRect.h - 2;
		for (int x = 1; x < pxRect.w - 1; x++) {
			for (int y = 0; y < maxY; y++) {
				int _y = y;
				if (flipped) _y = (maxY - y) - 1;
				s.set_at(
					/* x */ x,
					/* y */ 1 + y,
					/* color */
					new Color(0f, 0f, 0f, 1f - ((float)(_y) / maxY))
				);
			}
		}
		surface.blit(s, pxRect.x, pxRect.y);
	}
}

package com.sillypantscoder.geometrydash.tile;

import java.awt.Color;

import com.sillypantscoder.gdgenetic.Surface;
import com.sillypantscoder.geometrydash.Rect;
import com.sillypantscoder.geometrydash.View;

public class BasicBlock extends TileBlock {
	public BasicBlock(View view, double x, double y) {
		super(view, x, y);
	}
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

package com.sillypantscoder.geometrydash.tile;

import java.awt.Color;

import com.sillypantscoder.gdgenetic.Surface;
import com.sillypantscoder.geometrydash.DoubleRect;
import com.sillypantscoder.geometrydash.IntRect;
import com.sillypantscoder.geometrydash.Utils;

public class BasicSpike extends TileDeath {
	public BasicSpike(double x, double y, boolean flipped) {
		super(x, y, flipped);
	}
	public DoubleRect getRect() {
		return super.getRect().relative(0.4, 0.3, 0.2, 0.5);
	}
	public int[][] drawForNetwork() {
		if (flipped) return new int[][] {
			new int[] { 0, 2, 2, 0 },
			new int[] { 0, 2, 2, 0 },
			new int[] { 0, 2, 2, 0 },
			new int[] { 0, 0, 0, 0 }
		};
		else return new int[][] {
			new int[] { 0, 0, 0, 0 },
			new int[] { 0, 2, 2, 0 },
			new int[] { 0, 2, 2, 0 },
			new int[] { 0, 2, 2, 0 }
		};
	}
	public void drawForHuman(Surface surface, IntRect pxRect) {
		Surface s = new Surface(pxRect.w, pxRect.h, Color.WHITE);
		int maxY = pxRect.h - 1;
		if (flipped) maxY += 1;
		for (int y = flipped ? 1 : 0; y < maxY; y++) {
			int _y = y;
			if (flipped) _y = (maxY - y);
			// calculate triangle shape at this position
			int sidePadding = (int)(Utils.map(_y, 0, maxY, pxRect.w / 2, 0));
			int rightSide = ((pxRect.w) - sidePadding) - 1;
			for (int x = 0; x < pxRect.w; x++) {
				s.set_at(x, y, new Color(0f, 0f, 0f, 1f - ((float)(_y) / maxY)));
				if (x < sidePadding) s.set_at(x, y, new Color(0, 0, 0, 0));
				if (x == sidePadding) s.set_at(x, y, Color.WHITE);
				if (x == rightSide) s.set_at(x, y, Color.WHITE);
				if (x > rightSide) s.set_at(x, y, new Color(0, 0, 0, 0));
			}
		}
		surface.blit(s, pxRect.x, pxRect.y);
	}
}

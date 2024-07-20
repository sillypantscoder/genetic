package com.sillypantscoder.geometrydash.tile;

import java.awt.Color;

import com.sillypantscoder.gdgenetic.Surface;
import com.sillypantscoder.geometrydash.Rect;
import com.sillypantscoder.geometrydash.Utils;

public class BasicSpike extends TileDeath {
	public BasicSpike(double x, double y) {
		super(x, y);
	}
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

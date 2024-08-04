package com.sillypantscoder.geometrydash.tile;

import java.awt.Color;

import com.sillypantscoder.gdgenetic.Surface;
import com.sillypantscoder.geometrydash.Player;
import com.sillypantscoder.geometrydash.IntRect;

public abstract class GravityPortal extends Portal {
	public GravityPortal(int x, int y) {
		super(x, y, 3);
	}
	public abstract int getGravity();
	public abstract Color getColor();
	public void activate(Player player) {
		player.gravity = getGravity();
	}
	public int[][] drawForNetwork() {
		return new int[][] {
			new int[] { 4, 4, 4, 4 },
			new int[] { 4, 4, 4, 4 },
			new int[] { 4, 4, 4, 4 },
			new int[] { 4, 4, 4, 4 }
		};
	}
	public void drawForHuman(Surface surface, IntRect pxRect) {
		Surface s = new Surface(pxRect.w, pxRect.h * 3, new Color(0, 0, 0, 0));
		s.drawEllipse(Color.BLACK, (pxRect.w / 2) + 2, (int)(pxRect.h * 1.5), (pxRect.w / 2) - 2, (int)(pxRect.h * 1.5) - 2, 4);
		s.drawEllipse(getColor(), (pxRect.w / 2) - 2, (int)(pxRect.h * 1.5), (pxRect.w / 2) - 2, (int)(pxRect.h * 1.5) - 2, 4);
		surface.blit(s, pxRect.x, pxRect.y - pxRect.h);
	}
}

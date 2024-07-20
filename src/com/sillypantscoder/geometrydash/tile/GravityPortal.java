package com.sillypantscoder.geometrydash.tile;

import java.awt.Color;

import com.sillypantscoder.gdgenetic.Surface;
import com.sillypantscoder.geometrydash.Player;
import com.sillypantscoder.geometrydash.Rect;
import com.sillypantscoder.geometrydash.View;

public abstract class GravityPortal extends Portal {
	public GravityPortal(View view, int x, int y) {
		super(view, x, y, 3);
	}
	public abstract int getGravity();
	public abstract Color getColor();
	public void activate(Player player) {
		player.gravity = getGravity();
	}
	public int[][] drawForNetwork() {
		if (view.player.gravity == this.getGravity()) {
			return new int[][] {
				new int[] { 0, 0, 0, 0 },
				new int[] { 0, 0, 0, 0 },
				new int[] { 0, 0, 0, 0 },
				new int[] { 0, 0, 0, 0 }
			};
		}
		return new int[][] {
			new int[] { 4, 4, 4, 4 },
			new int[] { 4, 4, 4, 4 },
			new int[] { 4, 4, 4, 4 },
			new int[] { 4, 4, 4, 4 }
		};
	}
	public void drawForHuman(Surface surface, Rect pxRect) {
		Surface s = new Surface((int)(pxRect.w), (int)(pxRect.h * 3), new Color(0, 0, 0, 0));
		s.drawEllipse(Color.BLACK, (int)(pxRect.w / 2.0) + 2, (int)(pxRect.h * 1.5), (int)(pxRect.w / 2.0) - 2, (int)(pxRect.h * 1.5) - 2, 4);
		s.drawEllipse(getColor(), (int)(pxRect.w / 2.0) - 2, (int)(pxRect.h * 1.5), (int)(pxRect.w / 2.0) - 2, (int)(pxRect.h * 1.5) - 2, 4);
		surface.blit(s, (int)(pxRect.x), (int)(pxRect.y - pxRect.h));
	}
}
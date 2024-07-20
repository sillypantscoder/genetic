package com.sillypantscoder.geometrydash.tile;

import java.awt.Color;

import com.sillypantscoder.gdgenetic.Surface;
import com.sillypantscoder.geometrydash.Player;
import com.sillypantscoder.geometrydash.Rect;

public class JumpOrb extends Orb {
	public JumpOrb(double x, double y) {
		super(x, y);
	}
	public void activate(Player player) {
		player.vy = 0.33 * player.gravity;
	}
	public int[][] drawForNetwork() {
		return new int[][] {
			new int[] { 3, 3, 3, 3 },
			new int[] { 3, 3, 3, 3 },
			new int[] { 3, 3, 3, 3 },
			new int[] { 3, 3, 3, 3 }
		};
	}
	public void drawForHuman(Surface surface, Rect pxRect) {
		Surface s = new Surface((int)(pxRect.w), (int)(pxRect.h), new Color(0, 0, 0, 0));
		s.drawCircle(Color.WHITE, (int)(pxRect.w / 2.0), (int)(pxRect.h / 2.0), (int)(pxRect.w / 2.0) - 1, 2);
		s.drawCircle(Color.YELLOW, (int)(pxRect.w / 2.0), (int)(pxRect.h / 2.0), (int)(pxRect.w / 2.0) - 4);
		surface.blit(s, (int)(pxRect.x), (int)(pxRect.y));
	}
}

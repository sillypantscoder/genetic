package com.sillypantscoder.geometrydash.tile;

import java.awt.Color;

import com.sillypantscoder.geometrydash.Player;
import com.sillypantscoder.windowlib.Surface;
import com.sillypantscoder.geometrydash.CubeMode;
import com.sillypantscoder.geometrydash.IntRect;

public class JumpOrb extends Orb {
	public JumpOrb(double x, double y) {
		super(x, y);
	}
	public void activate(Player player) {
		player.vy = CubeMode.JUMP_STRENGH * player.gravity;
	}
	public int[][] drawForNetwork() {
		return new int[][] {
			new int[] { 3, 3, 3, 3 },
			new int[] { 3, 3, 3, 3 },
			new int[] { 3, 3, 3, 3 },
			new int[] { 3, 3, 3, 3 }
		};
	}
	public void drawForHuman(Surface surface, IntRect pxRect) {
		Surface s = new Surface(pxRect.w, pxRect.h, new Color(0, 0, 0, 0));
		s.drawCircle(Color.WHITE, pxRect.w / 2, pxRect.h / 2, (pxRect.w / 2) - 1, 2);
		s.drawCircle(Color.YELLOW, pxRect.w / 2, pxRect.h / 2, (pxRect.w / 2) - 4);
		surface.blit(s, pxRect.x, pxRect.y);
	}
}

package com.sillypantscoder.geometrydash;

import com.sillypantscoder.geometrydash.tile.BasicBlock;

public class Test {
	public static void main(String[] args) {
		View v = new View();
		// Add some tiles
		v.tiles.add(new BasicBlock(v, 5, 0));
		// Tick a bunch of times
		for (int i = 0; i < 20; i++) tick(v);
		v.startPressing();
		tick(v);
		v.stopPressing();
		for (int i = 0; i < 45; i++) tick(v);
	}
	public static void tick(View v) {
		v.timeTick();
		System.out.print("x: ");
		System.out.print(Math.round(v.player.x * 100) / 100.0);
		System.out.print(" | y: ");
		System.out.print(Math.round(v.player.y * 100) / 100.0);
		System.out.print(" | vy: ");
		System.out.print(Math.round(v.player.vy * 100) / 100.0);
		System.out.print(" | has won: ");
		System.out.print(v.hasWon);
		System.out.print(" | has lost: ");
		System.out.println(v.hasDied);
	}
}
package com.sillypantscoder.gdgenetic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Supplier;

import com.sillypantscoder.geometrydash.View;
import com.sillypantscoder.geometrydash.tile.BasicBlock;
import com.sillypantscoder.geometrydash.tile.BasicSpike;
import com.sillypantscoder.geometrydash.tile.JumpOrb;
import com.sillypantscoder.geometrydash.tile.NormalGravityPortal;
import com.sillypantscoder.geometrydash.tile.ReverseGravityPortal;
import com.sillypantscoder.geometrydash.tile.Tile;

public class LevelGeneration {
	public static Random random = new Random();
	public static class Structure {
		public Tile[] tiles;
		public int width;
		public Structure(Tile[] tiles, int width) {
			this.tiles = tiles;
			this.width = width;
		}
		public Structure(ArrayList<Tile> tiles, int width) {
			this.tiles = tiles.toArray(new Tile[0]);
			this.width = width;
		}
		public Structure shift(double x, double y) {
			for (int i = 0; i < tiles.length; i++) {
				tiles[i].x += x;
				tiles[i].y += y;
			}
			return this;
		}
		public Structure plus(Structure other) {
			Tile[] combined = new Tile[tiles.length + other.tiles.length];
			System.arraycopy(tiles, 0, combined, 0, tiles.length);
			System.arraycopy(other.tiles, 0, combined, tiles.length, other.tiles.length);
			return new Structure(combined, this.width + other.width);
		}
		public ArrayList<Tile> tiles() {
			return new ArrayList<>(Arrays.asList(tiles));
		}
	}
	public static Supplier<Structure> getRandomStructure() {
		ArrayList<Supplier<Structure>> structures = new ArrayList<Supplier<Structure>>();
		structures.add(() -> makeSpike());
		structures.add(() -> makeCLGJump());
		structures.add(() -> makeDontJump());
		structures.add(() -> makeOrb());
		structures.add(() -> makeOrbTower());
		structures.add(() -> makeTowerOrb());
		structures.add(() -> makeShortUpsideDownSection());
		Supplier<Structure> s = structures.get(new Random().nextInt(structures.size()));
		return s;
	}
	public static void appendRandomStructure(View v) {
		Structure s = getRandomStructure().get();
		v.tiles.addAll(s.shift(v.generationX, 0).tiles());
		v.generationX += s.width;
	}
	public static View generateLevel() {
		View v = new View();
		v.tiles.add(new BasicSpike(2, 0));
		v.generationX = 4;
		v.ownTiles();
		return v;
	}
	// === STRUCTURES ===
	public static Structure makeSpike() {
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		tiles.add(new BasicSpike(3, 0));
		if (random.nextBoolean()) {
			return new Structure(tiles, 6);
		} else {
			tiles.add(new BasicSpike(4, 0));
			if (random.nextBoolean()) {
				return new Structure(tiles, 7);
			} else {
				tiles.add(new BasicSpike(5, 0));
				return new Structure(tiles, 8);
			}
		}
	}
	public static Structure makeCLGJump() {
		// can't let go jump (rotation doesn't work :/)
		return new Structure(new Tile[] {
			new BasicBlock(4, 1),
			new BasicSpike(4, 2),
			new BasicSpike(6, 0)
		}, 9);
	}
	public static Structure makeDontJump() {
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		for (int n : new int[] { 4, 5, 6, 7 }) {
			tiles.add(new BasicBlock(n, 1));
			tiles.add(new BasicSpike(n, 2));
		}
		return new Structure(tiles, 9);
	}
	public static Structure makeOrb() {
		return new Structure(new Tile[] {
			new JumpOrb(5 + Math.round(Math.random()), 1 + Math.round(Math.random())),
			new BasicSpike(4, 0),
			new BasicSpike(5, 0),
			new BasicSpike(6, 0),
			new BasicSpike(7, 0)
		}, 9);
	}
	public static Structure makeOrbTower() {
		return new Structure(new Tile[] {
			new JumpOrb(4, 1),
			new BasicBlock(7, 0),
			new BasicBlock(7, 1),
			new BasicBlock(7, 2)
		}, 10);
	}
	public static Structure makeTowerOrb() {
		return new Structure(new Tile[] {
			new JumpOrb(9, 1),
			new BasicBlock(4, 0),
			new BasicBlock(4, 1),
			new BasicSpike(5, 0),
			new BasicSpike(6, 0),
			new BasicSpike(7, 0),
			new BasicSpike(8, 0),
			new BasicSpike(9, 0),
			new BasicSpike(10, 0),
			new BasicSpike(11, 0)
		}, 13);
	}
	public static Structure makeShortUpsideDownSection() {
		return new Structure(new Tile[] {
			new BasicSpike(4, 0),
			new ReverseGravityPortal(4, 2),
			new BasicBlock(5, 5),
			new BasicBlock(6, 5),
			new BasicBlock(7, 5),
			new NormalGravityPortal(8, 2),
			new BasicBlock(9, 4),
			new BasicSpike(9, 5)
		}, 9);
	}
}

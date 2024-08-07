package com.sillypantscoder.gdgenetic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Supplier;

import com.sillypantscoder.geometrydash.View;
import com.sillypantscoder.geometrydash.tile.BasicBlock;
import com.sillypantscoder.geometrydash.tile.BasicSpike;
import com.sillypantscoder.geometrydash.tile.GravityOrb;
import com.sillypantscoder.geometrydash.tile.GravityPortal;
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
		public Structure flip(double newFloorY) {
			for (int i = 0; i < tiles.length; i++) {
				tiles[i].y = (newFloorY - tiles[i].y) - 1;
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
		public boolean hasTile(Class<? extends Tile> type) {
			for (Tile t : tiles) {
				if (type.isInstance(t)) return true;
			}
			return false;
		}
	}
	public static Supplier<Structure> getRandomStructure() {
		ArrayList<Supplier<Structure>> structures = new ArrayList<Supplier<Structure>>();
		structures.add(LevelGeneration::makeSpike);
		structures.add(LevelGeneration::makeSMSet);
		structures.add(LevelGeneration::makeLongBlocks);
		structures.add(LevelGeneration::makeCLGJump);
		structures.add(LevelGeneration::makeDontJump);
		structures.add(LevelGeneration::makeOrb);
		structures.add(LevelGeneration::makeOrbTower);
		structures.add(LevelGeneration::makeTowerOrb);
		structures.add(LevelGeneration::makeLongUpsideDownSection);
		structures.add(LevelGeneration::makeSunkenSpikes);
		structures.add(LevelGeneration::makeLessSunkenSpikes);
		structures.add(LevelGeneration::makeSunkenSpikes);
		structures.add(LevelGeneration::makeLessSunkenSpikes);
		structures.add(LevelGeneration::makeSunkenSpikes);
		structures.add(LevelGeneration::makeLessSunkenSpikes);
		structures.add(LevelGeneration::makeSunkenSpikes);
		structures.add(LevelGeneration::makeLessSunkenSpikes);
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
	public static Structure makeSMSet() {
		return new Structure(new Tile[] {
			new BasicSpike(3, 0),
			new BasicSpike(4, 0),
			new BasicSpike(5, 0),
			new BasicBlock(6, 0),
			new BasicSpike(7, 0),
			new BasicSpike(8, 0),
			new BasicSpike(9, 0),
			new BasicBlock(10, 0),
			new BasicBlock(10, 1),
			new BasicSpike(11, 0),
			new BasicSpike(12, 0),
			new BasicSpike(13, 0),
			new BasicBlock(14, 0),
			new BasicBlock(14, 1),
			new BasicBlock(14, 2)
		}, 15);
	}
	public static Structure makeLongBlocks() {
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		int width = 2 + random.nextInt(3);
		int numInner = 1 + random.nextInt(2);
		for (int i = 0; i < numInner; i++) {
			Structure inner = getRandomStructure().get();
			if (random.nextDouble() < 0.3) inner = makeLongBlocks();
			inner.shift(width, 1);
			tiles.addAll(inner.tiles());
			width += inner.width;
		}
		width += random.nextInt(3);
		for (int i = 0; i < width; i++) {
			tiles.add(new BasicBlock(3 + i, 0));
		}
		return new Structure(tiles, 3 + width);
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
		int padStart = 2 + random.nextInt(1);
		int padEnd = 0 + random.nextInt(3);
		int n = 1 + random.nextInt(3);
		for (int i = 0; i < n; i++) {
			tiles.add(new BasicBlock(padStart + i, 1));
			tiles.add(new BasicSpike(padStart + i, 2));
		}
		return new Structure(tiles, padStart + n + padEnd);
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
	public static Structure makeLongUpsideDownSection() {
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		// Prompt the player to jump
		tiles.add(new BasicSpike(4, 0));
		// Switch gravity
		if (random.nextBoolean()) {
			tiles.add(new ReverseGravityPortal(4, 2));
		} else {
			tiles.add(new GravityOrb(5, 2));
			tiles.add(new BasicSpike(5, 0));
			tiles.add(new BasicSpike(6, 0));
			tiles.add(new BasicSpike(7, 0));
		}
		// Generate inner structures
		int width = 5;
		int numInner = 1 + random.nextInt(4);
		for (int i = 0; i < numInner; i++) {
			Structure inner = getRandomStructure().get();
			if (inner.hasTile(GravityPortal.class)) continue;
			if (inner.hasTile(GravityOrb.class)) continue;
			inner.flip(5);
			inner.shift(width, 0);
			tiles.addAll(inner.tiles());
			width += inner.width;
		}
		// Generate floor
		for (int i = 0; i < width + 3; i++) {
			tiles.add(new BasicBlock(5 + i, 5));
		}
		// Prompt the player to jump
		tiles.add(new BasicSpike(4 + width, 4));
		tiles.add(new BasicSpike(5 + width, 4));
		tiles.add(new BasicSpike(6 + width, 4));
		tiles.add(new BasicSpike(7 + width, 4));
		// Switch gravity back
		if (random.nextBoolean()) {
			tiles.add(new NormalGravityPortal(5 + width, 2));
		} else {
			tiles.add(new GravityOrb(5 + width, 2));
		}
		return new Structure(tiles, 6 + width);
	}
	public static Structure makeSunkenSpikes() {
		return new Structure(new Tile[] {
			new BasicBlock(4, 0),
			new BasicBlock(4, 1),
			new BasicSpike(5, 0),
			new BasicSpike(6, 0),
			new BasicSpike(7, 0),
			new BasicSpike(8, 0),
			new BasicBlock(9, 0),
			new BasicBlock(9, 1)
		}, 13);
	}
	public static Structure makeLessSunkenSpikes() {
		return new Structure(new Tile[] {
			new BasicBlock(4, 0),
			new BasicBlock(5, 0),
			new BasicSpike(6, 0),
			new BasicSpike(7, 0),
			new BasicSpike(8, 0),
			new BasicSpike(9, 0),
			new BasicBlock(10, 0),
			new BasicBlock(11, 0)
		}, 15);
	}
}

package com.sillypantscoder.gdgenetic;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;

import com.sillypantscoder.geometrydash.Rect;
import com.sillypantscoder.geometrydash.View;
import com.sillypantscoder.geometrydash.tile.BasicBlock;
import com.sillypantscoder.geometrydash.tile.BasicSpike;
import com.sillypantscoder.geometrydash.tile.Tile;
import com.sillypantscoder.geometrydash.tile.TileDeath;

public class NetworkEvaluator {
	public static void main(String[] args) {
		// Level Generation
		View v = LevelGeneration.generateLevel();
		BinaryOperator<Integer> getTile = (x, y) -> {
			if (y < 0) return 1;
			for (int i = 0; i < v.tiles.size(); i++) {
				Tile tile = v.tiles.get(i);
				if (tile.x == x && tile.y == y) {
					int a = tile.drawForNetwork();
					return a;
				}
			}
			return 0;
		};
		for (int y = (int)(Math.ceil(v.getStageHeight())); y > -1; y--) {
			for (int x = 0; x < v.getStageWidth(); x++) {
				int p = getTile.apply(x, y);
				String s = p==0 ? "-" : (p==1 ? "#" : "^");
				System.out.print(s);
			}
			System.out.println();
		}
		// Rendering
		ArrayList<Double> inputs = Rendering.getNetworkInputs(v);
		for (int i = 0; i < inputs.size(); i += 3) {
			if (i % (5*3) == 0) System.out.println();
			System.out.print(
				inputs.get(i+0)==1 ? "-" :
				inputs.get(i+1)==1 ? "#" : "V");
		}
		System.out.println();
		System.out.println("camera: (" + Rendering.getCameraX(v) + ", " + Rendering.getCameraY(v) + ")");
		// Simulation
		Network network = Network.createZeroLayer(5 * 5 * 3);
		double score = evaluateNetwork(network);
		System.out.println(score);
	}
	public static class LevelGeneration {
		public static int addSpike(View v, int x) {
			v.tiles.add(new BasicSpike(v, x + 3, 0, 0));
			if (new Random().nextBoolean()) {
				return 6;
			} else {
				v.tiles.add(new BasicSpike(v, x + 4, 0, 0));
				if (new Random().nextBoolean()) {
					return 7;
				} else {
					v.tiles.add(new BasicSpike(v, x + 5, 0, 0));
					return 8;
				}
			}
		}
		public static int addBlocks(View v, int x) {
			if (new Random().nextBoolean()) v.tiles.add(new BasicSpike(v, x + 2, 0, 0));
			v.tiles.add(new BasicBlock(v, x + 3, 0, 0));
			v.tiles.add(new BasicBlock(v, x + 4, 0, 0));
			v.tiles.add(new BasicBlock(v, x + 5, 0, 0));
			v.tiles.add(new BasicBlock(v, x + 6, 0, 0));
			if (new Random().nextBoolean()) v.tiles.add(new BasicSpike(v, x + 6, 1, 0));
			if (new Random().nextBoolean()) {
				return 8;
			} else {
				v.tiles.add(new BasicSpike(v, x + 7, 0, 0));
				return 9;
			}
		}
		public static int addLongBlocks(View v, int x) {
			if (new Random().nextBoolean()) v.tiles.add(new BasicSpike(v, x + 3, 0, 0));
			v.tiles.add(new BasicBlock(v, x + 4, 0, 0)); if (new Random().nextBoolean()) v.tiles.add(new BasicSpike(v, x + 4, 1, 0));
			v.tiles.add(new BasicBlock(v, x + 5, 0, 0));
			v.tiles.add(new BasicBlock(v, x + 6, 0, 0));
			v.tiles.add(new BasicBlock(v, x + 7, 0, 0)); if (new Random().nextBoolean()) v.tiles.add(new BasicSpike(v, x + 7, 1, 0));
			v.tiles.add(new BasicBlock(v, x + 8, 0, 0)); if (new Random().nextBoolean()) v.tiles.add(new BasicSpike(v, x + 8, 1, 0));
			v.tiles.add(new BasicBlock(v, x + 9, 0, 0));
			v.tiles.add(new BasicBlock(v, x + 10, 0, 0));
			v.tiles.add(new BasicBlock(v, x + 11, 0, 0)); if (new Random().nextBoolean()) v.tiles.add(new BasicSpike(v, x + 11, 1, 0));
			if (new Random().nextBoolean()) v.tiles.add(new BasicSpike(v, x + 12, 0, 0));
			return 14;
		}
		public static View generateLevel() {
			View v = new View();
			v.tiles.add(new BasicSpike(v, 4, 0, 0));
			int currentX = 8;
			ArrayList<IntUnaryOperator> structures = new ArrayList<IntUnaryOperator>();
			structures.add((x) -> addSpike(v, x));
			structures.add((x) -> addBlocks(v, x));
			structures.add((x) -> addLongBlocks(v, x));
			Random r = new Random();
			for (int i = 0; i < 10; i++) {
				int width = structures.get(r.nextInt(structures.size())).applyAsInt(currentX);
				currentX += width;
			}
			return v;
		}
	}
	public static class Rendering {
		public static int getPixel(View view, double x, double y) {
			if (y < 0) {
				// Ground
				return 1;
			}
			int blockX = (int)(Math.floor(x));
			int blockY = (int)(Math.floor(y));
			Tile tile = null;
			for (int i = 0; i < view.tiles.size(); i++) {
				Tile t = view.tiles.get(i);
				if (t.x == blockX && t.y == blockY) {
					tile = t;
				}
			}
			if (tile == null) {
				// Air
				return 0;
			}
			int pixel = tile.drawForNetwork();
			// if (pixel==2)System.out.println("[" + x + ", " + y + " => " + pixelX + ", " + pixelY + "]");
			return pixel;
		}
		public static ArrayList<Double> get1HPixel(View view, double x, double y) {
			int pixel = getPixel(view, x, y);
			ArrayList<Double> oneh = new ArrayList<Double>();
			oneh.add(pixel==0 ? 1.0 : 0.0);
			oneh.add(pixel==1 ? 1.0 : 0.0);
			oneh.add(pixel==2 ? 1.0 : 0.0);
			return oneh;
		}
		/*
				/---------\
				|         |
				|  +++++  | (+ = visible block)
				|  +++++  |
				|  X++++  | (X = player)
				|  +++++  |
				|  +++++  | Camera position is lower left of the + square.
				|         | Coordinate origin is at lower left.
				\---------/
		 */
		public static double getCameraX(View view) {
			return Math.floor(view.player.x * 4.0) / 4.0;
		}
		public static double getCameraY(View view) {
			return (Math.floor(view.player.y * 4.0) / 4.0) - 2.0;
		}
		public static ArrayList<Double> getNetworkInputs(View view) {
			ArrayList<Double> inputs = new ArrayList<Double>();
			double cameraX = getCameraX(view);
			double cameraY = getCameraY(view);
			for (int y = 0; y < 5; y++) {
				for (int x = 0; x < 5; x++) {
					ArrayList<Double> pixel = get1HPixel(view, (x / 1.0) + cameraX, (y / 1.0) + cameraY);
					inputs.addAll(pixel);
				}
			}
			return inputs;
		}
	}
	public static class VideoMaker {
		public static void main(String[] args) {
			Network network = GeneticAlgorithm.createNetwork();
			runSimulation(network, 0);
		}
		public static void runSimulation(Network network, int filename) {
			View view = LevelGeneration.generateLevel();
			// Render the level
			Function<Double, Integer> cx = x -> (int)(Math.ceil(          (x + 2)           * Tile.RENDER_TILE_SIZE));
			Function<Double, Integer> cn = n -> (int)(Math.ceil(             n              * Tile.RENDER_TILE_SIZE));
			Function<Double, Integer> cy = y -> (int)(Math.ceil((view.getStageHeight() - y) * Tile.RENDER_TILE_SIZE));
			Surface surface = new Surface(cx.apply(view.getStageWidth()), cy.apply(0.0) + 1, Color.WHITE);
			for (int i = 0; i < view.tiles.size(); i++) {
				Tile t = view.tiles.get(i);
				Rect rect = t.getRect();
				Color color = Color.BLACK;
				if (t instanceof TileDeath) color = Color.RED;
				// System.out.println(rect.x + " > " + crd.apply(rect.x));
				surface.drawRect(color,
					cx.apply(rect.x),
					cy.apply(rect.y) - (cn.apply(rect.h) - 1),
					cn.apply(rect.w),
					cn.apply(rect.h));
			}
			// Run the simulation
			while (true) {
				boolean decision = getNetworkDecision(view, network);
				if (decision && !view.isPressing) {
					view.startPressing();
				} else {
					view.stopPressing();
				}
				view.timeTick();
				// Draw
				int px = cx.apply(view.player.x + 0.5);
				int py = cy.apply(view.player.y + 0.5);
				surface.set_at(px - 1, py, decision ? Color.ORANGE : Color.BLUE);
				// Finish
				if (view.hasDied) {
					// hitbox
					Rect r = view.player.getDeathRect();
					// surface.drawRect(Color.WHITE, cx.apply(r.x), cy.apply(r.y) - cn.apply(r.h), cn.apply(r.w), cn.apply(r.h));
					surface.drawRect(Color.RED, cx.apply(r.x), cy.apply(r.y) - cn.apply(r.h), cn.apply(r.w), cn.apply(r.h), 1);
					// x
					surface.set_at(px - 2, py - 2, Color.RED);
					surface.set_at(px - 1, py - 1, Color.RED);
					surface.set_at(px + 2, py - 2, Color.RED);
					surface.set_at(px + 1, py - 1, Color.RED);
					surface.set_at(px    , py    , Color.RED);
					surface.set_at(px - 1, py + 1, Color.RED);
					surface.set_at(px - 2, py + 2, Color.RED);
					surface.set_at(px + 1, py + 1, Color.RED);
					surface.set_at(px + 2, py + 2, Color.RED);
				}
				if (view.hasWon || view.hasDied) {
					// The simulation is over
					break;
				}
			}
			// Save the image
			String fn = "network" + filename + ".png";
			try { surface.writeToFile(fn); System.out.println("[Saved to file: " + fn + "]"); }
			catch (IOException e) { e.printStackTrace(); }
		}
	}
	public static boolean getNetworkDecision(View view, Network network) {
		double output = network.evaluate(Rendering.getNetworkInputs(view)).get(0);
		double probability = (output + 1) / 2;
		return Math.random() < probability;
	}
	public static int runSimulation(Network network) {
		View view = LevelGeneration.generateLevel();
		int frames = 0;
		while (true) {
			boolean decision = getNetworkDecision(view, network);
			// System.out.print("Start frame " + (frames + 1) + "; y is: " + (Math.round(view.player.y * 100) / 100) + "; decision is: " + decision + "; result is:");
			if (decision && !view.isPressing) {
				view.startPressing();
			} else {
				view.stopPressing();
			}
			view.timeTick();
			frames += 1;
			// if (view.hasWon) System.out.print(" [won!]");
			// else if (view.hasDied) System.out.print(" [died]");
			// else System.out.print(" [survived]");
			// System.out.println();
			if (view.hasWon || view.hasDied) {
				// The simulation is over
				return frames;
			}
		}
	}
	public static double evaluateNetwork(Network network) {
		int n_trials = 100;
		int totalScore = 0;
		for (int i = 0; i < n_trials; i++) {
			totalScore += runSimulation(network);
		}
		return (double)(totalScore) / n_trials;
	}
	public static double evaluateNetworks(ArrayList<Network> networks) {
		double totalScore = 0;
		for (int i = 0; i < networks.size(); i++) {
			totalScore += evaluateNetwork(networks.get(i));
		}
		return totalScore / networks.size();
	}
}

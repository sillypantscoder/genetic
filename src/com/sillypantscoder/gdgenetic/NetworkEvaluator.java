package com.sillypantscoder.gdgenetic;

import java.awt.Color;
import java.util.ArrayList;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import com.sillypantscoder.geometrydash.Rect;
import com.sillypantscoder.geometrydash.View;
import com.sillypantscoder.geometrydash.tile.Tile;

public class NetworkEvaluator {
	public static final int POINTS_PER_FRAME = 1;
	public static final int JUMP_PENALTY = -3;
	public static final int POINTLESS_JUMP_PENALTY = -4;
	public static final int VIDEO_MIN_OUTPUT_SCORE = 100;
	public static void main(String[] args) {
		// Level Generation
		View v = LevelGeneration.generateLevel();
		BinaryOperator<Integer> getTile = (x, y) -> {
			if (y < 0) return 1;
			for (int i = 0; i < v.tiles.size(); i++) {
				Tile tile = v.tiles.get(i);
				if (tile.x == x && tile.y == y) {
					int a = tile.drawForNetwork()[1][1];
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
		double score = evaluateNetwork(network, 0);
		System.out.println(score);
	}
	public static class Rendering {
		public static int getPixel(View view, double x, double y) {
			if (y < 0) {
				// Ground
				return 1;
			}
			int blockX = (int)(Math.floor(x));
			int blockY = (int)(Math.floor(y));
			int pixelX = (int)(Math.floor(x - blockX) * 4);
			int pixelY = (int)(Math.floor(x - blockX) * 4);
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
			int[][] block = tile.drawForNetwork();
			int pixel = block[pixelY][pixelX];
			return pixel;
		}
		public static ArrayList<Double> get1HPixel(View view, double x, double y) {
			int pixel = getPixel(view, x, y);
			ArrayList<Double> oneh = new ArrayList<Double>();
			oneh.add(pixel == 0 ? 1.0 : 0.0);
			oneh.add(pixel == 1 ? 1.0 : 0.0);
			oneh.add(pixel == 2 ? 1.0 : 0.0);
			oneh.add(pixel == 3 ? 1.0 : 0.0);
			oneh.add(pixel == 4 ? 1.0 : 0.0);
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
			for (int y = 0; y < 5 * 4; y++) {
				for (int x = 0; x < 5 * 4; x++) {
					int readY = y;
					if (view.player.gravity < 0) readY = ((5 * 4) - y) - 1;
					ArrayList<Double> pixel = get1HPixel(view, (x / 4.0) + cameraX, (readY / 4.0) + cameraY);
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
		public static Surface renderSimulation(View view, ArrayList<Double> playerX, ArrayList<Double> playerY, ArrayList<Double> clickX, ArrayList<Double> clickY) {
			// 1. Render the tiles
			Function<Double, Integer> cx = x -> (int)(Math.ceil(          (x + 2)           * Tile.RENDER_TILE_SIZE));
			Function<Double, Integer> cn = n -> (int)(Math.ceil(             n              * Tile.RENDER_TILE_SIZE));
			Function<Double, Integer> cy = y -> (int)(Math.ceil((view.getStageHeight() - y) * Tile.RENDER_TILE_SIZE));
			Surface surface = new Surface(cx.apply(view.getStageWidth()), cy.apply(0.0) + 1, new Color(0, 125, 255));
			for (int i = 0; i < view.tiles.size(); i++) {
				Tile t = view.tiles.get(i);
				Rect rect = new Rect(t.x, t.y, 1, 1);
				Rect pxRect = new Rect(
					cx.apply(rect.x), // x
					cy.apply(rect.y) - (cn.apply(rect.h) - 1), // y
					cn.apply(rect.w), // w
					cn.apply(rect.h)); // h
				t.drawForHuman(surface, pxRect);
				// hitboxes for testing
				/* if (t instanceof TileDeath) {
					Rect rect2 = t.getRotatedRect();
					surface.drawRect(Color.RED,
						cx.apply(rect2.x),
						cy.apply(rect2.y) - (cn.apply(rect2.h) - 1),
						cn.apply(rect2.w),
						cn.apply(rect2.h));
				} */
			}
			// 3. Simulation parameters
			int previousX = -10;
			int previousY = 0;
			int px = 0;
			int py = 0;
			// 2. Go through the simulation
			for (int i = 0; i < playerX.size(); i++) {
				px = cx.apply(playerX.get(i) + 0.5);
				py = cy.apply(playerY.get(i) + 0.5);
				// Draw path
				surface.drawLine(new Color(0, 255, 0), previousX - 1, previousY, px, py, 1);
				// Update previous position
				previousX = px;
				previousY = py;
			}
			// 3. Draw death effects
			if (view.hasDied) {
				// hitbox
				Rect r = view.player.getGeneralRect();
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
			// 4. Add the clicks
			for (int i = 0; i < clickX.size(); i++) {
				int clickPixelX = cx.apply(clickX.get(i) + 0.5);
				int clickPixelY = cy.apply(clickY.get(i) + 0.5);
				surface.drawRect(new Color(255, 100, 0), clickPixelX - 3, clickPixelY - 3, 6, 6);
			}
			return surface;
		}
		public static int runSimulation(Network network, int filename) {
			View view = LevelGeneration.generateLevel();
			// Simulation parameters
			ArrayList<Double> playerX = new ArrayList<Double>();
			ArrayList<Double> playerY = new ArrayList<Double>();
			ArrayList<Double> clickX = new ArrayList<Double>();
			ArrayList<Double> clickY = new ArrayList<Double>();
			boolean previousDecision = false;
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
				playerX.add(view.player.x);
				playerY.add(view.player.y);
				if (decision && !previousDecision) {
					clickX.add(view.player.x);
					clickY.add(view.player.y);
				}
				previousDecision = decision;
				// Finish
				if (view.hasWon || view.hasDied) {
					// The simulation is over
					break;
				}
			}
			// Save the image
			if (view.agentScore >= VIDEO_MIN_OUTPUT_SCORE) {
				Surface surface = renderSimulation(view, playerX, playerY, clickX, clickY);
				String fn = "outputs/score" + view.agentScore + "_network" + filename + "_v";
				surface.save(fn);
				// System.out.println("\t[Video saved to file: " + fn + "]");
			}
			return view.agentScore;
		}
	}
	public static boolean getNetworkDecision(View view, Network network) {
		double probability = network.evaluate(Rendering.getNetworkInputs(view)).get(0);
		// probability is on a scale from -1 to 1
		return Math.random() < probability;
	}
	public static double evaluateNetwork(Network network, int filename) {
		int n_trials = 100;
		int totalScore = 0;
		for (int i = 0; i < n_trials; i++) {
			totalScore += VideoMaker.runSimulation(network, filename);
		}
		return (double)(totalScore) / n_trials;
	}
	public static double[] evaluateNetworksWithoutPrintingData(ArrayList<Network> networks, int filename) {
		ThreadedNetworkEvaluator evaluator = new ThreadedNetworkEvaluator(networks, filename);
		evaluator.executeThreads(false);
		double totalScore = 0;
		double maxScore = Double.MIN_VALUE;
		for (int i = 0; i < networks.size(); i++) {
			if (! evaluator.results.containsKey(networks.get(i))) {
				continue;
			}
			double score = evaluator.results.get(networks.get(i));
			totalScore += score;
			if (score > maxScore) maxScore = score;
		}
		return new double[] {
			totalScore / networks.size(),
			maxScore
		};
	}
}

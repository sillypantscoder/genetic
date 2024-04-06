package com.sillypantscoder.gdgenetic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.function.IntUnaryOperator;

import com.sillypantscoder.geometrydash.View;
import com.sillypantscoder.geometrydash.tile.BasicBlock;
import com.sillypantscoder.geometrydash.tile.BasicSpike;
import com.sillypantscoder.geometrydash.tile.Tile;

public class NetworkEvaluator {
	public static void main(String[] args) {
		// Level Generation
		View v = LevelGeneration.generateLevel();
		v.tiles.add(new BasicSpike(v, 1, 0, 0));
		v.tiles.add(new BasicBlock(v, 0, 0, 0));
		for (int i = 0; i < v.tiles.size(); i++) {
			System.out.println();
			Tile tile = v.tiles.get(i);
			int[][] a = tile.drawForNetwork();
			String x_str = "X=" + tile.x + "  ";
			String y_str = "Y=" + tile.y + "  ";
			String space = " ".repeat(Math.max(x_str.length(), y_str.length()));
			x_str += " ".repeat(space.length() - x_str.length());
			y_str += " ".repeat(space.length() - y_str.length());
			System.out.print(space); System.out.println(String.join(" ", Arrays.stream(a[0]).mapToObj(String::valueOf).toArray(String[]::new)));
			System.out.print(x_str); System.out.println(String.join(" ", Arrays.stream(a[1]).mapToObj(String::valueOf).toArray(String[]::new)));
			System.out.print(y_str); System.out.println(String.join(" ", Arrays.stream(a[2]).mapToObj(String::valueOf).toArray(String[]::new)));
			System.out.print(space); System.out.println(String.join(" ", Arrays.stream(a[3]).mapToObj(String::valueOf).toArray(String[]::new)));
		}
		// Rendering
		ArrayList<Double> inputs = Rendering.getNetworkInputs(v);
		for (int i = 0; i < inputs.size(); i += 3) {
			if (i % (5*4*3) == 0) System.out.println();
			if (i % (5*4*3*4) == 0) System.out.println();
			if (i % (4*3) == 0) System.out.print(" ");
			System.out.print(
				inputs.get(i+0)==1 ? "-" :
				inputs.get(i+1)==1 ? "#" : "V");
		}
		System.out.println();
		System.out.println("camera: (" + Rendering.getCameraX(v) + ", " + Rendering.getCameraY(v) + ")");
		// Simulation
		Network network = Network.createZeroLayer(5*4 * 5*4 * 3);
		double score = evaluateNetwork(network);
		System.out.println(score);
	}
	public static class LevelGeneration {
		public static int add1Spike(View v, int x) {
			v.tiles.add(new BasicSpike(v, x + 3, 0, 0));
			return 6;
		}
		public static int add2Spikes(View v, int x) {
			v.tiles.add(new BasicSpike(v, x + 3, 0, 0));
			v.tiles.add(new BasicSpike(v, x + 4, 0, 0));
			return 7;
		}
		public static int add4Blocks(View v, int x) {
			v.tiles.add(new BasicBlock(v, x + 3, 0, 0));
			v.tiles.add(new BasicBlock(v, x + 4, 0, 0));
			v.tiles.add(new BasicBlock(v, x + 5, 0, 0));
			v.tiles.add(new BasicBlock(v, x + 6, 0, 0));
			return 8;
		}
		public static View generateLevel() {
			View v = new View();
			v.tiles.add(new BasicSpike(v, 4, 0, 0));
			int currentX = 8;
			ArrayList<IntUnaryOperator> structures = new ArrayList<IntUnaryOperator>();
			structures.add((x) -> add1Spike(v, x));
			structures.add((x) -> add2Spikes(v, x));
			structures.add((x) -> add4Blocks(v, x));
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
			int pixelX = (int)(Math.floor((x - blockX) * 4));
			int pixelY = (int)(Math.floor((y - blockY) * 4));
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
			int[][] pixels = tile.drawForNetwork();
			// if (pixels[pixelY][pixelX]==2)System.out.println("[" + x + ", " + y + " => " + pixelX + ", " + pixelY + "]");
			return pixels[3-pixelY][pixelX];
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
			for (int y = 0; y < 5*4; y++) {
				for (int x = 0; x < 5*4; x++) {
					ArrayList<Double> pixel = get1HPixel(view, (x / 4.0) + cameraX, (y / 4.0) + cameraY);
					inputs.addAll(pixel);
				}
			}
			return inputs;
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

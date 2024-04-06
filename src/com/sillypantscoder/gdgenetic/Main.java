package com.sillypantscoder.gdgenetic;

import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {
		// Make our network
		Network network = Network.createZeroLayer(5*4 * 5*4 * 3);
		ArrayList<Network> networkList = new ArrayList<Network>();
		networkList.add(network);
		// Initial score
		double initialScore = NetworkEvaluator.evaluateNetworks(networkList);
		System.out.println("Initial score is: " + initialScore);
		bar(initialScore);
		// Run a bunch of iterations
		int totalIterations = 40;
		double previousScore = 0;
		for (int i = 0; i < totalIterations + 1; i++) {
			// Find average score for this iteration
			System.out.println("Iterations done: " + i + "/" + totalIterations + " (" + neat(((double)(i)/totalIterations)*100.0) + "%)");
			double score = NetworkEvaluator.evaluateNetworks(networkList);
			System.out.print("\tAverage score is: " + neat(score));
			System.out.println(" (" + (score>previousScore ? "+" : "-") + neat(Math.abs(score - previousScore)) + " from last; " +
				(score>initialScore ? "+" : "-") + neat(Math.abs(score - initialScore)) + " from first)");
			previousScore = score;
			bar(score);
			// Run the iteration
			if (i == totalIterations) continue;
			networkList = GeneticAlgorithm.runOneIteration(networkList);
		}
	}
	public static double neat(double in) {
		return Math.round(in * 1000.0) / 1000.0;
	}
	public static void bar(double v) {
		// ascii art :D
		double spaces = (v - 19.5) * 100;
		System.out.println("-".repeat((int)(Math.round(spaces))) + "#");
	}
}

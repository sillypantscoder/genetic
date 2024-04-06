package com.sillypantscoder.gdgenetic;

import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {
		// Make our network
		Network network = Network.createZeroLayer(5*4 * 5*4 * 3);
		ArrayList<Network> networkList = new ArrayList<Network>();
		networkList.add(network);
		// Run a bunch of iterations
		double previousScore = 0;
		for (int i = 0; i < 10; i++) {
			// Find average score for this iteration
			System.out.print("Iteration #" + (i + 1) + " - average score is:");
			double score = NetworkEvaluator.evaluateNetworks(networkList);
			System.out.print(" " + neat(score));
			if (i == 0) System.out.println();
			else System.out.println(" (" + (score>previousScore ? "+" : "-") + neat(Math.abs(score - previousScore)) + ")");
			previousScore = score;
			// Run the iteration
			networkList = GeneticAlgorithm.runOneIteration(networkList);
		}
	}
	public static double neat(double in) {
		return Math.round(in * 1000.0) / 1000.0;
	}
}

package com.sillypantscoder.gdgenetic;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {
		// Make our network
		Network network = GeneticAlgorithm.createNetwork();
		ArrayList<Network> networkList = new ArrayList<Network>();
		networkList.add(network);
		// Initial score
		double initialScore = NetworkEvaluator.evaluateNetworks(networkList);
		System.out.println("Initial score is: " + initialScore);
		bar(initialScore);
		// Run a bunch of iterations
		int totalIterations = 100;
		double previousScore = initialScore;
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
			// Find which network is best
			Network best = GeneticAlgorithm.purgeNetworkList(networkList, 1).get(0);
			String filename = "network" + (i + 1) + ".txt";
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
				writer.write(best.save());
				writer.close();
				System.out.println("\t[Saved to file: " + filename + "]");
			} catch (IOException e) {
				System.out.println(best.save());
				e.printStackTrace();
			}
			NetworkEvaluator.VideoMaker.runSimulation(network, i + 1);
		}
	}
	public static double neat(double in) {
		return Math.round(in * 1000.0) / 1000.0;
	}
	public static void bar(double v) {
		// ascii art :D
		double spaces = (v - 97.5) * 10;
		try {
			System.out.println("-".repeat((int)(Math.round(spaces))) + "#");
		} catch (IllegalArgumentException e) {
			System.out.println("<- " + Math.round(spaces));
		}
	}
}

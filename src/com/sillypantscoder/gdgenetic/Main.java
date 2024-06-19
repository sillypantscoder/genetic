package com.sillypantscoder.gdgenetic;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
	public static final boolean SAVE_NN_FILES = true;
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		// Make our network
		Network network = GeneticAlgorithm.createNetwork();
		ArrayList<Network> networkList = new ArrayList<Network>();
		networkList.add(network);
		// Initial score
		double initialScore = NetworkEvaluator.evaluateNetworks(networkList)[0];
		System.out.println("Initial score is: " + initialScore);
		bar(initialScore);
		// Run a bunch of iterations
		int totalIterations = 1000;
		double previousScore = initialScore;
		for (int i = 0; i < totalIterations + 1; i++) {
			// Find average score for this iteration
			System.out.println("Iterations done: " + i + "/" + totalIterations + " (" + neat(((double)(i)/totalIterations)*100.0) + "%)");
			double[] scores = NetworkEvaluator.evaluateNetworks(networkList);
			double score = scores[0];
			int stagewidth = (int)(NetworkEvaluator.LevelGeneration.generateLevel().getStageWidth() / 0.2);
			System.out.print("\tAverage score is: " + neat(score) + "/" + stagewidth);
			System.out.println(" (" + (score>previousScore ? "+" : "-") + neat(Math.abs(score - previousScore)) + " from last; " +
				(score>initialScore ? "+" : "-") + neat(Math.abs(score - initialScore)) + " from first)");
			bar(score);
			System.out.println("\tMaximum score is: " + neat(scores[1]) + "/" + stagewidth);
			bar(scores[1]);
			previousScore = score;
			// Run the iteration
			if (i == totalIterations) continue;
			networkList = GeneticAlgorithm.runOneIteration(networkList);
			// Find which network is best
			Network best = GeneticAlgorithm.purgeNetworkList(networkList, 1).get(0);
			if (SAVE_NN_FILES || i == totalIterations - 1 || (i + 1) % 25 == 0) {
				String filename = "outputs/network" + (i + 1) + ".txt";
				try {
					BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
					writer.write(best.save());
					writer.close();
					System.out.println("\t[AI saved to file: " + filename + "]");
				} catch (IOException e) {
					System.out.println(best.save());
					e.printStackTrace();
				}
				best.visualize(new NetworkNode[0]).save(filename.substring(0, filename.length() - 3));
			}
			NetworkEvaluator.VideoMaker.runSimulation(best, i + 1);
			NetworkEvaluator.VideoMaker.runSimulation(best, i + 1);
			NetworkEvaluator.VideoMaker.runSimulation(best, i + 1);
		}
	}
	public static double neat(double in) {
		return Math.round(in * 1000.0) / 1000.0;
	}
	public static void bar(double v) {
		// ascii art :D
		double spaces = (v - 100) * 1;
		try {
			System.out.println("\t|" + "-".repeat((int)(Math.round(spaces))) + "#");
		} catch (IllegalArgumentException e) {
			System.out.println("\t| <- " + Math.round(spaces));
		}
	}
}

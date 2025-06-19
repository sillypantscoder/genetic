package com.sillypantscoder.gdgenetic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
	public static final boolean SAVE_NN_FILES = false;
	public static void main(String[] args) {
		// Make our network
		Network network = GeneticAlgorithm.createNetwork();
		ArrayList<Network> networkList = new ArrayList<Network>();
		networkList.add(network);
		// Initial score
		double initialScore = NetworkEvaluator.evaluateNetworksWithoutPrintingData(networkList, 0)[0];
		System.out.println("Initial score is: " + initialScore);
		bar(initialScore);
		// Run a bunch of iterations
		int totalIterations = 35;
		double previousScore = initialScore;
		for (int i = 0; i < totalIterations + 1; i++) {
			// Find average score for this iteration
			System.out.println("Iterations done: " + i + "/" + totalIterations + " (" + neat(((double)(i)/totalIterations)*100.0) + "%)");
			double[] scores = NetworkEvaluator.evaluateNetworksWithoutPrintingData(networkList, i + 1);
			double score = scores[0];
			int stagewidth = (int)(LevelGeneration.generateLevel().getStageWidth() / 0.2);
			System.out.print("\tAverage score is: " + neat(score) + "/" + stagewidth);
			System.out.println(" (" + (score>previousScore ? "+" : "-") + neat(Math.abs(score - previousScore)) + " from last; " +
				(score>initialScore ? "+" : "-") + neat(Math.abs(score - initialScore)) + " from first)");
			bar(score);
			System.out.println("\tMaximum score is: " + neat(scores[1]) + "/" + stagewidth);
			bar(scores[1]);
			previousScore = score;
			// Run the iteration
			if (i == totalIterations) continue;
			try {
				networkList = GeneticAlgorithm.runOneIteration(networkList, i + 1);
			} catch (Exception e) {
				Utils.log("ERROR!!!!! Iteration " + (i + 1) + " failed!");
				Utils.logError(e);
			}
			// Find which network is best
			Network best = GeneticAlgorithm.purgeNetworkList(networkList, 1, i + 1).get(0);
			if (SAVE_NN_FILES || i == totalIterations - 1 || (i + 1) % 25 == 0) {
				String filename = "outputs/network" + (i + 1);
				while (new File(filename + ".txt").exists()) filename += "_";
				try {
					BufferedWriter writer = new BufferedWriter(new FileWriter(filename + ".txt"));
					writer.write(best.save());
					writer.close();
					System.out.println("\t[AI saved to file: " + filename + ".txt]");
				} catch (IOException e) {
					System.out.println("WARNING!!! AI FAILED TO SAVE:");
					e.printStackTrace();
				}
				best.visualize(new NetworkNode[0]).save(filename);
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
		double spaces = (v - 40) * 1;
		try {
			System.out.println("\t|" + "-".repeat((int)(Math.round(spaces))) + "#");
		} catch (IllegalArgumentException e) {
			System.out.println("\t| <- " + Math.round(spaces));
		}
	}
}

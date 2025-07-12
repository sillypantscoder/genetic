package com.sillypantscoder.gdgenetic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
	public static final String[] LOAD_NETWORKS = new File("./load_networks/").list();
	public static final int totalIterations = 1000;
	public static void main(String[] args) {
		InfoWindow window = new InfoWindow();
		window.open("Geometry Dash AI", 500, 600);
		// Make our network
		Network network = GeneticAlgorithm.createNetwork();
		ArrayList<Network> networkList = new ArrayList<Network>();
		networkList.add(network);
		// Load previous networks
		for (String filename : LOAD_NETWORKS) {
			try {
				FileReader reader = new FileReader("./load_networks/" + filename);
				StringBuilder builder = new StringBuilder();
				int data = reader.read();
				while (data != -1) {
					builder.append((char)(data));
					data = reader.read();
				}
				networkList.add(Network.load(builder.toString()));
				reader.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// Initial score
		window.status = "Finding initial score";
		double initialScore = NetworkEvaluator.evaluateNetworksWithoutPrintingData(networkList)[0];
		System.out.println("Initial score is: " + initialScore);
		bar(initialScore);
		// Run a bunch of iterations
		int i = 0; double previousScore = initialScore;
		for (; i < totalIterations; i++) {
			window.iterationsDone = i;
			System.out.println("Iterations done: " + i + "/" + totalIterations + " (" + neat(((double)(i)/totalIterations)*100.0) + "%) - Starting iteration " + (i + 1));
			// Find average score for this iteration
			window.status = "Finding average score before this iteration";
			previousScore = showAverageAndMaximumPoolScores(networkList, previousScore, initialScore);
			// Run the iteration
			try {
				networkList = GeneticAlgorithm.runOneIteration(networkList, window);
			} catch (Exception e) {
				Utils.log("ERROR!!!!! Iteration " + (i + 1) + " failed!");
				Utils.logError(e);
			}
			window.status = "Saving networks";
			// Save networks
			for (Network n : networkList) {
				String filename = "outputs/network" + n.generations + "_";
				int fi = 1;
				while (new File(filename + fi + ".txt").exists()) fi += 1;
				try {
					BufferedWriter writer = new BufferedWriter(new FileWriter(filename + fi + ".txt"));
					writer.write(n.save());
					writer.close();
					System.out.println("\t[AI saved to file: " + filename + fi + ".txt]");
				} catch (IOException e) {
					System.out.println("WARNING!!! AI FAILED TO SAVE:");
					e.printStackTrace();
				}
				// n.visualize(new NetworkNode[0]).save(filename);
			}
			// End?
			if (window.stopSoon) break;
		}
		// End
		System.out.println("Iterations done: " + i + "/" + totalIterations + " (" + neat(((double)(i)/totalIterations)*100.0) + "%)");
		// Find average score at end
		window.status = "Finding average score";
		showAverageAndMaximumPoolScores(networkList, previousScore, initialScore);
		window.status = "Done";
		window.close();
	}
	public static double showAverageAndMaximumPoolScores(ArrayList<Network> networkList, double previousScore, double initialScore) {
		double[] scores = NetworkEvaluator.evaluateNetworksWithoutPrintingData(networkList);
		double score = scores[0];
		int stagewidth = (int)(LevelGeneration.generateLevel().getStageWidth() / 0.2);
		System.out.print("\tAverage score is: " + neat(score) + "/" + stagewidth);
		System.out.println(" (" + (score>previousScore ? "+" : "-") + neat(Math.abs(score - previousScore)) + " from last; " +
			(score>initialScore ? "+" : "-") + neat(Math.abs(score - initialScore)) + " from first)");
		bar(score);
		System.out.println("\tMaximum score is: " + neat(scores[1]) + "/" + stagewidth);
		bar(scores[1]);
		return score;
	}
	public static double neat(double in) {
		return Math.round(in * 1000.0) / 1000.0;
	}
	public static void bar(double v) {
		// ascii art :D
		double spaces = (v - 30) * 0.5;
		try {
			System.out.println("\t|" + "-".repeat((int)(Math.round(spaces))) + "#");
		} catch (IllegalArgumentException e) {
			System.out.println("\t| <- " + Math.round(spaces));
		}
	}
}

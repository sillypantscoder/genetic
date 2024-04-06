package com.sillypantscoder.gdgenetic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class GeneticAlgorithm {
	public static void main(String[] args) {
		// Make a network
		Network network = Network.createZeroLayer(5*4 * 5*4 * 3); // 5*4: Height     5*4: Width    3: Pixel depth
		// Put it in a set by itself
		ArrayList<Network> startingNetworks = new ArrayList<Network>();
		startingNetworks.add(network);
		System.out.println("Original score: " + NetworkEvaluator.evaluateNetworks(startingNetworks));
		// And test it!
		ArrayList<Network> betterNetworks = runOneIteration(startingNetworks);
		// See if our better networks do better than our original
		System.out.println("Better score: " + NetworkEvaluator.evaluateNetworks(betterNetworks));
		// Run another iteration!
		ArrayList<Network> evenBetterNetworks = runOneIteration(betterNetworks);
		System.out.println("Even better score: " + NetworkEvaluator.evaluateNetworks(evenBetterNetworks));
	}
	public static Network alterNetwork(Network input) {
		Network output = input.copy();
		// Get a list of all the connections
		HashSet<Connection> connections = output.getConnections();
		// Randomly select some
		ArrayList<Connection> connectionList = new ArrayList<>(connections);
		Random r = new Random();
		for (int i = 0; i < 20; i++) {
			// For each randomly chosen connection:
			int randomIndex = r.nextInt(connectionList.size());
			Connection chosen = connectionList.get(randomIndex);
			// Alter it
			if (r.nextBoolean()) {
				chosen.value += 0.1;
			} else {
				chosen.value -= 0.1;
			}
			// Make sure the resulting value is within the valid range
			if (chosen.value >= 1) chosen.value = 1;
			if (chosen.value <= -1) chosen.value = -1;
		}
		// Finish
		return output;
	}
	public static ArrayList<Network> splitNetwork(Network input) {
		ArrayList<Network> results = new ArrayList<Network>();
		results.add(input);
		for (int i = 0; i < 5; i++) {
			results.add(alterNetwork(input));
		}
		return results;
	}
	public static ArrayList<Network> splitNetworkList(ArrayList<Network> inputs) {
		ArrayList<Network> results = new ArrayList<Network>();
		for (int i = 0; i < inputs.size(); i++) {
			results.addAll(splitNetwork(inputs.get(i)));
		}
		return results;
	}
	public static ArrayList<Network> purgeNetworkList(ArrayList<Network> networks) {
		ArrayList<Double> scores = new ArrayList<Double>();
		double totalScore = 0;
		for (int i = 0; i < networks.size(); i++) {
			double score = NetworkEvaluator.evaluateNetwork(networks.get(i));
			scores.add(score);
			totalScore += score;
		}
		double avgScore = totalScore / networks.size();
		ArrayList<Network> goodNetworks = new ArrayList<Network>();
		for (int i = 0; i < networks.size(); i++) {
			if (scores.get(i) > avgScore) {
				goodNetworks.add(networks.get(i));
			}
		}
		return goodNetworks;
	}
	public static ArrayList<Network> runOneIteration(ArrayList<Network> networks) {
		System.out.print("[" + networks.size());
		// 1. Randomly alter all the networks
		ArrayList<Network> splitList = splitNetworkList(networks);
		System.out.print(" -> " + splitList.size());
		// 2. Evaluate the networks
		ArrayList<Network> goodNetworks = purgeNetworkList(splitList);
		System.out.println(" -> " + goodNetworks.size() + "]");
		return goodNetworks;
	}
}

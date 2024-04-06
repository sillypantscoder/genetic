package com.sillypantscoder.gdgenetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class GeneticAlgorithm {
	public static final int N_MODIFICATIONS_PER_ALTER = 10;
	public static final int N_SPLITS_PER_NETWORK = 40;
	public static final int N_BEST_NETWORKS = 30;
	public static void main(String[] args) {
		// Make a network
		Network network = createNetwork();
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
	public static Network createNetwork() {
		// Create a compatible network.
		return Network.createZeroLayer(5 * 5 * 3); // 5: Height     5: Width    3: Pixel depth
	}
	public static Network alterNetwork(Network input) {
		Network output = input.copy();
		// Get a list of all the connections
		HashSet<Connection> connections = output.getConnections();
		// Randomly select some
		ArrayList<Connection> connectionList = new ArrayList<>(connections);
		Random r = new Random();
		for (int i = 0; i < N_MODIFICATIONS_PER_ALTER; i++) {
			// For each randomly chosen connection:
			int randomIndex = r.nextInt(connectionList.size());
			Connection chosen = connectionList.get(randomIndex);
			// Alter it
			if (r.nextBoolean()) {
				chosen.value += Math.random();
			} else {
				chosen.value -= Math.random();
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
		for (int i = 0; i < N_SPLITS_PER_NETWORK; i++) {
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
	public static ArrayList<Network> purgeNetworkList(ArrayList<Network> networks, int number) {
		HashMap<Network, Double> scores = new HashMap<Network, Double>();
		for (int i = 0; i < networks.size(); i++) {
			double score = NetworkEvaluator.evaluateNetwork(networks.get(i));
			scores.put(networks.get(i), score);
			if (i > 0 && i % 100 == 0) System.out.print(" [@" + i + "]");
		}
		ArrayList<Network> sorted = new ArrayList<Network>(networks);
		sorted.sort(new Comparator<Network>() {
			@Override
			public int compare(Network n1, Network n2) {
				// -1 = n1 is less than n2, 1 = n1 is greater than n2, 0 = equal
				if (scores.get(n1) < scores.get(n2)) return -1;
				if (scores.get(n2) < scores.get(n1)) return 1;
				return 0;
			}
		});
		Collections.reverse(sorted);
		try {
			return new ArrayList<Network>(sorted.subList(0, number));
		} catch (IndexOutOfBoundsException e) {
			return new ArrayList<Network>(sorted);
		}
	}
	public static ArrayList<Network> runOneIteration(ArrayList<Network> networks) {
		System.out.print("\t[" + networks.size());
		// 1. Randomly alter all the networks
		ArrayList<Network> splitList = splitNetworkList(networks);
		System.out.print(" -> " + splitList.size());
		// 2. Evaluate the networks
		ArrayList<Network> goodNetworks = purgeNetworkList(splitList, N_BEST_NETWORKS);
		System.out.println(" -> " + goodNetworks.size() + "]");
		return goodNetworks;
	}
}

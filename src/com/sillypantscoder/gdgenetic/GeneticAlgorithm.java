package com.sillypantscoder.gdgenetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

public class GeneticAlgorithm {
	public static final int N_MODIFICATIONS_PER_ALTER = 2;
	public static final int N_SPLITS_PER_NETWORK = 80;
	public static final int N_BEST_NETWORKS = 5;
	public static void main(String[] args) {
		// Make a network
		Network network = createNetwork();
		// Put it in a set by itself
		ArrayList<Network> startingNetworks = new ArrayList<Network>();
		startingNetworks.add(network);
		System.out.println("Original score: " + NetworkEvaluator.evaluateNetworksWithoutPrintingData(startingNetworks, 0));
		// And test it!
		ArrayList<Network> betterNetworks = runOneIteration(startingNetworks, 0);
		// See if our better networks do better than our original
		System.out.println("Better score: " + NetworkEvaluator.evaluateNetworksWithoutPrintingData(betterNetworks, 0));
		// Run another iteration!
		ArrayList<Network> evenBetterNetworks = runOneIteration(betterNetworks, 0);
		System.out.println("Even better score: " + NetworkEvaluator.evaluateNetworksWithoutPrintingData(evenBetterNetworks, 0));
	}
	public static Network createNetwork() {
		// Create a compatible network.
		return Network.createZeroLayer(5 * 5 * 4 * 4 * 5); // 5: Height  5: Width  4: Block height  4: Block width  5: Pixel depth
	}
	public static Network alterNetwork(Network input) {
		Network currentNetwork = input.copy();
		Random r = new Random();
		// Create a new node
		RegularNode newNode = new RegularNode(1);
		// Get a list of all the nodes
		ArrayList<NetworkNode> nodes = new ArrayList<NetworkNode>(currentNetwork.getNodes());
		nodes.add(newNode); nodes.add(newNode);
		// Randomly select some
		for (int i = 0; i < N_MODIFICATIONS_PER_ALTER; i++) {
			// Select a random regular node (this will be the connection target)
			ArrayList<RegularNode> regularNodes = Utils.getRegularNodes(nodes);
			int randomIndex = r.nextInt(regularNodes.size());
			RegularNode chosen = regularNodes.get(randomIndex);
			// Find another random node (this will be the connection source)
			int randomIndex2 = r.nextInt(nodes.size());
			NetworkNode chosen2 = nodes.get(randomIndex2);
			// Make sure we are not creating a loop
			if (chosen.order <= chosen2.getOrder()) continue;
			// Connect the two nodes
			chosen.connections.add(new Connection(chosen2, r.nextBoolean() ? 1 : -1));
		}
		// Finish
		return currentNetwork;
	}
	public static Optional<Network> alterNetwork2(Network input) {
		Network currentNetwork = input.copy();
		Random r = new Random();
		// Get rid of some random connections
		ArrayList<Connection> connections = new ArrayList<Connection>(currentNetwork.getConnections());
		if (connections.size() <= 3) return Optional.empty();
		for (int i = 0; i < 1; i++) {
			int randomIndex = r.nextInt(connections.size());
			Connection chosen = connections.get(randomIndex);
			chosen.remove(currentNetwork);
		}
		// Finish
		return Optional.ofNullable(currentNetwork);
	}
	public static ArrayList<Network> splitNetwork(Network input) {
		ArrayList<Network> results = new ArrayList<Network>();
		results.add(input);
		for (int i = 0; i < N_SPLITS_PER_NETWORK; i++) {
			results.add(alterNetwork(input));
			alterNetwork2(input).ifPresent((v) -> results.add(v));
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
	public static ArrayList<Network> purgeNetworkList(ArrayList<Network> networks, int amtNetworksToKeep, int filename) {
		ThreadedNetworkEvaluator evaluator = new ThreadedNetworkEvaluator(networks, filename);
		evaluator.executeThreads(true);
		HashMap<Network, Double> scores = evaluator.results;
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
			return new ArrayList<Network>(sorted.subList(0, amtNetworksToKeep));
		} catch (IndexOutOfBoundsException e) {
			return new ArrayList<Network>(sorted);
		}
	}
	public static ArrayList<Network> runOneIteration(ArrayList<Network> networks, int filename) {
		System.out.print("\t[" + networks.size());
		// 1. Randomly alter all the networks
		ArrayList<Network> splitList = splitNetworkList(networks);
		System.out.println(" -> " + splitList.size() + "...");
		// 2. Evaluate the networks
		ArrayList<Network> goodNetworks = purgeNetworkList(splitList, N_BEST_NETWORKS, filename);
		System.out.println("\n\t... -> " + goodNetworks.size() + "]");
		return goodNetworks;
	}
}

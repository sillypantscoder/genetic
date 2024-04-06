package com.sillypantscoder.gdgenetic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class GeneticAlgorithm {
	public static void main(String[] args) {
		Network network = Network.createZeroLayer(4*4 * 5*4 * 3);
		//                      4*4: Height     5*4: Width    3: Pixel depth
	}
	public static Network alterNetwork(Network input) {
		Network output = input.copy();
		// Get a list of all the connections
		HashSet<Connection> connections = output.getConnections();
		// Randomly select one
		ArrayList<Connection> connectionList = new ArrayList<>(connections);
		int randomIndex = new Random().nextInt(connectionList.size());
		Connection chosen = connectionList.get(randomIndex);
		// And alter it
		if (new Random().nextBoolean()) {
			chosen.value += 0.1;
		} else {
			chosen.value -= 0.1;
		}
		// Make sure the resulting value is within the valid range
		if (chosen.value >= 1) chosen.value = 1;
		if (chosen.value <= -1) chosen.value = -1;
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
}

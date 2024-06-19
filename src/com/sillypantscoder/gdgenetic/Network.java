package com.sillypantscoder.gdgenetic;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.function.Function;

public class Network {
	public ArrayList<InputNode> inputs;
	public ArrayList<NetworkNode> outputs;
	public Network(ArrayList<InputNode> inputs, ArrayList<NetworkNode> outputs) {
		this.inputs = inputs;
		this.outputs = outputs;
	}
	public static void main(String[] args) {
		Network n = Network.createZeroLayer(3);
		HashSet<Connection> c = n.outputs.get(0).getConnections();
		c.iterator().next().value = 1;
		double r = n.evaluate(Utils.makeAL(new Double[] {
			1.0, 0.0, 1.0
		})).get(0);
		System.out.println(r);
		System.out.println(n.save());
		System.out.println("\n" + Network.load(n.save()).save());
	}
	public ArrayList<Double> evaluate(ArrayList<Double> inputs) {
		for (int i = 0; i < inputs.size(); i++) {
			this.inputs.get(i).value = Optional.ofNullable(inputs.get(i));
		}
		ArrayList<Double> results = Utils.map(outputs, e -> e.getValue());
		return results;
	}
	public String save() {
		ArrayList<String> results = new ArrayList<String>();
		// A list of all the node IDs. These will be used to ensure there are no duplicate nodes.
		HashMap<NetworkNode, Integer> ids = new HashMap<NetworkNode, Integer>();
		// Get a list of all the nodes
		HashSet<NetworkNode> nodes = new HashSet<NetworkNode>();
		for (int i = 0; i < inputs.size(); i++) {
			nodes.addAll(inputs.get(i).getNodes());
		}
		for (int i = 0; i < outputs.size(); i++) {
			nodes.addAll(outputs.get(i).getNodes());
		}
		// Save each one
		Function<NetworkNode, Integer> getNodeID = node -> {
			if (ids.containsKey(node)) {
				return ids.get(node);
			} else {
				int id = ids.size() + 1;
				ids.put(node, id);
				return id;
			}
		};
		for (NetworkNode node : nodes) {
			// 1. Get the ID of this node
			String savedNode = getNodeID.apply(node) + "";
			// 2. Get the IDs of the connected nodes
			if (node instanceof RegularNode rnode) {
				if (outputs.contains(node)) savedNode = "O" + savedNode;
				else savedNode = "R" + savedNode;
				for (Connection c : rnode.connections) {
					savedNode += "|" + c.value + "%" + getNodeID.apply(c.connection);
				}
			} else {
				savedNode = "I" + savedNode;
			}
			// 4. Finish!
			results.add(savedNode);
		}
		return String.join("\n", results);
	}
	public static Network load(String info) {
		String[] cells = info.split("\n");
		HashMap<Integer, NetworkNode> nodes = new HashMap<Integer, NetworkNode>();
		ArrayList<InputNode> inputs = new ArrayList<InputNode>();
		ArrayList<NetworkNode> outputs = new ArrayList<NetworkNode>();
		// First get the nodes
		// Connections must be done separately to ensure the node references are correct
		for (String savedNode : cells) {
			// First get the ID
			int nodeID = Integer.parseInt(savedNode.split("\\|")[0].substring(1));
			// First character is node type
			// I = input node, R = regular node, O = regular output node
			if (savedNode.charAt(0) == 'I') {
				InputNode node = new InputNode();
				nodes.put(nodeID, node);
				inputs.add(node);
			} else {
				RegularNode node = new RegularNode(Utils.getNextFreeOrder(nodes.values()));
				nodes.put(nodeID, node);
				if (savedNode.charAt(0) == 'O') {
					outputs.add(node);
					node.order += 10000;
				}
			}
		}
		// Then add the connections
		for (String savedNode : cells) {
			int nodeID = Integer.parseInt(savedNode.split("\\|")[0].substring(1));
			NetworkNode _node = nodes.get(nodeID);
			if (_node instanceof RegularNode node) {
				// This is a node we need to add connections to
				String[] connections = savedNode.split("\\|");
				for (int i = 1; i < connections.length; i++) {
					String data = connections[i];
					double value = Double.parseDouble(data.split("%")[0]);
					int otherID = Integer.parseInt(data.split("%")[1]);
					Connection o = new Connection(nodes.get(otherID), value);
					node.connections.add(o);
				}
			}
		}
		return new Network(inputs, outputs);
	}
	public Network copy() {
		return Network.load(save());
	}
	// public void prune() {
	// 	// Find duplicate connections (i.e. two connections between the same two nodes with opposite values)
	// 	ArrayList<NetworkNode> nodes = new ArrayList<NetworkNode>(getNodes());
	// 	for (int i = 0; i < nodes.size(); i++) {
	// 		if (nodes.get(i) instanceof RegularNode node) {
	// 			ArrayList<NetworkNode> positiveNodes = new ArrayList<NetworkNode>();
	// 			ArrayList<NetworkNode> negativeNodes = new ArrayList<NetworkNode>();
	// 			for (int j = 0; j < node.connections.size(); j++) {
	// 				if (node.connections.get(j).value >= 0) {
	// 					if (positiveNodes.contains())
	// 				}
	// 			}
	// 		}
	// 	}
	// }
	public HashSet<Connection> getConnections() {
		HashSet<Connection> result = new HashSet<Connection>();
		for (int i = 0; i < outputs.size(); i++) {
			result.addAll(outputs.get(i).getConnections());
		}
		return result;
	}
	public HashSet<NetworkNode> getNodes() {
		HashSet<NetworkNode> nodes = new HashSet<NetworkNode>();
		for (int i = 0; i < inputs.size(); i++) {
			nodes.addAll(inputs.get(i).getNodes());
		}
		for (int i = 0; i < outputs.size(); i++) {
			nodes.addAll(outputs.get(i).getNodes());
		}
		return nodes;
	}
	public Surface visualize(NetworkNode[] highlighted) {
		// Find all the layers
		ArrayList<ArrayList<NetworkNode>> layers = new ArrayList<ArrayList<NetworkNode>>();
		// Input nodes
		ArrayList<NetworkNode> inputNodes = new ArrayList<NetworkNode>();
		inputNodes.addAll(this.inputs);
		layers.add(inputNodes);
		// All other nodes
		ArrayList<NetworkNode> regularNodes = new ArrayList<NetworkNode>(this.getNodes());
		regularNodes.removeAll(inputNodes);
		int maxOrder = 1; for (int i = 0; i < regularNodes.size(); i++) { maxOrder = Math.max(maxOrder, regularNodes.get(i).getOrder()); }
		for (int i = 1; i <= maxOrder; i++) {
			ArrayList<NetworkNode> layer = new ArrayList<NetworkNode>();
			for (int j = 0; j < regularNodes.size(); j++) {
				if (regularNodes.get(j).getOrder() == i) {
					layer.add(regularNodes.get(j));
				}
			}
			if (layer.size() > 0) layers.add(layer);
		}
		// Find maximum nodes per layer
		int maxNodes = 0;
		for (int i = 0; i < layers.size(); i++) {
			maxNodes = Math.max(maxNodes, layers.get(i).size());
		}
		// Create the surface
		Surface s = new Surface((layers.size() * 140) + 10, (maxNodes * 15) + 10, Color.WHITE);
		// Find where all the nodes are
		HashMap<NetworkNode, Integer> nodeXs = new HashMap<NetworkNode, Integer>();
		HashMap<NetworkNode, Integer> nodeYs = new HashMap<NetworkNode, Integer>();
		for (int layer = 0; layer < layers.size(); layer++) {
			int x = (layer * 140) + 20;
			for (int c = 0; c < layers.get(layer).size(); c++) {
				double frac = (double)(c + 1) / (layers.get(layer).size() + 2);
				int y = (int)((frac * maxNodes) * 15) + 10;
				NetworkNode node = layers.get(layer).get(c);
				nodeXs.put(node, x);
				nodeYs.put(node, y);
			}
		}
		// Draw the connections
		for (int layer = 0; layer < layers.size(); layer++) {
			for (int c = 0; c < layers.get(layer).size(); c++) {
				NetworkNode node = layers.get(layer).get(c);
				if (node instanceof RegularNode rnode) {
					for (int connection = 0; connection < rnode.connections.size(); connection++) {
						Connection co = rnode.connections.get(connection);
						Color color = co.value == 0 ? Color.BLACK : (co.value > 0 ? Color.RED : Color.BLUE);
						s.drawLine(color, nodeXs.get(node), nodeYs.get(node), nodeXs.get(co.connection), nodeYs.get(co.connection), 3);
					}
				}
			}
		}
		// Draw the nodes
		for (int layer = 0; layer < layers.size(); layer++) {
			for (int c = 0; c < layers.get(layer).size(); c++) {
				NetworkNode node = layers.get(layer).get(c);
				Color color = (node instanceof InputNode) ? Color.ORANGE : (this.outputs.contains(node) ? Color.GREEN : Color.BLUE.brighter());
				Color outerColor = (Arrays.asList(highlighted).contains(node)) ? Color.RED : Color.BLACK;
				s.drawCircle(outerColor, nodeXs.get(node), nodeYs.get(node), 5);
				s.drawCircle(color, nodeXs.get(node), nodeYs.get(node), 4);
			}
		}
		return s;
	}
	/**
	 * Create a network with no initial connections, no hidden nodes, the specified number of inputs and one output.
	 * @param inputSize The number of inputs.
	 * @return The generated network.
	 */
	public static Network createZeroLayer(int inputSize) {
		ArrayList<InputNode> inputs = new ArrayList<InputNode>();
		for (int i = 0; i < inputSize; i++) inputs.add(new InputNode());
		RegularNode output = new RegularNode(10000);
		// for (int i = 0; i < inputs.size(); i++) {
		// 	output.connections.add(new Connection(inputs.get(i), -1.0));
		// }
		Network result = new Network(inputs, Utils.makeAL(new NetworkNode[] { output }));
		return result;
	}
	public int getNextFreeOrder() {
		int order = 1;
		while (true) {
			boolean found = false;
			ArrayList<RegularNode> nodes = Utils.getRegularNodes(new ArrayList<NetworkNode>(this.getNodes()));
			for (int i = 0; i < nodes.size(); i++) {
				if (nodes.get(i).order == order) {
					found = true;
					break;
				}
			}
			if (!found) break;
			order++;
		}
		return order;
	}
}

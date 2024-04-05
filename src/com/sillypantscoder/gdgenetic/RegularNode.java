package com.sillypantscoder.gdgenetic;

import java.util.ArrayList;
import java.util.HashSet;

public class RegularNode extends NetworkNode {
	public ArrayList<Connection> connections;
	public RegularNode() {
		connections = new ArrayList<Connection>();
	}
	public RegularNode(ArrayList<Connection> connections) {
		this.connections = connections;
	}
	public double getValue() {
		ArrayList<Double> values = Utils.map(connections, e -> e.getValue());
		double sum = 0.0;
		for (Double value : values) {
			sum += value;
		}
		return values.isEmpty() ? 0 : sum / values.size();
	}
	public HashSet<Connection> getConnections() {
		ArrayList<Connection> connections = new ArrayList<Connection>();
		for (int i = 0; i < this.connections.size(); i++) {
			connections.addAll(this.connections.get(i).getConnections());
		}
		// Remove duplicates
		HashSet<Connection> set = new HashSet<Connection>(connections);
		return set;
	}
}

package com.sillypantscoder.gdgenetic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class RegularNode extends NetworkNode {
	public ArrayList<Connection> connections;
	public RegularNode() {
		connections = new ArrayList<Connection>();
	}
	public double getValue() {
		ArrayList<Double> values = new ArrayList<Double>();
		for (int i = 0; i < connections.size(); i++) {
			values.add(connections.get(i).getValue());
		}
		double sum = 0.0;
		for (Double value : values) {
			sum += value;
		}
		return values.isEmpty() ? 0 : sum / values.size();
	}
	public ArrayList<Connection> getConnections() {
		ArrayList<Connection> connections = new ArrayList<Connection>();
		for (int i = 0; i < this.connections.size(); i++) {
			connections.addAll(this.connections.get(i).getConnections());
		}
		// Remove duplicates
		Set<Connection> set = new HashSet<Connection>(connections);
		connections.clear();
		connections.addAll(set);
		return connections;
	}
}

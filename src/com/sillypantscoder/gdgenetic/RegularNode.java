package com.sillypantscoder.gdgenetic;

import java.util.ArrayList;
import java.util.HashSet;

public class RegularNode extends NetworkNode {
	public ArrayList<Connection> connections;
	public int order;
	public RegularNode(int order) {
		connections = new ArrayList<Connection>();
		this.order = order;
	}
	public RegularNode(ArrayList<Connection> connections, int order) {
		this.connections = connections;
		this.order = order;
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
	public int getOrder() {
		return order;
	}
}

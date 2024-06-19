package com.sillypantscoder.gdgenetic;

import java.util.ArrayList;
import java.util.HashSet;

public class Connection {
	public NetworkNode connection;
	public double value;
	public Connection(NetworkNode to, double value) {
		this.connection = to;
		this.value = value;
	}
	public HashSet<Connection> getConnections() {
		HashSet<Connection> c = connection.getConnections();
		c.add(this);
		return c;
	}
	public double getValue() {
		return this.connection.getValue() * this.value;
	}
	public void remove(Network network) {
		ArrayList<NetworkNode> nodes = new ArrayList<NetworkNode>(network.getNodes());
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.get(i) instanceof RegularNode rnode) {
				if (rnode.connections.contains(this)) rnode.connections.remove(this);
			}
		}
	}
}

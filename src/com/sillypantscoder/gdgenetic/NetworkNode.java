package com.sillypantscoder.gdgenetic;

import java.util.HashSet;

public abstract class NetworkNode {
	public abstract double getValue();
	public HashSet<Connection> getConnections() {
		return new HashSet<Connection>();
	}
	public HashSet<NetworkNode> getNodes() {
		HashSet<NetworkNode> result = new HashSet<NetworkNode>();
		result.add(this);
		for (Connection connection : getConnections()) {
			result.addAll(connection.connection.getNodes());
		}
		return result;
	}
}

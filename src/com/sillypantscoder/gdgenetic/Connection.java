package com.sillypantscoder.gdgenetic;

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
}

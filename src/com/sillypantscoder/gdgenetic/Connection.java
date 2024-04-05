package com.sillypantscoder.gdgenetic;

import java.util.ArrayList;

public class Connection {
	public NetworkNode connection;
	public double value;
	public Connection(NetworkNode to, double value) {
		this.connection = to;
		this.value = value;
	}
	public ArrayList<Connection> getConnections() {
		ArrayList<Connection> c = connection.getConnections();
		c.add(this);
		return c;
	}
	public double getValue() {
		return this.connection.getValue() * this.value;
	}
}

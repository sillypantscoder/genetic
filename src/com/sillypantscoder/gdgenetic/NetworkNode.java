package com.sillypantscoder.gdgenetic;

import java.util.ArrayList;

public abstract class NetworkNode {
	public abstract double getValue();
	public ArrayList<Connection> getConnections() {
		return new ArrayList<Connection>();
	}
}

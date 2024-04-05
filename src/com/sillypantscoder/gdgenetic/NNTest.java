package com.sillypantscoder.gdgenetic;

import java.util.ArrayList;

public class NNTest {
	public static void main(String[] args) {
		Network n = Network.createZeroLayer(3);
		ArrayList<Connection> c = n.outputs.get(0).getConnections();
		c.get(0).value = 1;
		double r = n.evaluate(Utils.makeAL(new Double[] {
			1.0, 0.0, 1.0
		})).get(0);
		System.out.println(r);
	}
}

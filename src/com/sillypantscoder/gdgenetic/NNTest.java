package com.sillypantscoder.gdgenetic;

import java.util.HashSet;

public class NNTest {
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
}

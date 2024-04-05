package com.sillypantscoder.gdgenetic;

import java.util.ArrayList;
import java.util.Optional;

public class Network {
	public ArrayList<InputNode> inputs;
	public ArrayList<NetworkNode> outputs;
	public Network(ArrayList<InputNode> inputs, ArrayList<NetworkNode> outputs) {
		this.inputs = inputs;
		this.outputs = outputs;
	}
	public ArrayList<Double> evaluate(ArrayList<Double> inputs) {
		for (int i = 0; i < inputs.size(); i++) {
			this.inputs.get(i).value = Optional.ofNullable(inputs.get(i));
		}
		ArrayList<Double> results = new ArrayList<Double>();
		for (int i = 0; i < outputs.size(); i++) {
			results.add(outputs.get(i).getValue());
		}
		return results;
	}
	/**
	 * Create a network with zero hidden layers and one output.
	 * @param inputSize The number of inputs.
	 * @return The generated network.
	 */
	public static Network createZeroLayer(int inputSize) {
		ArrayList<InputNode> inputs = new ArrayList<InputNode>();
		for (int i = 0; i < inputSize; i++) inputs.add(new InputNode());
		RegularNode output = new RegularNode();
		for (int i = 0; i < inputs.size(); i++) {
			output.connections.add(new Connection(inputs.get(i), 0.0));
		}
		Network result = new Network(inputs, Utils.makeAL(new NetworkNode[] { output }));
		return result;
	}
}

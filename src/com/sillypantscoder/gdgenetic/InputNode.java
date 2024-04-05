package com.sillypantscoder.gdgenetic;

import java.util.Optional;

public class InputNode extends NetworkNode {
	public Optional<Double> value;
	public double getValue() {
		return value.orElse(0.0);
	}
}

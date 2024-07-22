package com.sillypantscoder.gdgenetic;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

public class Utils {
	public static<T> ArrayList<T> makeAL(T[] list) {
		ArrayList<T> result = new ArrayList<T>();
		for (int i = 0; i < list.length; i++) {
			result.add(list[i]);
		}
		return result;
	}
	public static<T,E> ArrayList<E> map(ArrayList<T> list, Function<T,E> converter) {
		ArrayList<E> result = new ArrayList<E>();
		for (int i = 0; i < list.size(); i++) {
			result.add(converter.apply(list.get(i)));
		}
		return result;
	}
	public static ArrayList<RegularNode> getRegularNodes(ArrayList<NetworkNode> nodes) {
		ArrayList<RegularNode> result = new ArrayList<RegularNode>();
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.get(i) instanceof RegularNode rnode) {
				result.add(rnode);
			}
		}
		return result;
	}
	public static String join(String sep, Object[] o) {
		ArrayList<String> results = new ArrayList<String>();
		for (int i = 0; i < o.length; i++) {
			results.add(o[i].toString());
		}
		return String.join(sep, results);
	}
	public static void log(Object... o) {
		try {
			FileWriter writer = new FileWriter("log.txt", true);
			writer.write(join(" ", o) + "\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void logWithoutNewline(Object... o) {
		try {
			FileWriter writer = new FileWriter("log.txt", true);
			writer.write(" " + join(" ", o));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void logNetwork(Network network) {
		String result = "Network:\n\tInputs:";
		for (int i = 0; i < network.inputs.size(); i++) {
			result += "\n\t\t- " + network.inputs.get(i).toString();
		}
		result += "\n\tOutputs:";
		for (int i = 0; i < network.outputs.size(); i++) {
			result += logNode(network.outputs.get(i));
		}
		log(result);
	}
	public static String logNode(NetworkNode node) {
		String nodeResult = "\n\t- " + node.toString();
		if (node instanceof RegularNode rnode) {
			for (int i = 0; i < rnode.connections.size(); i++) {
				nodeResult += "\n\t\tConnection (" + rnode.connections.get(i).value + "):";
				try {
					nodeResult += logNode(rnode.connections.get(i).connection).replace("\n", "\n\t");
				} catch (StackOverflowError e) {
					return "";
				}
			}
		}
		return nodeResult;
	}
	public static int getNextFreeOrder(Collection<? extends NetworkNode> nodes) {
		int order = 1;
		while (true) {
			boolean found = false;
			ArrayList<RegularNode> rnodes = Utils.getRegularNodes(new ArrayList<NetworkNode>(nodes));
			for (int i = 0; i < rnodes.size(); i++) {
				if (rnodes.get(i).order == order) {
					found = true;
					break;
				}
			}
			if (!found) break;
			order++;
		}
		return order;
	}
	public static void logError(Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String stack = sw.toString();
		log("Stack Trace:\n" + stack);
	}
	public static String padLeft(String s, String filler, int width) {
		if (s.length() >= width) return s;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < width - s.length(); i++) {
			sb.append(filler);
		}
		sb.append(s);
		return sb.toString();
	}
	public static String padLeft(int s, String filler, int width) {
		return padLeft("" + s, filler, width);
	}
}

package com.sillypantscoder.gdgenetic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadedNetworkEvaluator {
	public static final int maxThreads = 9;
	private ArrayList<Network> networks;
	private ArrayList<Thread> threads;
	public HashMap<Network, Double> results;
	public AtomicInteger threadCount;
	public int filename;
	public ThreadedNetworkEvaluator(ArrayList<Network> networks, int filename) {
		this.networks = networks;
		this.threads = new ArrayList<Thread>();
		this.results = new HashMap<Network, Double>();
		this.createThreads();
		this.threadCount = new AtomicInteger(0);
		this.filename = filename;
	}
	public void createThreads() {
		for (int i = 0; i < this.networks.size(); i++) {
			Thread thread = createThread(i);
			threads.add(thread);
		}
	}
	public Thread createThread(int i) {
		return new Thread(() -> {
			double result = NetworkEvaluator.evaluateNetwork(networks.get(i), filename);
			results.put(networks.get(i), result);
			threadCount.decrementAndGet();
		}, "network-evaluator-" + i);
	}
	public void executeThreads(boolean print) {
		int i = 0;
		if (networks.size() < 20) print = false;
		while (true) {
			while (threadCount.get() < maxThreads) {
				// start new threads
				threads.get(i).start();
				i += 1;
				threadCount.incrementAndGet();
				if (i >= networks.size()) break;
			}
			if (print) printStatus();
			if (i >= networks.size()) break;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (print) printStatus();
		}
		while (threadCount.get() > 0) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (print) printStatus();
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void printStatus() {
		System.out.print("\tNetwork Evaluator: [");
		for (int i = 0; i < threads.size(); i += 3) {
			switch (threads.get(i).getState()) {
				case NEW:
					System.out.print("-");
					break;
				case TERMINATED:
					System.out.print("=");
					break;
				default:
					System.out.print("#");
			}
		}
		System.out.print("]\r");
	}
}

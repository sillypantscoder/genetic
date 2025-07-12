package com.sillypantscoder.gdgenetic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadedNetworkEvaluator {
	public static int maxThreads = 4;
	public static ThreadedNetworkEvaluator lock = null;
	public ArrayList<Network> networks;
	public ArrayList<Thread> threads;
	public HashMap<Network, Double> results;
	public AtomicInteger threadCount;
	public ThreadedNetworkEvaluator(ArrayList<Network> networks) {
		this.networks = networks;
		this.threads = new ArrayList<Thread>();
		for (@SuppressWarnings("unused") Network __ : networks) this.threads.add(null);
		this.results = new HashMap<Network, Double>();
		this.threadCount = new AtomicInteger(0);
	}
	public Runnable getRunnable(int i) {
		return () -> {
			double result = NetworkEvaluator.evaluateNetwork(networks.get(i));
			results.put(networks.get(i), result);
			threadCount.decrementAndGet();
		};
	}
	public void executeThreads(boolean print) {
		// Lock
		while (lock != null) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		lock = this;
		// Start threads
		int i = 0;
		if (networks.size() < 20) print = false;
		while (true) {
			while (threadCount.get() < maxThreads) {
				// start new threads
				Thread thread = new Thread(getRunnable(i), "network-evaluator-" + i);
				threads.set(i, thread);
				thread.start();
				i += 1;
				threadCount.incrementAndGet();
				if (i >= networks.size()) break;
			}
			if (print) printStatus(false);
			if (i >= networks.size()) break;
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (print) printStatus(false);
		}
		while (threadCount.get() > 0) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (print) printStatus(false);
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (print) printStatus(true);
		lock = null;
	}
	public void printStatus(boolean isEnd) {
		final int n_per_row = 110;
		int rows = 0;
		System.out.print("\tNetwork Evaluator: [");
		for (int i = 0; i < threads.size(); i++) {
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
			if ((i + 1) % n_per_row == 0) {
				System.out.print("]\n\t                   [");
				rows += 1;
			}
		}
		System.out.print("]\r");
		if (! isEnd) System.out.print("\u001b[1A".repeat(rows));
	}
}

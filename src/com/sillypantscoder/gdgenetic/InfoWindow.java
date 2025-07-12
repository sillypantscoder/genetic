package com.sillypantscoder.gdgenetic;

import java.awt.Color;

import com.sillypantscoder.geometrydash.CubeMode;
import com.sillypantscoder.windowlib.Surface;
import com.sillypantscoder.windowlib.Window;

public class InfoWindow extends Window {
	public String status = "Starting";
	public int iterationsDone = 0;
	public boolean stopSoon = false;
	public Surface getIcon() {
		Surface cube = CubeMode.getCubeIcon();
		int padding = 5;
		Surface s = new Surface(cube.get_width() + padding + padding, cube.get_height() + padding + padding, Color.BLUE);
		s.blit(cube, padding, padding);
		return s;
	}
	public Surface frame(int width, int height) {
		Surface frame = new Surface(width, height, Color.WHITE);
		// Status
		frame.blit(Surface.renderText(16, "Iterations done: " + iterationsDone + " (" + Main.neat(((double)(iterationsDone)/Main.totalIterations)*100.0) + "%)", Color.BLACK), 0, 0);
		frame.blit(Surface.renderText(16, "Currently on iteration " + (iterationsDone + 1), Color.BLACK), 0, 16);
		frame.blit(Surface.renderText(16, "Status: " + status, Color.BLACK), 0, 32);
		// Max threads
		frame.blit(Surface.renderText(16, "Max threads: " + ThreadedNetworkEvaluator.maxThreads, Color.BLACK), 0, 64);
		// Stop soon
		frame.blit(Surface.renderText(16, "(Q) Stop soon: " + stopSoon, Color.BLACK), 0, 80);
		// Network evaluator info
		if (ThreadedNetworkEvaluator.lock != null) {
			frame.blit(Surface.renderText(16, "Network Evaluator Status:", Color.BLACK), 0, 128);
			int x = 0; int y = 0; final int gridsize = 8;
			for (int i = 0; i < ThreadedNetworkEvaluator.lock.networks.size(); i++) {
				if (ThreadedNetworkEvaluator.lock.threads.get(i) == null) frame.drawRect(Color.GRAY, x * gridsize, 150 + (y * gridsize), gridsize, gridsize);
				else switch (ThreadedNetworkEvaluator.lock.threads.get(i).getState()) {
					case NEW:
						frame.drawRect(Color.BLACK, x * gridsize, 150 + (y * gridsize), gridsize, gridsize);
						break;
					case TERMINATED:
						frame.drawRect(Color.GREEN, x * gridsize, 150 + (y * gridsize), gridsize, gridsize);
						break;
					default:
						frame.drawRect(Color.ORANGE, x * gridsize, 150 + (y * gridsize), gridsize, gridsize);
						break;
				}
				x += 1;
				if (x * gridsize > width) {
					x = 0;
					y += 1;
				}
			}
		}
		// Finish
		return frame;
	}
	public void keyDown(String e) {
		if (e.equals("Up")) ThreadedNetworkEvaluator.maxThreads += 1;
		if (e.equals("Down")) ThreadedNetworkEvaluator.maxThreads -= 1;
		if (e.equals("Q")) this.stopSoon = !this.stopSoon;
	}
	public void keyUp(String e) {}
	public void mouseMoved(int x, int y) {}
	public void mouseDown(int x, int y) {}
	public void mouseUp(int x, int y) {}
	public void mouseWheel(int amount) {}
}

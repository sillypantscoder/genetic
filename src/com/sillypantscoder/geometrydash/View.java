package com.sillypantscoder.geometrydash;

import java.util.ArrayList;
import com.sillypantscoder.geometrydash.tile.Tile;
import com.sillypantscoder.gdgenetic.NetworkEvaluator;

public class View {
	public ArrayList<Tile> tiles;
	public Player player;
	public boolean isPressing = false;
	public boolean hasStartedPressing = false;
	public boolean hasWon = false;
	public boolean hasDied = false;
	public int agentScore = 0;
	public int generationX = 0;
	public View() {
		this.tiles = new ArrayList<Tile>();
		this.player = new Player(this);
	}
	public double getStageHeight() {
		double height = 1;
		for (int i = 0; i < tiles.size(); i++) {
			double tileHeight = tiles.get(i).y + 5;
			if (height < tileHeight) height = tileHeight;
		}
		return height;
	}
	public double getStageWidth() {
		double width = 1;
		for (int i = 0; i < tiles.size(); i++) {
			double tileWidth = tiles.get(i).x + 5;
			if (width < tileWidth) width = tileWidth;
		}
		return width;
	}
	public void tick(double amount) {
		this.player.tick(amount);
		for (int i = 0; i < this.tiles.size(); i++) {
			this.tiles.get(i).tick(amount);
		}
		this.player.finishTick(amount);
	}
	public void timeTick() {
		double n_frames = Math.ceil(Math.abs(this.player.vy * 4) + 1);
		for (int i = 0; i < n_frames; i++) {
			if (this.hasWon || this.hasDied) return;
			this.tick(1 / n_frames);
		}
		// Score
		agentScore += NetworkEvaluator.POINTS_PER_FRAME;
		if (isPressing) agentScore += NetworkEvaluator.JUMP_PENALTY;
		if (isPressing && !player.mode.jumpingHasEffect()) agentScore += NetworkEvaluator.POINTLESS_JUMP_PENALTY;
		// Add more
		if (player.x > generationX - 5) {
			NetworkEvaluator.LevelGeneration.appendRandomStructure(this);
		}
	}
	public void startPressing() {
		this.hasStartedPressing = true;
		this.isPressing = true;
	}
	public void stopPressing() {
		this.hasStartedPressing = false;
		this.isPressing = false;
	}
}

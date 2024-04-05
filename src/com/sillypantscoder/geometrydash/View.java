package com.sillypantscoder.geometrydash;

import java.util.ArrayList;
import com.sillypantscoder.geometrydash.tile.Tile;

public class View {
	public ArrayList<Tile> tiles;
	public Player player;
	public boolean isPressing = false;
	public boolean hasStartedPressing = false;
	public boolean hasWon = false;
	public boolean hasDied = false;
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

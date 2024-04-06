package com.sillypantscoder.gdgenetic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.util.ArrayList;
import java.awt.RenderingHints;
import java.awt.FontMetrics;

public class Surface {
	public BufferedImage img;
	public Surface(int width, int height, Color color) {
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		this.fill(color);
	}
	public Surface(BufferedImage image) {
		img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();
		g2d.drawImage(image, 0, 0, new DummyImageObserver());
		g2d.dispose();
	}
	public void fill(Color color) {
		Graphics2D graphics = img.createGraphics();
		graphics.setPaint(color);
		graphics.fillRect(0, 0, img.getWidth(), img.getHeight());
		graphics.dispose();
	}
	public void blit(Surface other, int x, int y) {
		Graphics2D g2d = img.createGraphics();
		g2d.drawImage(other.img, x, y, new DummyImageObserver());
		g2d.dispose();
	}
	public int get_width() {
		return img.getWidth();
	}
	public int get_height() {
		return img.getHeight();
	}
	public Surface copy() {
		Surface r = new Surface(get_width(), get_height(), Color.BLACK);
		r.blit(this, 0, 0);
		return r;
	}
	public void set_at(int x, int y, Color color) {
		if (x >= 0 && x < img.getWidth() && y >= 0 && y < img.getHeight()) {
			img.setRGB(x, y, color.getRGB());
		}
	}
	public Color get_at(int x, int y) {
		if (x >= 0 && x < img.getWidth() && y >= 0 && y < img.getHeight()) {
			return new Color(img.getRGB(x, y));
		}
		return Color.BLACK;
	}
	public void drawLine(Color color, int x1, int y1, int x2, int y2, int thickness) {
		Graphics2D g2d = img.createGraphics();
		g2d.setColor(color);
		BasicStroke bs = new BasicStroke(thickness);
		g2d.setStroke(bs);
		g2d.drawLine(x1, y1, x2, y2);
		g2d.dispose();
	}
	public void drawRect(Color color, int x, int y, int width, int height) {
		Graphics2D g2d = img.createGraphics();
		g2d.setColor(color);
		g2d.fillRect(x, y, width, height);
		g2d.dispose();
	}
	public void drawRect(Color color, int x, int y, int width, int height, int lineWidth) {
		Graphics2D g2d = img.createGraphics();
		g2d.setColor(color);
		g2d.setStroke(new BasicStroke(lineWidth));
		g2d.drawRect(x, y, width, height);
		g2d.dispose();
	}
	public void writeToFile(String filename) throws IOException {
		File outputfile = new File(filename);
		ImageIO.write(img, "png", outputfile);
	}
	public static Surface renderText(int size, String text, Color color) {
		// Measure the text
		Surface measure = new Surface(1, 1, Color.BLACK);
		Graphics2D big = (Graphics2D)(measure.img.getGraphics());
		FontMetrics fm = big.getFontMetrics();
		Surface ret = new Surface(fm.stringWidth(text), fm.getHeight(), new Color(0, 0, 0, 0));
		// Draw the text
		try {
			Graphics2D g2d = ret.img.createGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setColor(color);
			g2d.drawString(text, 0, fm.getAscent());
			g2d.dispose();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		// Finish
		return ret;
	}
	public static Surface combineVertically(ArrayList<Surface> surfaces, Color background) {
		int width = 1;
		for (int i = 0; i < surfaces.size(); i++) { int w = surfaces.get(i).get_width(); if (w > width) { width = w; } }
		int height = 1;
		for (int i = 0; i < surfaces.size(); i++) { int h = surfaces.get(i).get_height(); height += h; }
		Surface total = new Surface(width, height, background);
		int cum_y = 0;
		for (int i = 0; i < surfaces.size(); i++) {
			total.blit(surfaces.get(i), 0, cum_y);
			cum_y += surfaces.get(i).get_height();
		}
		return total;
	}
	public static class DummyImageObserver implements ImageObserver {
		public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
			return false;
		}
	}
}

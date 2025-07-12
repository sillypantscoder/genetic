package com.sillypantscoder.windowlib;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.util.ArrayList;
import java.awt.RenderingHints;
import java.awt.FontMetrics;

public class Surface {
	public BufferedImage img;
	public Graphics2D g2d;
	public Surface(int width, int height, Color color) {
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		g2d = img.createGraphics();
		this.fill(color);
	}
	public Surface(BufferedImage image) {
		img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		g2d = img.createGraphics();
		g2d.drawImage(image, 0, 0, null);
	}
	public void fill(Color color) {
		g2d.setPaint(color);
		g2d.fillRect(0, 0, img.getWidth(), img.getHeight());
	}
	public void blit(Surface other, int x, int y) {
		g2d.drawImage(other.img, x, y, null);
	}
	public void blit(Surface other, int centerX, int centerY, double rotation) {
		g2d.translate(centerX, centerY);
		g2d.rotate(Math.toRadians(rotation));
		g2d.translate(-centerX, -centerY);
		g2d.drawImage(other.img, centerX - other.get_width() / 2, centerY - other.get_height() / 2, null);
		g2d.dispose(); g2d = img.createGraphics();
	}
	public int get_width() {
		return img.getWidth();
	}
	public int get_height() {
		return img.getHeight();
	}
	public Surface copy() {
		Surface r = new Surface(get_width(), get_height(), new Color(0, 0, 0, 0));
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
		g2d.setColor(color);
		g2d.setStroke(new BasicStroke(thickness));
		g2d.drawLine(x1, y1, x2, y2);
	}
	public void drawRect(Color color, int x, int y, int width, int height) {
		g2d.setColor(color);
		g2d.fillRect(x, y, width, height);
	}
	public void drawRect(Color color, int x, int y, int width, int height, int lineWidth) {
		g2d.setColor(color);
		g2d.setStroke(new BasicStroke(lineWidth));
		g2d.drawRect(x, y, width, height);
	}
	public void drawEllipse(Color color, int cx, int cy, int rx, int ry) {
		g2d.setColor(color);
		g2d.fillOval(cx - rx, cy - ry, rx*2, ry*2);
	}
	public void drawEllipse(Color color, int cx, int cy, int rx, int ry, int lineWidth) {
		g2d.setColor(color);
		g2d.setStroke(new BasicStroke(lineWidth));
		g2d.drawOval(cx - rx, cy - ry, rx*2, ry*2);
	}
	public void drawCircle(Color color, int cx, int cy, int r) {
		this.drawEllipse(color, cx, cy, r, r);
	}
	public void drawCircle(Color color, int cx, int cy, int r, int lineWidth) {
		this.drawEllipse(color, cx, cy, r, r, lineWidth);
	}
	public void drawCircle(Color color, double cx, double cy, double r) {
		drawCircle(color, (int)(cx), (int)(cy), (int)(r));
	}
	public void drawCircle(Color color, double cx, double cy, double r, int lineWidth) {
		drawCircle(color, (int)(cx), (int)(cy), (int)(r), lineWidth);
	}
	public void eraseCircle(int cx, int cy, int r) {
		g2d.setColor(new Color(0, 0, 0, 0));
		g2d.setComposite(AlphaComposite.Clear);
		g2d.fillOval(cx - r, cy - r, r*2, r*2);
	}
	public void drawArc(Color color, double x, double y, double radius, double degStart, double degEnd) {
		g2d.setColor(color);
		g2d.fillArc((int)(x - radius), (int)(y - radius), (int)(radius*2), (int)(radius*2), 90 - (int)(degStart), -(int)(degEnd - degStart));
	}
	public void drawPolygon(Color color, int[][] points) {
		g2d.setColor(color);
		int[] xPoints = new int[points.length];
		int[] yPoints = new int[points.length];
		for (int i = 0; i < points.length; i++) {
			xPoints[i] = points[i][0];
			yPoints[i] = points[i][1];
		}
		g2d.fillPolygon(xPoints, yPoints, points.length);
	}
	public void drawPolygon(Color color, double[][] points, double centerX, double centerY, double scale) {
		int[][] drawPoints = new int[points.length][2];
		for (int i = 0; i < points.length; i++) {
			drawPoints[i][0] = (int)((points[i][0] * scale) + centerX);
			drawPoints[i][1] = (int)((points[i][1] * scale) + centerY);
		}
		this.drawPolygon(color, drawPoints);
	}
	public Surface scaleValues(float amount) {
		RescaleOp op = new RescaleOp(new float[] { amount, amount, amount, amount }, new float[] { 0, 0, 0, 0 }, null);
		BufferedImage newImg = op.filter(this.img, null);
		return new Surface(newImg);
	}
	public Surface scale_size(int amount) {
		int newWidth = this.img.getWidth() * amount;
		int newHeight = this.img.getHeight() * amount;
		return this.resize(newWidth, newHeight);
	}
	public Surface resize(int newWidth, int newHeight) {
		BufferedImage newImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = newImg.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawImage(this.img, 0, 0, newWidth, newHeight, 0, 0, this.img.getWidth(), this.img.getHeight(), null);
		g2d.dispose();
		return new Surface(newImg);
	}
	public Surface crop(int x, int y, int w, int h) {
		BufferedImage newImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = newImg.createGraphics();
		g2d.drawImage(this.img, 0, 0, w, h, x, y, x + w, y + h, null);
		g2d.dispose();
		return new Surface(newImg);
	}
	public Surface flipVertically() {
		BufferedImage newImg = new BufferedImage(this.img.getWidth(), this.img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = newImg.createGraphics();
		g2d.drawImage(this.img, 0, this.img.getHeight(), this.img.getWidth(), 0, 0, 0, this.img.getWidth(), this.img.getHeight(), null);
		g2d.dispose();
		return new Surface(newImg);
	}
	public void writeToFile(String filename) throws IOException {
		File outputfile = new File(filename);
		ImageIO.write(img, "png", outputfile);
	}
	public void save(String name) {
		int n = 1;
		while (new File(name + n + ".png").exists()) {
			n += 1;
		}
		try {
			this.writeToFile(name + n + ".png");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static Surface renderText(int size, String text, Color color) {
		if (text.length() == 0) return new Surface(1, 1, new Color(0, 0, 0, 0));
		// Measure the text
		Font font = new Font("Monospaced", 0, size);
		Surface measure = new Surface(1, 1, Color.BLACK);
		Graphics2D big = (Graphics2D)(measure.img.getGraphics());
		big.setFont(font);
		FontMetrics fm = big.getFontMetrics();
		Surface ret = new Surface(fm.stringWidth(text), fm.getHeight(), new Color(0, 0, 0, 0));
		// Draw the text
		try {
			Graphics2D g2d = ret.img.createGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setFont(font);
			g2d.setColor(color);
			g2d.drawString(text, 0, fm.getAscent());
			g2d.dispose();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		// Finish
		return ret;
	}
	public static Surface renderWrappedText(int size, String text, Color color, int maxWidth) {
		// Get font
		Font font = new Font("Monospaced", 0, size);
		// Measure the text
		Surface measure = new Surface(1, 1, Color.BLACK);
		Graphics2D big = (Graphics2D)(measure.img.getGraphics());
		big.setFont(font);
		FontMetrics fm = big.getFontMetrics();
		// Draw the text
		ArrayList<Surface> surfaces = new ArrayList<Surface>();
		String[] words = text.split(" ");
		String line = "";
		for (String word : words) {
			if (fm.stringWidth(line + " " + word) > maxWidth) {
				surfaces.add(renderText(size, line, color));
				line = word;
			} else {
				if (line.length() == 0) {
					line = word;
				} else {
					line += " " + word;
				}
			}
		}
		surfaces.add(renderText(size, line, color));
		return combineVertically(surfaces, new Color(0, 0, 0, 0));
	}
	public static Surface combineVertically(Surface[] surfaces, Color background) {
		int width = 1;
		for (Surface surface : surfaces) { int w = surface.get_width(); if (w > width) width = w; }
		int height = 1;
		for (Surface surface : surfaces) { int h = surface.get_height(); height += h; }
		Surface total = new Surface(width, height, background);
		int cum_y = 0;
		for (Surface surface : surfaces) {
			// int imgX = (width / 2) - (surface.get_width() / 2);
			total.blit(surface, 0, cum_y);
			cum_y += surface.get_height();
		}
		return total;
	}
	public static Surface combineVertically(ArrayList<Surface> surfaces, Color background) {
		return combineVertically(surfaces.toArray(new Surface[surfaces.size()]), background);
	}
}

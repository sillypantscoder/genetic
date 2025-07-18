package com.sillypantscoder.windowlib;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * A panel that automatically redraws itself.
 */
public class RepaintingPanel extends JPanel {
	// Keep track of open windows
	public static ArrayList<RepaintingPanel> panelsOpen = new ArrayList<RepaintingPanel>();
	// Frame
	protected JFrame frame;
	protected Timer timer;
	// Handlers
	public BiFunction<Integer, Integer, BufferedImage> painter;
	public Consumer<String> keyDown;
	public Consumer<String> keyUp;
	public BiConsumer<Integer, Integer> mouseMoved;
	public BiConsumer<Integer, Integer> mouseDown;
	public BiConsumer<Integer, Integer> mouseUp;
	public Consumer<Integer> mouseWheel;
	/**
	* Called by the runtime system whenever the panel needs painting.
	*/
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		try {
			g.drawImage(painter.apply(getWidth(), getHeight()), 0, 0, null);
		} catch (Throwable t) {
			t.printStackTrace();
			try {
				// set debugger on next line
				Thread.sleep(Duration.ofSeconds(1));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public void closeWindow() {
		frame.setVisible(false);
		frame.dispose();
		this.timer.stop();
		panelsOpen.remove(this);
		if (panelsOpen.isEmpty()) {
			System.exit(0);
		}
	}
	public void startAnimation() {
		this.timer = new Timer(16, (e) -> {
			frame.revalidate();
			frame.getContentPane().repaint();
		});
		this.timer.start();
	}
	public void run(String title, Surface icon, int width, int height) {
		frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().add(this, BorderLayout.CENTER);
		frame.setIconImage(icon.img);
		frame.setVisible(true);
		startAnimation();
		this.addEventListeners();
		panelsOpen.add(this);
	}
	public void addEventListeners() {
		RepaintingPanel panel = this;
		// Add mouse listener
		MouseListener ml = new MouseListener() {
			public void mouseClicked(MouseEvent arg0) { }
			public void mouseEntered(MouseEvent arg0) { }
			public void mouseExited(MouseEvent arg0) { }
			public void mousePressed(MouseEvent arg0) { panel.mouseDown.accept(arg0.getX(), arg0.getY()); }
			public void mouseReleased(MouseEvent arg0) { panel.mouseUp.accept(arg0.getX(), arg0.getY()); }
		};
		panel.addMouseListener(ml);
		// Add mouse motion listener
		MouseMotionListener mml = new MouseMotionListener() {
			public void mouseDragged(MouseEvent arg0) { panel.mouseMoved.accept(arg0.getX(), arg0.getY()); }
			public void mouseMoved(MouseEvent arg0) { panel.mouseMoved.accept(arg0.getX(), arg0.getY()); }
		};
		panel.addMouseMotionListener(mml);
		// Add keyboard listener
		panel.frame.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) { panel.keyDown.accept(KeyEvent.getKeyText(e.getKeyCode())); }
			public void keyReleased(KeyEvent e) { panel.keyUp.accept(KeyEvent.getKeyText(e.getKeyCode())); }
			public void keyTyped(KeyEvent e) { }
		});
		// Add mouse wheel listener
		panel.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) { panel.mouseWheel.accept((int)(e.getPreciseWheelRotation() * 15)); }
		});
		// Add window listener
		panel.frame.addWindowListener(new WindowListener() {
			public void windowActivated(WindowEvent e) {}
			public void windowClosed(WindowEvent e) {}
			public void windowClosing(WindowEvent e) { panel.closeWindow(); }
			public void windowDeactivated(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowOpened(WindowEvent e) {}
		});
	}
}
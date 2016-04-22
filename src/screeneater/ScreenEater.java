package screeneater;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

/**
 * JFrameの表示枠の表示／非表示を切り替えるサンプルコードです。
 */
public class ScreenEater implements Runnable {
	JFrame mainFrame;
	Color startColor = Color.BLACK;
	Color nowColor = startColor;
	static List<Point> celllist = new ArrayList<Point>();
	static ColorTable nowtable;
	Point screenSize;
	BufferedImage image = null;
	DrawingCanvas drawCanvas;
	Thread th = null;
	int counter = 0;
	int luckey = 30;
	Point startPos;

	public ScreenEater() {
		init();
		if (th == null) {
			th = new Thread(this);
			th.start();
		}
	}

	public static void main(String[] args) {
		new ScreenEater();
	}

	public void init() {
		screenSize = new Point(
				Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit
						.getDefaultToolkit().getScreenSize().height);
		java.awt.GraphicsEnvironment env = java.awt.GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		java.awt.DisplayMode displayMode = env.getDefaultScreenDevice()
				.getDisplayMode();
		mainFrame = new JFrame();
		// frame.add( component ); // 適当なコンポーネントを追加
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setBounds(0, 0, displayMode.getWidth(),
				displayMode.getHeight());
		mainFrame.setUndecorated(true);
		mainFrame.validate();

		try {
			image = new Robot().createScreenCapture(new Rectangle(Toolkit
					.getDefaultToolkit().getScreenSize()));
		} catch (Exception e) {
		}
		if (image != null) {
			System.out.println(image.toString());
		}

		drawCanvas = new DrawingCanvas(image);
		drawCanvas.addMouseListener(new muListener());
		mainFrame.add(drawCanvas);
		System.out.println(screenSize);
		celllist.add(MouseInfo.getPointerInfo().getLocation());
		nowtable = new ColorTable(screenSize.x, screenSize.y);
		nowtable.setTable(celllist.get(0), 1);
		mainFrame.setVisible(true);

		startPos = MouseInfo.getPointerInfo().getLocation();
	}

	public void run() {
		while (th != null) {
			try {
				Thread.sleep(56 / 4 / 2 /7);
			} catch (InterruptedException ex) {
			}
			check();
			delete();
			painting();
		}
		th = null;
	}

	public void sleep(long i) {
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void check() {
		int c = celllist.size();
		for (int index = 0; index < c; index++) {
			boolean mark = false;
			Point point = celllist.get(index);
			if (point.x > 0
					&& nowtable.getTable(point.x - 1, point.y) == 0
					&& celllist.indexOf(new Point(point.x - 1, point.y)) == -1) {
				mark = true;
				Color colortmp = new Color(image.getRGB(point.x - 1, point.y));
				int luck = Math.abs(nowColor.getRed() - colortmp.getRed())
						+ Math.abs(nowColor.getGreen() - colortmp.getGreen())
						+ Math.abs(nowColor.getBlue() - colortmp.getBlue());
				luck /= luckey;
				luck++;
				if (new Random().nextInt(luck) == 0) {
					celllist.add(new Point(point.x - 1, point.y));
				}
			}
			if (point.x + 1 < screenSize.x
					&& nowtable.getTable(point.x + 1, point.y) == 0
					&& celllist.indexOf(new Point(point.x + 1, point.y)) == -1) {
				mark = true;
				Color colortmp = new Color(image.getRGB(point.x + 1, point.y));
				int luck = Math.abs(nowColor.getRed() - colortmp.getRed())
						+ Math.abs(nowColor.getGreen() - colortmp.getGreen())
						+ Math.abs(nowColor.getBlue() - colortmp.getBlue());
				luck /= luckey;
				luck++;
				if (new Random().nextInt(luck) == 0) {
					celllist.add(new Point(point.x + 1, point.y));
				}
			}
			if (point.y > 0
					&& nowtable.getTable(point.x, point.y - 1) == 0
					&& celllist.indexOf(new Point(point.x, point.y - 1)) == -1) {
				mark = true;
				Color colortmp = new Color(image.getRGB(point.x, point.y - 1));
				int luck = Math.abs(nowColor.getRed() - colortmp.getRed())
						+ Math.abs(nowColor.getGreen() - colortmp.getGreen())
						+ Math.abs(nowColor.getBlue() - colortmp.getBlue());
				luck /= luckey;
				luck++;
				if (new Random().nextInt(luck) == 0) {
					celllist.add(new Point(point.x, point.y - 1));
				}
			}
			if (point.y < screenSize.y + 1
					&& nowtable.getTable(point.x, point.y + 1) == 0
					&& celllist.indexOf(new Point(point.x, point.y + 1)) == -1) {
				mark = true;
				Color colortmp = new Color(image.getRGB(point.x, point.y + 1));
				int luck = Math.abs(nowColor.getRed() - colortmp.getRed())
						+ Math.abs(nowColor.getGreen() - colortmp.getGreen())
						+ Math.abs(nowColor.getBlue() - colortmp.getBlue());
				luck /= luckey;
				luck++;
				if (new Random().nextInt(luck) == 0) {
					celllist.add(new Point(point.x, point.y + 1));
				}
			}
			if (!mark) {
				celllist.remove(index);
				c--;
				nowtable.setTable(point, 2);
			}
		}
		c = celllist.size();
		for (int index = 0; index < c; index++) {
			nowtable.setTable(celllist.get(index), 1);

		}

	}

	public void delete() {

	}

	public void painting() {
		Graphics gra = image.getGraphics();
		for (int index = 0; index < celllist.size(); index++) {
					gra.setColor(nowColor);
					gra.fillRect(celllist.get(index).x, celllist.get(index).y, 1, 1);
		}

		nowColor = new Color((int)Math.abs(Math.sin(counter/(32*Math.PI))*255)/2,
				0,
				0);
		//nowColor = Color.black;
		counter++;
		if (counter == 360) {
			counter = 0;
			// nowColor = Color.BLACK;
		}
		//nowColor = new Random().nextInt(4) == 0 ? Color.BLACK : Color.WHITE;
		drawCanvas.setImage(image);
		drawCanvas.repaint();
		// drawCanvas.addMouseListener(new muListener());
		;
		// System.out.println(nowColor);*/

	}
}

class DrawingCanvas extends Canvas {
	BufferedImage image;

	DrawingCanvas(BufferedImage i) {
		image = i;
	}

	DrawingCanvas() {
	}

	public void setImage(BufferedImage i) {
		image = i;
	}

	public void paint(Graphics g) {
		g.drawImage(image, 0, 0, this);
	}

	public void update(Graphics g) {
		paint(g);
	}
}



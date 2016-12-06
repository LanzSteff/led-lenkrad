package ledStripeSimulator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class LedPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private final LedPanelFacade facade;
	private final Point ledPositions[];
	private final int nrOfLedPixel;
	private Color[] ca;
	private int offset;
	private int radiusOld = -1;

	public LedPanel(int nrOfLedPixel) {
		this.nrOfLedPixel = nrOfLedPixel;
		this.facade = new LedPanelFacade(this);
		ledPositions = new Point[nrOfLedPixel];
		ca = new Color[nrOfLedPixel];
		for (int i = 0; i < ledPositions.length; i++) {
			ledPositions[i] = new Point();
			ca[i] = Color.black;
		}
	}

	public void paint(Graphics g) {
		super.paintComponent(g);

		if (ca == null) {
			return;
		}

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		int w = this.getWidth();
		int h = this.getHeight();

		int r = Math.min(w, h) / 2;
		int dPix = r / 30;
		if (dPix < 1) {
			dPix = 1;
		}
		r -= dPix;

		calcPixPos(r);

		int x0 = (w - dPix) / 2;
		int y0 = (h - dPix) / 2;

		for (int i = 0; i < ledPositions.length; i++) {
			g2.setPaint(ca[(i + offset) % nrOfLedPixel]);
			g2.fillOval(x0 + ledPositions[i].x, y0 - ledPositions[i].y, dPix,
					dPix);
		}
	}

	public void setColors(Color[] ca) {
		this.ca = ca;
		_repaint();
	}

	public void setOffset(int offset) {
		offset %= nrOfLedPixel;
		if (offset < 0) {
			offset += nrOfLedPixel;
		}
		this.offset = offset;
		_repaint();
	}

	public int getNrOfLedPixels() {
		return nrOfLedPixel;
	}

	public LedPanelFacade getFacade() {
		return facade;
	}

	//

	private void calcPixPos(int radius) {
		if (radius == radiusOld) {
			return;
		}
		radiusOld = radius;
		double fact = 2.0 * Math.PI / nrOfLedPixel;
		for (int i = 0; i < ledPositions.length; i++) {
			Point p = ledPositions[i];
			p.x = (int) (radius * Math.cos(fact * i));
			p.y = (int) (radius * Math.sin(fact * i));
		}
	}

	private void _repaint() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				repaint();
			}
		});
	}
}

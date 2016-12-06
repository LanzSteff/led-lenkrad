package ledStripeSimulator;

import java.awt.Color;

import application.LedInterface;

public class LedPanelFacade implements LedInterface {

	private final LedPanel ledPanel;
	private final int pixelCnt;

	private int brightness;
	private Color color;
	private final int bwData[];
	private final Color colorData[];

	public LedPanelFacade(final LedPanel ledPanel) {
		this.ledPanel = ledPanel;
		this.pixelCnt = ledPanel.getNrOfLedPixels();
		this.bwData = new int[pixelCnt];
		this.colorData = new Color[pixelCnt];
		this.brightness = 255;
		this.color = Color.RED;
	}

	@Override
	public void setOffset(int offset) {
		ledPanel.setOffset(offset);
	}

	@Override
	public void setColor(Color color) {
		this.color = color;
		updateColorFromBw();
	}

	@Override
	public void setBrightness(int brightness) {
		this.brightness = brightness;
		updateColorFromBw();
	}

	@Override
	public void setBwPattern(byte a[]) {
		for (int i = 0; i < pixelCnt; i++) {
			bwData[i] = 0x00ff & a[i];
		}
		// System.arraycopy(a, 0, bwData, 0, pixelCnt);
		updateColorFromBw();
	}

	@Override
	public void setColorPattern(byte a[]) {
		for (int i = 0, j = 0; i < a.length; j++) {
			int r = 0x00ff & a[i++];
			int b = 0x00ff & a[i++];
			int g = 0x00ff & a[i++];
			if (r == 0 && g == 0 && b == 0) {
				r = b = g = 40;
			}
			//System.out.println(r + " " + g + " " + b);
			colorData[j] = new Color(r, b, g);
		}
		ledPanel.setColors(colorData);
	}

	private void updateColorFromBw() {
		int factR = color.getRed() * brightness;
		int factG = color.getGreen() * brightness;
		int factB = color.getBlue() * brightness;
		for (int i = 0; i < pixelCnt; i++) {
			int bw = bwData[i];
			int r = (bw * factR) >> 16;
			int g = (bw * factG) >> 16;
			int b = (bw * factB) >> 16;
			if (r == 0 && g == 0 && b == 0) {
				r = b = g = 40;
			}
			colorData[i] = new Color(r, g, b);
		}
		ledPanel.setColors(colorData);
	}
}

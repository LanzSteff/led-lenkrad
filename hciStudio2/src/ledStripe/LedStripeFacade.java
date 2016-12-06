package ledStripe;

import java.awt.Color;

import jssc.SerialPortException;
import application.LedInterface;

public class LedStripeFacade implements LedInterface {
	
	private final int nrOfLedPixel;
	private final Engine engine;

	public LedStripeFacade(int nrOfLedPixel) {
		this.nrOfLedPixel = nrOfLedPixel;
		engine = new Engine();
	}

	/* (non-Javadoc)
	 * @see ledStripe.ledInterface#setOffset(int)
	 */
	@Override
	public void setOffset(int offset) {
		try {
			
			int help = (offset + nrOfLedPixel / 4 * 3)
					% nrOfLedPixel;
			System.out.println(help);

			if (help < 0) {
				engine.sendMessage_int(help + 144, 46);
			} else {

				engine.sendMessage_int(help, 46);
			}
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see ledStripe.ledInterface#setColor(java.awt.Color)
	 */
	@Override
	public void setColor(Color color) {
		try {
			engine
					.sendMessage(
							new byte[] { (byte) (color.getGreen()),
									(byte) (color.getRed()),
									(byte) (color.getBlue()) }, 30);
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see ledStripe.ledInterface#setBrightness(int)
	 */
	@Override
	public void setBrightness(int brightness) {
		if (brightness >= 0 && brightness < 256) {
			try {
				engine.sendMessage(
						new byte[] { (byte) brightness }, 31);
			} catch (SerialPortException e) {
				e.printStackTrace();
			}
		}
	}

	/* (non-Javadoc)
	 * @see ledStripe.ledInterface#setBwPattern(byte[])
	 */
	@Override
	public void setBwPattern(byte[] a) {
		try {
			engine.sendMessage(a, 20);
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see ledStripe.ledInterface#setColorPattern(byte[])
	 */
	@Override
	public void setColorPattern(byte[] a) {
		try {
			engine.sendMessage(a, 1);
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}
}

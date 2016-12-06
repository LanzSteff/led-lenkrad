package ledStripe;

import jssc.SerialPortException;

public class Communication {

	private final byte START_SYMBOL = '$';
	private final Uart uart;
	private boolean onSendingMsg;

	public Communication(Uart uart) {
		this.uart = uart;
		new Thread() {
			public void run() {
				while (true) {
					if (!onSendingMsg) {
						uart_sendChar('#');
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
				}
			}
		}.start();
	}

	public boolean isSendReady() {
		return uart.isSendReady();
	}

	public void sendMessage(int id, int val) throws SerialPortException {
		byte o[] = new byte[4];
		o[0] = (byte) (val >> 24);
		o[1] = (byte) (val >> 16);
		o[2] = (byte) (val >> 8);
		o[3] = (byte) (val >> 0);
		sendMessage(id, o);
	}

	public void sendMessage(int id, byte data[]) throws SerialPortException {
		onSendingMsg = true;
		byte checksum = 0;
		uart.sendByte(START_SYMBOL);
		// checksum ^= START_SYMBOL;
		uart.sendByte((byte) id);
		checksum ^= (byte) id;
		int len = data.length;
		byte lenH = (byte) (len >> 8);
		uart.sendByte(lenH);
		checksum ^= lenH;
		byte lenL = (byte) len;
		uart.sendByte(lenL);
		checksum ^= lenL;
		uart.sendByteArray(data);
		for (int i = 0; i < data.length; i++) {
			checksum ^= data[i];
		}
		uart.sendByte(checksum);
		onSendingMsg = false;
	}

	// public boolean ping() {
	// try {
	// boolean isConnected = false;
	// uart.sendByte((byte) '#');
	// int t = 1000;
	// while (--t > 0) {
	// int c = uart.readByte();
	// if ((char) c == '#') {
	// isConnected = true;
	// // System.out.println("**");
	// }
	// Thread.sleep(1);
	// }
	// return isConnected;
	// } catch (Exception e) {
	// return false;
	// }
	//
	// }

	public void uart_sendChar(char c) {
		try {
			uart.sendByte((byte) c);
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}

	public boolean ping() {
		try {
			uart.sendByte((byte) '#');
			int t = 1000;
			while (--t > 0) {
				int c = uart.readByte();
				if ((char) c == '#' || (char) c == '~') {
					return true;
				}
				Thread.sleep(1);
			}
			return false;
		} catch (Exception e) {
			return false;
		}

	}

//	private void setPattern() {
//		byte a[] = new byte[144];
//		for (int i = 0; i < a.length; i++) {
//			// a[i]=1;
//			double d = (Math.sin(1.0 / 144 * i * Math.PI * 4));
//			a[i] = (byte) (40 * d * d);
//		}
//	}
}

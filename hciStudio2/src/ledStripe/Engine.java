package ledStripe;

import jssc.SerialPortException;

public class Engine {
	Uart uart;
	boolean b;
	public Communication com;

	byte color[];

	public Engine() {
		System.out.println("engine");
		uart = new Uart();
		b = uart.connect("COM14"); //COM30 = Simulator, COM14 = sonst

		if (b) {
			System.out.println("uart Ok");
		} else {
			System.out.println("uart failed");
			System.exit(1);
		}
		 try {
		 Thread.sleep(5000);
		 } catch (InterruptedException e1) {
		 }
		while (true) {
			int c;
			try {
				c = uart.readByte();
				if (c == '#') {
					break;
				}
				Thread.sleep(100);
			} catch (SerialPortException | InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Initial '#' received");
		com = new Communication(uart);
		try {
			if (!com.ping()) {
				System.out.println("communication failed");
				System.exit(0);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("communication OK");
		color = new byte[] { 0, 0, (byte) (255) };
	}

	// wird nur bei rmi verwendet.
	public boolean ping() {
		return com.ping();
	}

	public void sendMessage(byte[] data, int id) throws SerialPortException {
		boolean isTransmitted = false;
		while (!isTransmitted) {
			com.sendMessage(id, data);
			//System.out.print("s");
			int t = 30;
			while (--t > 0) {
				int c = uart.readByte();
				if ((char) c == 'e') { // err
					continue;
				}
				if ((char) c == '*') {
					isTransmitted = true;
					t = 0;
				}
				if (c != -1) {
				}
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
				}
				//System.out.print("t");
			}
		}
	}
	
	
	public void sendMessage_int(int mi , int id) throws SerialPortException {
		boolean isTransmitted = false;
		while (!isTransmitted) {
			com.sendMessage(id, mi);
			int t = 1000;
			while (--t > 0) {
				int c = uart.readByte();
				if ((char) c == '*') {
					isTransmitted = true;
					t = 0;
				}
				if (c != -1) {
				}
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
				}
			}
		}
	}
}

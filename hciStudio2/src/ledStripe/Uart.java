package ledStripe;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class Uart {

	private SerialPort serialPort;

	public boolean connect(String port) {
		serialPort = new SerialPort(port);
		boolean ok = false;

		// https://code.google.com/p/java-simple-serial-connector/issues/detail?id=5

		// if (serialPort != null && serialPort.isOpened()) {
		// try {
		// serialPort.purgePort(1);
		// serialPort.purgePort(2);
		// serialPort.closePort();
		// } catch (SerialPortException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }

		try {
			ok = serialPort.openPort();
			serialPort.setParams(115200, 8, 1, 0);
//			serialPort.setParams(38400, 8, 1, 0);
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
		String[] portNames = SerialPortList.getPortNames();
		for (int i = 0; i < portNames.length; i++) {
			System.out.println(portNames[i]);
		}
		return ok;
	}

	public void sendByte(byte a) throws SerialPortException {
		serialPort.writeByte(a);

		// System.out.printf("%02x\n", a);
	}

	public void sendByteArray(byte ba[]) throws SerialPortException {
		
		while (readByte() != '~'){
			//System.out.print('w');
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
			}
		}
		//while(readByte() != -1); // Empfangspuffer leeren
		serialPort.writeBytes(ba); 
//		serialPort.writeBytes(ba); 
//		while(serialPort.getOutputBufferBytesCount() > 0){
//			
//		}
//		while (readByte() != '~'){
//			System.out.print('w');
//		}
//		try {
//			Thread.sleep(10);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		for (byte b : ba) {
//			sendByte(b);
//		}
	}

	public int readByte() throws SerialPortException {
		if (serialPort.getInputBufferBytesCount() > 0) {
			return serialPort.readBytes(1)[0];
		} else {
			return -1;
		}
	}

	public boolean isSendReady() {
		try {
			return serialPort.getOutputBufferBytesCount() == 0;
		} catch (SerialPortException e) {
			e.printStackTrace();
			return true;
		}
	}

}

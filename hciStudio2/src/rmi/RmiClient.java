package rmi;

import java.rmi.Naming;
import java.rmi.RemoteException;

public class RmiClient {

	private RMIServerInterface server;

	boolean connected;

	private String serverAddress;

	public RmiClient(final String serverAddress) {
		this.serverAddress = serverAddress;
		startPingThread();
	}

	public void startPingThread() {
		new Thread() {
			@Override
			public void run() {
				while (true) {
					ping();
				}
			}
		}.start();
	}

	public int getSpeed() {
		if (connected) {
			try {
				return server.getSpeed();
			} catch (RemoteException e) {
				return -1;
			}
		}
		return -1;
	}
	
	public int getRPM() {
		if(connected) {
			try {
				return server.getRPM();
			} catch (RemoteException e) {
				return -1;
			}
		}
		return -1;
	}

	public String getTriggerMessage() {
		if (connected) {
			try {
				return server.getTriggerMsg();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	private void connect() {
		try {
			server = (RMIServerInterface) Naming.lookup(serverAddress);
			System.out.println("\nrmi connected.");
			connected = true;
		} catch (Exception e) {
			System.err.print("*");
		}
	}

	private void ping() {
		try {
			connected = server.ping();
		} catch (Exception e) {
			connected = false;
			connect();
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

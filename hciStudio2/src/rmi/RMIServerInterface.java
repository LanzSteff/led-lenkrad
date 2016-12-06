package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerInterface extends Remote {
	int getSpeed() throws RemoteException;
	
	int getRPM() throws RemoteException;

	String getTriggerMsg() throws RemoteException;

	boolean ping() throws RemoteException;
}

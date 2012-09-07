package asteroidsa.network.communication;

import java.util.Observable;

import asteroidsa.network.Host;
import asteroidsa.network.NetworkApplicationData;

public abstract class NetworkCommunication extends Observable {

    /** Message data to send/received */
    protected NetworkApplicationData networkApplicationData = null;
	
	/**
	 * @return a reference to the data message to be observed
	 */
    public NetworkApplicationData getNetworkApplicationData() {
		return networkApplicationData;
	}

	/**
	 * Sets the networkApplicationData instance to be observed
	 * @param networkApplicationData the networkApplicationData instance to be observed
	 */
	public void setNetworkApplicationData(NetworkApplicationData networkApplicationData) {
		this.networkApplicationData = networkApplicationData;
	}
    
	
	/** 
     * Notifies dependent objects about a new received message.
     * Dependent objects will receive a <code>NetworkApplicationData</code> instance 
     */
    public void notifyNewMessage()
    {
        setChanged();
        notifyObservers(networkApplicationData);
    }
	
    
	/* 
	 * ================================================================================================= 
	 */
    
	
	/**
	 * Starts the service for receiving messages from other hosts
	 * @param networkApplicationData type of data to be sent/received through this communication service
	 * @return true if action was successful, false otherwise
	 */
	public abstract boolean startService(NetworkApplicationData networkApplicationData);
	
	
	/**
	 * Connects to a server
	 * @param target target host
	 * @return true if a connection is achieved, or false otherwise
	 */
	public abstract  boolean connectToServerHost(Host target);
	
	
	/**      
	 * Sends asynchronously a single data message to a target
	 * @param target destination host IP
	 * @param data message content
	 * @return true if action was successful, false otherwise 
	 */
	public abstract boolean sendMessage(String targetIP, NetworkApplicationData data);
	
	
	/**
	 * Sends asynchronously a single data message to all known hosts
	 * @param data message content
	 * @return true if action was successful, false otherwise 
	 */
	public abstract boolean sendMessageToAllHosts(NetworkApplicationData data);

	

}

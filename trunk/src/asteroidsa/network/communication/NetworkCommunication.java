package asteroidsa.network.communication;

import java.util.Observable;

import asteroidsa.network.Host;
import asteroidsa.network.NetworkApplicationData;
import asteroidsa.network.NetworkApplicationDataProducer;

public abstract class NetworkCommunication extends Observable {

	/** Interval between status updates to the other hosts */
	public static final int BROADCAST_LOCAL_STATUS_INTERVAL_MS = 30;
	
	/** Local data producer instance */
	protected NetworkApplicationDataProducer producer = null;
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
	 * Sets de producer instance in charge of filling the data to be broadcasted
	 * @param producer the NetworkApplicationDataProducer instance
	 */
	public void setProducer(NetworkApplicationDataProducer producer) {
		this.producer = producer;
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
	 * Should be started in a separate thread
	 * @return true if action was successful, false otherwise
	 */
	public abstract boolean startService();
	
	/**
	 * Starts the service in charge of broadcasting local status to other hosts
	 * Should be started in a separate thread
	 * @return true if action was successful, false otherwise
	 */
	public abstract boolean startBroadcast();

	/**
	 * Stops the service for receiving messages from other hosts
	 * @return true if action was successful, false otherwise
	 */
	public abstract boolean stopService();
	
	/**
	 * Stops the service in charge of broadcasting local status to other hosts
	 * @return true if action was successful, false otherwise
	 */
	public abstract boolean stopBroadcast();
	
	/**
	 * Connects to a server
	 * @param target target host
	 * @return true if a connection is achieved, or false otherwise
	 */
	public abstract  boolean connectToServerHost(Host target);
	
	/**      
	 * Sends a single data message to a target
	 * @param targetIP destination host IP
	 * @param data message content
	 * @return true if action was successful, false otherwise 
	 */
	public abstract boolean sendMessage(String targetIP, NetworkApplicationData data);
	
	/**
	 * Sends a single data message to all known hosts
	 * @param data message content
	 * @return true if action was successful, false if one or more messages
	 * 	where unable to be sent 
	 */
	public abstract boolean sendMessageToAllHosts(NetworkApplicationData data);


}

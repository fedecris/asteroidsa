package asteroidsa.network.communication;

import java.util.Observable;

import asteroidsa.network.Host;
import asteroidsa.network.Logger;
import asteroidsa.network.NetworkApplicationData;
import asteroidsa.network.NetworkApplicationDataProducer;

public abstract class NetworkCommunication extends Observable implements Runnable {

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
    
    /** 
     * In charge of sending local status to the other hosts periodically
     */
    public void run() {
    	while (true) {
        	sendMessageToAllHosts(producer.produceNetworkApplicationData());
        	try {
        		Thread.sleep(BROADCAST_LOCAL_STATUS_INTERVAL_MS);
        	}
        	catch (Exception e) { 
        		Logger.e("Error en StatusHandler(): " + e.getMessage()); 
        	}
        }
    }
    
	
    
	/* 
	 * ================================================================================================= 
	 */
    
	
	/**
	 * Starts the service for receiving messages from other hosts
	 * @return true if action was successful, false otherwise
	 */
	public abstract boolean startService();
	
	
	/**
	 * Starts the thread in charge of broadcast local status to other hosts
	 * @return true if action was successful, false otherwise
	 */
	public abstract boolean startBroadcast();
	
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

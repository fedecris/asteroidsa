package asteroidsa.network.communication;

import asteroidsa.network.Host;
import asteroidsa.network.NetworkApplicationData;
import asteroidsa.network.NetworkApplicationDataConsumer;
import asteroidsa.network.NetworkApplicationDataProducer;

public abstract class NetworkCommunication {

	/** Interval between status updates to the other hosts */
	public static final int BROADCAST_LOCAL_STATUS_INTERVAL_MS = 30;
	
	/** Local data producer instance */
	protected NetworkApplicationDataProducer producer = null;
	/** Remote data consumer instance */
	protected NetworkApplicationDataConsumer consumer = null;
    /** Message data to send/received */
    protected NetworkApplicationData networkApplicationData = null;
    
    /**
     * Default producer getter
     * @return the local data producer
     */
	public NetworkApplicationDataProducer getProducer() {
		return producer;
	}

	/**
	 * Default producer setter
	 * @param producer the local data producer
	 */
	public void setProducer(NetworkApplicationDataProducer producer) {
		this.producer = producer;
	}

	/**
	 * Default consumer getter
	 * @return the remote data consumer
	 */
	public NetworkApplicationDataConsumer getConsumer() {
		return consumer;
	}

	/**
	 * Default consumer setter
	 * @param consumer the remote data consumer
	 */
	public void setConsumer(NetworkApplicationDataConsumer consumer) {
		this.consumer = consumer;
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

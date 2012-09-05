package asteroidsa.network.communication;

import java.util.HashMap;

import asteroidsa.network.Host;
import asteroidsa.network.NetworkApplicationData;

public abstract class NetworkCommunication {

	/** TCP Listener */
	protected static TCPListener listener = null;
	/** TCP Clients: Target Host IP - TCPConnection */
	protected static HashMap<String, TCPClient> clientPool = new HashMap<String, TCPClient>();
    /** Mensaje de envio o recepción */
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
    
	
	/* 
	 * ================================================================================================= 
	 */
    
	
	/**
	 * Starts the listener service for receiving messages from other hosts
	 * @param networkApplicationData type of data to be sent/received through this communication service
	 * @return true if action was successful, false otherwise
	 */
	public abstract boolean startListener(NetworkApplicationData networkApplicationData);
	
	
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

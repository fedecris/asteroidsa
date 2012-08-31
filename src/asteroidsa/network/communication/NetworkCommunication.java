package asteroidsa.network.communication;

import asteroidsa.network.Host;
import asteroidsa.network.NetworkApplicationData;

public interface NetworkCommunication {

	
	/**
	 * Starts the listener service for receiving messages from other hosts
	 */
	public void startListener();
	
	
	/**
	 * Connects to a server
	 * @param target target host
	 */
	public void connectToServerHost(Host target);
	
	
	/**      
	 * Sends asynchronously a single data message to a target
	 * @param target destination host
	 * @param data message content
	 */
	public void sendMessage(Host target, NetworkApplicationData data);
	
	
	/**
	 * Sends asynchronously a single data message to all known hosts
	 * @param data message content
	 */
	public void sendMessageToAllHosts(NetworkApplicationData data);

	
	/**
	 * @return a reference to the data message to be observed
	 */
	public NetworkApplicationData getNetworkApplicationData();
	
	/**
	 * Sets the networkApplicationData instance to be observed
	 * @param networkApplicationData the networkApplicationData instance to be observed
	 */
	public void setNetworkApplicationData(NetworkApplicationData networkApplicationData);
}

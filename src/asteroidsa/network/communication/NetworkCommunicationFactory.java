package asteroidsa.network.communication;


public class NetworkCommunicationFactory {
	
	/** Network communication through TCP Sockets */
	public static final int COMMUNICATION_METHOD_TCP = 1;
	
	/**
	 * Default method for network communication
	 * @return method identifier for default network communication method
	 */
	public static int getDefaultNetworkCommunication() {
		return COMMUNICATION_METHOD_TCP;
	}
	
	/**
	 * Factory for network communication
	 * @param method identifier for default network communication method
	 * @return an instance of the network communication method selected, or null otherwise
	 */
	public static NetworkCommunication getNetworkCommunication(int method) {
		switch (method) {
		case COMMUNICATION_METHOD_TCP:
			return new TCPCommunication();
		default:
			return null;
		}
	}
}

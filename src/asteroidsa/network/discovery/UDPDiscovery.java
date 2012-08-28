package asteroidsa.network.discovery;

public class UDPDiscovery extends HostDiscovery {

	/** UDP Port */
	protected static final int UDP_PORT = 9998;
	/** UDP Group */
	protected static final String UDP_GROUP = "230.0.0.1";
	/** Field Separator */
	protected static final String DATAGRAM_FIELD_SPLIT = ":..:";
	/** Buffer size */
	protected static final int BUFFER_SIZE = 256;
	/** Discovery is running */
	protected static boolean running = false;
	
	
	/**
	 * Starts UDP host discovery
	 */
	public void startDiscovery() {
		running = true;
		
		// Listener
        UDPListener listener = new UDPListener();
        Thread threadA = new Thread(listener);
        threadA.start();
        
        // Client
        UDPClient client = new UDPClient();
        Thread threadB = new Thread(client);
        threadB.start();
	}
	
	public void stopDiscovery() {
		running = false;
	}
	
	
}

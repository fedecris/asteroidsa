package asteroidsa.network.discovery;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

class UDPDiscovery extends HostDiscovery {

	/** Status update interval (ms) */
	public static final int PING_INTERVAL_MS = 1000;
	/** UDP Port */
	protected static final int UDP_PORT = 9998;
	/** UDP Group */
	protected static final String UDP_GROUP = "230.0.0.1";
	/** Field Separator */
	protected static final String DATAGRAM_FIELD_SPLIT = ":..:";
	/** Buffer size */
	protected static final int BUFFER_SIZE = 64;
	/** Discovery is running */
	protected static boolean running = false;
	/** Status data */
	protected DatagramPacket packet = null;
	/** Status to be sent/received */
	protected byte[] buf = new byte[BUFFER_SIZE];
	/** UDP Host group */
	protected InetAddress group = null;
	/** Broadcast to other hosts */ 
	protected MulticastSocket socket = null;
	
	/**
	 * Starts UDP host discovery, creating two threads: one for the server and one for the client
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
	
	/**
	 * Stops UDP host discovery.  The thread created in <code>startDiscovery()</code> end normally.
	 */
	public void stopDiscovery() {
		running = false;
	}
	
	
}

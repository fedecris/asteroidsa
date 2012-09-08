package asteroidsa.network;

/**
 * Network support main class
 */

import asteroidsa.network.communication.NetworkCommunication;
import asteroidsa.network.communication.NetworkCommunicationFactory;
import asteroidsa.network.discovery.HostDiscovery;
import asteroidsa.network.discovery.HostDiscoveryFactory;

public class NetworkStartup {

	protected static NetworkCommunication networkComm = null;
	protected static HostDiscovery networkDiscovery = null;
	
	public static boolean configureStartup(NetworkApplicationDataObserver observer, NetworkApplicationDataProducer producer) {
		
		try {
	    	// Discovery handler
	        networkDiscovery = HostDiscoveryFactory.getHostDiscovery(HostDiscoveryFactory.getDefaultDiscoveryMethod());
	        // Communication listener
	        networkComm = NetworkCommunicationFactory.getNetworkCommunication(NetworkCommunicationFactory.getDefaultNetworkCommunication());
	        networkComm.addObserver(observer);
	        networkComm.setProducer(producer);
	        return true;
		} 
		catch (Exception e) {
			Logger.e("Error in NetworkStartup(): " + e.getMessage());
			return false;
		}
        
	}
	

	public static boolean doStartup() {
		
		try {
			networkComm.startService();	
	        
	        // FIXME: WHY?
	        Thread.sleep(5000);
	        
	        networkDiscovery.startDiscovery();
	        
	        // FIXME: WHY?
	        Thread.sleep(5000);

	        // Communication client
	        networkComm.startBroadcast();	
	        return true;
		}
		catch (Exception e) {
			Logger.e("Error in NetworkStartup(): " + e.getMessage());
			return false;
		}		
        
	}

	
	
}


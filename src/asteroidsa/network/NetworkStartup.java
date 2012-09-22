package asteroidsa.network;

/**
 * Network support main class.  Applications interested in using this framework should
 * invoke the static methods configureStartup() and doStartup() in that order.
 * 
 * FIXME: Mejorar el log.  Deberia automatizar temas como el stack trace 
 * FIXME: Mejorar metodos orientados a QoS. True/false convertir a algo más "rico".  Contabilizar nro de paquetes enviados/perdidos, etc.
 * FIXME: Ver temas de concurrencia en colecciones, no solo del framework NetworkDCQ, sino tambien en la app.
 * FIXME: Mejorar comentarios y documentacion
 */

import asteroidsa.network.communication.NetworkCommunication;
import asteroidsa.network.communication.NetworkCommunicationFactory;
import asteroidsa.network.discovery.HostDiscovery;
import asteroidsa.network.discovery.HostDiscoveryFactory;

public class NetworkStartup {

	/**
	 * NetworkCommunication implementation logic
	 */
	protected static NetworkCommunication networkCommunication = null;
	
	/**
	 * NetworkDicovery implementation logic
	 */
	protected static HostDiscovery networkDiscovery = null;
	
	
	/**
	 * Network main application entry point for configuration.
	 * @param observer 
	 * 		instance in charge of updating the local model based on the received messages from other hosts
	 * @param producer
	 * 		instance in charge of setting the local information to be broadcasted periodically to the other hosts
	 * @return
	 * 		true of configuration was OK, or false otherwise
	 * @throws 
	 * 		Exception if an application is trying to configure an already configured startup
	 */
	public static boolean configureStartup(NetworkApplicationDataConsumer consumer, NetworkApplicationDataProducer producer) throws Exception {
		
		try {
			
			if (networkDiscovery!=null || networkCommunication!=null)
				throw new Exception ("NetworkStartup already configured.");
			
	    	// Discovery handler
	        networkDiscovery = HostDiscoveryFactory.getHostDiscovery(HostDiscoveryFactory.getDefaultDiscoveryMethod());
	        // Communication listener
	        networkCommunication = NetworkCommunicationFactory.getNetworkCommunication(NetworkCommunicationFactory.getDefaultNetworkCommunication());
	        networkCommunication.setConsumer(consumer);
	        networkCommunication.setProducer(producer);
	        return true;
		} 
		catch (Exception e) {
			Logger.e("Error in NetworkStartup(): " + e.getMessage());
			return false;
		}
        
	}
	
	/**
	 * Network main application entry point for starting host discovery and host communication logic.
	 * Must execute the proper configuration first. 
	 * @return 
	 * 		true if startup was OK, or false otherwise
	 * @throws 
	 * 		Exception in case of error or misconfiguration
	 */
	public static boolean doStartup() throws Exception {
		
		// Was the startup correctly configured?
		if (networkCommunication==null || networkDiscovery==null)
			throw new Exception ("NetworkStartup not configured.  Invoke configureStartup() first.");
		
		try {
			// Communication server
			if (!networkCommunication.startService())
				throw new Exception ("Error starting network communication service");
	        
	        // FIXME: WHY?
	        Thread.sleep(2000);
	        
	        // Discovery service
	        if (!networkDiscovery.startDiscovery())
	        	throw new Exception ("Error starting network discovery service");
	        
	        // FIXME: WHY?
	        Thread.sleep(2000);

	        // Communication client
	        if (!networkCommunication.startBroadcast())
	        	throw new Exception ("Error starting network communication broadcast");
	        	
	        return true;
		}
		catch (Exception e) {
			Logger.e("Error in DoStartup(): " + e.getMessage());
			return false;
		}		
        
	}

	
	
}


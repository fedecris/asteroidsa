package asteroidsa.network.discovery;

public class HostDiscoveryFactory {
	
	/** Host discovery through UDP Sockets */
	public static final int DISCOVERY_METHOD_UDP = 1;
	
	/**
	 * Default method for host discovery
	 * @return method identifier for default host discovery method
	 */
	public static int getDefaultDiscoveryMethod() {
		return DISCOVERY_METHOD_UDP;
	}
	
	/**
	 * Factory for host discovery methods
	 * @param method identifier for default host discovery method
	 * @return an instance of the discovery method selected, or null otherwise
	 */
	public static HostDiscovery getHostDiscovery(int method) {
		switch (method) {
		case DISCOVERY_METHOD_UDP:
			return new UDPDiscovery();
		default:
			return null;
		}
	}

}

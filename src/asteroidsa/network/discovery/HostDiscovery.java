package asteroidsa.network.discovery;

import java.util.concurrent.ConcurrentHashMap;

import asteroidsa.network.Host;
import asteroidsa.network.Logger;

public abstract class HostDiscovery {

	/** Status update interval (ms) */
	public static final int DISCOVERY_INTERVAL_MS = 1000;
	/** Host local */
	public static Host thisHost = null;
	/** The other hosts list. IP->Host details */
	public static ConcurrentHashMap<String, Host> otherHosts = new ConcurrentHashMap<String, Host>();
	
	/**
	 * Start the discovery method for finding hosts
	 * Subclasses must periodically update otherHosts data
	 */
	public abstract boolean startDiscovery();

	/**
	 * Stop the discovery method for finding hosts
	 */
	public abstract void stopDiscovery();
	

	/**
	 * Set this host IP
	 */
	static {
		// Get IPv4 IP
		thisHost = Host.getLocalHostAddresAndIP();
		if (thisHost == null || thisHost.getHostIP() == null || thisHost.getHostIP().length() == 0) {
			Logger.e("No IP! Exiting...");
			System.exit(1);
		}
		// Initially conected
		thisHost.setConnected(true);
	}
	
	@Override
	public String toString() {
		if (thisHost != null && thisHost.getHostIP() != null)
			return thisHost.getHostIP();
		return "";
	}
}

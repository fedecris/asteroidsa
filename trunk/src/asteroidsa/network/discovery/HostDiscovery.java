package asteroidsa.network.discovery;

import java.util.ArrayList;
import java.util.Observable;

import android.util.Log;
import asteroidsa.network.Host;
import asteroidsa.utils.Globals;

public abstract class HostDiscovery {


	/** Host local */
	public static Host thisHost = null;
	/** The other hosts */
	public static ArrayList<Host> otherHosts = new ArrayList<Host>();
	
	/**
	 * Start the discovery method for finding hosts
	 * Subclasses must periodically update otherHosts data
	 */
	public abstract void startDiscovery();

	/**
	 * Stop the discovery method for finding hosts
	 */
	public abstract void stopDiscovery();
	

	/**
	 * Set this host 
	 */
	static {
		thisHost = Host.getLocalHostAddresAndIP();
		if (thisHost == null || thisHost.getHostIP() == null || thisHost.getHostIP().length() == 0) {
			Log.e(Globals.class.toString(), "No IP! Exiting...");
			System.exit(1);
		}
	}
}

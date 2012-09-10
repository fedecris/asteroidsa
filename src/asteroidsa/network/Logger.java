package asteroidsa.network;

import android.util.Log;
import asteroidsa.network.discovery.HostDiscovery;

public class Logger {

	/** Log identifier for NetworkDCQ (Discovery, Communication & QoS) */
	public static final String LOG_TAG = "NetworkDCQ";

	/**
	 * Sends a custom log DEBUG message
	 * @param message message to be logged
	 */
	public static void d(String message) {
		Log.d(LOG_TAG, formatMsg(message));
	}

	/**
	 * Sends a custom log WARN message
	 * @param message message to be logged
	 */
	public static void w(String message) {
		Log.w(LOG_TAG, formatMsg(message));
	}

	/**
	 * Sends a custom log ERROR message
	 * @param message message to be logged
	 */
	public static void e(String message) {
		Log.e(LOG_TAG, formatMsg(message));
	}
	
	/**
	 * Sends a custom log INFO message
	 * @param message message to be logged
	 */
	public static void i(String message) {
		Log.i(LOG_TAG, formatMsg(message));
	}

	/**
	 * Sends a custom log VERBOSE message
	 * @param message message to be logged
	 */
	public static void v(String message) {
		Log.v(LOG_TAG, formatMsg(message));
	}

	/**
	 * Adds aditional info to the message
	 * @param message original message to be logged
	 * @return augmented message
	 */
	protected static String formatMsg(String message) {
		return (new StringBuffer("[").append(HostDiscovery.thisHost).append("] ").append(message)).toString();
	}
}

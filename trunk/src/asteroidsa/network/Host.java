package asteroidsa.network;

import java.io.Serializable;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class Host implements Serializable {

	/** Serial Version UID */
	private static final long serialVersionUID = 2807206367538864478L;
	/** Host IP */
	private String hostIP;
	/** Current status (online or offline) */
	private boolean connected;
	
	
	public String toString() {
	    return " (" + hostIP + ") - " + (connected?"Online":"Offline");
	}

    public String getHostIP() {
        return hostIP;
    }

    public void setHostIP(String hostIP) {
        this.hostIP = hostIP;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
    
    public Host(String hostIP, boolean connected) {
        this.hostIP = hostIP;
        this.connected = connected;
    }

    /** 
     * Especificación de equals
     */
    public boolean equals(Object anObject) {
    	if (anObject == null)
    		return false;
    	
    	Host host = (Host)anObject;
    	if (getHostIP() == null && host.getHostIP() != null ||
    		getHostIP() != null && host.getHostIP() == null)
    	return false;
    	
    	return getHostIP().equals(((Host)host).getHostIP());
    }

    
    /**
     * Obtains this host IP
     * @returns IPv4 or null otherwise
     */
    public static Host getLocalHostAddresAndIP() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        // IPv4 only
                        if (inetAddress instanceof Inet6Address)
                                continue;
                    if (!inetAddress.isLoopbackAddress()) {
                        Host thisHost = new Host(inetAddress.getHostAddress().toString(), true);
                        return thisHost;
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    
}

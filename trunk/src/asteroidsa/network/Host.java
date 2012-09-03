package asteroidsa.network;

import java.io.Serializable;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class Host implements Serializable {

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
     * Determinar este host IP.
     * @returns IP (solo version 4) o null en caso contrario
     */
    public static Host getLocalHostAddresAndIP() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        // Solo IPv4
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

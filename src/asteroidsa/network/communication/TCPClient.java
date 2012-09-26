package asteroidsa.network.communication;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import asteroidsa.network.Host;
import asteroidsa.network.Logger;
import asteroidsa.network.NetworkApplicationData;
import asteroidsa.network.NetworkStartup;
import asteroidsa.network.discovery.HostDiscovery;

public class TCPClient extends TCPNetwork {

	/** Is this client already connected to a server? */
	protected boolean connected = false;
	
    /**
     * Constructor
     * @param ip del server al cual conectar
     */
    public TCPClient(String ip) {
        this.host = ip;
        this.port = TCP_PORT;
    }
    
    /**
     * Connects to the specified server
     * @return true if connection was successful, false otherwise
     */
    public boolean connect() {
    	Logger.i("Connecting to:" + host);
        try {
            socket = new Socket(host, port);
            toBuffer = new ObjectOutputStream(socket.getOutputStream());
            fromBuffer = new ObjectInputStream(socket.getInputStream()); 
            connected = true;
        }
        catch (Exception ex) { 
        	Logger.e(ex.getMessage());
            connected = false;
        } 
        return connected;
    }  
   
    /**
     * Prepares and send a message to the otherHosts
     * @param networkGameData message to send
     * @return true si pudo ser enviado o false en caso contrario
     */
    public void sendMessage(NetworkApplicationData networkGameData) throws Exception {
    	if (!connected) {
    		Logger.e("Cannot send message. Not connected to host:" + host);
    		return;
    	}
    	try {
    		write(networkGameData);
    	}
    	catch (Exception e) {
            // Tell the app that the connection with the host is lost
        	NetworkStartup.getCommunication().getConsumer().byeHost(new Host(host, false));
        	HostDiscovery.removeHost(host);
        	throw e;
    	}
    }

	/**
	 * Default Getter
	 * @return true if this server is already connected to a server or false otherwise
	 */
	public boolean isConnected() {
		return connected;
	}

	
}
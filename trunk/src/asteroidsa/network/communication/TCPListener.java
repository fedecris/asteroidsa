package asteroidsa.network.communication;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;

import asteroidsa.network.Logger;
import asteroidsa.network.NetworkApplicationData;



public class TCPListener extends TCPNetwork implements Runnable {
            
	/** The observed instance */
	NetworkCommunication networkCommInstance = null;
	
    /**
     * Creates TCP server connection
     */
    public TCPListener() {
        try {
            port = TCP_PORT;
            serverConn = new ServerSocket(port);   
        }
        catch (Exception ex) { 
        	Logger.e("Error al instanciar NetworkServer(): " + ex.getMessage()); 
        }
        
    }

    /**
     * Main loop, receives and notifies incoming messages
     */
    public synchronized void run() {
    	if (!listen()) {
    		Logger.e("Could not listen");
    		System.exit(1);
    	}
    		
        while (true) {
            try {
                // Wait for incoming messages
            	networkApplicationData = (NetworkApplicationData)receive();
                if (networkApplicationData == null) {
                	Logger.w("Received message is null");
                    continue;
                }

                // Update observed object data
                if (networkCommInstance == null)
                	networkCommInstance = NetworkCommunicationFactory.getNetworkCommunication(NetworkCommunicationFactory.getDefaultNetworkCommunication());
                networkCommInstance.setNetworkApplicationData(networkApplicationData);
                networkCommInstance.notifyNewMessage();
                
            }
            catch (Exception e) { 
            	Logger.e("Error en run de NetworkServer(): " + e.getMessage());
            }
        }
    }
    
    /**
     * Restarts sever socket
     */
    public void restartServer() {
        try {
                closeServer();
                serverConn = new ServerSocket(port);
        }
        catch (Exception e) { 
        	Logger.e("Error en restartServer(): " + e.getMessage()); 
        }
    }
    
    /**
     * Waits for new connections
     */
    public boolean listen() {
        try {   
            socket = serverConn.accept();
            toBuffer = new ObjectOutputStream(socket.getOutputStream());
            toBuffer.flush();
            fromBuffer = new ObjectInputStream(socket.getInputStream());
            return true;
        }
        catch (Exception ex) { 
        	Logger.e("Error en listen de NetworkServer(): " + ex.getMessage());
            return false;
        }
    }  
    

}
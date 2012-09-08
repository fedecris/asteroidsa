package asteroidsa.network.communication;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;

import asteroidsa.network.Logger;



public class TCPListener extends TCPNetwork implements Runnable {
            
	/** The observed instance */
	NetworkCommunication networkCommInstance = null;
	
    /**
     * Creates the TCP ServerSocket
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
     * Main loop, listens for new connection requests
     */
    public synchronized void run() {
    	while (true) {
    		listen();
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
     * Waits for new connections and spawns a new thread each time
     */
    public boolean listen() {
        try {   
            socket = serverConn.accept();
            toBuffer = new ObjectOutputStream(socket.getOutputStream());
            toBuffer.flush();
            fromBuffer = new ObjectInputStream(socket.getInputStream());
            new Thread(new TCPServer(socket, fromBuffer, toBuffer)).start();
            return true;
        }
        catch (Exception ex) { 
        	Logger.e("Error en listen de NetworkServer(): " + ex.getMessage());
            return false;
        }
    }  
    

}
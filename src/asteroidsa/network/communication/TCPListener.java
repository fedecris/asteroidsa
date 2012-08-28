package asteroidsa.network.communication;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;

import asteroidsa.network.NetworkApplicationData;



public class TCPListener extends TCPNetwork implements Runnable {
            
    /**
     * Crea la conexion server a fin de escruchar mensajes etrantes
     * @param puerto por el cual entraran los mensajes
     */
    public TCPListener(int puerto) {
        try {
            port = puerto;
            serverConn = new ServerSocket(port);   
        }
        catch (Exception ex) { System.err.println ("Error en instanciar NetworkServer: " + ex.getMessage()); }
    }

    /**
     * Main loop, receives and notifies incoming messages
     */
    public synchronized void run() {
    	if (!listen())
    		System.exit(1);

    	
        while   (true) {
            try {
                // Listen for incoming messages
                NetworkApplicationData newMessage = (NetworkApplicationData)receive();
                if (newMessage == null)
                    continue;

                // TODO: NOTIFY INCOMING MESSAGE!!!

            }
            catch (Exception e) { System.err.println("Error en run de NetworkServer: "+e.getMessage()); }
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
        catch (Exception e) { System.err.println("Error en restartServer():" + e.getMessage()); }
    }
    
    /**
     * Esperar la recepción de mensajes y setear los buffers correspondientes
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
            System.err.println ("Error en listen de NetworkServer: " + ex.getMessage());
            return false;
        }
    }  
    
  
    public NetworkApplicationData getMessage() {
        return networkGameData;
    }

    public void setMessage(NetworkApplicationData networkGameData) {
        this.networkGameData = networkGameData;
    }
}
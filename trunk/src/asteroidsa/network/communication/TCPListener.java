package asteroidsa.network.communication;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;

import android.util.Log;
import asteroidsa.network.Logger;
import asteroidsa.network.NetworkApplicationData;



public class TCPListener extends TCPNetwork implements Runnable {
            
    /**
     * Crea la conexion server a fin de escuchar mensajes entrantes
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
    	if (!listen())
    		System.exit(1);

    	
        while   (true) {
            try {
                // Listen for incoming messages
                NetworkApplicationData newMessage = (NetworkApplicationData)receive();
                if (newMessage == null)
                    continue;

                networkApplicationData.copy(newMessage);
                networkApplicationData.notifyNewMessage();

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
        	Logger.e("Error en listen de NetworkServer(): " + ex.getMessage());
            return false;
        }
    }  
    

}
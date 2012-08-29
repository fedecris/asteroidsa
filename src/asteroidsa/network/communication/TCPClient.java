package asteroidsa.network.communication;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import asteroidsa.network.NetworkApplicationData;

public class TCPClient extends TCPNetwork implements Runnable {

	/** Is this client connected to a server listening? */
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
     * Intenta conectar con el servidor a fin de enviar el mensaje
     * @return true si fue posible la conexion o false en caso contrario
     */
    public boolean connect() {
        try {
            socket = new Socket(host, port);
            toBuffer = new ObjectOutputStream(socket.getOutputStream());
            toBuffer.flush();
            fromBuffer = new ObjectInputStream(socket.getInputStream()); 
            connected = true;
        }
        catch (Exception ex) { 
            System.err.println ("Error en NetworkClient: " +ex.getMessage());
            connected = false;
        } 
        return connected;
    }  
   
     /**
     * Prepares and send a message to the otherHosts
     * @param networkGameData message to send
     * @return true si pudo ser enviado o false en caso contrario
     */
    public boolean sendMessage(NetworkApplicationData networkGameData) {
    	return write(networkGameData);
    }

    
    /**
     * Send local application state
     */
	public void run() {
		sendMessage(networkGameData);
	}

	
	public boolean isConnected() {
		return connected;
	}

	
}
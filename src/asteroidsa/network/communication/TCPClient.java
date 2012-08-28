package asteroidsa.network.communication;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import asteroidsa.network.NetworkApplicationData;

public class TCPClient extends TCPNetwork implements Runnable {

    /**
     * Constructor
     * @param ip del server al cual conectar
     * @param puerto al cual conectar
     */
    public TCPClient(String ip, int puerto) {
        this.host = ip;
        this.port = puerto;
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
            return true;
        }
        catch (Exception ex) { 
            System.err.println ("Error en NetworkClient: " +ex.getMessage()); 
            return false;
        } 
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
     * Periodically send local application state
     */
	public void run() {

		// Intentar conectar
		while (!connect());
		
		while (true) {
			// Update local state

			// TODO: UPDATE LOCAL DATA BEFORE SEND
			sendMessage(networkGameData);
			
			try {
				Thread.sleep(30);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}        
     
}
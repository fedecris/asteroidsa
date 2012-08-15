package ar.com.federicocristina.asteroidsa.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import ar.com.federicocristina.asteroidsa.utils.Globals;

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
     * Prepara y envia un mensaje al destinatario previamente configurado
     * @param networkGameData mensaje a enviar
     * @return true si pudo ser enviado o false en caso contrario
     */
    public boolean sendMessage(NetworkGameData networkGameData) {
    	return write(networkGameData);
    }

	public void run() {

		// Intentar conectar
		while (!connect());
		
		while (true) {
			// Actualizar el estado de la nave a enviar
			networkGameData = new NetworkGameData(new Host(Globals.thisHost.getHostIP(), true), Globals.starShip);
			sendMessage(networkGameData);
			
			try {
				Thread.sleep(30);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}        
     
}
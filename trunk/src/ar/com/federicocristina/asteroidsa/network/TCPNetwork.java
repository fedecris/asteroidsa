package asteroidsa.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.util.Log;


public class TCPNetwork {

    /** Host y puerto de conexion */
    protected String host;
    protected int port;     
    
    /** Socket cliente y servidor */
    protected Socket socket;
    protected ServerSocket serverConn;
    
    /** Lectura / escritura del buffer */
    protected ObjectOutputStream toBuffer = null;
    protected ObjectInputStream fromBuffer = null;
    
    /** Mensaje de envio o recepción */
    protected NetworkGameData networkGameData;

    /**
     * Escribe datos a la salida
     * @param datos
     */
    public boolean write(NetworkGameData datos) {
        try {
            toBuffer.writeObject(datos);
            toBuffer.flush();
            return true;
        }
        catch (Exception ex) { 
            Log.w("FEDE", ex.getMessage());
            return false;
        }
    }   
    
    /**
     * Lee datos de la entrada
     */
    public NetworkGameData receive() {
        NetworkGameData datos = null;
        try {
            datos = (NetworkGameData)fromBuffer.readObject();
        }   
        catch (Exception ex) { 
            Log.w("FEDE", ex.getMessage()); 
        }
        return datos;
    }  
    
    /**
     * Cierra el socket 
     */
    public void close() {
        try {       
            socket.close();
        }
        catch (Exception ex) {
            Log.w("FEDE", ex.getMessage()); 
        }      
    }
    
    /**
     * Cierra el server socket 
     */
    public void closeServer() {
        try {       
            socket.close();
            serverConn.close();
        }
        catch (Exception ex) {
            Log.w("FEDE", ex.getMessage()); 
        }      
    }
    
        
}

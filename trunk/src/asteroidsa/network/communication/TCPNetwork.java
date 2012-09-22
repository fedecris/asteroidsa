package asteroidsa.network.communication;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import asteroidsa.network.Logger;
import asteroidsa.network.NetworkApplicationData;


public class TCPNetwork extends TCPCommunication {

	/** TCP Port */
	public static final int TCP_PORT = 9999;
	
    /** Configured Host IP */
    protected String host;
    /** Configured Host port */
    protected int port;     

    /** Client socket */
    protected Socket socket;
    /** Server socket */
    protected ServerSocket serverConn;
    
    /** Buffer for writing objects */
    protected ObjectOutputStream toBuffer = null;
    /** Buffer for reading objects */
    protected ObjectInputStream fromBuffer = null;

	/**
     * Writes an object to the stream
     * @param data object to be written
     */
    public boolean write(NetworkApplicationData data) {
        try {
            toBuffer.writeObject(data);
            toBuffer.flush();
            return true;
        }
        catch (Exception ex) { 
        	Logger.w(ex.getMessage());
            return false;
        }
    }   
    
    /**
     * Reads the next object from the stream
     * @return received data or null otherwise
     */
    int exceptionCounter = 0;
    public NetworkApplicationData receive() {
        NetworkApplicationData data = null;
        try {
        	data = (NetworkApplicationData)fromBuffer.readObject();
        }   
        catch (Exception ex) {
        	exceptionCounter++;
        	if (exceptionCounter%100==0)
        		Logger.w("Exception reading object:" + ex.getMessage()); 
        }
        return data;
    }  
    
    /**
     * Closes the socket 
     */
    public void close() {
        try {       
            socket.close();
        }
        catch (Exception ex) {
        	Logger.w(ex.getMessage()); 
        }      
    }
    
    /**
     * Closes the server socket 
     */
    public void closeServer() {
        try {       
            socket.close();
            serverConn.close();
        }
        catch (Exception ex) {
            Logger.w(ex.getMessage()); 
        }      
    }
    


    
}

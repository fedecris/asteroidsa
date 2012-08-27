package asteroidsa.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;

import asteroidsa.model.LaserBeam;
import asteroidsa.model.StarShip;
import asteroidsa.utils.Globals;



public class TCPServer extends TCPNetwork implements Runnable {
            
    /**
     * Crea la conexion server a fin de escruchar mensajes etrantes
     * @param puerto por el cual entraran los mensajes
     */
    public TCPServer(int puerto) {
        try {
            port = puerto;
            serverConn = new ServerSocket(port);   
        }
        catch (Exception ex) { System.err.println ("Error en instanciar NetworkServer: " + ex.getMessage()); }
    }

    /**
     * Loop general de la aplicación de escucha de mensajes
     * Queda en espera de mensajes entrantes, luego itera indefinidamente
     */
    public synchronized void run() {
    	if (!listen())
    		System.exit(1);

    	
        while   (true) {
            try {
                // Escuchar conexiones entrantes
                NetworkGameData newMessage = (NetworkGameData)receive();
                if (newMessage == null)
                    continue;

                StarShip remoteShip = null;
            	// Si no existe el host remoto, agregarlo a la nomina local de globals
            	if (!Globals.otherHosts.contains(newMessage.getRemoteHost())) {
            		// Agregarlo (ByeBye MVC... muejeje)
        	    	Globals.otherHosts.add(newMessage.getRemoteHost());
        	    	remoteShip = new StarShip();
        	    	remoteShip.position = newMessage.position;
        	    	remoteShip.vector = newMessage.vector;
        	    	remoteShip.heading = newMessage.heading;
        	    	Globals.otherShips.add(remoteShip);
        	    }
        	    else {
        	    	// Recuperar host remoto y actualizar la nave remota en globals
        	    	int pos = Globals.otherHosts.indexOf(newMessage.getRemoteHost());
        	    	remoteShip = Globals.otherShips.get(pos);
        	    	remoteShip.position.x = newMessage.position.x;
        	    	remoteShip.position.y = newMessage.position.y;
        	    	remoteShip.vector.x = newMessage.vector.x;
        	    	remoteShip.vector.y = newMessage.vector.y;
        	    	remoteShip.heading = newMessage.heading;
        	    }
            	// Process laser hosts!
            	int i=0;
            	for (LaserBeam remoteLaserBeam : remoteShip.ammo) {
            		remoteLaserBeam.active = newMessage.shotActive[i];
            		remoteLaserBeam.position.x = newMessage.shotPosition[i].x;
            		remoteLaserBeam.position.y = newMessage.shotPosition[i].y;
            		remoteLaserBeam.vector.x = newMessage.shotVector[i].x;
            		remoteLaserBeam.vector.y = newMessage.shotVector[i].y;
            		remoteLaserBeam.heading = newMessage.shotHeading[i];
            		
            		// Hit?
            		if ( ((remoteLaserBeam.position.x - Globals.starShip.position.x)*(remoteLaserBeam.position.x - Globals.starShip.position.x) + 
            			  (remoteLaserBeam.position.y - Globals.starShip.position.y)*(remoteLaserBeam.position.y - Globals.starShip.position.y)) 
            			   < Globals.starShip.width/4*Globals.starShip.width/4) {
            			 Globals.lifeLost();
            			 continue;
            		}
            		
            		i++;
            	}
            }
            catch (Exception e) { System.err.println("Error en run de NetworkServer: "+e.getMessage()); }
        }
    }
    
    /**
     * Reinicia el sever socket
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
    
   
    /** --- GETTERS Y SETTERS */ 
    public NetworkGameData getMessage() {
        return networkGameData;
    }

    public void setMessage(NetworkGameData networkGameData) {
        this.networkGameData = networkGameData;
    }
}
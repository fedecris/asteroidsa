package asteroidsa.network.discovery;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import asteroidsa.utils.Globals;

public class UDPClient extends UDPDiscovery implements Runnable {
	
	/** Intervalo de envio de estado */
	public static final int PING_INTERVAL_MS = 1000;
	/** Paquete de estado a enviar */
	protected DatagramPacket packet = null;
	/** Grupo de hosts */
	protected InetAddress group = null;
	/** Envio broadcast a otros hosts */ 
	protected MulticastSocket socket = null;
	
	public void run() {
		
        try {
        	
        	group = InetAddress.getByName(UDP_GROUP);
            socket = new MulticastSocket(UDP_PORT);
        	
        	while (Globals.running) {

        		// enviar status actual
	    		sendPing();

	            // actualizar el status cada cierto intervalo de tiempo
                Thread.sleep(PING_INTERVAL_MS);
	    	}
	    }
        catch (InterruptedException e) { e.printStackTrace(); }
	    catch (IOException e) { e.printStackTrace(); }
	    catch (Exception e) { e.printStackTrace(); }
	    
		try {
			sendPing();
			if (socket != null)
				socket.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	protected void sendPing() throws Exception {
		// enviar mi estado actual
        byte[] buf = new byte[256];
        String dString = null;
        dString = Globals.thisHost.getHostIP() + "" + (Globals.thisHost.isConnected()?"Y":"N"); 
        buf = dString.getBytes();

        packet = new DatagramPacket(buf, buf.length, group, UDP_PORT);
        socket.send(packet);
	}
	
}


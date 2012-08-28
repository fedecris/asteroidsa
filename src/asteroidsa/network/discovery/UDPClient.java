package asteroidsa.network.discovery;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

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
        	
        	// Get UDP group and socket 
        	group = InetAddress.getByName(UDP_GROUP);
            socket = new MulticastSocket(UDP_PORT);
        	
        	while (running) {

        		// Send current status
	    		sendPing();

	            // Sleep for a while and resend status
                Thread.sleep(PING_INTERVAL_MS);
	    	}
	    }
        catch (InterruptedException e) { 
        	e.printStackTrace(); 
        }
	    catch (IOException e) { 
	    	e.printStackTrace(); 
	    }
	    catch (Exception e) { 
	    	e.printStackTrace(); 
	    }
	    
		try {
			sendPing();
			if (socket != null)
				socket.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends datagram with host information (IP-Active(Y/N))
	 * @throws Exception
	 */
	protected void sendPing() throws Exception {
		// Send current status
        byte[] buf = new byte[BUFFER_SIZE];
        String dString = thisHost.getHostIP() + DATAGRAM_FIELD_SPLIT + (thisHost.isConnected()?"Y":"N"); 
        buf = dString.getBytes();
        packet = new DatagramPacket(buf, buf.length, group, UDP_PORT);
        socket.send(packet);
	}
	
}


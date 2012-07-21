package ar.com.federicocristina.asteroidsa.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import ar.com.federicocristina.asteroidsa.utils.Globals;

public class UDPClient extends Thread {
	
	protected DatagramSocket socket = null;
	
	public void run() {
	    while (Globals.running) {
	        try {
	    		// enviar status actual
	    		sendPing();

	            // actualizar el status cada cierto intervalo de tiempo
                sleep(50);
	        }
            catch (InterruptedException e) { e.printStackTrace(); }
	        catch (IOException e) { e.printStackTrace(); }
	        catch (Exception e) { e.printStackTrace(); }
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
	
	
	protected void sendPing() throws Exception {
		// enviar mi estado actual
        byte[] buf = new byte[256];
        String dString = null;
        dString = Globals.thisHost.getHostIP() + "::" + 
        			Globals.starShip.heading + "::" + 
        			Globals.starShip.position.x + "::" +  
        			Globals.starShip.position.y + "::" + 
        			Globals.starShip.vector.x + "::" + 
        			Globals.starShip.vector.y;
        buf = dString.getBytes();

        InetAddress group = InetAddress.getByName(Globals.GROUP_IP);
        DatagramPacket packet;
        packet = new DatagramPacket(buf, buf.length, group, Globals.PORT_UDP);
        MulticastSocket socket = new MulticastSocket(Globals.PORT_UDP);
        socket.send(packet);
	}
	
}


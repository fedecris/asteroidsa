package asteroidsa.network.discovery;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import asteroidsa.model.StarShip;
import asteroidsa.network.Host;
import asteroidsa.utils.Globals;
import asteroidsa.utils.Point2Df;

public class UDPListener extends UDPDiscovery implements Runnable {
	
	public void run() {
		try {
			MulticastSocket socket = new MulticastSocket(UDP_PORT);
			InetAddress group = InetAddress.getByName(UDP_GROUP);
			socket.joinGroup(group);

			DatagramPacket packet;
			while (Globals.running) {
			    byte[] buf = new byte[256];
			    packet = new DatagramPacket(buf, buf.length);
			    socket.receive(packet);

			    String received = new String(packet.getData());
			    managePing(received);
			}
			socket.leaveGroup(group);
			socket.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Proceso los mensajes recibidos
	 * @param received String que contiene IP:heading:xpos:ypos:xvec:yvec
	 */
	protected void managePing(String received) {
		// parsear el ping con el host y estado
	    String[] values = received.split("::");
	    Host host = new Host(values[0], true);
	    StarShip otherShip = null;
	    
	    // Omitir mismo host
	    if (Globals.thisHost.equals(host))
	    	return;
	    
	    // Log.d(Globals.LOG_TAG, "["+Globals.thisHost.getHostIP()+"] ping recibido desde: "+values[0]);
	    
	    // Procesar info de otros hosts
	    // ya existia el host?
	    if (!Globals.otherHosts.contains(host)) {
	    	Globals.otherHosts.add(host);

	    	// ByeBye MVC... muejeje
	    	otherShip = new StarShip();
	    	Globals.otherShips.add(otherShip);
	    }
	    else
	    {
	    	int pos = Globals.otherHosts.indexOf(host);
	    	otherShip = Globals.otherShips.get(pos);
	    }

	    // Actualizar estado de la otra navecita
	    otherShip.heading = Float.parseFloat(values[1]);
	    otherShip.position = new Point2Df(Float.parseFloat(values[2]), Float.parseFloat(values[3]));
	    otherShip.vector = new Point2Df(Float.parseFloat(values[4]), Float.parseFloat(values[5]));
	}

}
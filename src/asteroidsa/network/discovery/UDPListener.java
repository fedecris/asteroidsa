package asteroidsa.network.discovery;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import asteroidsa.network.Host;
import asteroidsa.network.communication.NetworkCommunication;
import asteroidsa.network.communication.NetworkCommunicationFactory;

public class UDPListener extends UDPDiscovery implements Runnable {
	
	public void run() {
		try {
			MulticastSocket socket = new MulticastSocket(UDP_PORT);
			InetAddress group = InetAddress.getByName(UDP_GROUP);
			socket.joinGroup(group);

			DatagramPacket packet;
			while (running) {
				// Receive datagramas
			    byte[] buf = new byte[BUFFER_SIZE];
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
	 * Received datagramas processing.  Updates hosts list.
	 * @param received String containing IP:..:active(Y/N)
	 */
	protected void managePing(String received) {
		// Parse the datagram
	    String[] values = received.split(DATAGRAM_FIELD_SPLIT);
	    Host host = new Host(values[0], "Y".equals(values[1])?true:false);
	    
	    // Omit this host
	    if (thisHost.equals(host))
	    	return;
	    
	    // Is the host already included in the list?
	    if (!otherHosts.contains(host.getHostIP())) {
	    	otherHosts.add(host.getHostIP());
	    	NetworkCommunication networkComm = NetworkCommunicationFactory.getNetworkCommunication(NetworkCommunicationFactory.getDefaultNetworkCommunication());
	    	networkComm.connectToServerHost(host);
	    }
	    else {
	    	otherHosts.set(otherHosts.indexOf(host.getHostIP()), host.getHostIP());
	    }
	}

}
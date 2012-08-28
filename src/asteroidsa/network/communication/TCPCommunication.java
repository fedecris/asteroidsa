package asteroidsa.network.communication;

import java.util.ArrayList;

import asteroidsa.network.discovery.HostDiscovery;

public class TCPCommunication {

	// TCP Port
	public static final int TCP_PORT = 9999;
	
	
	public static ArrayList<TCPClient> commPool = new ArrayList<TCPClient>();
	
	
	public void startCommunication() {
        // Iniciar Server
        TCPListener server = new TCPListener(TCP_PORT);
        Thread serverRun = new Thread(server);
        serverRun.start();

        // TODO: SHOULD ITERATE hostList and create a connection pool
//        String host1IP = "192.168.1.121";
//        String host2IP = "192.168.1.108";
//        TCPClient client = new TCPClient(host1IP.equals(HostDiscovery.thisHost.getHostIP())?host2IP:host1IP, TCP_PORT);
        Thread clientRun = new Thread(client);
        clientRun.start();


	}
	
}

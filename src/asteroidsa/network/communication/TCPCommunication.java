package asteroidsa.network.communication;

import java.util.HashMap;

import asteroidsa.network.Host;
import asteroidsa.network.Logger;
import asteroidsa.network.NetworkApplicationData;

public class TCPCommunication extends NetworkCommunication {

	/** TCP Listener */
	protected static TCPListener listener = null;
	/** TCP Clients: Target Host IP - TCPConnection */
	protected static HashMap<String, TCPClient> clientPool = new HashMap<String, TCPClient>();
	
	
	@Override
	public boolean startService(NetworkApplicationData networkApplicationData) {

		try {
	        // Create a new listener (server)
	        listener = new TCPListener();
	        // Starts the listener in a new thread
	        new Thread(listener).start();
	        return true;
		} 
		catch (Exception e) {
			Logger.e("Error en startListener(): " + e.getMessage());
			return false;
		}
	}


	@Override
	public boolean connectToServerHost(Host target) {
		if (clientPool.get(target.getHostIP()) == null) 
			clientPool.put(target.getHostIP(), new TCPClient(target.getHostIP()));

		TCPClient client = clientPool.get(target.getHostIP());
		return client.isConnected() || client.connect();
	}
	
	
	@Override
	public boolean sendMessage(String targetIP, NetworkApplicationData data) {

		TCPClient client = clientPool.get(targetIP);	
		if (client==null) {
			Logger.e("Error en sendMessage(): client is null");
			return false;
		}
		
		if (!client.isConnected() && !client.connect()) {
			Logger.e("Error en sendMessage(): cannot connect to client!");
			return false;	
		}
		
		return client.sendMessage(data);
	}

	
	@Override
	public boolean sendMessageToAllHosts(NetworkApplicationData data) {
		for (String targetIP : clientPool.keySet()) 
			if (!sendMessage(targetIP, data))
				return false;
		return true;
	}

	




}

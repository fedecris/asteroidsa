package asteroidsa.network.communication;

import java.util.HashMap;

import asteroidsa.network.Host;
import asteroidsa.network.NetworkApplicationData;

public class TCPCommunication implements NetworkCommunication {

	/** TCP Listener */
	public static TCPListener listener = null;
	/** TCP Clients: Target Host - TCPConnection */
	public static HashMap<Host, TCPClient> clientPool = new HashMap<Host, TCPClient>();
    /** Mensaje de envio o recepción */
    protected NetworkApplicationData networkApplicationData = null;
	
	
	@Override
	public void startListener() {
        // Start listener
        listener = new TCPListener();
        Thread serverRun = new Thread(listener);
        serverRun.start();
	}


	@Override
	public void connectToServerHost(Host target) {
		if (clientPool.get(target) == null) 
			clientPool.put(target, new TCPClient(target.getHostIP()));

		TCPClient client = clientPool.get(target);
		client.connect();
	}
	
	
	@Override
	public void sendMessage(Host target, NetworkApplicationData data) {
		// Add to pool if not exists
		if (clientPool.get(target) == null) 
			return;	// TODO: throw exception? log? return false? etc.
		
		sendAsyncMsg(target, data);
	}

	
	@Override
	public void sendMessageToAllHosts(NetworkApplicationData data) {
		for (Host target : clientPool.keySet())
			sendAsyncMsg(target, data);
	}

	
	
	/**
	 * Creates a new thread for each message to send
	 * @param target target host
	 * @param data message to send
	 */
	protected void sendAsyncMsg(Host target, NetworkApplicationData data) {
		
		// Set info to send
		TCPClient client = clientPool.get(target);
		
		if (!client.isConnected() && !client.connect())
			return;	// TODO: ACA HAY QUE LOGGEAR EL PROBLEMA
		
		client.setNetworkApplicationData(data);
		client.sendMessage(data);
		// Start an independient thread to send data
	//	Thread clientThread = new Thread(client);
	//	clientThread.start();
	}
	

	
    public NetworkApplicationData getNetworkApplicationData() {
		return networkApplicationData;
	}

	public void setNetworkApplicationData(NetworkApplicationData networkApplicationData) {
		this.networkApplicationData = networkApplicationData;
	}



}

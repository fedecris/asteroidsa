package asteroidsa.network.communication;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import asteroidsa.network.Host;
import asteroidsa.network.Logger;
import asteroidsa.network.NetworkApplicationData;
import asteroidsa.network.discovery.HostDiscovery;

public class TCPCommunication extends NetworkCommunication implements Runnable{

	/** TCP Listener */
	protected static TCPListener listener = null;
	/** TCP Clients: Target Host IP - TCPConnection */
	protected static ConcurrentHashMap<String, TCPClient> clientPool = new ConcurrentHashMap<String, TCPClient>();
	/** Communication listener is running */
	protected static boolean listenerRunning = false;
	/** Communication broadcast is running */
	protected static boolean broadcastRunning = false;
	// Local keySet of client pool (IPs)
	private Set<String> localClientPool = null;
	
	@Override
	public boolean startService() {

		try {
	        // Create a new listener (server)
			listenerRunning = true;
	        listener = new TCPListener();
	        Logger.i("Nuevo TCPListener");
	        // Starts the listener in a new thread
	        new Thread(listener).start();
	        return true;
		} 
		catch (Exception e) {
			Logger.e(e.getMessage());
			return false;
		}
	}

	
	@Override
	public boolean startBroadcast() {

		try {
	        // Create the client for broadcasting
			broadcastRunning = true;
			new Thread(this).start();
	        return true;
		} 
		catch (Exception e) {
			Logger.e(e.getMessage());
			return false;
		}
	}
	
	
	@Override
	public boolean stopService() {
		listenerRunning = false;
		return false;
	}
	
	@Override
	public boolean stopBroadcast() {
		broadcastRunning = false;
		return false;
	}


	@Override
	public boolean connectToServerHost(Host target) {
		if (clientPool.get(target.getHostIP()) == null) 
			clientPool.put(target.getHostIP(), new TCPClient(target.getHostIP()));

		TCPClient client = clientPool.get(target.getHostIP());
		if (client!=null) {
			client.connect();
			return client.connected;
		}
		return false; 
	}
	
	@Override
	public synchronized void sendMessage(String targetIP, NetworkApplicationData data) {

		// dont send nulls
		if (data==null)
			return;
		// get client from pool
		if (clientPool.get(targetIP)==null) {
			Logger.e("Client is null");
			return;
		}
		if (!clientPool.get(targetIP).connected) {
			Logger.w("Client not connected!");
			return;
		}
		try {
			clientPool.get(targetIP).sendMessage(data);
		}
		catch (Exception e) {
			clientPool.remove(targetIP);
		}
	}

	
	@Override
	public synchronized void sendMessageToAllHosts(NetworkApplicationData data) {
		localClientPool = clientPool.keySet();		// FIXME: Causes GC
		for (String targetIP : localClientPool) {
			if (HostDiscovery.otherHosts.get(targetIP)!=null && HostDiscovery.otherHosts.get(targetIP).isOnLine())
				sendMessage(targetIP, data);
		}
	}


    /** 
     * In charge of sending local status to the other hosts periodically
     */
    public void run() {
    	while (broadcastRunning) {
    		if (clientPool.size() > 0)
    			sendMessageToAllHosts(producer.produceNetworkApplicationData());
        	try {
        		Thread.sleep(BROADCAST_LOCAL_STATUS_INTERVAL_MS);
        	}
        	catch (Exception e) { 
        		Logger.w(e.getMessage()); 
        	}
        }
    }


}

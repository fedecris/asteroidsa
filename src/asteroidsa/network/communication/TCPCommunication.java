package asteroidsa.network.communication;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import asteroidsa.network.Host;
import asteroidsa.network.Logger;
import asteroidsa.network.NetworkApplicationData;

public class TCPCommunication extends NetworkCommunication implements Runnable{

	/** TCP Listener */
	protected static TCPListener listener = null;
	/** TCP Clients: Target Host IP - TCPConnection */
	protected static ConcurrentHashMap<String, TCPClient> clientPool = new ConcurrentHashMap<String, TCPClient>();
	/** Communication listener is running */
	protected static boolean listenerRunning = false;
	/** Communication broadcast is running */
	protected static boolean broadcastRunning = false;

	
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

		TCPClient client = clientPool.get(targetIP);	
		if (client==null) {
			Logger.e("Client is null");
			return;
		}
		if (!client.connected) {
			Logger.e("Client not connected!");
			return;
		}
		try {
			client.sendMessage(data);
		}
		catch (Exception e) {
			clientPool.remove(targetIP);
		}
	}

	
	@Override
	public synchronized void sendMessageToAllHosts(NetworkApplicationData data) {
		Set<String> keySet = clientPool.keySet();
		for (String targetIP : keySet) {
			sendMessage(targetIP, data);
		}
	}


    /** 
     * In charge of sending local status to the other hosts periodically
     */
    public void run() {
    	while (broadcastRunning) {
    		networkApplicationData = producer.produceNetworkApplicationData();
    		if (networkApplicationData==null) {
            	Logger.w("Message to be sent is null");
                continue;
    		}
    		if (clientPool.size() > 0)
    			sendMessageToAllHosts(networkApplicationData);
        	try {
        		Thread.sleep(BROADCAST_LOCAL_STATUS_INTERVAL_MS);
        	}
        	catch (Exception e) { 
        		Logger.w(e.getMessage()); 
        	}
        }
    }


}

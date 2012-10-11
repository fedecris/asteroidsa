package asteroidsa.network.communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import asteroidsa.network.Host;
import asteroidsa.network.Logger;
import asteroidsa.network.NetworkStartup;
import asteroidsa.network.discovery.HostDiscovery;

public class TCPServer extends TCPListener implements Runnable {

	/**
	 * Constructor
	 */
	public TCPServer(Socket socket, ObjectInputStream fromBuffer, ObjectOutputStream toBuffer) {
		Logger.i("Creating connection to: " + socket.getInetAddress());
		this.socket = socket;
		this.fromBuffer = fromBuffer;
		this.toBuffer = toBuffer;
	}

	
	/**
	 * Receives and notifies incoming messages
	 */
	public synchronized void run() {
		boolean ok = true;
        while (listenerRunning && ok) {
            try {
                // Wait for incoming messages
            	networkApplicationData = receive();
                if (networkApplicationData == null)
                    continue;

                // Update data to be consumed
                NetworkStartup.getCommunication().getConsumer().newData(networkApplicationData);
                networkApplicationData = null;
            }
            catch (IOException ex) {
                // Tell the app that the connection with the host is lost, or has too many errors
            	String ip = socket.getInetAddress().toString().substring(1);
            	NetworkStartup.getCommunication().getConsumer().byeHost(new Host(ip, false));
            	HostDiscovery.removeHost(ip);
            	ok = false;
            }
            catch (Exception e) { 
            	Logger.e(e.getMessage());
            }
        }
        try {
        	socket.close();
        }
        catch (Exception e) {
        	Logger.e(e.getMessage());
        }
    }
}
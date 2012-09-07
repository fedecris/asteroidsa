package asteroidsa.network.communication;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import asteroidsa.network.Logger;
import asteroidsa.network.NetworkApplicationData;

public class TCPServer extends TCPListener implements Runnable {

	
	public TCPServer(Socket socket, ObjectInputStream fromBuffer, ObjectOutputStream toBuffer) {
		this.socket = socket;
		this.fromBuffer = fromBuffer;
		this.toBuffer = toBuffer;
	}
	
	
	/**
	 * Receives and notifies incoming messages
	 */
	public synchronized void run() {
        while (true) {
            try {
                // Wait for incoming messages
            	networkApplicationData = (NetworkApplicationData)receive();
                if (networkApplicationData == null) {
                	Logger.w("Received message is null");
                    continue;
                }

                // Update observed object data
                if (networkCommInstance == null)
                	networkCommInstance = NetworkCommunicationFactory.getNetworkCommunication(NetworkCommunicationFactory.getDefaultNetworkCommunication());
                networkCommInstance.setNetworkApplicationData(networkApplicationData);
                networkCommInstance.notifyNewMessage();
                
            }
            catch (Exception e) { 
            	Logger.e("Error en run de NetworkServer(): " + e.getMessage());
            }
        }
    }
}

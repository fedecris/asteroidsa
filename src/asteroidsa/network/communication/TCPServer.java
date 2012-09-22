package asteroidsa.network.communication;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import asteroidsa.network.Logger;
import asteroidsa.network.NetworkApplicationData;

public class TCPServer extends TCPListener implements Runnable {

	/**
	 * Constructor
	 */
	public TCPServer(Socket socket, ObjectInputStream fromBuffer, ObjectOutputStream toBuffer) {
		this.socket = socket;
		this.fromBuffer = fromBuffer;
		this.toBuffer = toBuffer;
	}

	
	/**
	 * Receives and notifies incoming messages
	 */
	public synchronized void run() {
        while (listenerRunning) {
            try {
                // Wait for incoming messages
            	networkApplicationData = (NetworkApplicationData)receive();
                if (networkApplicationData == null)
                    continue;

                // Update data to be consumed
                if (networkCommInstance == null)
                	networkCommInstance = NetworkCommunicationFactory.getNetworkCommunication(NetworkCommunicationFactory.getDefaultNetworkCommunication());
                networkCommInstance.getConsumer().newData(networkApplicationData);
            }
            catch (Exception e) { 
            	Logger.e(e.getMessage());
            }
        }
    }
}

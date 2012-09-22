package asteroidsa.utils;

import asteroidsa.model.LaserBeam;
import asteroidsa.model.StarShip;
import asteroidsa.network.Host;
import asteroidsa.network.NetworkApplicationData;
import asteroidsa.network.NetworkApplicationDataConsumer;
import asteroidsa.network.communication.NetworkCommunication;
import asteroidsa.network.communication.NetworkCommunicationFactory;

public class AsteroidsNetworkConsumer implements NetworkApplicationDataConsumer {

	/**
	 * Updates model data depending on the received message
	 */
	public void newData(NetworkApplicationData data) {

		AsteroidsNetworkApplicationData message = (AsteroidsNetworkApplicationData)data;
		
        StarShip remoteShip = null;
    	// Retrieve ship remote IP and update accordingly
        if (Globals.otherShips.get(message.getSourceHost().getHostIP())==null)
        	Globals.otherShips.put(message.getSourceHost().getHostIP(), new StarShip());
       	remoteShip = Globals.otherShips.get(message.getSourceHost().getHostIP());
       	// Set info
    	remoteShip.position.x = message.position.x;
    	remoteShip.position.y = message.position.y;
    	remoteShip.vector.x = message.vector.x;
    	remoteShip.vector.y = message.vector.y;
    	remoteShip.heading = message.heading;
    	// Process laser hosts!
    	int i=0;
    	for (LaserBeam remoteLaserBeam : remoteShip.ammo) {
    		remoteLaserBeam.active = message.shotActive[i];
    		remoteLaserBeam.position.x = message.shotPosition[i].x;
    		remoteLaserBeam.position.y = message.shotPosition[i].y;
    		remoteLaserBeam.vector.x = message.shotVector[i].x;
    		remoteLaserBeam.vector.y = message.shotVector[i].y;
    		remoteLaserBeam.heading = message.shotHeading[i];
    		
    		// Hit?
    		if ( ((remoteLaserBeam.position.x - Globals.starShip.position.x)*(remoteLaserBeam.position.x - Globals.starShip.position.x) + 
    			  (remoteLaserBeam.position.y - Globals.starShip.position.y)*(remoteLaserBeam.position.y - Globals.starShip.position.y)) 
    			   < Globals.starShip.width/4*Globals.starShip.width/4) {
    			 Globals.lifeLost();
    			 continue;
    		}
    		
    		i++;
    	}
	}
	
	/**
	 * Handles new joins to the group of hosts
	 */
	public void newHost(Host aHost) {
    	NetworkCommunication networkComm = NetworkCommunicationFactory.getNetworkCommunication(NetworkCommunicationFactory.getDefaultNetworkCommunication());
    	networkComm.connectToServerHost(aHost);
	}
	
	
}

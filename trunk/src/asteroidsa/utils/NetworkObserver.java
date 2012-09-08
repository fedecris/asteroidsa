package asteroidsa.utils;

import java.util.Observable;

import asteroidsa.model.LaserBeam;
import asteroidsa.model.StarShip;
import asteroidsa.network.NetworkApplicationDataObserver;

public class NetworkObserver implements NetworkApplicationDataObserver {

	/**
	 * Updates model data depending on the received message
	 */
	public void update(Observable observable, Object data) {

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
	
	

}

package asteroidsa.utils;

import asteroidsa.model.LaserBeam;
import asteroidsa.model.StarShip;
import asteroidsa.network.Host;
import asteroidsa.network.Logger;
import asteroidsa.network.NetworkApplicationData;
import asteroidsa.network.NetworkApplicationDataConsumer;
import asteroidsa.network.NetworkStartup;
import asteroidsa.network.discovery.HostDiscovery;


public class AsteroidsNetworkConsumer implements NetworkApplicationDataConsumer {

	AsteroidsNetworkApplicationData remoteShipInfo;
	
	/**
	 * Updates model data depending on the received message
	 */
	public synchronized void newData(NetworkApplicationData data) {

		remoteShipInfo = (AsteroidsNetworkApplicationData)data;
		
        StarShip remoteShip = null;
    	// Retrieve ship remote IP and update accordingly
        if (Globals.otherShips.get(remoteShipInfo.getSourceHost().getHostIP())==null)
        	Globals.otherShips.put(remoteShipInfo.getSourceHost().getHostIP(), new StarShip());
       	remoteShip = Globals.otherShips.get(remoteShipInfo.getSourceHost().getHostIP());
       	
       	// Is host online? If not, no need to draw
       	if (HostDiscovery.otherHosts.get(data.getSourceHost().getHostIP())!=null && !HostDiscovery.otherHosts.get(data.getSourceHost().getHostIP()).isOnLine())
       		return;
       	
       	// Set info
       	remoteShip.position = remoteShipInfo.position;
    	remoteShip.vector = remoteShipInfo.vector;
    	remoteShip.heading = remoteShipInfo.heading;
    	// Process laser hosts!
    	int i=0;
    	for (LaserBeam remoteLaserBeam : remoteShip.ammo) {
    		remoteLaserBeam.active = remoteShipInfo.shotActive[i];
    		remoteLaserBeam.position = remoteShipInfo.shotPosition[i];
    		remoteLaserBeam.vector = remoteShipInfo.shotVector[i];
    		remoteLaserBeam.heading = remoteShipInfo.shotHeading[i];
    		
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
    	if (!NetworkStartup.getCommunication().connectToServerHost(aHost))
    		Logger.e("Could not connect to host: " + aHost.getHostIP());
	}

	
	/**
	 * Handles a departure of a host from the group
	 */
	public synchronized void byeHost(Host aHost) {
		if (Globals.otherShips!=null && aHost!=null)
			Globals.otherShips.remove(aHost.getHostIP());
	}

	
}

package asteroidsa.utils;

import asteroidsa.model.StarShip;
import asteroidsa.network.Host;
import asteroidsa.network.Logger;
import asteroidsa.network.NetworkApplicationData;
import asteroidsa.network.NetworkApplicationDataConsumer;
import asteroidsa.network.NetworkStartup;
import asteroidsa.network.discovery.HostDiscovery;


public class AsteroidsNetworkConsumer implements NetworkApplicationDataConsumer {

	/** Received remote information */
	protected AsteroidsNetworkApplicationData remoteShipInfo;
	// Internal use.  Ship data based on remote info.
    private StarShip remoteShip = null;
    
	/**
	 * Updates model data depending on the received message
	 */
	public synchronized void newData(NetworkApplicationData receivedData) {

		remoteShipInfo = (AsteroidsNetworkApplicationData)receivedData;

    	// Retrieve ship remote IP and update accordingly
        if (Globals.otherShips.get(remoteShipInfo.getSourceHost().getHostIP())==null)
        	Globals.otherShips.put(remoteShipInfo.getSourceHost().getHostIP(), new StarShip());
       	remoteShip = Globals.otherShips.get(remoteShipInfo.getSourceHost().getHostIP());
       	
       	// Is host online? If not, no need to draw
       	if (HostDiscovery.otherHosts.get(receivedData.getSourceHost().getHostIP())!=null && !HostDiscovery.otherHosts.get(receivedData.getSourceHost().getHostIP()).isOnLine())
       		return;
       	
       	// Set info
       	remoteShip.position = remoteShipInfo.position;
    	remoteShip.vector = remoteShipInfo.vector;
    	remoteShip.heading = remoteShipInfo.heading;
    	// Process laser hosts!
    	for (int i=0; i<remoteShip.ammo.size(); i++) { 
    		remoteShip.ammo.get(i).active = remoteShipInfo.shotActive[i];
    		remoteShip.ammo.get(i).position = remoteShipInfo.shotPosition[i];
    		remoteShip.ammo.get(i).vector = remoteShipInfo.shotVector[i];
    		remoteShip.ammo.get(i).heading = remoteShipInfo.shotHeading[i];
    		
    		// Hit?
    		if ( ((remoteShip.ammo.get(i).position.x - Globals.starShip.position.x)*(remoteShip.ammo.get(i).position.x - Globals.starShip.position.x) + 
    			  (remoteShip.ammo.get(i).position.y - Globals.starShip.position.y)*(remoteShip.ammo.get(i).position.y - Globals.starShip.position.y)) 
    			   < Globals.starShip.width/4*Globals.starShip.width/4) {
    			 Globals.lifeLost();
    			 continue;
    		}
    		
    		i++;
    	}
    	
    	remoteShip = null;
    	remoteShipInfo = null;
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

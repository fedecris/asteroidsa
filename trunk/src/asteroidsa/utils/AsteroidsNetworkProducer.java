package asteroidsa.utils;

import networkdcq.NetworkApplicationData;
import networkdcq.NetworkApplicationDataProducer;
import networkdcq.discovery.HostDiscovery;

public class AsteroidsNetworkProducer implements NetworkApplicationDataProducer {

	/** Local information instance	*/
	AsteroidsNetworkApplicationData instance = null;
	
	public NetworkApplicationData produceNetworkApplicationData() {
		if (instance == null)
			instance = new AsteroidsNetworkApplicationData();
		instance.setData(HostDiscovery.thisHost, Globals.starShip);
		return instance;
	}




}

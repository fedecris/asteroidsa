package asteroidsa.utils;

import asteroidsa.network.NetworkApplicationData;
import asteroidsa.network.NetworkApplicationDataProducer;
import asteroidsa.network.discovery.HostDiscovery;

public class AsteroidsNetworkProducer implements NetworkApplicationDataProducer {

	/** Local information instance	*/
	AsteroidsNetworkApplicationData instance = null;
	
	public NetworkApplicationData produceNetworkApplicationData() {
		instance = new AsteroidsNetworkApplicationData();
		instance.setData(HostDiscovery.thisHost, Globals.starShip);
		return instance;
	}




}

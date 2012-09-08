package asteroidsa.utils;

import asteroidsa.network.NetworkApplicationData;
import asteroidsa.network.NetworkApplicationDataProducer;
import asteroidsa.network.discovery.HostDiscovery;

public class NetworkProducer implements NetworkApplicationDataProducer {

	public NetworkApplicationData produceNetworkApplicationData() {
		return new AsteroidsNetworkApplicationData(HostDiscovery.thisHost, Globals.starShip);
	}




}

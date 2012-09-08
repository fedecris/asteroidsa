package asteroidsa.network;

public interface NetworkApplicationDataProducer {

	/**
	 * Load and set the local information to be sent to other hosts
	 * @return A subclass of {@link NetworkApplicationData} with the
	 * 			updated information about the local status of the app 
	 */
	public NetworkApplicationData produceNetworkApplicationData();
}

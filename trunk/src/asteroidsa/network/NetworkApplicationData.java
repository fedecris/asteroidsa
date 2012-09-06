package asteroidsa.network;

/**
 * This class must be extended in order to augment the information to send/receive 
 */

import java.io.Serializable;

public abstract class NetworkApplicationData implements Serializable {

	/** Host that sends the message */
	protected Host sourceHost = null;
	
    public Host getSourceHost() {
		return sourceHost;
	}

    /**
     * Generates a copy of the message
     * @param source
     */
    public abstract void copy(NetworkApplicationData source);

	
}

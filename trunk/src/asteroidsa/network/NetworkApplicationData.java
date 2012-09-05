package asteroidsa.network;

/**
 * This class must be extended in order to augment the information to send/receive 
 */

import java.io.Serializable;
import java.util.Observable;

public abstract class NetworkApplicationData extends Observable implements Serializable {

	/** Host that sends the message */
	protected Host sourceHost = null;
	
    public Host getSourceHost() {
		return sourceHost;
	}

	/** 
     * Notifies dependent objects about the new message
     */
    public void notifyNewMessage()
    {
        setChanged();
        notifyObservers(this);
    }
	
    /**
     * Generates a copy of the message
     * @param source
     */
    public abstract void copy(NetworkApplicationData source);

	
}

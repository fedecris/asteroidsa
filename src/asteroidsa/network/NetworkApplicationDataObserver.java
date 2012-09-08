package asteroidsa.network;

import java.util.Observable;
import java.util.Observer;

/**
 * 
 * The classes implementing this interface are in charge of processing  
 * the received messages from other hosts at the application level and   
 * update the local status based on the received information.
 */

public interface NetworkApplicationDataObserver extends Observer {

	/**
	 * New communication message received.
	 * @param observable is the observed object
	 * @param data is a subclass of {@link NetworkApplicationData} containing the received information
	 */
	public void update(Observable observable, Object data);	
}

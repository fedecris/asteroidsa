package asteroidsa.utils;

import asteroidsa.model.StarShip;
import networkdcq.Host;
import networkdcq.NetworkApplicationData;

public class AsteroidsNetworkApplicationData extends NetworkApplicationData {

	/** serialVersionUID */
	private static final long serialVersionUID = 7853200087637751560L;
	/** Position */
	protected Point2Df position = new Point2Df(100, 100);
	/** Heading angle */
	protected float heading = 0;
	/** Lasershots active */
	protected boolean[] shotActive = new boolean[StarShip.AMMO_COUNT];
	/** Lasershots position */
	protected Point2Df[] shotPosition = new Point2Df[StarShip.AMMO_COUNT];
	/** Lasershots heading */
	protected float[] shotHeading = new float[StarShip.AMMO_COUNT];
	


	/**
	 * Sets the general data to be sent based on the passed parameters
	 * @param host the host which originates the message to be sent
	 * @param ship local ship information 
	 */
	public void setData(Host host, StarShip ship) {
		setHost(host);
		setShip(ship);
	}

	/**
	 * Sets the host information
	 * @param host the host which originates the message to be sent
	 */
	protected void setHost(Host host) {
		this.sourceHost = host;
	}

	/**
	 * Sets the ship information
	 * @param ship local ship information
	 */
	protected void setShip(StarShip ship) {
		position.x = ship.position.x;
		position.y = ship.position.y;
		heading = ship.heading;
		for (int i=0; i<ship.ammo.size(); i++) {
			if (shotPosition[i]==null) shotPosition[i] = new Point2Df(0, 0);
			shotActive[i] = ship.ammo.get(i).active;
			shotPosition[i].x = ship.ammo.get(i).position.x;
			shotPosition[i].y = ship.ammo.get(i).position.y;
			shotHeading[i] = ship.ammo.get(i).heading;
			i++;
		}
	}
	
	@Override
	public String toString() {
		return "Heading: " + heading;
	}
}

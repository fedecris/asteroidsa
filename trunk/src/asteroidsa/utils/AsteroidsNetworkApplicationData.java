package asteroidsa.utils;

import asteroidsa.network.Host;
import asteroidsa.model.LaserBeam;
import asteroidsa.utils.Point2Df;
import asteroidsa.model.StarShip;
import asteroidsa.network.NetworkApplicationData;

public class AsteroidsNetworkApplicationData extends NetworkApplicationData {

	// Host origen del msj
	protected Host remoteHost = null;
	// Position
	protected Point2Df position = new Point2Df(100, 100);
	// Current directional speed
	protected Point2Df vector = new Point2Df(0, 0);
	// Heading angle
	protected float heading = 0;
	// Lasershots!
	protected boolean[] shotActive = new boolean[StarShip.AMMO_COUNT];
	protected Point2Df[] shotPosition = new Point2Df[StarShip.AMMO_COUNT];
	protected Point2Df[] shotVector = new Point2Df[StarShip.AMMO_COUNT];
	protected float[] shotHeading = new float[StarShip.AMMO_COUNT];
	
	
	public AsteroidsNetworkApplicationData() {
		
	}
	
	public AsteroidsNetworkApplicationData(Host host, StarShip ship) {
		this.remoteHost = host;
		setRemoteShip(ship);
		int i=0;
		for (LaserBeam beam : ship.ammo) {
			shotActive[i] = beam.active;
			shotPosition[i] = new Point2Df(beam.position.x, beam.position.y);
			shotVector[i] = new Point2Df(beam.vector.x, beam.vector.y);
			shotHeading[i] = beam.heading;
			i++;
		}
	}

	public void setRemoteShip(StarShip ship) {
		position.x = ship.position.x;
		position.y = ship.position.y;
		vector.x = ship.vector.x;
		vector.y = ship.vector.y;
		heading = ship.heading;
	}
	
	
	public void copy(NetworkApplicationData source) {
		AsteroidsNetworkApplicationData s = (AsteroidsNetworkApplicationData)source; 
		this.remoteHost = s.remoteHost;
		this.position = s.position;
		this.vector = s.vector;
		this.heading = s.heading;
		this.shotActive = s.shotActive;
		this.shotPosition = s.shotPosition;
		this.shotVector = s.shotVector;
		this.shotHeading = s.shotHeading;
	}
}

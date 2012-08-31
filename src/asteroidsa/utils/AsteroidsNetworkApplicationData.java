package asteroidsa.utils;

import asteroidsa.network.Host;
import asteroidsa.model.LaserBeam;
import asteroidsa.utils.Point2Df;
import asteroidsa.model.StarShip;
import asteroidsa.network.NetworkApplicationData;

public class AsteroidsNetworkApplicationData extends NetworkApplicationData {

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
		this.sourceHost = host;
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
		this.sourceHost = s.sourceHost;
		this.position.x = s.position.x;
		this.position.y = s.position.y;
		this.vector = s.vector;
		this.vector.x = s.vector.x;
		this.vector.y = s.vector.y;
		this.heading = s.heading;
		shotActive = new boolean[StarShip.AMMO_COUNT];
		shotPosition = new Point2Df[StarShip.AMMO_COUNT];
		shotVector = new Point2Df[StarShip.AMMO_COUNT];
		shotHeading = new float[StarShip.AMMO_COUNT];
		for (int i=0; i<this.shotActive.length; i++) {
			shotPosition[i] = new Point2Df(0, 0);
			shotVector[i] = new Point2Df(0, 0);
			this.shotActive[i] = s.shotActive[i];
			this.shotPosition[i].x = s.shotPosition[i].x;
			this.shotPosition[i].y = s.shotPosition[i].y;
			this.shotVector[i].x = s.shotVector[i].x;
			this.shotVector[i].y = s.shotVector[i].y;
			this.shotHeading[i] = s.shotHeading[i];
		}
	}
}

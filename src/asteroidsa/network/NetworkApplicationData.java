package asteroidsa.network;

import java.io.Serializable;

import asteroidsa.model.LaserBeam;
import asteroidsa.model.StarShip;
import asteroidsa.utils.Point2Df;

public class NetworkApplicationData implements Serializable {

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
	
	
	public NetworkApplicationData() {
		
	}
	
	public NetworkApplicationData(Host host, StarShip ship) {
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
	

	public Host getRemoteHost() {
		return remoteHost;
	}

	public void setRemoteHosts(Host remoteHost) {
		this.remoteHost = remoteHost;
	}

	public Point2Df getPosition() {
		return position;
	}

	public void setPosition(Point2Df position) {
		this.position = position;
	}

	public Point2Df getVector() {
		return vector;
	}

	public void setVector(Point2Df vector) {
		this.vector = vector;
	}

	public float getHeading() {
		return heading;
	}

	public void setHeading(float heading) {
		this.heading = heading;
	}

	public void setRemoteHost(Host remoteHost) {
		this.remoteHost = remoteHost;
	}
	
	
}

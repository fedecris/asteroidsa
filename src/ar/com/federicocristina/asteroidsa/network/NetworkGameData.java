package ar.com.federicocristina.asteroidsa.network;

import java.io.Serializable;

import ar.com.federicocristina.asteroidsa.model.StarShip;
import ar.com.federicocristina.asteroidsa.utils.Point2Df;

public class NetworkGameData implements Serializable {

	// Host origen del msj
	protected Host remoteHost = null;
	// Position
	protected Point2Df position = new Point2Df(100, 100);
	// Current directional speed
	protected Point2Df vector = new Point2Df(0, 0);
	// Heading angle
	protected float heading = 0;
	
	public NetworkGameData() {
		
	}
	
	public NetworkGameData(Host host, StarShip ship) {
		this.remoteHost = host;
		setRemoteShip(ship);
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

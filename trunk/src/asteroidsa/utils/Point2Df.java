package asteroidsa.utils;

import java.io.Serializable;

public class Point2Df implements Serializable {

	/** Serial Version UID */
	private static final long serialVersionUID = -8149951119625929042L;
	/** X Coordinate */
	public float x = 0;
	/** Y Coordinate */
	public float y = 0;
	
	
	public Point2Df() {
		
	}
	
	public Point2Df(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	
}

package ar.com.federicocristina.asteroidsa.utils;

import java.io.Serializable;

public class Point2Df implements Serializable {

	public float x = 0;
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

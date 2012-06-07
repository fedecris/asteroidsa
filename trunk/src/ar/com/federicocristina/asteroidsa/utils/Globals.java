package ar.com.federicocristina.asteroidsa.utils;

import java.util.Vector;

import android.graphics.Point;
import ar.com.federicocristina.asteroidsa.model.Asteroid;
import ar.com.federicocristina.asteroidsa.model.Star;
import ar.com.federicocristina.asteroidsa.model.StarShip;

public class Globals {

	// Screen size
	public static Point canvasSize = null;
	// Accelerometer vectors
	public static float[] acel = {1, 2, 0};
	// Asteroids collection
	public volatile static Vector<Asteroid> asteroids = new Vector<Asteroid>();
	// Stars collection
	public volatile static Vector<Star> stars = new Vector<Star>();	
	// Our ship!
	public static StarShip starShip = null;
	// Level
	public static int level = 1;
	// Lives
	public static int lives = 5;
	// Points
	public static int points = 0;
	// Points
	public static int MAX_STARS = 50; 
	
	/**
	 * Initial values
	 */
	public static void startup()
	{
		// A star ship, stars and at least one asteroid
        Globals.starShip = new StarShip();
        stars.clear();
        for (int i=0; i<MAX_STARS; i++)
        	new Star();
        asteroids.clear();
        for (int i=0; i<level; i++)
        	new Asteroid();
	}
	
	/**
	 * A new hit!
	 * @param a dead asteroid
	 */
	public static void byeAsteroid(Asteroid a)
	{
		points += 10;
		asteroids.remove(a);
		a = null;
		if (asteroids.size() == 0)
		{
			level++;
			startup();
		}
	}
	
	/**
	 * Oops! Star ship crashed with an asteroid
	 */
	public static void lifeLost()
	{
		lives--;
		if (lives == 0) {
			lives = 5;
			points = 0;
			level = 1;
		}
		startup();
		
	}
}

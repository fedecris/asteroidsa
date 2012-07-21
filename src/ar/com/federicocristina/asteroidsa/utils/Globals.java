package ar.com.federicocristina.asteroidsa.utils;

import java.util.Vector;

import android.graphics.Point;
import android.graphics.PointF;
import ar.com.federicocristina.asteroidsa.model.Asteroid;
import ar.com.federicocristina.asteroidsa.model.Star;
import ar.com.federicocristina.asteroidsa.model.StarShip;
import ar.com.federicocristina.asteroidsa.network.Host;
import ar.com.federicocristina.asteroidsa.network.UDPClient;
import ar.com.federicocristina.asteroidsa.network.UDPListener;


public class Globals {

	// Game running
	public static boolean running = true;
	// Model size
	public static Point modelSize = new Point(100, 100); 
	// Screen size
	public static Point canvasSize = null;
	// Relation between model & screen size (example: (8, 4.8) in a 800x480 grid).
	public static PointF model2canvas = null;
	// Relation between model & screen size (example: (.080, .048) in a 800x480 grid).
	public static PointF canvas2model = null;	
	// Accelerometer vectors
	public static float[] acel = {1, 2, 0};
	// Asteroids collection
	public volatile static Vector<Asteroid> asteroids = new Vector<Asteroid>();
	// Stars collection
	public volatile static Vector<Star> stars = new Vector<Star>();	
	// Our ship!
	public static StarShip starShip = null;
	// The others ships!
	public static Vector<StarShip> otherShips = null;
	// Host local
	public static Host thisHost = null;
	// The other hosts
	public static Vector<Host> otherHosts = null;
	// Level
	public static int level = 0;
	// Lives
	public static int lives = 5;
	// Points
	public static int points = 0;
	// Total front stars
	public static int MAX_FRONT_STARS = 0; 
	// Total back stars
	public static int MAX_BACK_STARS = 0; 	

	/** Network Specific */ 
	public static final int PORT_UDP = 9998;
	public static final String GROUP_IP = "230.0.0.1";
	
	
	/**
	 * Initial values
	 */
	public static void startup()
	{
		// A star ship, stars and at least one asteroid
        Globals.starShip = new StarShip();
        stars.clear();
        for (int i=0; i<MAX_BACK_STARS; i++)
        	new Star(false);               
        for (int i=0; i<MAX_FRONT_STARS; i++)
        	new Star(true);
        asteroids.clear();
        for (int i=0; i<level; i++)
        	new Asteroid();
        // First time configuration
        if (thisHost == null)
        {
	        thisHost = Host.getLocalHostAddresAndIP();
	        otherShips = new Vector<StarShip>();
	        otherHosts= new Vector<Host>();
	        UDPListener listener = new UDPListener();
	        listener.start();
	        UDPClient client = new UDPClient();
	        client.start();
        }
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

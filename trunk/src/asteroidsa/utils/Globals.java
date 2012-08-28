package asteroidsa.utils;

import java.util.ArrayList;

import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;
import asteroidsa.model.Asteroid;
import asteroidsa.model.Star;
import asteroidsa.model.StarShip;
import asteroidsa.network.Host;
import asteroidsa.network.communication.TCPClient;
import asteroidsa.network.communication.TCPListener;
import asteroidsa.network.discovery.HostDiscovery;


public class Globals {

	/** Game specific */
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
	public static float[] acel = {0, 0, 0};
	// Asteroids collection
	public volatile static ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
	// Stars collection
	public volatile static ArrayList<Star> stars = new ArrayList<Star>();	
	// Our ship!
	public static StarShip starShip = null;
	// The others ships!
	public static ArrayList<StarShip> otherShips = null;
	// Initial Level 
	public final static int INITIAL_LEVEL = 0;
	// Level (& number of asteroids!)
	public static int level = INITIAL_LEVEL;
	// Max lives
	public final static int MAX_LIVES = 5;
	// Lives
	public static int lives = 5;
	// Points
	public static int points = 0;
	// Total front stars
	public static int MAX_FRONT_STARS = 20; 
	// Total back stars
	public static int MAX_BACK_STARS = 20; 	
	
	/** Network Specific */ 
	
	/** Log Specific */
	public static final String LOG_TAG = "Asteroidsa";
	
	/** Input specific */
	public static final int INPUT_KEYBOARD		= 0;
	public static final int INPUT_ACCELEROMETER	= 1;
	public static int inputMethod = INPUT_ACCELEROMETER;
	
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
        if (HostDiscovery.thisHost == null)
        {
	        otherShips = new ArrayList<StarShip>();

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
			lives = MAX_LIVES;
			points = 0;
			level = INITIAL_LEVEL;
		}
		startup();
		
	}
}

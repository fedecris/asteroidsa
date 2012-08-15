package ar.com.federicocristina.asteroidsa.utils;

import java.util.ArrayList;
import java.util.Vector;

import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;
import ar.com.federicocristina.asteroidsa.model.Asteroid;
import ar.com.federicocristina.asteroidsa.model.Star;
import ar.com.federicocristina.asteroidsa.model.StarShip;
import ar.com.federicocristina.asteroidsa.network.Host;
import ar.com.federicocristina.asteroidsa.network.TCPClient;
import ar.com.federicocristina.asteroidsa.network.TCPServer;
import ar.com.federicocristina.asteroidsa.network.UDPClient;
import ar.com.federicocristina.asteroidsa.network.UDPListener;


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
	public static float[] acel = {(float)(Math.random() - 0.5f) * 1.5f, (float)(Math.random() - 0.5f) * 1.5f, (float)(Math.random() - 0.5f) * 1.5f};
	// Asteroids collection
	public volatile static ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
	// Stars collection
	public volatile static ArrayList<Star> stars = new ArrayList<Star>();	
	// Our ship!
	public static StarShip starShip = null;
	// The others ships!
	public static ArrayList<StarShip> otherShips = null;
	// Host local
	public static Host thisHost = null;
	// The other hosts
	public static ArrayList<Host> otherHosts = null;
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
	// UDP Port
	public static final int PORT_UDP = 9998;
	// TCP Port
	public static final int PORT_TCP = 9999;
	// UDP Group
	public static final String GROUP_IP = "230.0.0.1";
	// TCP Server
	public static final String SERVER_IP_TCP = "10.0.0.10";

	
	/** Log Specific */
	public static final String LOG_TAG = "Asteroidsa";
	
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
	        if (thisHost == null) {
	        	Log.e(Globals.class.toString(), "No Wifi! Exiting...");
	        	System.exit(1);
	        }
	        otherShips = new ArrayList<StarShip>();
	        otherHosts = new ArrayList<Host>();
	        
//	        UDPListener listener = new UDPListener();
//	        listener.start();
//	        UDPClient client = new UDPClient();
//	        client.start();
	        
	        // Iniciar Server
	        TCPServer server = new TCPServer(PORT_TCP);
	        Thread serverRun = new Thread(server);
	        serverRun.start();


	        // TODO: Deshardcode!!
	        TCPClient client = new TCPClient("10.0.0.15".equals(thisHost.getHostIP())?"10.0.0.16":"10.0.0.15", PORT_TCP);
	        Thread clientRun = new Thread(client);
	        clientRun.start();
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

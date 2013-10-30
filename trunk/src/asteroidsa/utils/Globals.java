package asteroidsa.utils;

import java.util.ArrayList;


import android.graphics.Point;
import android.graphics.PointF;
import asteroidsa.model.Asteroid;
import asteroidsa.model.Star;
import asteroidsa.model.StarShip;
import networkdcq.NetworkDCQ;
import networkdcq.util.IterateableConcurrentHashMap;


public class Globals {

	// Game specific
	/** Game running */
	public static boolean running = true;
	/** Model size (1% to 100% */
	public static final Point modelSize = new Point(100, 100); 
	/** Screen size */
	public static Point canvasSize = null;
	/** Relation between model & screen size (example: (8, 4.8) in a 800x480 grid). */
	public static PointF model2canvas = null;
	/** Relation between model & screen size (example: (.080, .048) in a 800x480 grid). */
	public static PointF canvas2model = null;	
	/** Accelerometer vectors */
	public static float[] acel = {0, 0, 0};
	/** Asteroids collection */
	public volatile static ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
	/** Stars collection */
	public volatile static ArrayList<Star> stars = new ArrayList<Star>();	
	/** Our ship! */
	public static StarShip starShip = null;
	/** The others ships! IP->Starship */
	public static IterateableConcurrentHashMap<String, StarShip> otherShips = new IterateableConcurrentHashMap<String, StarShip>();
	/** Initial Level */
	public final static int INITIAL_LEVEL = 1;
	/** Level (& number of asteroids!) */
	public static int level = INITIAL_LEVEL;
	/** Max lives */
	public final static int MAX_LIVES = 5;
	/** Lives */
	public static int lives = 5;
	/** Points */
	public static int points = 0;
	/** Total front stars */
	public static int MAX_FRONT_STARS = 10; 
	/** Total back stars */
	public static int MAX_BACK_STARS = 10; 	
	
	// Input specific
	/** Input through keyboard */
	public static final int INPUT_KEYBOARD		= 0;
	/** Input through accelerometer */
	public static final int INPUT_ACCELEROMETER	= 1;
	/** Input through virtual keyboard (HUD) */
	public static final int INPUT_VIRTUAL	= 2;
	/** Different number of input methods */
	public static final int INPUT_METHODS_COUNT = 3;
	/** Default input method  */
	public static int inputMethod = INPUT_ACCELEROMETER;
	/** String corresponding input method */
	public static String[] virtualMethods =  {"Keyboard", "Accelerometer", "Virtual"}; 
	
	// First time configuration run
	protected static boolean firstConf = false;

	// Game Mode
	/** Just one player */
	public static final String GAME_MODE_SINGLE_PLAYER = "Single player";
	/** More than one player (via DCQ) */
	public static final String GAME_MODE_MULTI_PLAYER = "Multi player";
	/** Current game mode */
	public static String CURRENT_GAME_MODE = GAME_MODE_SINGLE_PLAYER;
	
	/**
	 * Initial values
	 */
	public static void startup()
	{
		// A star ship, stars and at least one asteroid
        initStarShip();
        initStars();
        initAsteroids();
        // First time configuration
        if (!firstConf)
        {	
        	try {
	        	NetworkDCQ.configureStartup(new AsteroidsNetworkConsumer(), new AsteroidsNetworkProducer(), null, null);
	        	firstConf=true;
	        	NetworkDCQ.doStartup(true, true, true);
        	}
        	catch (Exception e) {
        		e.printStackTrace();
        	}
        }
	}
	
	protected static void initStarShip() {
		starShip = new StarShip();
	}
	
	protected static void initStars() {
        stars.clear();
        for (int i=0; i<MAX_BACK_STARS; i++)
        	new Star(false);               
        for (int i=0; i<MAX_FRONT_STARS; i++)
        	new Star(true);
	}
	
	protected static void initAsteroids() {
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
		if (CURRENT_GAME_MODE == GAME_MODE_SINGLE_PLAYER) {
			lives--;
			if (lives == 0) {
				lives = MAX_LIVES;
				points = 0;
				level = INITIAL_LEVEL;
			}
		}
		startup();
	}
	
	public static void cycleStarsLevel() {
		MAX_FRONT_STARS = (MAX_FRONT_STARS+10) % 100;
		MAX_BACK_STARS = MAX_FRONT_STARS;
		initStars();
	}

	public static void cycleInputMethod() {
		if (inputMethod++ == INPUT_METHODS_COUNT-1)
			inputMethod = 0;
	}
	
}

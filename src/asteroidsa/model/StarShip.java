package asteroidsa.model;


import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.util.FloatMath;
import asteroidsa.network.Logger;
import asteroidsa.utils.Globals;

public class StarShip extends Sprite {


	/** Soften accelerometer reading for movement, heading */
	protected static final float SOFT_ROTATION = 20f;
	/** Soften accelerometer reading for movement, heading */
	public static final float SOFT_THROOTLE = 20f;
	/** Dead zone (rotation) */
	protected static final float DEAD_ZONE_ROTATION = 0.5f;
	/** Dead zone (throttle) */	
	protected static final float DEAD_ZONE_THROOTLE = 5;
	/** laser beam amount */
    public static final int AMMO_COUNT = 1;
    /** The color of the local ship */
    public static final int LOCAL_SHIP_COLOR = Color.GREEN;
    /** The color of the other ships */
    public static final int REMOTE_SHIP_COLOR = Color.BLUE;
	// Path for drawing ship
	private Path p = new Path();
	// Laser beams
    public ArrayList<LaserBeam> ammo = new ArrayList<LaserBeam>();
	// Problematic asteroid approaching!
    private Asteroid approachingAsteroid = null;
    // Internal use variable
	private float posX = 0;
    // Internal use variable
	private float posY = 0;
	
	/**
	 * Constructor
	 */
	public StarShip()
	{
		topSpeed = .3f;
		position.x = (float)Math.random() * Globals.modelSize.x;
		position.y = (float)Math.random() * Globals.modelSize.y;
		width = 3 * Globals.model2canvas.x;
		height = 3 * Globals.model2canvas.x;
        for (int j = 0; j < AMMO_COUNT; j++)
            ammo.add(new LaserBeam(j));
        active = true;
        heading = 0;
        headingSpeed = 0; // (float)(Math.random() - 0.5f) * 1.5f;
	}

	
	@Override
	public void update() {

		if (Globals.inputMethod == Globals.INPUT_ACCELEROMETER) {
			// Update heading according to accelerometer
			if (Globals.acel[1] > DEAD_ZONE_ROTATION)
				headingSpeed = (Globals.acel[1] - DEAD_ZONE_ROTATION) / SOFT_ROTATION;
			if (Globals.acel[1] < -DEAD_ZONE_ROTATION)
				headingSpeed = (Globals.acel[1] + DEAD_ZONE_ROTATION) / SOFT_ROTATION;
			
			// Change speed according to accelerometer
			if (Globals.acel[0] - DEAD_ZONE_THROOTLE < 0) {
		    	vector.x = vector.x + FloatMath.cos(heading) * - (Globals.acel[0] - DEAD_ZONE_THROOTLE) / SOFT_THROOTLE; 
		        vector.y = vector.y + FloatMath.sin(heading) * - (Globals.acel[0] - DEAD_ZONE_THROOTLE) / SOFT_THROOTLE;
			}
		}
		updatePosition(true, true);
		
        // collision with an asteroid?
		for (int i=0; i<Globals.asteroids.size(); i++)
		{
			approachingAsteroid = Globals.asteroids.get(i);
			if (approachingAsteroid!=null && approachingAsteroid.active &&
				((position.x - approachingAsteroid.position.x)*(position.x - approachingAsteroid.position.x) + (position.y - approachingAsteroid.position.y)*(position.y - approachingAsteroid.position.y)) < approachingAsteroid.width/4*approachingAsteroid.width/4 ) 
	       		{  
					Logger.d(position.x + ", " + position.y + " - " + approachingAsteroid.position.x + "," + approachingAsteroid.position.y);
                	Globals.lifeLost();
	       		}
		}
		
		// Update my laser beams
		for (int i=0; i<ammo.size(); i++)
			ammo.get(i).update();
	}

	
	/**
	 * Fires a laser beam, if available
	 */
	public void fire()
	{
	    // any ammo available?
		for (int i=0; i<ammo.size(); i++) {
			if (!ammo.get(i).active) {
				ammo.get(i).fire(position.x, position.y, heading);
				break;
			}
		}
	}
	

	@Override
	public void draw(Canvas canvas) {

		// Wings - (My ship or other?)
		if (this.equals(Globals.starShip))
			paint.setColor(LOCAL_SHIP_COLOR);
		else
			paint.setColor(REMOTE_SHIP_COLOR);
		paint.setStrokeWidth(4);
		p.reset();
		posX = position.x * Globals.model2canvas.x;
		posY = position.y * Globals.model2canvas.y;
		p.moveTo(posX + FloatMath.cos(heading) * width, 			posY + FloatMath.sin(heading) * height );
		p.lineTo(posX + FloatMath.cos((heading+90)%360) * width, 	posY + FloatMath.sin((heading+90)%360) * height );
		p.lineTo(posX + FloatMath.cos((heading-90)%360) * width, 	posY + FloatMath.sin((heading-90)%360) * height );
		p.lineTo(posX + FloatMath.cos(heading) * width, 			posY + FloatMath.sin(heading) * height );
		canvas.drawPath(p, paint);	
		
		// Main line ship
		paint.setColor(Color.RED);
		canvas.drawLine(posX, posY, (posX + FloatMath.cos(heading) * width), (posY + FloatMath.sin(heading) * height), paint);
		canvas.drawLine(posX, posY, (posX - FloatMath.cos(heading) * width/2), (posY - FloatMath.sin(heading) * height/2), paint);
		
	    // draw my laser shots!
		for (int i=0; i<ammo.size(); i++)
			ammo.get(i).draw(canvas);
	}
	
}

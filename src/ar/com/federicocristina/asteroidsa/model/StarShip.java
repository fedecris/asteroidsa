package ar.com.federicocristina.asteroidsa.model;


import java.util.Vector;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import ar.com.federicocristina.asteroidsa.utils.Globals;

public class StarShip extends Sprite {

	// Path for drawing ship
	private Path p = new Path();
	// Soften accelerometer reading for movement & heading
	protected static final float SOFT_ROTATION = 15f;
	protected static final float SOFT_THROOTLE = 15f;
	// Dead zone (rotation)
	protected static final float DEAD_ZONE_ROTATION = 0.5f;
	// Dead zone (throttle)	
	protected static final float DEAD_ZONE_THROOTLE = 5;
	// laser beams
    protected static final int AMMO_COUNT = 5;
    private Vector<LaserBeam> ammo = new Vector<LaserBeam>();
	
	
	/**
	 * Constructor
	 */
	public StarShip()
	{
		position.x = 100;
		position.y = 100;
		width = 20;
		height = 20;
		paint.setColor(Color.GREEN);
		paint.setStrokeWidth(4);
        for (int j = 0; j < AMMO_COUNT; j++)
            ammo.add(new LaserBeam());
        active = true;
	}
	
	@Override
	public void update() {

		// Update heading according to accelerometer
		if (Globals.acel[1] > DEAD_ZONE_ROTATION)
			headingSpeed = (Globals.acel[1] - DEAD_ZONE_ROTATION) / SOFT_ROTATION;
		if (Globals.acel[1] < -DEAD_ZONE_ROTATION)
			headingSpeed = (Globals.acel[1] + DEAD_ZONE_ROTATION) / SOFT_ROTATION;
		
		// Change speed according to accelerometer
		if (Globals.acel[0] - DEAD_ZONE_THROOTLE < 0) {
	    	vector.x = vector.x + (float)Math.cos(heading) * -(Globals.acel[0] - DEAD_ZONE_THROOTLE) / SOFT_THROOTLE; 
	        vector.y = vector.y + (float)Math.sin(heading) * -(Globals.acel[0] - DEAD_ZONE_THROOTLE) / SOFT_THROOTLE;
	        if (vector.x > topSpeed) vector.x = topSpeed;
	        if (vector.x < -topSpeed) vector.x = -topSpeed;
	        if (vector.y > topSpeed) vector.y = topSpeed;
	        if (vector.y < -topSpeed) vector.y = -topSpeed;    
		}
		updatePosition(true, true);
		
        // collision with an asteroid?
		for (Asteroid a : Globals.asteroids)
			if (a.active &&
	       			position.x > a.position.x - a.width/1f &&  
	       			position.x < a.position.x + a.width/1f && 
	       			position.y > a.position.y - a.height/1f &&
	       			position.y < a.position.y + a.height/1f)
	       		{  
                	Globals.lifeLost();
	       		}
		
		// Update my laser beams
	    for (LaserBeam aBeam : ammo)
	    	aBeam.update();
	}

	
	/**
	 * Fires a laser beam, if available
	 */
	public void fire()
	{
	    // any ammo available?
	    for (LaserBeam aBeam : ammo)
	    	if (!aBeam.active)
	    	{
	    		aBeam.fire(position.x, position.y, heading);
	    		break;
	    	}
	}
	
	@Override
	public void draw(Canvas canvas) {

		// Wings
		paint.setColor(Color.GREEN);
		p.reset();
		p.moveTo(position.x + (float)Math.cos(heading) * width, 			position.y + (float)Math.sin(heading) * height );
		p.lineTo(position.x + (float)Math.cos((heading+90)%360) * width, 	position.y + (float)Math.sin((heading+90)%360) * height );
		p.lineTo(position.x + (float)Math.cos((heading-90)%360) * width, 	position.y + (float)Math.sin((heading-90)%360) * height );
		p.lineTo(position.x + (float)Math.cos(heading) * width, 			position.y + (float)Math.sin(heading) * height );
		canvas.drawPath(p, paint);	
		
		// Main line ship
		paint.setColor(Color.RED);
		canvas.drawLine(position.x, position.y, (float)(position.x+Math.cos(heading) * width), (float)(position.y+Math.sin(heading) * height), paint);
		canvas.drawLine(position.x, position.y, (float)(position.x-Math.cos(heading) * width/2), (float)(position.y-Math.sin(heading) * height/2), paint);
		
	    // draw my laser shots!
	    for (LaserBeam aBeam : ammo)
	    	aBeam.draw(canvas);
	}
	

}

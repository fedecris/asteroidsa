package ar.com.federicocristina.asteroidsa.model;

import android.graphics.Canvas;
import android.graphics.Color;
import ar.com.federicocristina.asteroidsa.utils.Globals;

public class LaserBeam extends Sprite {

	/**
	 * Fire! from the star ship
	 * @param xPos starting x Point
	 * @param yPos starting y Point
	 * @param heading direction of the beam
	 */
    public void fire(float xPos, float yPos, float heading)
    {
    	width = 2 * Globals.model2canvas.x;
    	height = 2 * Globals.model2canvas.y;
        position.x = xPos;
        position.y = yPos;
        vector.x = (float)Math.cos(heading) * 1.5f; 
        vector.y = (float)Math.sin(heading) * 1.5f;
        this.heading = heading;
        active = true;
    }
	
	@Override
	public void update() {

		if (!active)
			return;
		
        // update motion
		updatePosition(true, false);
		
        // collision with an asteroid?
		Asteroid targetAsteroid = null;
		for (Asteroid a : Globals.asteroids)
       		if (a.active &&
       		    ((position.x - a.position.x)*(position.x - a.position.x) + (position.y - a.position.y)*(position.y - a.position.y)) < a.width/4*a.width/4  )
       		{
           		active = false;
           		targetAsteroid = a;
           		break;
        	}
        
        if (targetAsteroid!=null) 
        	targetAsteroid.newHit();
	
	}

	@Override
	public void draw(Canvas canvas) {
        if (!active)
        	return;
    	paint.setStrokeWidth(4);
    	paint.setColor(Color.RED);
        canvas.drawLine(position.x * Globals.model2canvas.x, 
        				position.y * Globals.model2canvas.y, 
        				position.x * Globals.model2canvas.x + (float)Math.cos(heading) * width, 
        				position.y * Globals.model2canvas.y + (float)Math.sin(heading) * height, 
        				paint );
	}
	

}

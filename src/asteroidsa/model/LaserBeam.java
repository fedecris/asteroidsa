package asteroidsa.model;

import android.graphics.Canvas;
import android.graphics.Color;
import asteroidsa.utils.Globals;

public class LaserBeam extends Sprite {

	/** Shot identifier */
	int shotID = -1;
	/** Target asteroid to be shot */
	private Asteroid targetAsteroid = null;
	
	public LaserBeam(int id) {
		shotID = id;
    	width = 2 * Globals.model2canvas.x;
    	height = 2 * Globals.model2canvas.y;
	}
	
	/**
	 * Fire! from the star ship
	 * @param xPos starting x Point
	 * @param yPos starting y Point
	 * @param heading direction of the beam
	 */
    public void fire(float xPos, float yPos, float heading)
    {
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
		targetAsteroid = null;
		for (int i=0; i< Globals.asteroids.size(); i++) {
       		if (Globals.asteroids.get(i).active &&
       		    ((position.x - Globals.asteroids.get(i).position.x)*(position.x - Globals.asteroids.get(i).position.x) + 
       		    (position.y - Globals.asteroids.get(i).position.y)*(position.y - Globals.asteroids.get(i).position.y)) < 
       		    Globals.asteroids.get(i).width/4*Globals.asteroids.get(i).width/4  )
       		{
           		active = false;
           		targetAsteroid = Globals.asteroids.get(i);
           		break;
        	}
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

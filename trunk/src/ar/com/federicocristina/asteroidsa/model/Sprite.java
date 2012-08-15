package ar.com.federicocristina.asteroidsa.model;


import java.io.Serializable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import ar.com.federicocristina.asteroidsa.utils.Globals;
import ar.com.federicocristina.asteroidsa.utils.Point2Df;

public abstract class Sprite implements Serializable {

	// Current position
	public Point2Df position = new Point2Df(100, 100);
	// Current directional speed
	public Point2Df vector = new Point2Df(0, 0);
	// Heading angle
	public float heading = 0;
	// Size
	protected transient float width = 1, height = 1;
	// Max speed
	protected transient float topSpeed = 1f;
	// Heading angle speed
	protected transient float headingSpeed = 0;	
	// Basic color
	protected transient Color color = new Color();
	// Paint properties
	protected transient Paint paint = new Paint();
	// Is active?
	protected transient boolean active = false;
	// Visual margin
	protected transient float margin = 5f;

	/**
     * Updates sprite model (position, logic, etc.)
	 */
	public abstract void update();
	
	/**
	 * Renders to the canvas
	 * @param canvas
	 */
	public abstract void draw(Canvas canvas);
	
    /**
     * update position of the sprite
     * @param updateHeading if heading variable should be updated
     */
	public void updatePosition(boolean updateHeading, boolean cycle)
	{
		// Is the sprite moving?
		if (!active)
			return; 
		
		// Update position & heading
		position.x += vector.x;
		position.y += vector.y;
		if (updateHeading)
			heading = (heading +headingSpeed)%360;
		
		checkMargins(cycle);
	}

	/**
	 * Check if sprite position is correct
	 * @param cycle If sprite goes beyond margins, reset at the opposite position
	 */
	public void checkMargins(boolean cycle)
	{
		if (position.x > Globals.modelSize.x + margin && vector.x > 0)
			if (cycle)
				position.x = -margin;
			else
				active = false;
        if (position.x < -margin && vector.x < 0)
        	if (cycle)
        		position.x = Globals.modelSize.x + margin;
			else
				active = false;        
        if (position.y > Globals.modelSize.y + margin && vector.y > 0)
        	if (cycle)
        		position.y = -margin;
			else
				active = false;        
        if (position.y < -margin && vector.y < 0)
        	if (cycle)        	
        		position.y = Globals.modelSize.y + margin;
			else
				active = false;        
	}
	
	
}

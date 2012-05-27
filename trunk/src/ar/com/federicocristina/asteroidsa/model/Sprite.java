package ar.com.federicocristina.asteroidsa.model;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import ar.com.federicocristina.asteroidsa.utils.Globals;

public abstract class Sprite {

	// Size
	protected float width = 10, height = 10;
	// Current position
	protected PointF position = new PointF(100, 100);
	// Current directional speed
	protected PointF vector = new PointF(0, 0);
	// Max speed
	protected float topSpeed = 3f;
	// Heading angle
	protected float heading = 0;
	// Heading angle speed
	protected float headingSpeed = 0;	
	// Basic color
	protected Color color = new Color();
	// Paint properties
	protected Paint paint = new Paint();
	// Is active?
	protected boolean active = false;
	// Visual margin
	protected float margin = 5f;

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
		if (position.x > Globals.canvasSize.x + margin && vector.x > 0)
			if (cycle)
				position.x = -margin;
			else
				active = false;
        if (position.x < -margin && position.x < 0)
        	if (cycle)
        		position.x = Globals.canvasSize.x + margin;
			else
				active = false;        
        if (position.y > Globals.canvasSize.y && vector.y > 0)
        	if (cycle)
        		position.y = -margin;
			else
				active = false;        
        if (position.y < -margin && position.y < 0)
        	if (cycle)        	
        		position.y = Globals.canvasSize.y;
			else
				active = false;        
	}
}

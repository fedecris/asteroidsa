package asteroidsa.model;


import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.util.FloatMath;
import android.util.Log;
import android.view.KeyEvent;
import asteroidsa.network.discovery.HostDiscovery;
import asteroidsa.utils.Globals;

public class StarShip extends Sprite {

	// Path for drawing ship
	private Path p = new Path();
	// Soften accelerometer reading for movement & heading
	protected static final float SOFT_ROTATION = 20f;
	protected static final float SOFT_THROOTLE = 20f;
	// Dead zone (rotation)
	protected static final float DEAD_ZONE_ROTATION = 0.5f;
	// Dead zone (throttle)	
	protected static final float DEAD_ZONE_THROOTLE = 5;
	// laser beams
    public static final int AMMO_COUNT = 1;
    public ArrayList<LaserBeam> ammo = new ArrayList<LaserBeam>();
	
	
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
		Asteroid a = null;
		for (int i=0; i<Globals.asteroids.size(); i++)
		{
			a = Globals.asteroids.get(i);
			if (a!=null && a.active &&
				((position.x - a.position.x)*(position.x - a.position.x) + (position.y - a.position.y)*(position.y - a.position.y)) < a.width/4*a.width/4 ) 
	       		{  
					Log.d(Globals.LOG_TAG, position.x + ", " + position.y + " - " + a.position.x + "," + a.position.y);
                	Globals.lifeLost();
	       		}
		}
		
		// Update my laser beams
	    for (LaserBeam aBeam : ammo)
	    	aBeam.update();
	}

	
	public boolean processKeyEvent(int keyCode) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			headingSpeed -= .01f;
			if (headingSpeed < -.05f)
				headingSpeed = -.05f;
			return true;
		}
		else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			headingSpeed += .01f;
			if (headingSpeed > .05f)
				headingSpeed = .05f;
			return true;
		}
		else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			vector.x = vector.x + FloatMath.cos(heading) / StarShip.SOFT_THROOTLE; 
			vector.y = vector.y + FloatMath.sin(heading) / StarShip.SOFT_THROOTLE;
			return true;
		}
		else if (keyCode == KeyEvent.KEYCODE_SPACE) {
			Globals.starShip.fire();
			return true;
		}
		else if (keyCode == KeyEvent.KEYCODE_O) {
			HostDiscovery.thisHost.setOnLine(!HostDiscovery.thisHost.isOnLine());
		}
		
		return false;

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

		// Wings - (My ship or other?)
		if (this.equals(Globals.starShip))
			paint.setColor(Color.GREEN);
		else
			paint.setColor(Color.BLUE);
		paint.setStrokeWidth(4);
		p.reset();
		float posX = position.x * Globals.model2canvas.x;
		float posY = position.y * Globals.model2canvas.y;
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
	    for (LaserBeam aBeam : ammo)
	    	aBeam.draw(canvas);
	}
	
}

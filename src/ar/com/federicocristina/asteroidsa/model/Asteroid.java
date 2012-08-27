package asteroidsa.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Path;
import asteroidsa.utils.Globals;

public class Asteroid extends Sprite {

	// Path for drawing the asteroid
	public Path shape = new Path();
	
    // total asteroid points
	protected int totalPoints = 18;

	// Transformation matrix
	Matrix matrix = new Matrix();
	
	// Asteroid Stamina Energy!
	protected int energy = 3;
	
    // initial size
	protected int initialWidth =  5 * (int)Globals.model2canvas.x;
	protected int initialHeight = 5 * (int)Globals.model2canvas.x;
    
	
	public Asteroid(float xPos, float yPos, float pWidth, float pHeight, int pEnergy)
	{
		this();
		energy = pEnergy;
		position.x = xPos;
		position.y = yPos;
		width = pWidth;
		height = pHeight;
//		createShape();
	}
		
	
	public Asteroid() {
		position.x = Globals.modelSize.x - Globals.starShip.position.x;
		position.y = Globals.modelSize.y - Globals.starShip.position.y;
		width = initialWidth;
		height = initialHeight;
		vector.x = (float)(Math.random() - 0.5f) * 1f;
		vector.y = (float)(Math.random() - 0.5f) * 1f;
        heading = 0;
        headingSpeed = (float)(Math.random() - 0.5f) * 1.5f;
        margin = width/4f;
        topSpeed = .2f;
        createShape();
        active = true;
		Globals.asteroids.add(this);
	}
	
	/**
	 * Asteroid shape (and closing with the last point)
	 */
    private void createShape()
    {
        shape.reset();
        shape.moveTo((float)(Math.cos(Math.toRadians(0))*(width+(Math.random()*width/2-width/2))), (float)(Math.sin(Math.toRadians(0))*(height+(Math.random()*height/2-height/2))));
        for (int j=0; j<360; j+= 360 / totalPoints)
       		shape.lineTo((float)(Math.cos(Math.toRadians(j))*(width+(Math.random()*width/2-width/2))), (float)(Math.sin(Math.toRadians(j))*(height+(Math.random()*height/2-height/2))));
        shape.close();
    }
	
	@Override
	public void update() {
		if (!active)
			return;
		updatePosition(true, true);
	}

	@Override
	public void draw(Canvas canvas) {
		
		if (!active)
			return;
		
		paint.setStrokeWidth(4);
		paint.setColor(Color.YELLOW);

//		// Rotate original shape
//		matrix.reset();
//		matrix.setRotate(headingSpeed); 
//		shape.transform(matrix);
//
//		// Copy, translate and draw the copy
//		Path dst = new Path(shape);
//		matrix.reset();
//		matrix.setTranslate(position.x * Globals.model2canvas.x - position.x, position.y * Globals.model2canvas.y - position.y);
//		dst.transform(matrix);
//		canvas.drawPath(dst, paint);

		canvas.drawCircle(position.x * Globals.model2canvas.x, position.y * Globals.model2canvas.y, width, paint);
		
	}
	
	public void newHit()
	{
		// big enough?
		Globals.points += 10;
		energy--;
        if (energy > 0)
        {
            // from 1 big asteroid -> 2 small pieces
            width = width / 2;
            height = height / 2;        
            createShape();

            new Asteroid(position.x, position.y, width, height, energy);
        }
        else
        {
            // total desintegration!
            active = false;
            Globals.byeAsteroid(this);
        }
		
	}

}

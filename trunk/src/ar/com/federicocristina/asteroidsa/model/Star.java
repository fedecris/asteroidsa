package ar.com.federicocristina.asteroidsa.model;

import android.graphics.Canvas;
import android.graphics.Color;
import ar.com.federicocristina.asteroidsa.utils.Globals;

public class Star extends Sprite {

	// Is it a background or a foreground star?
	protected boolean front = false;
	
	
	public Star(boolean front)
	{
		this();
		this.front = front;
	}
	
	public Star()
	{
		position.x = (float)Math.random() * Globals.modelSize.x;
		position.y = (float)Math.random() * Globals.modelSize.y;
		vector.x = -Globals.starShip.vector.x * (front?1f:0.5f);
		vector.y = -Globals.starShip.vector.y * (front?1f:0.5f);
        active = true;

		Globals.stars.add(this);
	}
	
	
	@Override
	public void update() {
		vector.x = -Globals.starShip.vector.x * (front?1f:0.5f);
		vector.y = -Globals.starShip.vector.y * (front?1f:0.5f);
		updatePosition(false, true);
	}

	@Override
	public void draw(Canvas canvas) {
		paint.setColor(front?Color.WHITE:Color.GRAY);
		canvas.drawCircle(position.x * Globals.model2canvas.x, position.y * Globals.model2canvas.y, 1, paint);
	}

}

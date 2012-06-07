package ar.com.federicocristina.asteroidsa.model;

import android.graphics.Canvas;
import android.graphics.Color;
import ar.com.federicocristina.asteroidsa.utils.Globals;

public class Star extends Sprite {

	public Star()
	{
		position.x = (float)Math.random() * Globals.canvasSize.x;
		position.y = (float)Math.random() * Globals.canvasSize.y;
		vector.x = -Globals.starShip.vector.x;
		vector.y = -Globals.starShip.vector.y;
        margin = width;
        active = true;

		Globals.stars.add(this);
	}
	
	
	@Override
	public void update() {
		vector.x = -Globals.starShip.vector.x;
		vector.y = -Globals.starShip.vector.y;
		updatePosition(false, true);
	}

	@Override
	public void draw(Canvas canvas) {
		paint.setColor(Color.WHITE);
		canvas.drawCircle(position.x, position.y, 1, paint);
	}

}

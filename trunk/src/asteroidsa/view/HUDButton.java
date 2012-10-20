package asteroidsa.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import asteroidsa.utils.Globals;

public class HUDButton {

	public Point position = new Point();
	public Point size = new Point();
	public String text = "";
	public int keyCodeAction = 0;
	protected Rect drawArea = new Rect();
	
	public HUDButton(Point position, Point size, String text, int keyCodeAction) {
		this.position = position;
		this.size = size;
		this.text = text;
		this.keyCodeAction = keyCodeAction;
		this.drawArea = new Rect((int)(position.x * Globals.model2canvas.x), 
									(int)(position.y * Globals.model2canvas.y), 
									(int)((position.x + size.x) * Globals.model2canvas.x), 
									(int)((position.y + size.y) * Globals.model2canvas.y));
	}
	
	/**
	 * Draws the visual component
	 */
	public void draw(Canvas canvas, Paint paint) {
		canvas.drawRect(drawArea, paint);
		canvas.drawText(text, (position.x) * Globals.model2canvas.x, (int)(position.y + size.y/1.7) * Globals.model2canvas.y, paint);
	}
	
	/**
	 * Checks if the touched area corresponds with this button
	 * @param event touch event to be handled 
	 * @return true if the touch event will be handled by this button, false otherwise
	 */
	public boolean handleOnTouchEvent(MotionEvent event, AsteroidsaActivity activity) {
		if (drawArea.contains((int)event.getX(), (int)event.getY())) {
			activity.processKeyEvent(keyCodeAction);
			return true;
		}
		return false;
	}
}

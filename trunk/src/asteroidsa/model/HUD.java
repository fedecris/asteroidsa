package asteroidsa.model;

/**
 * Heads-up display
 */

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.KeyEvent;
import android.view.MotionEvent;
import asteroidsa.network.discovery.HostDiscovery;
import asteroidsa.utils.Globals;
import asteroidsa.utils.Point2Df;

public class HUD {

	static boolean displayHUD = true;
	static Paint paint = new Paint();
	static final int TEXT_SIZE = 10;
	static final int HUD_COLOR = Color.WHITE;

	/** Controls size */
	static final Point BUTTON_SIZE 			= new Point(9, 9);
	/** Controls Position */
	static final Point BUTTON_LEFT_POS 		= new Point(70, 80);
	static final Point BUTTON_RIGHT_POS 	= new Point(80, 80);
	static final Point BUTTON_THROTTLE_POS 	= new Point(75, 70);
	static final Point BUTTON_FIRE_POS 		= new Point(10, 75);
	/** Controls rendering area */
	static final Rect CONTROL_LEFT 			= new Rect((int)(BUTTON_LEFT_POS.x * Globals.model2canvas.x), 
														(int)(BUTTON_LEFT_POS.y * Globals.model2canvas.y), 
														(int)((BUTTON_LEFT_POS.x + BUTTON_SIZE.x) * Globals.model2canvas.x), 
														(int)((BUTTON_LEFT_POS.y + BUTTON_SIZE.y) * Globals.model2canvas.y));
	static final Rect CONTROL_RIGHT 		= new Rect((int)(BUTTON_RIGHT_POS.x * Globals.model2canvas.x), 
														(int)(BUTTON_RIGHT_POS.y * Globals.model2canvas.y), 
														(int)((BUTTON_RIGHT_POS.x + BUTTON_SIZE.x) * Globals.model2canvas.x), 
														(int)((BUTTON_RIGHT_POS.y + BUTTON_SIZE.y) * Globals.model2canvas.y));
	static final Rect CONTROL_THROTTLE 		= new Rect((int)(BUTTON_THROTTLE_POS.x * Globals.model2canvas.x), 
														(int)(BUTTON_THROTTLE_POS.y * Globals.model2canvas.y), 
														(int)((BUTTON_THROTTLE_POS.x + BUTTON_SIZE.x) * Globals.model2canvas.x), 
														(int)((BUTTON_THROTTLE_POS.y + BUTTON_SIZE.y) * Globals.model2canvas.y));
	static final Rect CONTROL_FIRE	 		= new Rect((int)(BUTTON_FIRE_POS.x * Globals.model2canvas.x), 
														(int)(BUTTON_FIRE_POS.y * Globals.model2canvas.y), 
														(int)((BUTTON_FIRE_POS.x + BUTTON_SIZE.x) * Globals.model2canvas.x), 
														(int)((BUTTON_FIRE_POS.y + BUTTON_SIZE.y) * Globals.model2canvas.y));
	
	static {
		paint.setTextSize(TEXT_SIZE);
    	paint.setColor(HUD_COLOR);
    	paint.setTypeface(Typeface.SANS_SERIF);
    	paint.setStyle(Style.STROKE);
	}
	
	public static void draw(Canvas canvas) {
		if (displayHUD) {
			drawHosts(canvas);
			if (Globals.INPUT_ACCELEROMETER != Globals.inputMethod)
				drawControls(canvas);
		}
	}
	
	protected static void drawHosts(Canvas canvas) {
		paint.setTextSize(TEXT_SIZE);
		paint.setColor(StarShip.LOCAL_SHIP_COLOR);
		canvas.drawText(HostDiscovery.thisHost.getHostIP() + (HostDiscovery.thisHost.isOnLine()?" (Online)":" (OffLine)"), TEXT_SIZE / 2, TEXT_SIZE*2, paint);
		for (int i = 0; i < Globals.otherShips.size(); i++) {
			paint.setColor(StarShip.REMOTE_SHIP_COLOR);
			canvas.drawText(HostDiscovery.otherHosts.getValueAt(i).getHostIP() + (HostDiscovery.otherHosts.getValueAt(i).isOnLine()?" (Online)":" (OffLine)"), TEXT_SIZE / 2, (i+2)*TEXT_SIZE*2, paint);
		}
	}
	
	protected static void drawControls(Canvas canvas) {
		paint.setColor(HUD_COLOR);
		paint.setAlpha(127);
		canvas.drawRect(CONTROL_LEFT, paint);
		canvas.drawRect(CONTROL_RIGHT, paint);
		canvas.drawRect(CONTROL_THROTTLE, paint);
		canvas.drawRect(CONTROL_FIRE, paint);
		paint.setTextSize(TEXT_SIZE*2);
		canvas.drawText("<<", (BUTTON_LEFT_POS.x + BUTTON_SIZE.x/2) * Globals.model2canvas.x, (BUTTON_LEFT_POS.y + BUTTON_SIZE.y/2) * Globals.model2canvas.y, paint);
		canvas.drawText(">>", (BUTTON_RIGHT_POS.x + BUTTON_SIZE.x/2) * Globals.model2canvas.x, (BUTTON_RIGHT_POS.y + BUTTON_SIZE.y/2) * Globals.model2canvas.y, paint);
		canvas.drawText("/\\", (BUTTON_THROTTLE_POS.x + BUTTON_SIZE.x/2) * Globals.model2canvas.x, (BUTTON_THROTTLE_POS.y + BUTTON_SIZE.y/2) * Globals.model2canvas.y, paint);
		canvas.drawText("><", (BUTTON_FIRE_POS.x + BUTTON_SIZE.x/2) * Globals.model2canvas.x, (BUTTON_FIRE_POS.y + BUTTON_SIZE.y/2) * Globals.model2canvas.y, paint);
	}
	
	
	public static void handleOnTouch(MotionEvent event) {
		// Process HUD input
		if (Globals.INPUT_ACCELEROMETER != Globals.inputMethod) {
			if (CONTROL_LEFT.contains((int)event.getX(), (int)event.getY()))
				Globals.starShip.processKeyEvent(KeyEvent.KEYCODE_DPAD_LEFT);
			else if (CONTROL_RIGHT.contains((int)event.getX(), (int)event.getY()))
				Globals.starShip.processKeyEvent(KeyEvent.KEYCODE_DPAD_RIGHT);
			else if (CONTROL_THROTTLE.contains((int)event.getX(), (int)event.getY()))
				Globals.starShip.processKeyEvent(KeyEvent.KEYCODE_DPAD_UP);
			else if (CONTROL_FIRE.contains((int)event.getX(), (int)event.getY()))
				Globals.starShip.processKeyEvent(KeyEvent.KEYCODE_SPACE);
		}
		else
			// Fire touching somewhere on the screen!
			Globals.starShip.processKeyEvent(KeyEvent.KEYCODE_SPACE);
	}

}

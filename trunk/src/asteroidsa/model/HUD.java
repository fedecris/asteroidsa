package asteroidsa.model;

/**
 * Heads-up display
 */

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.KeyEvent;
import android.view.MotionEvent;
import asteroidsa.network.discovery.HostDiscovery;
import asteroidsa.utils.Globals;

public class HUD {

	static boolean displayHUD = true;
	static final int TEXT_SIZE = 10;
	static final int HUD_COLOR = Color.WHITE;
	static Paint paint = new Paint();
	static final Rect CONTROL_LEFT 		= new Rect((int)(70 * Globals.model2canvas.x), (int)(80 * Globals.model2canvas.y), (int)(79 * Globals.model2canvas.x), (int)(89 * Globals.model2canvas.y));
	static final Rect CONTROL_RIGHT 	= new Rect((int)(80 * Globals.model2canvas.x), (int)(80 * Globals.model2canvas.y), (int)(89 * Globals.model2canvas.x), (int)(89 * Globals.model2canvas.y));
	static final Rect CONTROL_THROTTLE 	= new Rect((int)(75 * Globals.model2canvas.x), (int)(70 * Globals.model2canvas.y), (int)(84 * Globals.model2canvas.x), (int)(79 * Globals.model2canvas.y));
	static final Rect CONTROL_FIRE 		= new Rect((int)(10 * Globals.model2canvas.x), (int)(75 * Globals.model2canvas.y), (int)(19 * Globals.model2canvas.x), (int)(84 * Globals.model2canvas.y));
	
	static {
		paint.setTextSize(TEXT_SIZE);
    	paint.setColor(HUD_COLOR);
    	paint.setTypeface(Typeface.SANS_SERIF);
    	paint.setStyle(Style.STROKE);
	}
	
	public static void draw(Canvas canvas) {
		if (displayHUD) {
			drawHosts(canvas);
			drawControls(canvas);
		}
	}
	
	protected static void drawHosts(Canvas canvas) {
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
	}
	
	
	public static void handleOnTouch(MotionEvent event) {
		if (CONTROL_LEFT.contains((int)event.getX(), (int)event.getY()))
			Globals.starShip.processKeyEvent(KeyEvent.KEYCODE_DPAD_LEFT);
		else if (CONTROL_RIGHT.contains((int)event.getX(), (int)event.getY()))
			Globals.starShip.processKeyEvent(KeyEvent.KEYCODE_DPAD_RIGHT);
		else if (CONTROL_THROTTLE.contains((int)event.getX(), (int)event.getY()))
			Globals.starShip.processKeyEvent(KeyEvent.KEYCODE_DPAD_UP);
		else if (CONTROL_FIRE.contains((int)event.getX(), (int)event.getY()))
			Globals.starShip.processKeyEvent(KeyEvent.KEYCODE_SPACE);
	}

}

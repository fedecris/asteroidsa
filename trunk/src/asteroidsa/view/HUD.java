package asteroidsa.view;

/**
 * Heads-up display
 */

import java.util.Vector;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Typeface;
import android.view.MotionEvent;
import asteroidsa.model.StarShip;
import networkdcq.discovery.HostDiscovery;
import asteroidsa.utils.Globals;

public class HUD {

	public static boolean displayHUD = false;
	public static boolean displaySettings = false;
	static Paint paint = new Paint();
	static int TEXT_SIZE = 10;
	static int HUD_COLOR = Color.WHITE;

	// Configuration button
	static HUDButton configButton = null;
	// HUD visual components
	static Vector<HUDButton> actionButtons = new Vector<HUDButton>();
	// Setting buttons
	static Vector<HUDButton> settingButtons = new Vector<HUDButton>();
	
	// Controls size 
	static final Point BUTTON_SIZE 			= new Point(14, 14);
	// Control buttons Position 
	static final Point BUTTON_LEFT_POS 		= new Point(65, 80);
	static final Point BUTTON_RIGHT_POS 	= new Point(80, 80);
	static final Point BUTTON_THROTTLE_POS 	= new Point(73, 65);
	static final Point BUTTON_FIRE_POS 		= new Point(10, 75);
	// Settings button Position 
	static final Point BUTTON_SETTINGS_POS 	= new Point(85, 1);
	static final Point BUTTON_ONLINE_POS 	= new Point(10, 40);
	static final Point BUTTON_HUD_POS 		= new Point(30, 40);
	static final Point BUTTON_DETAIL_POS 	= new Point(50, 40);
	static final Point BUTTON_INPUT_POS 	= new Point(70, 40);
	
	static {
		// Initial configuration
		paint.setTextSize(TEXT_SIZE);
    	paint.setTypeface(Typeface.MONOSPACE);
    	paint.setStyle(Style.FILL);
    	// Main configuration button
    	configButton = new HUDButton(BUTTON_SETTINGS_POS, 		BUTTON_SIZE, "    ?    ", 	AsteroidsaActivity.KEYCODE_SETTING_SETTINGS);
    	// Create actionButtons
    	actionButtons.add(new HUDButton(BUTTON_LEFT_POS, 		BUTTON_SIZE, "    <<   ", 	AsteroidsaActivity.KEYCODE_CONTROL_LEFT));
    	actionButtons.add(new HUDButton(BUTTON_RIGHT_POS, 		BUTTON_SIZE, "    >>   ",	AsteroidsaActivity.KEYCODE_CONTROL_RIGHT));
    	actionButtons.add(new HUDButton(BUTTON_THROTTLE_POS, 	BUTTON_SIZE, "    /\\  ", 	AsteroidsaActivity.KEYCODE_CONTROL_THROTTLE));
    	actionButtons.add(new HUDButton(BUTTON_FIRE_POS, 		BUTTON_SIZE, "    ><   ", 	AsteroidsaActivity.KEYCODE_CONTROL_FIRE));
    	// Create settingButtons
    	settingButtons.add(new HUDButton(BUTTON_ONLINE_POS, 	BUTTON_SIZE, "  Online ", 	AsteroidsaActivity.KEYCODE_SETTING_ONLINE));
    	settingButtons.add(new HUDButton(BUTTON_HUD_POS, 		BUTTON_SIZE, "   HUD   ", 	AsteroidsaActivity.KEYCODE_SETTING_HUD_DISPLAY));
    	settingButtons.add(new HUDButton(BUTTON_DETAIL_POS, 	BUTTON_SIZE, "  Detail ", 	AsteroidsaActivity.KEYCODE_SETTING_DETAIL_LEVEL));
    	settingButtons.add(new HUDButton(BUTTON_INPUT_POS, 		BUTTON_SIZE, "  Input  ", 	AsteroidsaActivity.KEYCODE_SETTING_INPUT_METHOD));
	}
	
	/**
	 * Draw everything (if corresponds)
	 * @param canvas
	 */
	public static void draw(Canvas canvas) {
		drawConfigurationButton(canvas);
		if (displayHUD) {
			drawHosts(canvas);
			drawInputMethod(canvas);
			drawGameMode(canvas);
			if (Globals.CURRENT_GAME_MODE == Globals.GAME_MODE_SINGLE_PLAYER)
				drawLevelAndLives(canvas);
		}
		if (Globals.inputMethod == Globals.INPUT_VIRTUAL)
			drawControls(canvas);
		if (displaySettings)
			drawSettingButtons(canvas);
	}
	
	protected static void drawConfigurationButton(Canvas canvas) {
		paint.setTextSize(TEXT_SIZE*2);
		paint.setColor(HUD_COLOR);
		paint.setAlpha(30);
		configButton.draw(canvas, paint);
	}
	
	protected static void drawHosts(Canvas canvas) {
		paint.setTextSize(TEXT_SIZE);
		paint.setColor(StarShip.LOCAL_SHIP_COLOR);
		paint.setAlpha(255);
		canvas.drawText(HostDiscovery.thisHost.getHostIP() + (HostDiscovery.thisHost.isOnLine()?" (Online)":" (OffLine)"), TEXT_SIZE / 2, TEXT_SIZE*2, paint);
		for (int i = 0; i < Globals.otherShips.size(); i++) {
			paint.setColor(StarShip.REMOTE_SHIP_COLOR);
			canvas.drawText(HostDiscovery.otherHosts.getValueAt(i).getHostIP() + (HostDiscovery.otherHosts.getValueAt(i).isOnLine()?" (Online)":" (OffLine)"), TEXT_SIZE / 2, (i+2)*TEXT_SIZE*2, paint);
		}
	}
	
	protected static void drawInputMethod(Canvas canvas) {
		paint.setTextSize(TEXT_SIZE);
		paint.setColor(HUD_COLOR);
		paint.setAlpha(255);
		canvas.drawText("Input method: " + Globals.virtualMethods[Globals.inputMethod], 40 * Globals.model2canvas.x,  2 * Globals.model2canvas.y, paint);
	}

	protected static void drawGameMode(Canvas canvas) {
		paint.setTextSize(TEXT_SIZE);
		paint.setColor(HUD_COLOR);
		paint.setAlpha(255);
		canvas.drawText("Game mode: " + Globals.CURRENT_GAME_MODE, 40 * Globals.model2canvas.x,  4 * Globals.model2canvas.y, paint);
	}
	
	protected static void drawLevelAndLives(Canvas canvas) {
		paint.setTextSize(TEXT_SIZE);
		paint.setColor(HUD_COLOR);
		paint.setAlpha(255);
		canvas.drawText(Globals.points + " points - " + Globals.lives + " lives", 40 * Globals.model2canvas.x,  6 * Globals.model2canvas.y, paint);		
	}
	
	protected static void drawControls(Canvas canvas) {
		paint.setColor(HUD_COLOR);
		paint.setAlpha(50);
		paint.setTextSize(TEXT_SIZE*2);
		for (int i=0; i<actionButtons.size(); i++) {
			actionButtons.get(i).draw(canvas, paint);
		}
	}
	
	protected static void drawSettingButtons(Canvas canvas) {
		paint.setColor(HUD_COLOR);
		paint.setAlpha(100);
		paint.setTextSize(TEXT_SIZE*2);
		for (int i=0; i<settingButtons.size(); i++) {
			settingButtons.get(i).draw(canvas, paint);
		}
	}
	
	/**
	 * Handles received single touch event
	 * @param event touch event to be handled
	 */
	public static void handleSingleTouch(MotionEvent event, AsteroidsaActivity activity) {
		// Process main configuration button
		if (configButton.handleOnTouchEvent(event, activity))
			return;
		// Process setting input
		if (displaySettings) {
			for (int i=0; i < settingButtons.size(); i++)
				if (settingButtons.get(i).handleOnTouchEvent(event, activity))
					return;
		}
		// Fire touching somewhere on the screen!
		if (Globals.inputMethod == Globals.INPUT_ACCELEROMETER)
			activity.processKeyEvent(AsteroidsaActivity.KEYCODE_CONTROL_FIRE);
	}

	/**
	 * Handles received continuous touch event
	 * @param event touch event to be handled
	 */
	public static void handleContinuousTouch(MotionEvent event, AsteroidsaActivity activity) {
		// Process HUD input
		if (Globals.inputMethod == Globals.INPUT_VIRTUAL) {
			for (int i=0; i < actionButtons.size(); i++)
				if (actionButtons.get(i).handleOnTouchEvent(event, activity))
					return;
		}
	}

	
	public static void cycleSettingButtons() {
		displaySettings = !displaySettings;		
	}
	
	
}

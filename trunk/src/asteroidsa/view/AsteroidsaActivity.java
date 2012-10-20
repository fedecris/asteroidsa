package asteroidsa.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.FloatMath;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import asteroidsa.model.StarShip;
import asteroidsa.network.Logger;
import asteroidsa.network.discovery.HostDiscovery;
import asteroidsa.utils.Globals;

public class AsteroidsaActivity extends Activity implements OnTouchListener, SensorEventListener {
	
	// Constants
	/** Throttle control key */
	public static int KEYCODE_CONTROL_THROTTLE 			= KeyEvent.KEYCODE_DPAD_UP;
	/** Left control key */
	public static int KEYCODE_CONTROL_LEFT 				= KeyEvent.KEYCODE_DPAD_LEFT;
	/** Right control key */
	public static int KEYCODE_CONTROL_RIGHT 			= KeyEvent.KEYCODE_DPAD_RIGHT;
	/** Fire! control key */
	public static int KEYCODE_CONTROL_FIRE 				= KeyEvent.KEYCODE_SPACE;
	/** Show setting buttons */
	public static int KEYCODE_SETTING_SETTINGS 			= KeyEvent.KEYCODE_S;
	/** Change status online-offline*/
	public static int KEYCODE_SETTING_ONLINE 			= KeyEvent.KEYCODE_O;
	/** Display HUD */
	public static int KEYCODE_SETTING_HUD_DISPLAY 		= KeyEvent.KEYCODE_H;
	/** Draw graphics level */
	public static int KEYCODE_SETTING_DETAIL_LEVEL 		= KeyEvent.KEYCODE_D;
	/** Input method */
	public static int KEYCODE_SETTING_INPUT_METHOD 		= KeyEvent.KEYCODE_I;
	/** Exit Game */
	public static int KEYCODE_ACTION_EXIT 				= KeyEvent.KEYCODE_BACK;
	
	// Rendering engine
	protected FastRenderView renderView;
	// Keep display ON!!
	protected WakeLock wakeLock; 
	// Support multicast packets!!
	WifiManager.MulticastLock multicastLock = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	// Basic setup
        super.onCreate(savedInstanceState);
        Logger.i("Starting...");
        
        // Full screen settings
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        renderView = new FastRenderView(this);
        renderView.setOnTouchListener(this);
        setContentView(renderView);
        PowerManager powerManager = (PowerManager)getBaseContext().getSystemService(Context.POWER_SERVICE); 
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock"); 
        // Window Size
        Display display = getWindowManager().getDefaultDisplay();
        Globals.canvasSize = new Point(display.getWidth(), display.getHeight()); 
        // Permitir multicast
        WifiManager wm = (WifiManager)getSystemService(Context.WIFI_SERVICE); 
        WifiManager.MulticastLock multicastLock = wm.createMulticastLock("mydebuginfo"); 
        multicastLock.setReferenceCounted(true);
        multicastLock.acquire();
        
        // Accelerometer sensor
        SensorManager manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() == 0) {
        	Globals.inputMethod = Globals.INPUT_KEYBOARD;
        } else {
            Sensor accelerometer = manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
            if (!manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)) {
            	Log.e(this.getClass().toString(), "No accelerometer!...");
            	Globals.inputMethod = Globals.INPUT_KEYBOARD;
            }
        }
        
        // Increase heap size a little in order to avoid GC too frequently
        dalvik.system.VMRuntime.getRuntime().setMinimumHeapSize(8*1024*1024);
        
        // Initialize
		Globals.model2canvas = new PointF((float)Globals.canvasSize.x / (float)Globals.modelSize.x, (float)Globals.canvasSize.y / (float)Globals.modelSize.y);
		Globals.canvas2model = new PointF((float)Globals.modelSize.x / (float)Globals.canvasSize.x, (float)Globals.modelSize.y / (float)Globals.canvasSize.y);
        Globals.startup();
    }
    
    protected void onResume() {
        super.onResume();
        renderView.resume();
        if (wakeLock!=null)
        	wakeLock.acquire();
        if (multicastLock!=null && !multicastLock.isHeld())
        	multicastLock.acquire();
    }
    
    protected void onPause() {
        super.onPause();         
        renderView.pause();
        if (wakeLock!=null)
        	wakeLock.release();
        if (multicastLock!=null && multicastLock.isHeld())
        	multicastLock.release();
    }
	
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN)
			HUD.handleOnTouch(event, this);
		return true;
	}	
    
    public void onSensorChanged(SensorEvent event) {
    	Globals.acel = event.values;
    }
    
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // nothing to do here
    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return processKeyEvent(keyCode);
	}
	
	/**
	 * Process input key events
	 * @param keyCode received keyCode
	 * @return true if it was handled, or false otherwise
	 */
	public boolean processKeyEvent(int keyCode) {
		// EXIT
		if (keyCode == KEYCODE_ACTION_EXIT) {
			Logger.i("Exiting...");
			System.exit(0);
		}
		// LEFT
		else if (keyCode == KEYCODE_CONTROL_LEFT) {
			Globals.starShip.headingSpeed -= .01f;
			if (Globals.starShip.headingSpeed < -.05f)
				Globals.starShip.headingSpeed = -.05f;
			return true;
		}
		// RIGHT
		else if (keyCode == KEYCODE_CONTROL_RIGHT) {
			Globals.starShip.headingSpeed += .01f;
			if (Globals.starShip.headingSpeed > .05f)
				Globals.starShip.headingSpeed = .05f;
			return true;
		}
		// GAS!
		else if (keyCode == KEYCODE_CONTROL_THROTTLE) {
			Globals.starShip.vector.x = Globals.starShip.vector.x + FloatMath.cos(Globals.starShip.heading) / StarShip.SOFT_THROOTLE; 
			Globals.starShip.vector.y = Globals.starShip.vector.y + FloatMath.sin(Globals.starShip.heading) / StarShip.SOFT_THROOTLE;
			return true;
		}
		// FIRE!
		else if (keyCode == KEYCODE_CONTROL_FIRE) {
			Globals.starShip.fire();
			return true;
		}
		// CHANGE HOST ONLINE STATUS
		else if (keyCode == KEYCODE_SETTING_ONLINE) {
			HostDiscovery.thisHost.setOnLine(!HostDiscovery.thisHost.isOnLine());
			return true;
		}
		// CHANGE HUD DISPLAY
		else if (keyCode == KEYCODE_SETTING_HUD_DISPLAY) {
			HUD.displayHUD = !HUD.displayHUD;
			return true;
		}
		// CYCLE DETAIL LEVEL
		else if (keyCode == KEYCODE_SETTING_DETAIL_LEVEL) {
			Globals.cycleStarsLevel();
			return true;
		}
		// CYCLE INPUT METHOD
		else if (keyCode == KEYCODE_SETTING_INPUT_METHOD) {
			Globals.cycleInputMethod();
			return true;
		}
		// CYCLE SHOW/HIDE SETTING BUTTONS
		else if (keyCode == KEYCODE_SETTING_SETTINGS) {
			HUD.cycleSettingButtons();
			return true;
		}
		
		return false;

	}
}
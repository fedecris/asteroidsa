package ar.com.federicocristina.asteroidsa.view;

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
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import ar.com.federicocristina.asteroidsa.utils.Globals;

public class AsteroidsaActivity extends Activity implements OnTouchListener, SensorEventListener {
	
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
        Log.i(this.getClass().toString(), "Starting...");
        
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
        	System.exit(1);
        } else {
            Sensor accelerometer = manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
            if (!manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)) {
            	Log.e(this.getClass().toString(), "No accelerometer! Exiting...");
            	System.exit(1);
            }
        }
        
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
    
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN)
			Globals.starShip.fire();
		return true;
	}

	
	
    @Override
    public void onSensorChanged(SensorEvent event) {
    	Globals.acel = event.values;
    }
    
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // nothing to do here
    }
}
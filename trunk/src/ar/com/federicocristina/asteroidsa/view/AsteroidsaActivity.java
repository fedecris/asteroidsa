package ar.com.federicocristina.asteroidsa.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
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
        
        Display display = getWindowManager().getDefaultDisplay();
        Globals.canvasSize = new Point(display.getWidth(), display.getHeight()); 
        
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
    }
    
    protected void onPause() {
        super.onPause();         
        renderView.pause();
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
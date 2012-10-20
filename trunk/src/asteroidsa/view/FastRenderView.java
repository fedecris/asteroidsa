package asteroidsa.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import asteroidsa.network.Logger;
import asteroidsa.utils.Globals;

public class FastRenderView extends SurfaceView implements Runnable {

    Thread renderThread = null;
    SurfaceHolder holder;
    private int spriteSize = 0;
    private int i = 0;

    
    public FastRenderView(Context context) {
        super(context);           
        holder = getHolder();
    }

    public void resume() {          
        Globals.running = true;
        renderThread = new Thread(this);
        renderThread.start();         
    }

    public void pause() {        
    	Globals.running = false;                        
        while(true) {
            try {
                renderThread.join();
                break;
            } catch (InterruptedException e) {
                // retry
            }
        }
        renderThread = null;        
    }

    Canvas canvas = null;
    public void run() {
    	
        while(Globals.running) {

	        	try {
	        	
	        	// Update model for each asteroid
	        	spriteSize = Globals.asteroids.size();
	        	for (i=0; i < spriteSize; i++)
	        		Globals.asteroids.get(i).update();
	
	        	// Update model for each star
	        	spriteSize = Globals.stars.size();
	        	for (i=0; i < spriteSize; i++)
	        		Globals.stars.get(i).update();        	
	        	
	        	// Update model for the ship
	        	Globals.starShip.update();
	        	
	        	// Wait for valid lock
	            if(!holder.getSurface().isValid())
	                continue;
	            
	            canvas = holder.lockCanvas(); 
	            if (Globals.canvasSize == null)
	            	Globals.canvasSize = new Point(canvas.getWidth(), canvas.getHeight());
	
	            drawSurface(canvas);                                           
	            holder.unlockCanvasAndPost(canvas);
	            canvas = null;
        	}
        	catch (Exception e) {
        		Logger.e(e.getMessage());
        	}
        }
    }

    private void drawSurface(Canvas canvas) {
    	
    	try {
	    	// Clear screen
	    	canvas.drawRGB(0, 0, 0);
	    	
	    	// draw stars!
	    	spriteSize = Globals.stars.size();
	    	for (i=0; i < spriteSize; i++)
	    		Globals.stars.get(i).draw(canvas);
	    	
	    	// draw each asteroid
	    	spriteSize = Globals.asteroids.size();
	    	for (i=0; i < spriteSize; i++)
	    		Globals.asteroids.get(i).draw(canvas);
	
	    	// draw other ships
	    	spriteSize = Globals.otherShips.getValueList().size();
	    	for (i=0; i < spriteSize; i++)
	    		Globals.otherShips.getValueList().get(i).draw(canvas);
	    	
	    	// draw ship
	    	Globals.starShip.draw(canvas);

	    	// draw hud
	    	HUD.draw(canvas);
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}

    }

}

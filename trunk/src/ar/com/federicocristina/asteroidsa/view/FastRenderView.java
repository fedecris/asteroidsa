package ar.com.federicocristina.asteroidsa.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Typeface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import ar.com.federicocristina.asteroidsa.utils.Globals;

public class FastRenderView extends SurfaceView implements Runnable {

    Thread renderThread = null;
    SurfaceHolder holder;
    int spriteSize = 0;
    Paint paint = new Paint();
    
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

    public void run() {
    	
        while(Globals.running) {

        	// Update model for each asteroid
        	spriteSize = Globals.asteroids.size();
        	for (int i=0; i < spriteSize; i++)
        		Globals.asteroids.get(i).update();

        	// Update model for each star
        	spriteSize = Globals.stars.size();
        	for (int i=0; i < spriteSize; i++)
        		Globals.stars.get(i).update();        	
        	
        	// Update model for the ship
        	Globals.starShip.update();
        	
        	
        	// Update model for the other ships
        	// TODO!!!
        	
        	
            if(!holder.getSurface().isValid())
                continue;
            
            Canvas canvas = holder.lockCanvas(); 
            if (Globals.canvasSize == null)
            	Globals.canvasSize = new Point(canvas.getWidth(), canvas.getHeight());

            drawSurface(canvas);                                           
            holder.unlockCanvasAndPost(canvas);            

        }
    }

    private void drawSurface(Canvas canvas) {
    	// Clear screen
    	canvas.drawRGB(0, 0, 0);
    	
    	// draw stars!
    	spriteSize = Globals.stars.size();
    	for (int i=0; i < spriteSize; i++)
    		Globals.stars.get(i).draw(canvas);
    	
    	// draw each asteroid
    	spriteSize = Globals.asteroids.size();
    	for (int i=0; i < spriteSize; i++)
    		Globals.asteroids.get(i).draw(canvas);

    	// draw other ships
    	spriteSize = Globals.otherShips.size();
    	for (int i=0; i < spriteSize; i++)
    		Globals.otherShips.get(i).draw(canvas);
    	
    	// draw ship
    	Globals.starShip.draw(canvas);
    	
    	paint.setTextSize(20);
    	paint.setColor(Color.WHITE);
    	paint.setStyle(Style.FILL);
    	paint.setTypeface(Typeface.SANS_SERIF);
    	canvas.drawText("Level:"+Globals.level, 10, 20, paint);
    	canvas.drawText("Lives:"+Globals.lives, 100, 20, paint);
    	canvas.drawText("Points:"+Globals.points, 200, 20, paint);

    }

}

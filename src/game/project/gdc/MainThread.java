package game.project.gdc;

import android.graphics.Canvas;
//import android.util.Log;
import android.view.SurfaceHolder;
import game.project.gdc.MainGamePanel;

public class MainThread extends Thread {
	//private static final String TAG = MainThread.class.getSimpleName();
	private boolean running;
	
	private MainGamePanel myPanel;
	private SurfaceHolder myHolder;
	
	private static final int MAX_FPS = 25;
	private static final int MAX_FRAME_SKIPS = 5;
	private static final int FRAME_PERIOD = 1000/MAX_FPS;
	
	public MainThread(SurfaceHolder _holder, MainGamePanel _panel) {
		this.myHolder = _holder;
		this.myPanel = _panel;
	}
	
	public void setRunning(boolean _running) {
		this.running = _running;
	}
	
	public boolean getRunning() {
		return this.running;
	}
	
	@Override
	public void run() {
		long beginTime;
		int sleepTime;
		long timeDiff;
		int frameSkipped;
		
		Canvas c;
		while (running) {
			c = null;
			try {
				c = myHolder.lockCanvas();
				synchronized(myHolder) {
					beginTime = System.currentTimeMillis();
					frameSkipped = 0;
					
					if (myPanel.getIsReleased()) {
						myPanel.update();
						myPanel.draw(c);
					} else {
						myPanel.draw(c);
					}
					
					timeDiff = System.currentTimeMillis()-beginTime;
					sleepTime = (int) (FRAME_PERIOD - timeDiff);
					
					if (sleepTime > 0) {
						try {
							Thread.sleep(sleepTime);
						} catch (InterruptedException e) {
							
						}
					}
					
					while (sleepTime < 0 && frameSkipped <= MAX_FRAME_SKIPS) {
						myPanel.update();
						sleepTime += FRAME_PERIOD;
						frameSkipped++;
					}
				}
			} finally {
				if (c != null) {
					myHolder.unlockCanvasAndPost(c);
				}
			}
		}
	}	
}

package game.project.gdc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
//import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import game.project.gdc.gameplay.Level;
import game.project.gdc.gameplay.Obstacle;
import game.project.gdc.gameplay.Player;

public class MainGamePanel extends SurfaceView implements SurfaceHolder.Callback {
	//private static final String TAG = MainGamePanel.class.getSimpleName();
	private SurfaceHolder holder = getHolder();
	
	//Measurement Variables
	private static float unitX;
	private static float unitY;
	private static float pointRadius;
	
	//GamePlay Variables
	private int currentNumberOfPoints;
	private float[][] points;
	private boolean isReleased;
	private int currentLevel;
		//Levels
	private Level[] levels;
	private int noOfLevels;
		//Thread
	private MainThread myThread;
		//Graphics
	private Bitmap lime;
	private Bitmap lemon;
	private Bitmap wall;
	private Bitmap background;
	private Rect bSource;
	private Rect bDest;
	
	Context context;
	
	//Graphic Variables
	public void init(Context context) {
		this.context = context;
		
		holder.addCallback(this);
		setFocusable(true);
	}

	public MainGamePanel(Context context){
		super(context);
		init(context);
	}

	public MainGamePanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public MainGamePanel(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		init(context);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	public void surfaceCreated(SurfaceHolder holder) {
		//Measurements (Once)
		unitX = ((float)getWidth())/500;
		unitY = ((float)getHeight())/300;
		pointRadius = unitX*50;
		//Graphics (once)
		lime = BitmapFactory.decodeResource(getResources(),R.drawable.lime);
		lemon = BitmapFactory.decodeResource(getResources(), R.drawable.lemon);
		wall = BitmapFactory.decodeResource(getResources(), R.drawable.brick);
		
		//Load Game (Once)
		try {
			readLevels();
		} catch (IOException e) {
			e.printStackTrace();
		}
		loadGame(0);
		
		myThread = new MainThread(holder,this);
		myThread.setRunning(true);
		myThread.start();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		try {
			myThread.setRunning(false);
			myThread.join();
		} catch (InterruptedException e) {
			
		}
	}
	
	public void readLevels() throws IOException {
		InputStream levelsInput;
		levelsInput = getResources().openRawResource(R.raw.levels);
		BufferedReader levelReader = new BufferedReader(new InputStreamReader(levelsInput));
		String bufferedString;
		
		bufferedString = levelReader.readLine();
		noOfLevels = Integer.parseInt(bufferedString);
		levels = new Level[noOfLevels];
		for (int i=0;i<noOfLevels;i++) {
			levels[i] = new Level(this.context,this,lemon);
			bufferedString = levelReader.readLine();
			while (!bufferedString.equals("next")) {
				String[] dimensions = bufferedString.split(" ");
				Obstacle obstacle = new Obstacle(Float.parseFloat(dimensions[0])*unitX, Float.parseFloat(dimensions[1])*unitY, 
												Float.parseFloat(dimensions[2])*unitX, Float.parseFloat(dimensions[3])*unitY,wall);
				levels[i].addObstacle(obstacle);
				bufferedString = levelReader.readLine();
			}
			bufferedString = levelReader.readLine();
			while (!bufferedString.equals("next")) {
				String[] dimensions = bufferedString.split(" ");
				Player player = new Player(Float.parseFloat(dimensions[0])*unitX, Float.parseFloat(dimensions[1])*unitY, 
											Float.parseFloat(dimensions[2])*unitX,Color.parseColor(dimensions[3]),lime);
				levels[i].addPlayer(player);
				bufferedString = levelReader.readLine();
			}
			bufferedString = levelReader.readLine();
			String[] dimensions =bufferedString.split(" ");
			levels[i].setDestination(Float.parseFloat(dimensions[0])*unitX, Float.parseFloat(dimensions[1])*unitY, 
										Float.parseFloat(dimensions[2])*unitX, Float.parseFloat(dimensions[3])*unitY);
			bufferedString = levelReader.readLine();
			levels[i].setMaxNoOfPoints(Integer.parseInt(bufferedString));
		}
	}
	
	public void loadGame(int _level) {
		currentNumberOfPoints = 0;
		currentLevel = _level;
		isReleased = false;
		points = new float[2][levels[currentLevel].getMaxNoOfPoints()];
		levels[currentLevel].resetPlayers();
		
		int resId = getResources().getIdentifier("level"+currentLevel, "drawable", context.getPackageName());
		background = BitmapFactory.decodeResource(getResources(), resId);
		bSource = new Rect(0,0,background.getWidth(),background.getHeight());
		bDest = new Rect(0,0,this.getWidth(),this.getHeight());
	}
	
	public int getNoOfLevels() {
		return this.noOfLevels;
	}
	
	public int getMaxNumberOfPoints() {
		return this.levels[currentLevel].getMaxNoOfPoints();
	}
	
	public int getCurrentLevel () {
		return this.currentLevel;
	}
	
	public boolean getIsReleased() {
		return this.isReleased;
	}
	
	public void setIsReleased(boolean _isReleased) {
		this.isReleased = _isReleased;
	}
	
	public void update() {
		levels[currentLevel].update(this.points, Math.min(currentNumberOfPoints, levels[currentLevel].getMaxNoOfPoints()));
	}
	
	public void draw(Canvas canvas) {
		canvas.drawBitmap(background, bSource, bDest,null);
		
		//Level draw code
		levels[currentLevel].draw(canvas,points,Math.min(currentNumberOfPoints, levels[currentLevel].getMaxNoOfPoints()));
		
		//Gravity draw code
		Paint textPaint = new Paint();
		textPaint.setColor(Color.DKGRAY);
		textPaint.setTextAlign(Paint.Align.CENTER);
		textPaint.setTextSize(25);
		Paint graPaint = new Paint(); graPaint.setColor(0x75888888);
		String status = new String();
		for (int i=0;i<Math.min(currentNumberOfPoints, levels[currentLevel].getMaxNoOfPoints());i++) {
			canvas.drawCircle(points[0][i], points[1][i], pointRadius, graPaint);
		}
		for (int i=0;i<levels[currentLevel].getMaxNoOfPoints()-currentNumberOfPoints;i++)
			status += "O";
		canvas.drawText("Level "+currentLevel+": "+status, 
							getWidth()/2, textPaint.getTextSize(), textPaint);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			//if (currentNumberOfPoints < levels[currentLevel].getMaxNoOfPoints()) {
			addGravityPoint(event.getX(),event.getY());
			//}
		}
		super.onTouchEvent(event);
		return true;
	}
	
	public void addGravityPoint(float x, float y) {
		points[0][currentNumberOfPoints%levels[currentLevel].getMaxNoOfPoints()] = x;
		points[1][currentNumberOfPoints%levels[currentLevel].getMaxNoOfPoints()] = y;
		currentNumberOfPoints++;
	}
}

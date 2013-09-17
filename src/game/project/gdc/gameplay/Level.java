package game.project.gdc.gameplay;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
//import android.graphics.Color;
import android.content.Context;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
//import android.util.Log;

import game.project.gdc.MainGamePanel;
import game.project.gdc.NotificationActivity;
import game.project.gdc.R;

public class Level {
	//private static final String TAG = Level.class.getSimpleName();
	
	public class Destination {
		private float x,y,width,height;
		private Rect sourceRect;
		private Bitmap lemon;
		public Destination(float _x, float _y, float _width, float _height, Bitmap lemon) {
			this.x = _x;
			this.y = _y;
			this.width = _width;
			this.height = _height;
			
			this.lemon = lemon;
			sourceRect = new Rect(0,0,lemon.getWidth(),lemon.getHeight());
		}
		public float getTop() {
			return this.y;
		}
		public float getBottom() {
			return this.y+this.height;
		}
		public float getLeft() {
			return this.x;
		}
		public float getRight() {
			return this.x+this.width;
		}
		
		public void draw(Canvas canvas) {
			/*Paint paint = new Paint();
			paint.setColor(0xffffff00);
			canvas.drawRect(this.x, this.y, this.x+this.width, this.y+this.height, paint);*/
			
			Rect destRect = new Rect((int) this.x,(int) this.y,(int) (this.x+this.width), (int) (this.y+this.height));
			canvas.drawBitmap(lemon, sourceRect, destRect,null);
		}
	}
	
	private Context context;
	private MainGamePanel myPanel;
	
	private ArrayList<Obstacle> obstacles;
	private ArrayList<Player> players;
	private Destination destination;
	private int maxNoOfPoints;
	private Bitmap lemon;
	
	private SoundPool sounds;
	private int crash;
	private int win;
	private int wall;
	
	public Level(Context context, MainGamePanel panel, Bitmap lemon) {
		this.context = context;
		myPanel = panel;
		
		this.lemon = lemon;
		obstacles = new ArrayList<Obstacle>();
		players = new ArrayList<Player>();
		
		sounds = new SoundPool(3,AudioManager.STREAM_MUSIC,0);
		crash = sounds.load(context, R.raw.obstacle,1);
		win = sounds.load(context, R.raw.lemon,1);
		wall = sounds.load(context, R.raw.wall,1);
	}
	
	public ArrayList<Obstacle> getObstacles() {
		return obstacles;
	}
	
	public void addObstacle(Obstacle obstacle) {
		this.obstacles.add(obstacle);
	}
	
	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	public void addPlayer(Player player) {
		this.players.add(player);
	}
	
	public void setDestination(float x, float y, float width, float height) {
		this.destination = new Destination(x,y,width,height,this.lemon);
	}
	
	public Destination getDestination() {
		return destination;
	}
	
	public void setMaxNoOfPoints(int maxNoOfPoints) {
		this.maxNoOfPoints = maxNoOfPoints;
	}
	
	public int getMaxNoOfPoints() {
		return this.maxNoOfPoints;
	}
	
	public void resetPlayers() {
		for (Player p:players) {
			p.setIsPresent(true);
			p.getSpeed().setXv(0);
			p.getSpeed().setYv(0);
			p.resetPosition();
		}
	}
	
	public void draw(Canvas canvas,float[][] points,int numberOfPoints) {
		destination.draw(canvas);
		for (Player p:players) {
			if (p.getIsPresent()) {
				p.draw(canvas,points,numberOfPoints);
			}
		}
		for (Obstacle o:obstacles) {
			o.draw(canvas);
		}
	}
	
	public void update(float[][] points, int noOfPoints) {
		int noNotPresent = 0;
		for (Player p:players) {
			if (p.getIsPresent()) {
				int status = p.update(points, noOfPoints, myPanel.getWidth(), myPanel.getHeight(),this.obstacles, this.destination);
				if (status == 1) {
					sounds.play(crash, 1.0f, 1.0f, 0, 0, 1.5f);
					myPanel.loadGame(myPanel.getCurrentLevel());
				} else if (status == 2) {
					sounds.play(win, 1.0f, 1.0f, 0, 0, 1.5f);
					p.setIsPresent(false);
				} else if (status == 3) {
					sounds.play(wall, 1.0f, 1.0f, 0, 0, 1.5f);
				}
			} else noNotPresent++;
		}
		if (noNotPresent >= players.size()) {
			if (myPanel.getCurrentLevel()<myPanel.getNoOfLevels()-1) {
				myPanel.loadGame(myPanel.getCurrentLevel()+1);
				//Toast.makeText(context, "Next Level!", Toast.LENGTH_SHORT).show();
			} else {
				myPanel.loadGame(0);
				Intent mIntent = new Intent(context, NotificationActivity.class);
				context.startActivity(mIntent);
				//Toast.makeText(context, "You win!", Toast.LENGTH_SHORT).show();
			}
		}
	}
}

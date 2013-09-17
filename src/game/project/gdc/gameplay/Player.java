package game.project.gdc.gameplay;

import java.util.ArrayList;

import android.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import game.project.gdc.gameplay.component.Speed;
import game.project.gdc.gameplay.Obstacle;
import game.project.gdc.gameplay.Level;

public class Player {
	private static final String TAG = Player.class.getSimpleName();
	
	private float ox,oy,x,y,radius,colLeft,colTop,colRight,colBottom;
	private Speed speed;
	private int playerColor;
	private Bitmap lime;
	private boolean isPresent;
	private Rect sourceRect;
	
	public Player(float x, float y, float radius, int playerColor, Bitmap lime) {
		isPresent = true;
		
		this.x = x; this.y = y;
		this.ox = x; this.oy = y;
		this.radius = radius;
		this.playerColor = playerColor;
		
		colLeft = (float) (x-radius); colTop = (float) (y-radius);
		colRight = (float) (x+radius); colBottom = (float) (y+radius);
		this.lime = lime;
		sourceRect = new Rect(0,0,lime.getWidth(),lime.getHeight());
		
		this.speed = new Speed(0,0);
	}
	
	public void resetPosition() {
		x = ox; y = oy;
		colLeft = (float) (x-radius); colTop = (float) (y-radius);
		colRight = (float) (x+radius); colBottom = (float) (y+radius);	
	}
	
	public Speed getSpeed() {
		return this.speed;
	}
	
	public boolean getIsPresent() {
		return this.isPresent;
	}
	
	public void setIsPresent(boolean isPresent) {
		this.isPresent = isPresent;
	}
	
	public void draw(Canvas canvas, float[][] points, int noOfPoints) {
		/*Paint paint = new Paint();
		paint.setColor(playerColor);*/
		
		//canvas.drawCircle(this.x, this.y, radius, paint);
		Rect destRect = new Rect((int) colLeft, (int) colTop, (int) colRight, (int) colBottom);
		canvas.drawBitmap(lime, sourceRect, destRect,null);
		
		Paint stringPaint = new Paint(); stringPaint.setColor(Color.RED);
		stringPaint.setStrokeWidth(4); stringPaint.setAlpha(60);
		for (int i=0;i<noOfPoints;i++) {
			canvas.drawLine(points[0][i], points[1][i], x, y, stringPaint);
		}
	}
	
	//update return 0: nothing 1:collides obstacles or walls 2:finish
	public int update(float[][] points, int noOfPoints, float maxX, float maxY, ArrayList<Obstacle> obstacles, Level.Destination dest) {
		//Find new speed and positions
		float totalX = 0; float totalY = 0;
		for (int i=0;i<noOfPoints;i++) {
			float dx = points[0][i]-this.x;
			float dy = points[1][i]-this.y;
			float ds = (float) Math.sqrt(Math.pow(dx,2)+Math.pow(dy,2));
			float force = (float) Math.sqrt(Math.abs(maxX-ds));
			float forceX = force*(dx/ds) ;
			float forceY = force*(dy/ds);
			
			//if (dx < 0) forceX *= -1;
			//if (dy < 0) forceY *= -1;
			totalX += forceX;
			totalY += forceY;
		}
		
		this.speed.setXv(this.speed.getXv()+totalX/200); this.speed.setYv(this.speed.getYv()+totalY/200);
		this.x += this.speed.getXv(); this.y += this.speed.getYv();
		colLeft = (float) (x-radius); colTop = (float) (y-radius);
		colRight = (float) (x+radius); colBottom = (float) (y+radius);
		
		//Wall Collision
		if (colLeft<0 && this.speed.getXv()<0) {
			this.speed.toggleXv();
			return 3;
		}
		if (colRight>maxX && this.speed.getXv()>0) {
			this.speed.toggleXv();
			return 3;
		}
		if (colTop<0 && this.speed.getYv()<0) {
			this.speed.toggleYv();
			return 3;
		}
		if (colBottom>maxY && this.speed.getYv()>0) {
			this.speed.toggleYv();
			return 3;
		}
		//Obstacle collision
		for (Obstacle o:obstacles) {
			if (!(colLeft > o.getRight() || colRight < o.getLeft() || colTop > o.getBottom() || colBottom < o.getTop())) {
				return 1;
			}
		}
		if (!(colLeft > dest.getRight() || colRight < dest.getLeft() || colTop > dest.getBottom() || colBottom < dest.getTop()))
			return 2;
		return 0;
	}
}

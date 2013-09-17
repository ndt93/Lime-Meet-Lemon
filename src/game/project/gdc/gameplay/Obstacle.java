package game.project.gdc.gameplay;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Obstacle {
	private float x,y,width,height;
	private Bitmap wall;
	private Rect sourceRect;
	public Obstacle(float _x, float _y, float _width, float _height, Bitmap wall) {
		this.x = _x;
		this.y = _y;
		this.width = _width;
		this.height = _height;
		
		this.wall = wall;
		sourceRect = new Rect(0,0,wall.getWidth(),wall.getHeight());
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
		paint.setColor(Color.RED);
		canvas.drawRect(this.x, this.y, this.x+this.width, this.y+this.height, paint);*/
		Rect destRect = new Rect((int) this.x,(int) this.y,(int) (this.x+this.width),(int) (this.y+height));
		canvas.drawBitmap(wall, sourceRect, destRect,null);
	}
}

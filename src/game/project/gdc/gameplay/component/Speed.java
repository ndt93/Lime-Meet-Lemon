package game.project.gdc.gameplay.component;

public class Speed {
	private float xv, yv;
	public Speed(float _xv, float _yv) {
		this.xv = _xv;
		this.yv = _yv;
	}
	
	public void setXv(float _xv) {
		this.xv = _xv;
	}
	
	public float getXv() {
		return this.xv;
	}
	
	public void setYv(float _yv) {
		this.yv = _yv;
	}
	
	public float getYv() {
		return this.yv;
	}
	
	public void toggleXv() {
		this.xv *= -1;
	}
	
	public void toggleYv() {
		this.yv *= -1;
	}
}

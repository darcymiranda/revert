package server;

public class Missile extends Bullet{
	
	private float oldx, oldy, oldr, oldxv, oldyv;

	private boolean posChanged;
	
	public Missile(float x, float y, float xv, float yv, float r, int id) {
		super(x, y, xv, yv, r, id);
	}
	
	public void tick(){
		super.tick();
		
		posChanged = (oldx != x || oldy != y || oldr != r || oldxv != xv || oldyv != yv);
		
		oldx = x;
		oldy = y;
		oldr = r;
		oldxv = xv;
		oldyv = yv;
		
	}
	
	public boolean hasPositionChanged(){ return posChanged; }

}

package server;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class Bullet {
	
	public float x,y,r;
	public float xv, yv;
	
	public int test_id = 0; // TESTING ID
	
	private int id;
	
	private float maxTravelTime = 250;
	private float travelTime;
	
	private Shape hitBox;
	
	public Bullet(float x, float y, float xv, float yv, float r, int id){
		this.x = x;
		this.y = y;
		this.xv = xv;
		this.yv = yv;
		this.r = r;
		this.id = id;
		
		// TODO: get 'real' width/heights
		hitBox = new Rectangle(x,y,10,2);
	}
	
	public void tick(){
		
		travelTime++;
		
		//velocity.x = -((speed * delta) * (float) Math.sin(Math.toRadians(rotation+180)));
		//velocity.y = -((speed * delta) * (float) Math.cos(Math.toRadians(rotation+180)));
		
		x += xv;
		y -= yv;
		
	}
	
	
	public Shape getHitBox(){
		return hitBox;
	}
	public int getId(){ return id; }
	
	public void collide(Ship ship){
		travelTime = maxTravelTime;
	}
	
	public boolean hasExpired(){
		return travelTime >= maxTravelTime;
	}
}

package server;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class Bullet {
	
	public float x,y,r;
	public float xv, yv;
	
	private float maxTravelTime = 250;
	private float travelTime;
	
	public Bullet(float x, float y, float xv, float yv, float r){
		super();
		this.x = x;
		this.y = y;
		this.xv = xv;
		this.yv = yv;
		this.r = r;
	}
	
	public void update(){
		
		travelTime++;
		
		//velocity.x = -((speed * delta) * (float) Math.sin(Math.toRadians(rotation+180)));
		//velocity.y = -((speed * delta) * (float) Math.cos(Math.toRadians(rotation+180)));
		
		x += xv;
		y -= yv;
		
	}
	
	// TODO: get 'real' width/heights
	public Shape getHitBox(){
		return new Rectangle(x, y, 10, 10);
	}
	
	public void collide(Ship ship){
		travelTime = maxTravelTime;
	}
	
	public boolean hasExpired(){
		return travelTime >= maxTravelTime;
	}
}

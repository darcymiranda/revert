package server;

import packet.Packet;

public class Bullet {
	
	public float x,y,r;
	public float xv, yv;
	
	public Bullet(float x, float y, float xv, float yv){
		super();
		this.x = x;
		this.y = y;
		this.xv = xv;
		this.yv = yv;
	}
	
	public void update(){
		
		//velocity.x = -((speed * delta) * (float) Math.sin(Math.toRadians(rotation+180)));
		//velocity.y = -((speed * delta) * (float) Math.cos(Math.toRadians(rotation+180)));
		
		x += xv;
		y -= yv;
	}

}

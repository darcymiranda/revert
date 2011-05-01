package server;

public class Bullet {
	
	public float x,y,r;
	public float xv, yv;
	
	private boolean hasSentToOtherClients;
	
	private float maxTravelTime = 250;
	private float travelTime;
	
	public Bullet(float x, float y, float xv, float yv, float r){
		super();
		this.x = x - xv;
		this.y = y + yv;
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
	
	public boolean hasExpired(){
		return travelTime > maxTravelTime;
	}
	
	public boolean hasSentToOtherClients(){ return hasSentToOtherClients; }
	public void setSentToOtherClients(boolean s){ hasSentToOtherClients = s; }
}

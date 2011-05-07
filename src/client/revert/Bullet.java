package client.revert;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;

public class Bullet extends Entity {    
	
	private boolean hasSentToOtherClients;
	private boolean dead;
	
	private float speed = 900f / 1000f;
	private float maxTravelTime = 250;
	private float travelTime;
	
	public Bullet(float x, float y){
		super();
		velocity = new Vector2f(0,0);
		serverPosition.x = x;
		serverPosition.y = y;
		clientPosition.x = x;
		clientPosition.y = y;
		
		collidable = true;
	}
	
	public void collide(Entity e){
		dead = true;
	}
	
	public void update(GameContainer gc, int delta, boolean interpolate){
		super.update(gc, delta, interpolate);
		
		travelTime++;
		
		velocity.x = -((speed * delta) * (float) Math.sin(Math.toRadians(rotation+180)));
		velocity.y = -((speed * delta) * (float) Math.cos(Math.toRadians(rotation+180)));
		
		clientPosition.x += velocity.x;
		clientPosition.y -= velocity.y;
	}
	
	public boolean hasExpired(){
		return travelTime > maxTravelTime || dead;
	}
	
	public boolean hasSentToOtherClients(){ return hasSentToOtherClients; }
	public void setSentToOtherClients(boolean s){ hasSentToOtherClients = s; }

}

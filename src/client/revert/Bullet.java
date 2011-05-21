package client.revert;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;

public class Bullet extends Entity {    
	
	private boolean hasSentToOtherClients;
	
	protected boolean dead;
	protected float speed = 15;
	protected float maxTravelTime = 250;
	protected float travelTime;
	
	public Bullet(float x, float y, float r, Vector2f shipVel){
		super();
		
		clientPosition.x = x;
		clientPosition.y = y;
		rotation = r;
		
		velocity = new Vector2f(-(speed * (float) Math.sin(Math.toRadians(rotation+180))) + shipVel.x,
								-(speed * (float) Math.cos(Math.toRadians(rotation+180))) + shipVel.y);
		
		//velocity.x = -((speed * delta) * (float) Math.sin(Math.toRadians(rotation+180)));
		//velocity.y = -((speed * delta) * (float) Math.cos(Math.toRadians(rotation+180)));
		
		collidable = true;
	}
	
	public void collide(Entity e){
		dead = true;
	}
	
	public void update(GameContainer gc, int delta){
		super.update(gc, delta);
		
		travelTime++;
	}
	
	public boolean hasExpired(){
		return (travelTime > maxTravelTime) || dead;
	}
	
	public boolean hasSentToOtherClients(){ return hasSentToOtherClients; }
	public void setSentToOtherClients(boolean s){ hasSentToOtherClients = s; }

}

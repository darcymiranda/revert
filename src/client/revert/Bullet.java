package client.revert;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;

public class Bullet extends Entity {    
	
	private boolean hasSentToOtherClients;
	
	public int test_id = 99999;
	public boolean test_real = false;
	
	protected boolean dead;
	protected float speed = 0.45f;		/* dont forget to change on server //ya ya duplicate code blah blah// */
	protected final float maxTravelTime = 250;
	protected float travelTime;
	
	public Bullet(float x, float y, float r, Vector2f shipVel){
		super();
		
		clientPosition.x = x;
		clientPosition.y = y;
		rotation = r;
		
		velocity = new Vector2f(-(speed * (float) Math.sin(Math.toRadians(rotation+180))) + shipVel.x,
								-(speed * (float) Math.cos(Math.toRadians(rotation+180))) + shipVel.y);
		
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
		return (travelTime >= maxTravelTime) || dead;
	}
	
	public boolean hasSentToOtherClients(){ return hasSentToOtherClients; }
	public void setSentToOtherClients(boolean s){ hasSentToOtherClients = s; }

}

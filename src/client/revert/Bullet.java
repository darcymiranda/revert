package client.revert;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;

import packet.Packet;

public class Bullet extends Entity {       
	
	private float speed = 500f / 1000f;
	private float maxTravelTime = 100;
	private float travelTime;
	
	public Bullet(float x, float y){
		super();
		velocity = new Vector2f(0,0);
		serverPosition.x = x;
		serverPosition.y = y;
		clientPosition.x = x;
		clientPosition.y = y;
	}
	
	public void update(GameContainer gc, int delta){
		super.update(gc, delta);
		
		travelTime++;
		
		velocity.x = -((speed * delta) * (float) Math.sin(Math.toRadians(rotation+180)));
		velocity.y = -((speed * delta) * (float) Math.cos(Math.toRadians(rotation+180)));
		
		clientPosition.x += velocity.x;
		clientPosition.y -= velocity.y;
	}
	
	public boolean hasExpired(){
		return travelTime > maxTravelTime;
	}

}

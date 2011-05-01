package client.revert;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;

import packet.Packet;

public class Bullet extends Entity {       
	
	private Vector2f startPosition;
	
	private boolean hasSentToOtherClients;
	
	private float speed = 900f / 1000f;
	private float maxTravelTime = 250;
	private float travelTime;
	
	private int id = 0;
	
	public Bullet(float x, float y){
		super();
		velocity = new Vector2f(0,0);
		startPosition = new Vector2f(x,y);
		serverPosition.x = x;
		serverPosition.y = y;
		clientPosition.x = x;
		clientPosition.y = y;
	}
	
	public void update(GameContainer gc, int delta, boolean interpolate){
		super.update(gc, delta, interpolate);
		
		travelTime++;
		
		velocity.x = -((speed * delta) * (float) Math.sin(Math.toRadians(rotation+180)));
		velocity.y = -((speed * delta) * (float) Math.cos(Math.toRadians(rotation+180)));
		
		clientPosition.x += velocity.x;
		clientPosition.y -= velocity.y;
	}
	
	@Override
	public Packet getPacket(){
		return new Packet(Packet.UPDATE_SELF_BULLET, clientPosition.x, clientPosition.y, velocity.x, velocity.y, rotation);
	}
	
	public boolean hasExpired(){
		return travelTime > maxTravelTime;
	}
	
	public boolean hasSentToOtherClients(){ return hasSentToOtherClients; }
	public void setSentToOtherClients(boolean s){ hasSentToOtherClients = s; }

}

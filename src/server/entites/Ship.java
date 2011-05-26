package server.entites;

import org.newdawn.slick.geom.Vector2f;

import packet.Packet;

public class Ship extends Entity {
	
	private boolean space;
	
	private boolean oldSpace;
	private boolean shootChanged;
	
	private int health = 15;
	
	public Ship(float x, float y, int height, int width, boolean alive){
		super(new Vector2f(x,y));
		this.isAlive = alive;
	}
	
	public void update(){
		super.update();
		
		if(health < 1)
			isAlive = false;
		
		shootChanged = (space != oldSpace);
		
		oldSpace = space;
		
	}
	
	@Override
	public void collide(Entity e){
		if(e instanceof Missile){
			health -=9;
		}
		else if(e instanceof Bullet){
			health -=2;
		}
	}
	
	/**
	 * Grab data sent from the client to update the server side data on the next tick.
	 * @param packet
	 */
	public void netUpdate(Packet packet){
		
		position.x = packet.getPositionX();
		position.y = packet.getPositionY();
		rotation = packet.getRotationR();
		velocity.x = packet.getVelocityX();
		velocity.y = packet.getVelocityY();
	}
	
	public void updateInput(Packet packet) {
		space = packet.getPressedSpace();
	}
	
	
	public boolean isShooting(){ return space; }
	public boolean isAlive(){ return isAlive; }
	
	/**
	 * Returns if the shooting state changed.
	 * @return
	 */
	public boolean hasShootingChanged(){ return shootChanged; }

}

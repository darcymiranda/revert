package server.entites;

import org.newdawn.slick.geom.Vector2f;

import packet.Packet;

public class Ship extends Entity {
	
	private boolean space;
	
	private boolean oldSpace;
	private boolean shootChanged;
	
	private int health = 15;

	private int rate = 125;
	private float accuracy = .96f;
	
	public Ship(float x, float y, int height, int width, boolean alive){
		super(new Vector2f(x,y));
		this.isAlive = alive;
		this.height = height;
		this.width = width;
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
	 * TODO: A packet should not be required here. Eventually, the accuracy should be
	 * caculated server side and a packet should be shipped out to the client; of which,
	 * the client will interpolate it, with it's local side bullet.
	 * @param packet
	 * @return
	 */
	public Bullet shoot(Packet packet){
		
		float cr = packet.getRotationR();
		
		float cx = position.x + (super.width / 2 - 5),
			  cy = position.y + (super.height / 2 - 5); 
		
		return new Bullet(cx, cy, velocity.x, velocity.y, cr, id);
		
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

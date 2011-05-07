package server;

import java.util.ArrayList;

import packet.Packet;

public class Ship {
	
	public float x, y, r;
	public float xv, yv;
	private float oldx, oldy, oldr, oldxv, oldyv;
	private boolean space;
	
	private boolean posChanged;
	private boolean isAlive;
	private boolean oldSpace;
	private boolean isShooting;
	
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	
	public Ship(float x, float y, boolean alive){
		this.x = x; this.y = y; this.isAlive = alive;
	}
	
	/**
	 * Preform process operations to calculate given positions and other logical data.
	 */
	public void tick(){
		
		//x += xv;
		//y -= yv;
		
		// determines if the position has changed from the last one
		posChanged = (oldx != x || oldy != y || oldr != r || oldxv != xv || oldyv != yv);
		
		// determine if there has been a change in inputs
		isShooting = (space != oldSpace);
		
		oldx = x;
		oldy = y;
		oldr = r;
		oldxv = xv;
		oldyv = yv;
		
		oldSpace = space;
		
		// Bullets
		for(int i = 0; i < bullets.size(); i++){
			bullets.get(i).update();
			if(bullets.get(i).hasExpired()) bullets.remove(i);
		}
		
	}
	
	/**
	 * Grab data sent from the client to update the server side data on the next tick.
	 * @param packet
	 */
	public void update(Packet packet){
		
		x = packet.getPositionX();
		y = packet.getPositionY();
		r = packet.getRotationR();
		xv = packet.getVelocityX();
		yv = packet.getVelocityY();
		
	}
	
	public void updateBullets(Packet packet){
		Bullet bullet = new Bullet(packet.getPositionX(), packet.getPositionY(), packet.getVelocityX(), packet.getVelocityY(), packet.getRotationR());
		bullets.add(bullet);
	}
	
	public void updateInput(Packet packet) {
		space = packet.getPressedSpace();
	}
	
	public ArrayList<Bullet> getBullets(){
		return bullets;
	}
	
	public boolean hasPositionChanged(){ return posChanged; }
	
	public boolean isShooting(){ return space; }
	
	/**
	 * Returns if the shooting state changed.
	 * @return
	 */
	public boolean hasShootingChanged(){ return isShooting; }

}

package server;

import java.util.ArrayList;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import packet.Packet;

public class Ship {
	
	public float x, y, r;
	public float xv, yv;
	private float oldx, oldy, oldr, oldxv, oldyv;
	private boolean space;
	
	private boolean posChanged;
	private boolean isAlive;
	private boolean oldSpace;
	private boolean shootChanged;
	
	private int health = 15;
	
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
		
		if(health < 1){
			isAlive = false;
		}
		
		// determines if the position has changed from the last one
		posChanged = (oldx != x || oldy != y || oldr != r || oldxv != xv || oldyv != yv);
		
		// determine if shooting on/off has changed
		shootChanged = (space != oldSpace);
		
		oldx = x;
		oldy = y;
		oldr = r;
		oldxv = xv;
		oldyv = yv;
		
		oldSpace = space;
		
		// Bullets
		for(int i = 0; i < bullets.size(); i++){
			bullets.get(i).tick();
			if(bullets.get(i).hasExpired()) bullets.remove(i);
		}
		
	}
	
	public void collide(Bullet bullet){
		health -=3 ;
	}
	
	// TODO: get 'real' width/heights
	public Shape getHitBox(){
		return new Rectangle(x, y, 36, 45);
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
	public boolean isAlive(){ return isAlive; }
	
	/**
	 * Returns if the shooting state changed.
	 * @return
	 */
	public boolean hasShootingChanged(){ return shootChanged; }

}

package server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import packet.Packet;

public class Ship {
	
	public float x, y, r;
	public float xv, yv;
	private float oldx, oldy, oldr, oldxv, oldyv;
	
	private boolean posChanged;
	private boolean isAlive;
	
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
		
		oldx = x;
		oldy = y;
		oldr = r;
		oldxv = xv;
		oldyv = yv;
		
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
	
	public ArrayList<Bullet> getBullets(){
		return bullets;
	}
	
	public boolean hasPositionChanged(){ return posChanged; }
}

package server;

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
	
	private List<Bullet> bullets = new LinkedList<Bullet>();
	
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
		
		//System.out.println(x + " : " + oldx + "       " + y + " : " + oldy);
		
		oldx = x;
		oldy = y;
		oldr = r;
		oldxv = xv;
		oldyv = yv;
		
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
	
	public Iterator<Bullet> getBulletIterator(){
		return bullets.iterator();
	}
	
	public boolean hasPositionChanged(){ return posChanged; }
}

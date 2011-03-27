package server;

import packet.Packet;

public class Ship {
	
	private boolean w, a, s, d, e, q;
	
	public float x, y, r;
	public float xv, yv;
	private float oldx, oldy, oldr;
	
	private final float ACCELERATION = 4f / 1000;
	private final float maxVelocity = 5;
	
	private boolean posChanged;
	private boolean isAlive;
	
	public Ship(float x, float y, boolean alive){
		this.x = x; this.y = y; this.isAlive = alive;
	}
	
	/**
	 * Preform process operations to calculate given positions and other logical data.
	 */
	public void tick(){
		
		/* OLD
		if(!isAlive){	// change to true when isAlive is implemented properly
			if(w)
				y-=3;
			if(s)
				y+=3;
			if(a)
				x-=3;
			if(d)
				x+=3;
		}
		*/
		
		if(!isAlive){
			if(w)
				yv -= 1;
		}
		
		// determines if the position has changed from the last one
		posChanged = (oldx != x || oldy != y || oldr != r);
		
		oldx = x;
		oldy = y;
		oldr = r;
		
	}
	
	/**
	 * Grab data sent from the client to update the server side data on the next tick.
	 * @param packet
	 */
	public void update(Packet packet){
		w = packet.getPressedW();
		a = packet.getPressedA();
		s = packet.getPressedS();
		d = packet.getPressedD();
		e = packet.getPressedE();
		q = packet.getPressedQ();
	}
	
	public boolean hasPositionChanged(){ return posChanged; }
}

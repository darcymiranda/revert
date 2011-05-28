package client.revert;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.util.FastTrig;

import packet.Packet;

public class Ship extends NetEntity {
	
	private final float ACCELERATION = 0.005f;
	private final float maxVelocity = 5;
	private long lastFire;
	private int rate = 125;
	private float accuracy = .96f;
	private int health = 35;
	
	private boolean isShooting;
	private ConfigurableEmitter particleEngine;
	
	public Ship(Vector2f clientPosition, int id, boolean local){
		
		super();
		setLocal(local);
		collidable = true;
		
		super.clientPosition = clientPosition;
		this.id = id;
		
		particleEngine = (ConfigurableEmitter) Revert.cache.get("particle_ship_engine");
		particleEngine = particleEngine.duplicate();
		Revert.ps.addEmitter(particleEngine);
		
	}
	
	public void collide(Entity e){
		
		if(e instanceof Bullet){
			
			ConfigurableEmitter temp = (ConfigurableEmitter) Revert.cache.get("particle_hit_bullet"),
			temp2 = (ConfigurableEmitter) Revert.cache.get("particle_smoke");

			// duplicate the emitter from the cache so it doesnt overlap. not sure if it's worth caching
			// if we have to duplicate to avoid overlap
			temp = temp.duplicate();
			temp2 = temp2.duplicate();
			
			float centerx = e.clientPosition.x + e.getWidth() /2;
			float centery = e.clientPosition.y + e.getHeight() /2;
			
			temp.setPosition(centerx, centery);
			temp2.setPosition(centerx, centery);
			System.out.println(temp.getX() + " " + temp.getY());
			System.out.println(e.clientPosition.x + " " + e.clientPosition.y);
			
			Revert.ps.addEmitter(temp);
			Revert.ps.addEmitter(temp2);

		}
	}
	
	public void update(GameContainer gc, int delta){
	
		super.update(gc, delta);
		float rotation = super.getRotation();
		
		float centerx = getWidth() /2;
		float centery = getHeight() /2;
		
		// TODO: emitter is rotating the wrong way???
		particleEngine.setPosition((float) FastTrig.cos(Math.toRadians(rotation+90)) * centerx + (clientPosition.x + centerx),
									(float) FastTrig.sin(Math.toRadians(rotation+90)) * centery + (clientPosition.y + centery), false);
		particleEngine.angularOffset.setValue(rotation-180);
		
		if(health < 1){
			isAlive = false;
		}
		
		/**
		 * Input controls
		 */
		
		if(super.isLocal()){
			
			Input in = gc.getInput();
			
			/**
			 * Shooting
			 */
			isShooting = in.isKeyDown(Input.KEY_SPACE);
			if(isShooting){
	
				shoot();
				
			}
			
			if(in.isKeyPressed(Input.KEY_3)){
				
				shootMissile();
					
				Packet packet = new Packet(Packet.UPDATE_SELF_BULLET);
				packet.bulletType = 2;
				Revert.net.send(packet);
					
			}
			
			/**
			 * Movement
			 */
	
			if(in.isKeyDown(Input.KEY_E)){
				
				dirSpeed.x = (float) FastTrig.cos(Math.toRadians(rotation+90));
				dirSpeed.y = (float) FastTrig.sin(Math.toRadians(rotation+90));
				
				float xvelChange = velocity.x + (ACCELERATION * dirSpeed.x);
				if(xvelChange <= maxVelocity && xvelChange >= -maxVelocity){
					
					velocity.x -= (xvelChange - velocity.x) ; // minus the x velocity as it was only needed for the if statement
					
				}
				else{
					
					if(velocity.x > 0)
						velocity.x = maxVelocity /1.2f;
					else
						velocity.x = -maxVelocity /1.2f;
				}
				
				float yvelChange = velocity.y + (ACCELERATION * dirSpeed.y);
				if(yvelChange <= maxVelocity && yvelChange >= -maxVelocity){
	
					velocity.y -= (yvelChange - velocity.y); // minus the y velocity as it was only needed for the if statement
					
				}
				else{
					
					if(velocity.y > 0)
						velocity.y = maxVelocity /1.2f;
					else
						velocity.y = -maxVelocity /1.2f;
					
				}
			}
			
			if(in.isKeyDown(Input.KEY_Q)){
				
				dirSpeed.x = (float) FastTrig.sin(Math.toRadians(rotation-90));
				dirSpeed.y = (float) FastTrig.cos(Math.toRadians(rotation-90));
				
				float xvelChange = velocity.x + (ACCELERATION * dirSpeed.x);
				if(xvelChange <= maxVelocity && xvelChange >= -maxVelocity){
					
					velocity.x += (xvelChange - velocity.x) ; // minus the x velocity as it was only needed for the if statement
					
				}
				else{
					
					if(velocity.x > 0)
						velocity.x = maxVelocity /1.2f;
					else
						velocity.x = -maxVelocity /1.2f;
				}
				
				float yvelChange = velocity.y + (ACCELERATION * dirSpeed.y);
				if(yvelChange <= maxVelocity && yvelChange >= -maxVelocity){
	
					velocity.y += (yvelChange - velocity.y); // minus the y velocity as it was only needed for the if statement
					
				}
				else{
					
					if(velocity.y > 0)
						velocity.y = maxVelocity /1.2f;
					else
						velocity.y = -maxVelocity /1.2f;
					
				}
			}
			
			if(in.isKeyDown(Input.KEY_D)){
				
				rotation += 0.2f * delta;
				dirSpeed.x = (float) FastTrig.sin(Math.toRadians(rotation));
				dirSpeed.y = (float) FastTrig.cos(Math.toRadians(rotation));
				
			}
			
			if(in.isKeyDown(Input.KEY_A)){
				
				rotation += -0.2f * delta;
				dirSpeed.x = (float) FastTrig.sin(Math.toRadians(rotation));
				dirSpeed.y = (float) FastTrig.cos(Math.toRadians(rotation));
				
			}
			
			if(in.isKeyDown(Input.KEY_W)){
				
				// the calculation for increasing the x-axis velocity for forward movement
				float xvelChange = velocity.x + (ACCELERATION * dirSpeed.x);
				
				if(xvelChange <= maxVelocity && xvelChange >= -maxVelocity){
					
					velocity.x += xvelChange - velocity.x; // minus the x velocity as it was only needed for the if statement
					
				}
				else{
					
					if(velocity.x > 0)
						velocity.x = maxVelocity;
					else
						velocity.x = -maxVelocity;
				}
				
				// the calculation for increasing the y-axis velocity for forward movement
				float yvelChange = velocity.y + (ACCELERATION * dirSpeed.y);
				
				if(yvelChange <= maxVelocity && yvelChange >= -maxVelocity){
	
					velocity.y += yvelChange - velocity.y; // minus the y velocity as it was only needed for the if statement
	
				}
				else{
					
					if(velocity.y > 0)
						velocity.y = maxVelocity;
					else
						velocity.y = -maxVelocity;
					
				}
				
			}
			
			if(in.isKeyDown(Input.KEY_S)){
				
				// the calculation for increasing the x-axis velocity for backwards movement
				float xvelChange = velocity.x + (ACCELERATION * dirSpeed.x) / 2;
				
				if(xvelChange <= maxVelocity && xvelChange >= -maxVelocity){
					
					velocity.x -= xvelChange - velocity.x; // minus the x velocity as it was only needed for the if statement
					
				}
				else{
					
					if(velocity.x > 0)
						velocity.x = maxVelocity;
						else
						velocity.x = -maxVelocity;
					
				}
				
				// the calculation for increasing the y-axis velocity for backwards movement
				float yvelChange = velocity.y + (ACCELERATION * dirSpeed.y) / 2;
				
				if(yvelChange <= maxVelocity && yvelChange >= -maxVelocity){
					
					velocity.y -= yvelChange - velocity.y; // minus the y velocity as it was only needed for the if statement
					
				}
				else{
					
					if(velocity.y > 0)
						velocity.y = maxVelocity;
						else
						velocity.y = -maxVelocity;
					
				}
				
			}
			
		}
		// REMOTE
		else {
			if(isShooting){
				shoot();
			}
		}
		
		
		super.setRotation(rotation);
	
	}
	
	public void render(Graphics g){
		super.render(g);
		
		g.drawRect(serverPosition.x, serverPosition.y, getWidth(), getHeight());
		if(font != null)
			font.drawString(clientPosition.x + getWidth() - 50, clientPosition.y + getHeight(), displayText);
		
	}
	
	/**
	 * Shoot a bullet from the ship.
	 */
	private void shoot(){
		
		Bullet bullet;
		if (!(System.currentTimeMillis() - lastFire < rate)) {
			
			lastFire = System.currentTimeMillis();
			
			float calcRotation = rotation - (float)(Math.random() * ((-accuracy * 100) + 100));
			
			float cx = clientPosition.x + (super.getWidth() / 2 - 5), // (float)Math.sin(Math.toRadians(rotation/180))),
				  cy = clientPosition.y + (super.getHeight() / 2 - 5); // (float)Math.cos(Math.toRadians(rotation/180)));
			
			bullet = new Bullet(cx,cy, calcRotation, velocity, this);
			bullet.setImage((Image) Revert.cache.get("default_bullet"));
			bullet.id = id;
			Revert.ec.addBullet(bullet);
			
			// REMOTE
			if(super.isLocal()){
				Packet packet = new Packet(Packet.UPDATE_SELF_BULLET);
				packet.bulletType = 1;
				packet.setRotation(calcRotation);
				Revert.net.send(packet);
			}
		}
		
	}
	
	private void shootMissile(){
		float cx = clientPosition.x + (super.getWidth() / 2 - 5),
		cy = clientPosition.y + (super.getHeight() / 2 - 5); 
		
		Missile missile = new Missile(cx, cy, rotation,
				new Vector2f(velocity), this);
		missile.setImage((Image) Revert.cache.get("test_bullet"));
		
		Revert.ec.addBullet(missile);
	}
	
	/**
	 * TODO: NEEDS TO BE FUCKING REDONE!!
	 * @param serverId
	 */
	public void shootMissileRemote(int serverId){
		float cx = clientPosition.x + (super.getWidth() / 2 - 5),
		cy = clientPosition.y + (super.getHeight() / 2 - 5); 
		
		Missile missile = new Missile(cx, cy, rotation,
				new Vector2f(velocity), this);
		missile.serverId = serverId;
		missile.setImage((Image) Revert.cache.get("test_bullet"));
		
		Revert.ec.addBullet(missile);
	}
	
	@Override
	public Packet getPacket(){
		Packet packet = new Packet(Packet.UPDATE_SELF, clientPosition.x, clientPosition.y, velocity.x, velocity.y, rotation);
		packet.setKeySpace(isShooting);
		return packet;
	}
	
	public void takeDamage(){
		health -= 3;
	}
	
	public void destory(){
		super.destroy();
		Revert.ps.removeEmitter(particleEngine);
	}
	
	public boolean isShooting(){ return isShooting; }
	public void setShooting(boolean s){ this.isShooting = s; }
	
	public void setVelocity(float x, float y){ velocity.x = x; velocity.y = y; }


}

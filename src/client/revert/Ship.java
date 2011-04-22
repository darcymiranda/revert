package client.revert;

import java.awt.Font;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Vector2f;

import packet.Packet;


public class Ship extends Entity {
	
	private final float ACCELERATION = 4f / 1000;
	private final float maxVelocity = 5;
	private float delta;
	
	private Timer time = new Timer();;
	private List<Packet> packetInterval = new LinkedList<Packet>();
	
	public Ship(){
	}

	public Ship(Vector2f clientPosition, int id, boolean c){
		
		super();
		setControlled(c);
		
		this.clientPosition = clientPosition;
		this.id = id;
		
		velocity = new Vector2f();
		dirSpeed = new Vector2f();
		
		try{
			this.setImage(new Image("img/ship.png"));
		}catch(SlickException ex){
			ex.printStackTrace();
		}
		
	}
	
	/**
	 * Creates a ship instance.
	 * @param clientPosition the vector2f clientPosition of where it will spawn
	 * @param id the created ship instance
	 * @return
	 */
	public Ship createInstance(Vector2f clientPosition, int id, boolean c){
		return new Ship(clientPosition, id, c);
	}
	
	public void update(GameContainer gc, int delta){
	
		super.update(gc, delta);
		
		float rotation = super.getRotation();
		
		/**
		 * Input controls
		 */
		
		if(super.isControlled()){
			
			Input in = gc.getInput();
			
			/**
			 * Shooting
			 */
			/**
			
			Bullet bullet;
			
			if(in.isKeyDown(Input.KEY_SPACE)){
	
				try{
					
					if (!(System.currentTimeMillis() - lastFire < rate)) {
						
						lastFire = System.currentTimeMillis();
						
						// get center x coordinates of entity with rotation
						
						float calcRotaiton = rotation - (float)(Math.random() * ((accuracy * 100) + 100));
						
						float cx = clientPosition.x + ((super.getWidth() /2) * (float)Math.sin(Math.toRadians(rotation))),
							  cy = clientPosition.y + ((super.getHeight() /2) * (float)Math.cos(Math.toRadians(rotation)));
						
						bullet = new Bullet(new Vector2f(cx,cy), super.getTeam());
						bullet.setRotation(calcRotaiton);
						bullet.addAnimation(new Image("img/bullet.png"), 8);
						ec.add(bullet);
						
					}
					
				}catch(SlickException ex){
					System.out.println("Error creating bullet for " + this.toString() + ".\nError Reported: " + ex.toString());
				}
				
			}
			
			*/
			
			/**
			 * Movement
			 */
	
			if(in.isKeyDown(Input.KEY_E)){
				
				dirSpeed.x = (float) Math.sin(Math.toRadians(rotation-90));
				dirSpeed.y = (float) Math.cos(Math.toRadians(rotation-90));
				
				float xvelChange = velocity.x + ((ACCELERATION * delta) /5) * dirSpeed.x;
				if(xvelChange <= maxVelocity && xvelChange >= -maxVelocity){
					
					velocity.x -= (xvelChange - velocity.x) ; // minus the x velocity as it was only needed for the if statement
					
				}
				else{
					
					if(velocity.x > 0)
						velocity.x = maxVelocity /2;
					else
						velocity.x = -maxVelocity /2;
				}
				
				float yvelChange = velocity.y + ((ACCELERATION * delta) /5) * dirSpeed.y;
				if(yvelChange <= maxVelocity && yvelChange >= -maxVelocity){
	
					velocity.y -= (yvelChange - velocity.y) /5; // minus the y velocity as it was only needed for the if statement
					
				}
				else{
					
					if(velocity.y > 0)
						velocity.y = maxVelocity /2;
					else
						velocity.y = -maxVelocity /2;
					
				}
			}
			
			if(in.isKeyDown(Input.KEY_Q)){
				
				dirSpeed.x = (float) Math.sin(Math.toRadians(rotation-90));
				dirSpeed.y = (float) Math.cos(Math.toRadians(rotation-90));
				
				float xvelChange = velocity.x + ((ACCELERATION * delta) /5) * dirSpeed.x;
				if(xvelChange <= maxVelocity && xvelChange >= -maxVelocity){
					
					velocity.x += (xvelChange - velocity.x) ; // minus the x velocity as it was only needed for the if statement
					
				}
				else{
					
					if(velocity.x > 0)
						velocity.x = maxVelocity /2;
					else
						velocity.x = -maxVelocity /2;
				}
				
				float yvelChange = velocity.y + ((ACCELERATION * delta) /5) * dirSpeed.y;
				if(yvelChange <= maxVelocity && yvelChange >= -maxVelocity){
	
					velocity.y += (yvelChange - velocity.y) /5; // minus the y velocity as it was only needed for the if statement
					
				}
				else{
					
					if(velocity.y > 0)
						velocity.y = maxVelocity /2;
					else
						velocity.y = -maxVelocity /2;
					
				}
			}
			
			if(in.isKeyDown(Input.KEY_D)){
				
				rotation += 0.2f * delta;
				dirSpeed.x = (float) Math.sin(Math.toRadians(rotation));
				dirSpeed.y = (float) Math.cos(Math.toRadians(rotation));
				
			}
			
			if(in.isKeyDown(Input.KEY_A)){
				
				rotation += -0.2f * delta;
				dirSpeed.x = (float) Math.sin(Math.toRadians(rotation));
				dirSpeed.y = (float) Math.cos(Math.toRadians(rotation));
				
			}
			
			if(in.isKeyDown(Input.KEY_W)){
				
				// the calculation for increasing the x-axis velocity for forward movement
				float xvelChange = velocity.x + (ACCELERATION * delta) * dirSpeed.x;
				
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
				float yvelChange = velocity.y + (ACCELERATION * delta) * dirSpeed.y;
				
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
				float xvelChange = velocity.x + ((ACCELERATION * delta) * dirSpeed.x) / 2;
				
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
				float yvelChange = velocity.y + ((ACCELERATION * delta) * dirSpeed.y) / 2;
				
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
		else{
			/*
			Packet tp0, tp1;
			if(packetInterval.size() > 2){
				tp0 = packetInterval.get(0);
				tp1 = packetInterval.get(1);
				float distancex = tp1.getPositionX() - tp0.getPositionX();
				float distancey = tp1.getPositionY() - tp0.getPositionY();
				
				//if(distancex > 5) distancex /= 5;
				
				velocity.x = ((distancex - velocity.x) * (lastTick));
				velocity.y = -((distancey - velocity.y) * (lastTick));
				
				packetInterval.remove(0);
			
				System.out.println(lastTick + " :: " + velocity.x + " " + velocity.y + " distance x: " + distancex + " distance y: " + distancey + "        " + packetInterval.size());
				
			}
			*/
			
			
		}
		
		clientPosition.x += velocity.x;
		clientPosition.y -= velocity.y;
		
		super.setRotation(rotation);
	
	}
	
	public void render(GameContainer gc, Graphics g){
		super.render(g);
		
		System.out.println(font);
		
	}
	
	public Packet getPacket(){
		//System.out.println(velocity.x + " " + velocity.y);
		return new Packet(Packet.UPDATE_SELF, clientPosition.x, clientPosition.y, velocity.x, velocity.y, rotation);
	}
	
	
	float lastTick = 0;
	/**
	 * Requires a packet object to set this ships x and y serverPosition.
	 * @param p the packet to be read
	 */
	public void setPacket(Packet p){
		
		serverPosition.x = p.getPositionX();
		serverPosition.y = p.getPositionY();
		
		// send velocities and rotation of uncontrolled ships
		if(!super.isControlled()){
			super.setRotation(p.getRotationR());
			velocity.x = p.getVelocityX();
			velocity.y = p.getVelocityY();
			
			//lastTick = time.reset() /1000;
		
			float dx = serverPosition.x - clientPosition.x;
			float dy = serverPosition.y - clientPosition.y;
			
			//packetInterval.add(p);
			
			float xdistMax = 100+velocity.x;
			float ydistMax = 100+velocity.y;
			if((velocity.x == 0 || velocity.y == 0) ||
					(dx > xdistMax || dx < xdistMax) || (dy > ydistMax || dx < ydistMax)){
				
				//clientPosition.x = p.getPositionX();
				//clientPosition.y = p.getPositionY();
			}
			
		}
	}
}

package client.revert;

import java.awt.Font;
import java.util.Iterator;
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
	private long lastFire;
	private int rate = 100;
	private float accuracy = .97f;
	
	private List<Bullet> bullets = new LinkedList<Bullet>();
	
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
			
			Bullet bullet;
			
			if(in.isKeyDown(Input.KEY_SPACE)){
	
				try{
					
					if (!(System.currentTimeMillis() - lastFire < rate)) {
						
						lastFire = System.currentTimeMillis();
						
						float calcRotaiton = rotation - (float)(Math.random() * ((-accuracy * 100) + 100));
						
						float cx = clientPosition.x + (super.width / 2 - 5), // (float)Math.sin(Math.toRadians(rotation/180))),
							  cy = clientPosition.y + (super.height / 2 - 5); // (float)Math.cos(Math.toRadians(rotation/180)));
						
						bullet = new Bullet(cx,cy);
						bullet.setRotation(calcRotaiton);
						bullet.setImage(new Image("img/bullet.png"));
						bullets.add(bullet);
						
					}
					
				}catch(SlickException ex){
					System.err.println("Error creating bullet for " + this.toString() + ".\nError Reported: " + ex.toString());
				}
				
			}
			
			
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
		
		clientPosition.x += velocity.x;
		clientPosition.y -= velocity.y;
		
		super.setRotation(rotation);
		
		for(int i = 0; i < bullets.size(); i++){
			bullets.get(i).update(gc, delta);
			if(bullets.get(i).hasExpired()) bullets.remove(i);
		}
	
	}
	
	public void render(Graphics g){
		super.render(g);
		
		g.drawRect(serverPosition.x, serverPosition.y, width, height);
		font.drawString(clientPosition.x + width - 50, clientPosition.y + height, username);
		
		for(Bullet bullet : bullets) bullet.render(g);
		
	}

}

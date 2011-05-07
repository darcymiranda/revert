package client.revert;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Vector2f;

import packet.Packet;

/**
 * 
 * Display State - entity information to be rendered
 * Previous State - last ticks entity information
 * Simulated State - information of entity ahead by one tick
 * 
 * @author dmiranda
 *
 */
public abstract class Entity {
	
	protected Vector2f clientPosition;
	protected Vector2f serverPosition;
	protected Vector2f velocity;
	protected Vector2f dirSpeed; 	// Holds the direction speed;
	
	protected int height, width;
	protected int id;
	protected float rotation;
	
	private boolean isLocal; // determines if the player can control the ship
	private boolean isAlive;
	protected boolean collidable;
	
	private Image image;
	
	String username = "";
	protected UnicodeFont font;
	
	// Network states
	private EntityState displayState, previousState, simulateState;
	private float smoothing = 0;
	
	public Entity(){
		clientPosition = new Vector2f(200,200);
		serverPosition = new Vector2f(200,200);
		velocity = new Vector2f();
		dirSpeed = new Vector2f();
		isAlive = true;
		
		displayState = new EntityState(this);
		previousState =  new EntityState(this);
		simulateState =  new EntityState(this);
		
	}
	
	abstract public void collide(Entity e);
	
	/**
	 * Requires a packet object to set this ships x and y serverPosition.
	 * @param p the packet to be read
	 */
	public void updatePacket(Packet p){
		
		serverPosition.x = p.getPositionX();
		serverPosition.y = p.getPositionY();
		
		if(!isLocal){
			
			smoothing = 1;
			
			previousState.setState(simulateState);
			
			simulateState.setVelocity(p.getVelocityX(), p.getVelocityY());
			simulateState.setRotation(p.getRotationR());
		
			// Teleport entity to proper location, if the client and server positions are out of sync.
			float distance = serverPosition.distance(displayState.getPosition());
			if(distance > 75 || distance < -75)
				simulateState.setPosition((serverPosition.x - p.getVelocityX()), (serverPosition.y + p.getVelocityY()));

		}
	}
	
	/**
	 * Returns a packet object containing the rotation; x, y positions and x, y, velocities.
	 * @return
	 */
	public Packet getPacket(){
		return new Packet(Packet.UPDATE_SELF, clientPosition.x, clientPosition.y, velocity.x, velocity.y, rotation);
	}
	
	/**
	 * Add a graphic to the entity that will be rendered.
	 * @param img
	 */
	public void setImage(Image img){
		
		height = img.getHeight();
		width = img.getWidth();
		
		image = img.copy();
		
	}
	
	public void update(GameContainer gc, int delta, boolean interpolate){
		
		// interpolate non-local entities
		if(!isLocal && interpolate){
			previousState.update();
			simulateState.update();
			
			// determine smoothing factor - six equals ticks per packet sent
			smoothing -= (1 / 6);
			if(smoothing < 0) smoothing = 0;
			
			// interpolate
			displayState.setPosition(previousState.getPosition().x + (simulateState.getPosition().x - previousState.getPosition().x) * smoothing, 
					displayState.getPosition().y = previousState.getPosition().y + (simulateState.getPosition().y - previousState.getPosition().y) * smoothing);
			displayState.setRotation(previousState.getRotation() + (simulateState.getRotation() - previousState.getRotation()) * smoothing);
			
			// set new positions
			clientPosition.x = displayState.getPosition().x;
			clientPosition.y = displayState.getPosition().y;
			rotation = displayState.getRotation();
			
		}
		
		image.rotate(rotation - image.getRotation());
		
	}
	
	public void render(Graphics g){
		
		g.drawImage(image, clientPosition.x, clientPosition.y);
		
	}
	
	public float getRotation(){ return rotation; }
	public void setRotation(float rotation) { this.rotation = rotation; }
	
	public boolean isAlive(){ return isAlive; }
	public void setAlive(boolean isAlive){ this.isAlive = isAlive; }
	
	public boolean isLocal(){ return isLocal; }
	public void setLocal(boolean isLocal){ this.isLocal = isLocal; }
	
	public int getId(){ return id; }
	
	public Vector2f getClientPosition(){ return new Vector2f(clientPosition); }
	public Vector2f getServerPosition(){ return new Vector2f(serverPosition); }
	public Vector2f getVelocity(){ return new Vector2f(velocity); }
	
	public Vector2f getPosition(){ return displayState.getPosition(); }
	

}

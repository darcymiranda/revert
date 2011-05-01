package client.revert;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Vector2f;

import client.ActionSender;

import packet.Packet;

/**
 * 
 * -- clientPosition & serverPosition --
 * 
 * The clientPosition is the client-side position that the entity is current in. It is value passed to the server each tick. The serverPosition is
 * the position that the entity is in on the server, that originated from the client's clientPosition.
 * 
 * Only the serverSide position get's rendered, while the clientSide position is the one that is updated
 * each game tick.
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
	
	private Image image;
	
	private long time;
	
	public String username = "";
	public UnicodeFont font;
	
	// Network states
	private EntityState displayState, previousState, simulateState;
	private float curSmoothing = 0;
	
	public Entity(){
		clientPosition = new Vector2f(200,200);
		serverPosition = new Vector2f(-255,-255);
		velocity = new Vector2f();
		dirSpeed = new Vector2f();
		isAlive = true;
		
		displayState = new EntityState(this);
		previousState =  new EntityState(this);
		simulateState =  new EntityState(this);
		
	}
	
	/**
	 * Requires a packet object to set this ships x and y serverPosition.
	 * @param p the packet to be read
	 */
	public void setPacket(Packet p){
		
		boolean justSpawned = (serverPosition.x == -255 && serverPosition.y == -255);
		
		serverPosition.x = p.getPositionX();
		serverPosition.y = p.getPositionY();
		
		if(!isLocal){
			
			curSmoothing = 1;
			
			previousState.setState(simulateState);
			
			// TEMP - needed when a client spawns far away from starting position
			if(justSpawned){
				simulateState.setPosition(p.getPositionX(), p.getPositionY());
				System.out.println("SET!");
			}
			
			simulateState.setVelocity(p.getVelocityX(), p.getVelocityY());
			simulateState.setRotation(p.getRotationR());
		
			// Teleport entity to proper location, if the client and server positions are out of sync.
			float dx = serverPosition.x - clientPosition.x;
			float dy = serverPosition.y - clientPosition.y;
			float xdistMax = 75+velocity.x;
			float ydistMax = 75+velocity.y;
			if((velocity.x == 0 || velocity.y == 0) ||
					(dx > xdistMax || dx < -xdistMax) || (dy > ydistMax || dx < -ydistMax)){
				
				simulateState.setPosition(p.getPositionX(), p.getPositionY());
				
			}
			
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
		
		time = gc.getTime();
		
		// interpolate non-local entities
		if(!isLocal && interpolate){
			previousState.update();
			simulateState.update();
			
			curSmoothing -= (1 / 6);
			if(curSmoothing < 0) curSmoothing = 0;
			
			displayState.setPosition(previousState.getPosition().x + (simulateState.getPosition().x - previousState.getPosition().x) * curSmoothing, 
					displayState.getPosition().y = previousState.getPosition().y + (simulateState.getPosition().y - previousState.getPosition().y) * curSmoothing);
			displayState.setRotation(previousState.getRotation() + (simulateState.getRotation() - previousState.getRotation()) * curSmoothing);
			
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
	
	public void setPosition(float x, float y){
		clientPosition.x = x;
		clientPosition.y = y;
	}

}

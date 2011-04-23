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
	protected int id; 				// refers to the client id that owns the entity
	protected float rotation;
	
	private boolean isControlled; // determines if the player can control the ship
	private boolean isAlive;
	
	private Image image;
	
	/** TEMP **/
	public String username = "";
	public UnicodeFont font;
	
	public Entity(){
		clientPosition = new Vector2f(200,200);
		serverPosition = new Vector2f(200,200);
		isAlive = true;
	}
	
	/**
	 * Requires a packet object to set this ships x and y serverPosition.
	 * @param p the packet to be read
	 */
	public void setPacket(Packet p){
		
		serverPosition.x = p.getPositionX();
		serverPosition.y = p.getPositionY();
		
		// send velocities and rotation of uncontrolled ships
		if(!isControlled){
			rotation = p.getRotationR();
			velocity.x = p.getVelocityX();
			velocity.y = p.getVelocityY();
		
			float dx = serverPosition.x - clientPosition.x;
			float dy = serverPosition.y - clientPosition.y;
			
			float xdistMax = 100+velocity.x;
			float ydistMax = 100+velocity.y;
			if((velocity.x == 0 || velocity.y == 0) ||
					(dx > xdistMax || dx < xdistMax) || (dy > ydistMax || dx < ydistMax)){
				
				//clientPosition.x = p.getPositionX();
				//clientPosition.y = p.getPositionY();
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
	
	public void update(GameContainer gc, int delta){
		
		image.rotate(rotation - image.getRotation());
		
	}
	
	public void render(Graphics g){
		
		g.drawImage(image, clientPosition.x, clientPosition.y);
		
	}
	
	public float getRotation(){ return rotation; }
	public void setRotation(float rotation) { this.rotation = rotation; }
	
	public boolean isAlive(){ return isAlive; }
	public void setAlive(boolean isAlive){ this.isAlive = isAlive; }
	
	public boolean isControlled(){ return isControlled; }
	public void setControlled(boolean isControlled){ this.isControlled = isControlled; }
	
	public int getId(){ return id; }
	
	public Vector2f getClientPosition(){ return new Vector2f(clientPosition); }
	public Vector2f getServerPosition(){ return new Vector2f(serverPosition); }
	public void setPosition(float x, float y){
		clientPosition.x = x;
		clientPosition.y = y;
	}

}

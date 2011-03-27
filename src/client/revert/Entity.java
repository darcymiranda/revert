package client.revert;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

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
	
	private Image image;
	
	public Entity(){
		clientPosition = new Vector2f(200,200);
		serverPosition = new Vector2f(200,200);
	}
	
	/**
	 * A controlled instance creation of the Entity object.
	 * @return Entity
	 */
	public abstract Entity createInstance(Vector2f position);
	
	public abstract void setPacket(Packet p);
	
	public abstract boolean isControlled();
	
	/**
	 * Add a graphic to the entity that will be rendered.
	 * @param img
	 */
	public void setImage(Image img){
		
		height = img.getHeight();
		width = img.getWidth();
		
		image = img;
		
	}
	
	public void update(GameContainer gc, int delta){
		
		image.rotate(rotation - image.getRotation());
		
		
		
	}
	
	public void render(Graphics g){
		
		g.drawImage(image, serverPosition.x, serverPosition.y);
		
	}
	
	public float getRotation(){ return rotation; }
	public void setRotation(float rotation) { this.rotation = rotation; }
	
	public int getId(){ return id; }
	
	public Vector2f getClientPosition(){ return new Vector2f(clientPosition); }
	public Vector2f getServerPosition(){ return new Vector2f(serverPosition); }
	public void setPosition(float x, float y){
		clientPosition.x = x;
		clientPosition.y = y;
	}
	

}

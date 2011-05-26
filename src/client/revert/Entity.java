package client.revert;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public abstract class Entity {
	
	public Vector2f clientPosition;
	public Vector2f velocity;
	protected Vector2f minimapPosition;
	protected Vector2f dirSpeed; 	// Holds the direction speed;
	
	public int id;
	protected int height, width;
	protected float rotation;
	
	protected boolean isAlive;
	protected boolean collidable;
	
	private Image image;
	
	protected String displayText = "";
	protected UnicodeFont font;
	protected Color minimapColor;
	
	public Entity(){
		clientPosition = new Vector2f();
		minimapPosition = new Vector2f(-1, -1);
		velocity = new Vector2f();
		dirSpeed = new Vector2f();
		isAlive = true;
		
	}
	
	abstract public void collide(Entity e);
	
	/**
	 * Add a graphic to the entity that will be rendered.
	 * @param img
	 */
	public void setImage(Image img){
		
		height = img.getHeight();
		width = img.getWidth();
		
		image = img.copy();
		
	}
	
	public Shape getHitBox(){
		return new Rectangle(clientPosition.x, clientPosition.y, width, height);
	}
	
	public void destroy(){
		isAlive = false;
	}
	
	public void update(GameContainer gc, int delta){
		
		if(rotation < 0) rotation += 360;
		if(rotation > 360) rotation -= 360;
		
		clientPosition.x += velocity.x * delta;
		clientPosition.y -= velocity.y * delta;
		
		image.rotate(rotation - image.getRotation());
		
	}
	
	public void render(Graphics g){
		
		g.drawImage(image, clientPosition.x, clientPosition.y);
		
	}
	
	public float getRotation(){ return rotation; }
	public void setRotation(float rotation) { this.rotation = rotation; }
	
	public boolean isAlive(){ return isAlive; }
	public void setAlive(boolean isAlive){ this.isAlive = isAlive; }
	
	public int getId(){ return id; }
	
	public Vector2f getClientPosition(){ return new Vector2f(clientPosition); }
	public Vector2f getMinimapPosition() { return new Vector2f(minimapPosition); }
	public Vector2f getVelocity(){ return new Vector2f(velocity); }
	
	public void setMinimapPosition(Vector2f minimapPosition) { this.minimapPosition = minimapPosition; }
	
	public Color getMinimapColor() { return minimapColor; }
	

}

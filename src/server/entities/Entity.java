package server.entities;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import server.Constants;

public abstract class Entity {
	
	public Vector2f position;
	public Vector2f velocity;
	public float rotation;
	
	public int id;
	
	protected int height, width;
	
	protected boolean isAlive;
	protected boolean collidable;
	
	private Rectangle hitBox;
	private float oldx, oldy, oldr, oldxv, oldyv;
	private boolean posChanged;
	
	public Entity(Vector2f position){
		this.position = position;
		velocity = new Vector2f();
		isAlive = true;
		
		hitBox = new Rectangle(position.x, position.y, height, width);
		
	}
	
	abstract public void collide(Entity e);
	
	public Shape getHitBox(){
		return new Rectangle(position.x, position.y, width, height);
	}
	
	public void destroy(){
		isAlive = false;
	}
	
	public void update(){
		
		if(rotation < 0) rotation += 360;
		if(rotation > 360) rotation -= 360;
		
		position.x += velocity.x * Constants.SERVER_DELTA;
		position.y -= velocity.y * Constants.SERVER_DELTA;
		
		hitBox.setLocation(position.x, position.y);
		
		posChanged = (oldx != position.x || oldy != position.y || oldr != rotation ||
				oldxv != velocity.y || oldyv != velocity.y);
		
	}
	
	public boolean hasPositionChanged(){ return posChanged; }
	
	public float getRotation(){ return rotation; }
	public void setRotation(float rotation) { this.rotation = rotation; }
	
	public boolean isAlive(){ return isAlive; }
	public void setAlive(boolean isAlive){ this.isAlive = isAlive; }
	
	public int getHeight(){ return height; }
	public void setHeight(int h){
		this.height = h;
		hitBox.setHeight(h);
	}
	
	public int getWidth(){ return width; }
	public void setWidth(int w){
		this.width = w;
		hitBox.setWidth(w);
	}
	
	public int getId(){ return id; }
	
	public Vector2f getPosition(){ return new Vector2f(position); }
	public Vector2f getVelocity(){ return new Vector2f(velocity); }

}

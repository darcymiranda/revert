package client.revert;

import org.newdawn.slick.geom.Vector2f;

public class EntityState {
	
	public Vector2f position;
	public Vector2f velocity;
	
	public float rotation;
	
	public EntityState(Entity entity){
		position = entity.getClientPosition();
		velocity = entity.getVelocity();
		rotation = entity.getRotation();
	}
	
	public void update(int delta){
		
		position.x += velocity.x * delta;
		position.y -= velocity.y * delta;
		
	}
	
	public void setState(EntityState state){
		position = state.getPosition();
		velocity = state.getVelocity();
		rotation = state.getRotation();
	}

	public void setPosition(float x, float y){ position.x = x; position.y = y; }
	public void setVelocity(float x, float y){ velocity.x = x; velocity.y = y; }
	public void setRotation(float r){ rotation = r; }
	
	public Vector2f getPosition(){ return new Vector2f(position); }
	public Vector2f getVelocity(){ return new Vector2f(velocity); }
	public float getRotation(){ return rotation; }

}

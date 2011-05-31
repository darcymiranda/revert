package server.entities;

import org.newdawn.slick.geom.Vector2f;

import server.entities.Entity;

import server.Constants;
import server.World;
import util.Stopwatch;

public class Missile extends Bullet{

	private final int HEIGHT = 10,
					WIDTH = 2;
	
	private final float TURN_SPEED = 0.11f;
	private final float ACCELERATION = 0.0003f;
	private final int PREP_TIME = 50;	// how long the prep state lasts
	
	private final float RANGE = 2000;
	private final float MAX_SPEED = 0.50f;
	private float speed = 0;
	private int state = 0;  				// phases of the missile
	
	private Entity targetEntity;
	private Entity oldTargetEntity;
	private Stopwatch timer = new Stopwatch();
	
	public Missile(float x, float y, float xv, float yv, float r, Entity owner) {
		super(x, y, xv, yv, r, owner);
		super.height = HEIGHT;
		super.width = WIDTH;
	}
	
	public void trackTarget(Entity e){
		if(e.id != super.id){
			oldTargetEntity = targetEntity;
			targetEntity = e;
			state = 2;
		}
	}
	
	private void findTarget(){
		Ship temp = World.getInstance().getNearestShip(owner, RANGE);
		oldTargetEntity = targetEntity;

		if(temp != null){
			state++;
			targetEntity = temp;
		}
	}
	
	private void preping(){
		
		if(timer.elapsed() >= PREP_TIME*Constants.SERVER_DELTA){
			state++;
		}
	}
	
	private void chaseTarget(){
		if(targetEntity != null && targetEntity.isAlive()){
			
			velocity.x = -(speed * (float) Math.sin(Math.toRadians(rotation+180)));
			velocity.y = -(speed * (float) Math.cos(Math.toRadians(rotation+180)));
			
			Vector2f targetDir = new Vector2f(targetEntity.position.x + (targetEntity.width /2) - position.x + (width /2),
											targetEntity.position.y + (targetEntity.height /2) - position.y + (height/2));
			
			
			if(velocity.y * targetDir.x + velocity.x * targetDir.y > 0){
				rotation += TURN_SPEED * Constants.SERVER_DELTA;
			}
			else{
				rotation -= TURN_SPEED * Constants.SERVER_DELTA;
			}
			
		}
		else{
			state = 1;
		}
	}
	
	public void update(){
		super.update();
		
		velocity.x = -(speed * (float) Math.sin(Math.toRadians(rotation+180)));
		velocity.y = -(speed * (float) Math.cos(Math.toRadians(rotation+180)));
		
		if(speed < MAX_SPEED){
			speed += ACCELERATION * Constants.SERVER_DELTA;
			speed += ACCELERATION * Constants.SERVER_DELTA;
		}
		
		if(state == 0) preping();
		if(state == 1) findTarget();
		if(state == 2) chaseTarget();
		
	}
	
	public boolean hasTargetChanged(){
		if(targetEntity != null){
			if(oldTargetEntity != null &&
					targetEntity.id != oldTargetEntity.id){
				return true;
			}
			// if the target is not null now and the old one became null that
			// means that the target changed from nothing to something :)
			if(oldTargetEntity == null){
				oldTargetEntity = targetEntity;
				return true;
			}
		}
		return false;
	}
	
	public Entity getTargetedEntity(){
		return targetEntity;
	}
		
}

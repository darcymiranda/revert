package server.entities;

import org.newdawn.slick.geom.Vector2f;

import server.entities.Entity;

import server.Constants;
import util.Stopwatch;

public class Missile extends Bullet{

	private final int HEIGHT = 10,
					WIDTH = 2;
	
	private final float TURN_SPEED = 0.11f;
	private final float ACCELERATION = 0.003f;
	private final int PREP_TIME = 50;	// how long the prep state lasts
	
	private final float MAX_SPEED = 0.50f;
	private float speed = 0;
	private int state = 0;  				// phases of the missile
	
	private int delta;
	
	private Entity targetEntity;
	private Stopwatch timer = new Stopwatch();
	
	public Missile(float x, float y, float xv, float yv, float r, int id) {
		super(x, y, xv, yv, r, id);
		super.height = HEIGHT;
		super.width = WIDTH;
	}
	
	public void trackTarget(Entity e){
		if(e.id != super.id){
			targetEntity = e;
			state = 2;
		}
	}
	
	private void findTarget(){
		//targetEntity = Revert.ec.getNearestEntity(this, 2000);
		if(targetEntity != null)
			state++;
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
			speed += ACCELERATION;
			speed += ACCELERATION;
		}
		
		if(state == 0) preping();
		if(state == 1) findTarget();
		if(state == 2) chaseTarget();
		
	}
		
}

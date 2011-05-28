package client.revert;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.particles.ConfigurableEmitter;

import util.Stopwatch;

public class Missile extends Bullet {
	
	private final float TURN_SPEED = 0.11f;
	private final float ACCELERATION = 0.0003f;
	private final int PREP_TIME = 50;	// how long the prep state lasts
	
	private final float RANGE = 2000;
	private final float MAX_SPEED = 0.50f;
	private float speed = 0;
	private int state = 0;  				// phases of the missile
	
	private int delta;
	
	private Entity targetEntity;
	private Stopwatch timer = new Stopwatch();
	private ConfigurableEmitter trail;
	
	public Missile(float x, float y, float r, Vector2f shipVel, Entity owner){
		super(x, y, r, shipVel, owner);
		
		collidable = true;
		trail = (ConfigurableEmitter) Revert.cache.get("particle_missile_trail");
		trail = trail.duplicate();
		Revert.ps.addEmitter(trail);
		
	}
	
	public void trackTarget(Entity e){
		if(e != null)
			if(e.id != super.id){
				targetEntity = e;
				state = 2;
			}
	}
	
	private void findTarget(){
		targetEntity = Revert.ec.getNearestEntity(this, RANGE);
		if(targetEntity != null)
			state++;
	}
	
	private void preping(){
		
		if(timer.elapsed() >= PREP_TIME*delta){
			state++;
		}
	}
	
	private void chaseTarget(){
		if(targetEntity != null && targetEntity.isAlive()){
			
			velocity.x = -(speed * (float) Math.sin(Math.toRadians(rotation+180)));
			velocity.y = -(speed * (float) Math.cos(Math.toRadians(rotation+180)));
			
			Vector2f targetDir = new Vector2f(targetEntity.clientPosition.x + (targetEntity.getWidth() /2) - clientPosition.x + (getWidth() /2),
											targetEntity.clientPosition.y + (targetEntity.getHeight() /2) - clientPosition.y + (getHeight()/2));
			
			
			if(velocity.y * targetDir.x + velocity.x * targetDir.y > 0){
				rotation += TURN_SPEED * delta;
			}
			else{
				rotation -= TURN_SPEED * delta;
			}
			
		}
		else{
			state = 1;
		}
	}
	
	public void update(GameContainer gc, int delta){
		super.update(gc, delta);
		
		this.delta = delta;
		
		trail.setPosition(clientPosition.x + (getWidth()/2), clientPosition.y + (getHeight()/2), false);
		trail.angularOffset.setValue(rotation);
		
		velocity.x = -(speed * (float) Math.sin(Math.toRadians(rotation+180)));
		velocity.y = -(speed * (float) Math.cos(Math.toRadians(rotation+180)));
		
		if(speed < MAX_SPEED){
			speed += ACCELERATION * delta;
			speed += ACCELERATION * delta;
		}
		
		if(state == 0) preping();
		//if(state == 1) findTarget();
		if(state == 2) chaseTarget();
		
	}

	@Override
	public void collide(Entity e) {
		dead = true;
		targetEntity = null;
		state = -1;
	}

}

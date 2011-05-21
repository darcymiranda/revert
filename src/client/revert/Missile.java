package client.revert;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.particles.ConfigurableEmitter;

public class Missile extends Bullet {
	
	private final float TURN_SPEED = 100f;
	private final float speed = 300f / 1000f;
	
	private boolean targetAquired;
	
	private Entity targetEntity;
	
	public Missile(float x, float y, float r, Vector2f shipVel){
		super(x, y, r, shipVel);
		
		java.util.Random rand = new java.util.Random();
		if(rand.nextBoolean()){
		}
		
		collidable = true;
		
	}
	
	public void trackTarget(Entity e){
		targetEntity = e;
		if(targetEntity.id != super.id)
			targetAquired = true;
	}
	
	public void update(GameContainer gc, int delta){
		super.update(gc, delta);
		
		float dx = (targetEntity.clientPosition.x + targetEntity.width /2) - clientPosition.x;
		float dy = (targetEntity.clientPosition.y + targetEntity.height /2) - clientPosition.y;
		
		float distance = (float) Math.sqrt((dx *dx)+(dy*dy));
		
		dx /= distance;
		dy /= distance;
		
		float vx = dx * (TURN_SPEED * delta);
		float vy = dy * (TURN_SPEED * delta);
		
		float v = (float) Math.sqrt((vx * vx)+(vy * vy));
		
		if(v > speed){
			vx = (vx * (speed * delta)) / v;
			vy = (vy * (speed * delta)) / v;
		}
		
		velocity.x = vx;
		velocity.y = -vy;
		
		/*
		if(targetAquired || !targetEntity.isAlive()){
			float xDist = targetEntity.clientPosition.x - clientPosition.x;
			float yDist = targetEntity.clientPosition.y - clientPosition.y;
			System.out.println((velocity.x * xDist) - (velocity.y * yDist));
			if((velocity.x * xDist) - (velocity.y * yDist) > 0)
				rotation += TURN_SPEED;
			else
				rotation -= TURN_SPEED;
		}
		
		velocity.x = (speed * delta) * (float) Math.sin(Math.toRadians(rotation+180));
		velocity.y = (speed * delta) * (float) Math.cos(Math.toRadians(rotation+180));
		*/
		
	}

	@Override
	public void collide(Entity e) {
		dead = true;
	}

}

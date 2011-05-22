package client.revert;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.particles.ConfigurableEmitter;

public class Missile extends Bullet {
	
	private final float TURN_SPEED = 0.01f;
	private final float speed = 300f / 1000f;
	private final float acceleration = 1.1f;
	
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
		
		float vx = dx * TURN_SPEED;
		float vy = dy * TURN_SPEED;
		
		float total = (float) Math.sqrt((vx * vx)+(vy * vy));

		// ease the turn
		vx = (vx * (speed * delta)) / total;
		vy = (vy * (speed * delta)) / total;
		
		velocity.x = vx;
		velocity.y = -vy;
		
		rotation = (Math.round(180 - ((Math.atan2(targetEntity.clientPosition.x - clientPosition.x,
				targetEntity.clientPosition.y - clientPosition.y)) * 180/Math.PI)));
		
	}

	@Override
	public void collide(Entity e) {
		dead = true;
	}

}

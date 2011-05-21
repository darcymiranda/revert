package client.revert;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.particles.ConfigurableEmitter;

public class Missile extends Entity {
	
	private final float TURN_SPEED = 7f;
	private final float speed = 400f / 1000f;
	
	private float target;
	
	private ConfigurableEmitter test;
	
	public Missile(Vector2f position, float rotation){
		this.clientPosition = new Vector2f(position);
		this.target = rotation;
		
		java.util.Random rand = new java.util.Random();
		if(rand.nextBoolean()){
		}
		
		test = (ConfigurableEmitter) Revert.cache.get("particle_smoke");
		test = test.duplicate();
		Revert.ps.addEmitter(test);
		
		collidable = true;
		
	}
	
	public void update(GameContainer gc, int delta, boolean interpolate){
		super.update(gc, delta, interpolate);
		
		if(target > rotation)
		rotation += TURN_SPEED;
		
		test.setPosition(clientPosition.x, clientPosition.y);
		
		velocity.x = -((speed * delta) * (float) Math.sin(Math.toRadians(rotation+180)));
		velocity.y = -((speed * delta) * (float) Math.cos(Math.toRadians(rotation+180)));
		
		clientPosition.x += velocity.x;
		clientPosition.y -= velocity.y;
		
	}

	@Override
	public void collide(Entity e) {
		
	}

}

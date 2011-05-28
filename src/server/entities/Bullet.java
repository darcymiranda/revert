package server.entities;

import org.newdawn.slick.geom.Vector2f;


public class Bullet extends Entity{
	
	public int test_id = 0; // TESTING ID
	
	protected Entity owner;
	
	private final int HEIGHT = 10,
					WIDTH = 2;
	
	private float maxTravelTime = 250;
	private float travelTime;
	
	private float speed = 0.45f;	/* dont forget to change on client //ya ya duplicate code blah blah// */
	
	public Bullet(float x, float y, float xv, float yv, float r, Entity owner){
		super(new Vector2f(x,y));
		velocity.x = xv;
		velocity.y = yv;
		rotation = r;
		super.id = id;
		super.height = HEIGHT;
		super.width = WIDTH;
		this.owner = owner;
		
		velocity = new Vector2f(-(speed * (float) Math.sin(Math.toRadians(rotation+180))) + xv,
				-(speed * (float) Math.cos(Math.toRadians(rotation+180))) + yv);
		
	}

	public void update(){
		super.update();
		
		travelTime++;

	}
	
	public int getId(){ return id; }
	
	public boolean hasExpired(){
		return travelTime >= maxTravelTime;
	}
	
	public Entity getOwner(){ return owner; }

	@Override
	public void collide(Entity e) {
		travelTime = maxTravelTime;
	}
}

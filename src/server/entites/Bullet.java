package server.entites;

import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.Log;


public class Bullet extends Entity{
	
	public int test_id = 0; // TESTING ID
	
	private final int HEIGHT = 10,
					WIDTH = 2;
	
	private float maxTravelTime = 250;
	private float travelTime;
	
	private float speed = 0.45f;	/* dont forget to change on client //ya ya duplicate code blah blah// */
	
	public Bullet(float x, float y, float xv, float yv, float r, int id){
		super(new Vector2f(x,y));
		velocity.x = xv;
		velocity.y = yv;
		rotation = r;
		super.id = id;
		super.height = HEIGHT;
		super.width = WIDTH;
		
		velocity = new Vector2f(-(speed * (float) Math.sin(Math.toRadians(rotation+180))) + xv,
				-(speed * (float) Math.cos(Math.toRadians(rotation+180))) + yv);
		
	}

	public void update(){
		super.update();
		
		travelTime++;
		
		Log.info(position.x + " : " + position.y + " -- " + travelTime);

	}
	
	public int getId(){ return id; }
	
	public boolean hasExpired(){
		return travelTime >= maxTravelTime;
	}

	@Override
	public void collide(Entity e) {
		travelTime = maxTravelTime;
	}
}

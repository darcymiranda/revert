package server.entites;

import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.Log;


public class Bullet extends Entity{
	
	public int test_id = 0; // TESTING ID
	
	private final int HEIGHT = 10,
					WIDTH = 2;
	
	private float maxTravelTime = 250;
	private float travelTime;
	
	public Bullet(float x, float y, float xv, float yv, float r, int id){
		super(new Vector2f(x,y));
		velocity.x = xv;
		velocity.y = yv;
		rotation = r;
		super.id = id;
		super.height = HEIGHT;
		super.width = WIDTH;
		
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

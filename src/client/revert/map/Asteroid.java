package client.revert.map;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

import client.revert.Entity;
import client.revert.Revert;

/**
 * Small asteroid sprite 25X25
 * Med asteroid sprite   32X32
 * Large asteroid sprite 50X50
 * 
 * 
 * @author Scott
 *
 */
public class Asteroid extends Entity
{
	
	public static final float SMALL = 20;
	public static final float MEDIUM = 32;
	public static final float LARGE = 50;
	
	private float offset;
	
	public Asteroid(float x, float y, float offset)
	{
		this.clientPosition.x = x;
		this.clientPosition.y = y;
		this.offset = offset;
		this.minimapColor = new Color(128, 128, 128);
		this.setImage(selectAsteroid());
		
		
	}
	
	/**
	 * Chooses a random asteroid sprite based on the offset
	 * @param offset
	 */
	private Image selectAsteroid()
	{
		SpriteSheet spriteSheet = null;
		Image asteroid = null;
		
		if(offset == SMALL)
			spriteSheet = (SpriteSheet) Revert.cache.get("map_asteroid_smss");
		else if(offset == MEDIUM)
			spriteSheet = (SpriteSheet) Revert.cache.get("map_asteroid_mdss");
		else if(offset == LARGE)
			spriteSheet = (SpriteSheet) Revert.cache.get("map_asteroid_lgss");
		
		int temp = (int)(Math.random() * 8);
		
		asteroid = spriteSheet.getSprite(temp, 0);
		
		return asteroid;
	}
	
	@Override
	public void collide(Entity e) 
	{
		
	}
}

package client.revert.map;

import org.newdawn.slick.Image;

import client.revert.Entity;
import client.revert.Revert;

public class Asteroid extends Entity
{
	
	public Asteroid(float x, float y)
	{
		this.clientPosition.x = x;
		this.clientPosition.y = y;
		this.setImage((Image) Revert.cache.get("map_asteroid"));
	}
	
	@Override
	public void collide(Entity e) 
	{
		
	}
}

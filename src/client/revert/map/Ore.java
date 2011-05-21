package client.revert.map;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import client.revert.Entity;
import client.revert.Revert;

public class Ore extends Entity 
{
	
	public Ore(float x, float y)
	{
		
		this.clientPosition.x = x;
		this.clientPosition.y = y;
		
		this.setImage((Image) Revert.cache.get("map_ore"));
		this.minimapColor = new Color(255, 0, 110);
	}
	
	@Override
	public void collide(Entity e) 
	{
	}
}

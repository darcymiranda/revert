package client.revert.map;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import client.revert.Entity;
import client.revert.Revert;

public class Station extends Entity
{
	
	public Station(float x, float y)
	{
		
		this.clientPosition.x = x;
		this.clientPosition.y = y;
		
		this.setImage((Image) Revert.cache.get("map_station"));
		this.minimapColor = new Color(255, 216, 0);
	}
	
	@Override
	public void collide(Entity e) 
	{
		
	}
	
}

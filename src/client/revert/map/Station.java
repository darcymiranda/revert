package client.revert.map;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import client.revert.Entity;

public class Station extends Entity
{

	private float x, y;
	private String path = "img/station_001.png";
	private Image img;
	
	public Station(float x, float y)
	{
		
		this.x = x;
		this.y = y;
		
		try
		{
			this.img = new Image(path);
		}
		catch(SlickException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void collide(Entity e) 
	{
		
	}
	
	@Override
	public void render(Graphics g)
	{
		g.drawImage(img, x, y);
	}
	
}

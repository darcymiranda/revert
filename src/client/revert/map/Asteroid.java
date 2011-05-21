package client.revert.map;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import client.revert.Entity;

public class Asteroid extends Entity
{

	private float x, y;
	private String path = "img/asteroid_001.png";
	private Image img;
	
	public Asteroid(float x, float y)
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

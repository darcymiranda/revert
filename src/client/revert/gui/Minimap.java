package client.revert.gui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import client.revert.EntityController;

public class Minimap extends UIComponent 
{
	
	private MinimapHandler mh;
	private float width, height;
	
	public Minimap(GameContainer gc, EntityController ec)
	{
		
		width = 250;
		height = 250;
		
		xpos = gc.getWidth() - (width + 10);
		ypos = gc.getHeight() - (height + 10);
		
		mh = new MinimapHandler(xpos, ypos, width, height, ec);
		
		try 
		{
			image = new Image("img/ui/minimap.png");
		} 
		catch (SlickException e) 
		{
			e.printStackTrace();
		}
		
	}
	
	/**
	 * renders minimap components
	 */
	@Override
	public void render(Graphics g)
	{
		g.drawImage(image, xpos, ypos);
		
		//renders objects on minimap
		mh.render(g);
	}
	
	/**
	 * updates minimap
	 */
	public void update()
	{
		mh.update();
	}
}

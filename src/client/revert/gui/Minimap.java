package client.revert.gui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import client.revert.EntityController;
import client.revert.Revert;

public class Minimap extends UIComponent 
{
	
	private MinimapHandler mh;
	
	public Minimap(GameContainer gc, Revert revert)
	{
		
		width = 250;
		height = 250;
		
		xpos = gc.getWidth() - (width + 10);
		ypos = gc.getHeight() - (height + 60);
		
		mh = new MinimapHandler(xpos, ypos, width, height, revert);
		
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

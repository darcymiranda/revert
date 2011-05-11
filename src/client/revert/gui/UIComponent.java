package client.revert.gui;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public abstract class UIComponent 
{
	
	protected Image image;
	
	protected int xpos, ypos;
	
	public UIComponent()
	{		
	}
	
	/**
	 * Draws user interface component
	 * @param g
	 */
	public void render(Graphics g)
	{
		g.drawImage(image, xpos, ypos);
	}
}

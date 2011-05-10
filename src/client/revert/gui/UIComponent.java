package client.revert.gui;

import org.newdawn.slick.Image;

public abstract class UIComponent 
{
	
	protected Image image;
	
	private int xpos, ypos;
	
	public UIComponent()
	{
		
	}
	
	public abstract void setImage(Image image);
	
	public abstract Image getImage();
	
	public void setXPos(int xpos) { this.xpos = xpos; }
	
	public int getXPos() { return xpos; }
}

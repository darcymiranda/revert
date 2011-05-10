package client.revert.gui;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Minimap extends UIComponent 
{
	public Minimap()
	{
		
		try 
		{
			image = new Image("img/ui/minimap.png");
		} 
		catch (SlickException e) 
		{
			e.printStackTrace();
		}
		
	}

	@Override
	public void setImage(Image image) { this.image = image; }

	@Override
	public Image getImage() { return image;	}
}

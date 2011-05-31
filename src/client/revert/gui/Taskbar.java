package client.revert.gui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Taskbar extends UIComponent 
{
	
	
	
	public Taskbar(GameContainer gc)
	{
		width = 1280;
		height = 50;
		xpos = 0;
		ypos = gc.getHeight() - height;
		
		try
		{
			image = new Image("img/ui/taskbar.png");
		}
		catch(SlickException sex)
		{
		}
	}
}

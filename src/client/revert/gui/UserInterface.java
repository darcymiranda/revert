package client.revert.gui;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;

public class UserInterface 
{
	
	private ArrayList<UIComponent> components;
	
	private Minimap minimap;
	
	public UserInterface()
	{
		
		components = new ArrayList<UIComponent>();
		minimap = new Minimap();
		
	}
	
	public void render(Graphics g)
	{
		
	}
}

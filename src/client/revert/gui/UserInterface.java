package client.revert.gui;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import client.revert.Revert;

public class UserInterface 
{
	
	private ArrayList<UIComponent> components;
	
	private GameContainer gc;
	private Revert revert;
	
	private Minimap minimap;
	private Taskbar taskbar;
	
	public UserInterface(GameContainer gc, Revert revert)
	{
		
		this.gc = gc;
		this.revert = revert;
		
		components = new ArrayList<UIComponent>();
		
		minimap = new Minimap(gc, revert);
		taskbar = new Taskbar(gc);
		
		components.add(minimap);
		components.add(taskbar);
		
	}
	
	/**
	 * Draws all user interface components
	 * @param g
	 */
	public void render(Graphics g)
	{
		for(int i = 0; i < components.size(); i++)
		{
			components.get(i).render(g);
		}
	}
	
	/**
	 * updates user interface
	 */
	public void update()
	{
		minimap.update();
	}
}

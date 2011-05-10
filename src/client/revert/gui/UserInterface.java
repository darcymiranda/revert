package client.revert.gui;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import client.revert.EntityController;

public class UserInterface 
{
	
	private ArrayList<UIComponent> components;
	
	private GameContainer gc;
	private EntityController ec;
	
	private Minimap minimap;
	
	public UserInterface(GameContainer gc, EntityController ec)
	{
		
		this.gc = gc;
		this.ec = ec;
		
		components = new ArrayList<UIComponent>();
		
		minimap = new Minimap(gc, ec);
		components.add(minimap);
		
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
}

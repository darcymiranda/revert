package client.revert.gui;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import client.revert.Entity;
import client.revert.EntityController;

public class MinimapHandler 
{
	
	private int xpos, ypos, width, height;
	private final int MINIMAP_DISTANCE = 2000;
	
	private EntityController ec;
	
	private float entityXPos, entityYPos, minimapScale;
	
	public MinimapHandler(int xpos, int ypos, int width, int height, EntityController ec)
	{
		
		this.xpos = xpos;
		this.ypos = ypos;
		this.width = width;
		this.height = height;
		this.ec = ec;
		
		minimapScale = (width/2) / MINIMAP_DISTANCE;
		
	}
	
	/**
	 * Draws minimap objects
	 * @param g
	 */
	public void render(Graphics g)
	{
		ArrayList<Entity> pool = ec.getPool();
		Entity player = ec.getControlledShip();
		
		//draw player position (center of circle)
		g.setColor(new Color(0, 255, 0));
		g.fillRect(xpos + (width/2), ypos + (height/2), 3, 3);
		
		//draws others based on player position
		g.setColor(new Color(255, 0, 0));
		for(int i = 0; i < pool.size(); i++)
		{
			Entity temp = pool.get(i);
			float minimapXPos = temp.getMinimapPosition().x;
			float minimapYPos = temp.getMinimapPosition().y;
			
			if(temp != player)
				if(minimapXPos != -1 && minimapYPos != -1)
					g.fillRect(minimapXPos, minimapYPos, 3, 3);
		}
	}
	
	/**
	 * updates object on minimap
	 */
	public void update()
	{
		ArrayList<Entity> pool = ec.getPool();
		Entity player = ec.getControlledShip();
		
		for(int i = 0; i < pool.size(); i++)
		{
			Entity temp = pool.get(i);
			Vector2f entityPosition = temp.getClientPosition();
			
			if(player != null)
			{
				if(entityPosition.distance(player.getClientPosition()) < MINIMAP_DISTANCE)
				{				
					//sets the position on the minimap relative to their normal position
					entityXPos = (xpos + (width/2)) + ((entityPosition.x - player.getClientPosition().x) * minimapScale);
					entityYPos = (ypos + (height/2)) + ((entityPosition.y - player.getClientPosition().y) * minimapScale);
					temp.setMinimapPosition(new Vector2f(entityXPos, entityYPos));
				}
				else
				{
					temp.setMinimapPosition(new Vector2f(-1, -1));
				}
			}
		}
	}
}

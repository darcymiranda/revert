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
	
	private EntityController ec;
	
	public MinimapHandler(int xpos, int ypos, int width, int height, EntityController ec)
	{
		
		this.xpos = xpos;
		this.ypos = ypos;
		this.width = width;
		this.height = height;
		this.ec = ec;
		
	}
	
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
			Vector2f entityPosition = pool.get(i).getClientPosition();
			
			if(entityPosition.distance(player.getClientPosition()) < 2000)
			{
				//sets the position on the minimap relative to their normal position
				float entityXPos = (xpos + (width/2)) + ((entityPosition.x - player.getClientPosition().x) * 0.06f);
				float entityYPos = (ypos + (height/2)) + ((entityPosition.y - player.getClientPosition().y) * 0.06f);
				
				g.fillRect(entityXPos, entityYPos, 3, 3);
			}
		}
	}
}

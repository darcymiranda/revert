package client.revert.gui;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import client.revert.Entity;
import client.revert.Revert;

public class MinimapHandler 
{
	
	private float xpos, ypos, width, height;
	private final float MINIMAP_DISTANCE = 2000;
	private final int ICON_SIZE = 3;
	
	private Revert revert;
	
	private float entityXPos, entityYPos, minimapScale;
	
	public MinimapHandler(float xpos, float ypos, float width, float height, Revert revert)
	{
		
		this.xpos = xpos;
		this.ypos = ypos;
		this.width = width;
		this.height = height;
		this.revert = revert;
		
		minimapScale = ((width))/2 / MINIMAP_DISTANCE;
		
	}
	
	/**
	 * Draws minimap objects
	 * @param g
	 */
	public void render(Graphics g)
	{
		Entity player = revert.getLocalPlayer().getShip();
		
		// no point in rendering if player is null
		if(player == null) return;
		
		ArrayList<Entity> pool = Revert.ec.getEntityPool();
		Vector2f playerPos = player.getClientPosition();
		
		//draw player position (center of circle)
		g.setColor(new Color(0, 255, 0));
		g.fillRect(xpos + (width/2), ypos + (height/2), ICON_SIZE, ICON_SIZE);
		
		//draws others based on player position
		g.setColor(new Color(255, 0, 0));
		for(int i = 0; i < pool.size(); i++)
		{
			Entity e = pool.get(i);
			Vector2f pos = getRelativeMapPosition(e, playerPos);
			g.setColor(e.getMinimapColor());
			g.fillRect(pos.x, pos.y, ICON_SIZE, ICON_SIZE);
		}
		
		//draws map based on player position
		pool = revert.map.getMap();
		for(int i = 0; i < pool.size(); i++)
		{
			Entity e = pool.get(i);
			Vector2f pos = getRelativeMapPosition(e, playerPos);
			g.setColor(e.getMinimapColor());
			g.fillRect(pos.x, pos.y, ICON_SIZE, ICON_SIZE);
		}
		
	}
	
	private Vector2f getRelativeMapPosition(Entity e, Vector2f center){
		
		Vector2f entityPosition = e.getClientPosition();
		
		//sets the position on the minimap relative to their normal position
		entityXPos = xpos + (width/2) + (entityPosition.x - center.x) * minimapScale;
		entityYPos = ypos + (height/2) + (entityPosition.y - center.y) * minimapScale;
		
		// fit in the minimap
		if((entityYPos > ypos && entityYPos + ICON_SIZE < (ypos + height-1)) &&
				(entityXPos > xpos && entityXPos + ICON_SIZE < (xpos + width-1))){
			
			return new Vector2f(entityXPos, entityYPos);
		}
		
		return new Vector2f(-1,-1);
	}
}

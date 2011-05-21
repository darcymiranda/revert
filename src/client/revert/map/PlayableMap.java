package client.revert.map;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import client.revert.Entity;
import client.revert.Revert;

public class PlayableMap 
{
	
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	
	// false if map is missing pieces
	private boolean complete;	
	private int drawDistance = 1500;
	
	private Revert revert;
	
	public PlayableMap(Revert revert)
	{
		this.revert = revert;
	}
	
	public void render(Graphics g)
	{
		//draws map entities
		for(int i = 0; i < entities.size(); i++)
		{
			Entity temp = entities.get(i);
			Vector2f tempPos = temp.getClientPosition();
			Entity player = revert.getLocalPlayer().getShip();
			
			if(player != null)
				//draws objects depending on distance from player
				if(tempPos.distance(player.getClientPosition()) < drawDistance)
					temp.render(g);
						
		}
		
	}
	
	public void addAsteroid(float x, float y) { entities.add(new Asteroid(x, y)); }
	public void addOre(float x, float y) { entities.add(new Ore(x, y)); }
	public void addStation(float x, float y) { entities.add(new Station(x, y)); }
	
	public boolean isComplete(){ return complete; }
	public void setComplete(boolean c){ this.complete = c; }
	
	public ArrayList<Entity> getMap() { return entities; }
}
package client.revert.map;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import client.revert.Entity;

public class PlayableMap 
{
	
	private ArrayList<Vector2f> asteroids = new ArrayList<Vector2f>();
	private ArrayList<Vector2f> ores = new ArrayList<Vector2f>();
	private ArrayList<Vector2f> stations = new ArrayList<Vector2f>();
	
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	
	private Image asteroid, ore, station;
	
	// false if map is missing pieces
	private boolean complete;
	
	public PlayableMap()
	{
	}
	
	public void render(Graphics g)
	{
		//draws map entities
		for(int i = 0; i < entities.size(); i++)
		{
			entities.get(i).render(g);
		}
		
	}
	
	public void addAsteroid(float x, float y) { entities.add(new Asteroid(x, y)); }
	public void addOre(float x, float y) { entities.add(new Ore(x, y)); }
	public void addStation(float x, float y) { entities.add(new Station(x, y)); }
	
	public boolean isComplete(){ return complete; }
	public void setComplete(boolean c){ this.complete = c; }
}
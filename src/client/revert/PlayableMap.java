package client.revert;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class PlayableMap 
{
	
	private ArrayList<Vector2f> asteroids = new ArrayList<Vector2f>();
	private ArrayList<Vector2f> ores = new ArrayList<Vector2f>();
	private ArrayList<Vector2f> stations = new ArrayList<Vector2f>();
	
	private Image asteroid, ore, station;
	
	// false if map is missing pieces
	private boolean complete;
	
	public PlayableMap()
	{
		try
		{
			asteroid = new Image("img/asteroid_001.png");
			ore = new Image("img/ore_001.png");
			station = new Image("img/station_001.png");
		}
		catch(SlickException e)
		{
			e.printStackTrace();
		}
	}
	
	public void render(Graphics g)
	{
		//draws asteroids
		Vector2f temp;
		for(int i = 0; i < asteroids.size(); i++)
		{
			temp = asteroids.get(i);
			g.drawImage(asteroid, temp.x, temp.y);
		}
		//draws ores
		for(int i = 0; i < ores.size(); i++)
		{
			temp = asteroids.get(i);
			g.drawImage(ore, temp.x, temp.y);
		}
		//draws stations
		for(int i = 0; i < stations.size(); i++)
		{
			temp = asteroids.get(i);
			g.drawImage(station, temp.x, temp.y);
		}
	}
	
	public void addAsteroid(float x, float y) { asteroids.add(new Vector2f(x, y)); }
	public void addOre(float x, float y) { ores.add(new Vector2f(x, y)); }
	public void addStation(float x, float y) { stations.add(new Vector2f(x, y)); }
	
	public boolean isComplete(){ return complete; }
	public void setComplete(boolean c){ this.complete = c; }
}

package client.revert;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class Map 
{
	
	private ArrayList<Vector2f> asteroids = new ArrayList<Vector2f>();
	private ArrayList<Vector2f> ores = new ArrayList<Vector2f>();
	private ArrayList<Vector2f> stations = new ArrayList<Vector2f>();
	
	private Image asteroid, ore, station;
	
	public Map()
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
		for(int i = 0; i < asteroids.size(); i++)
		{
			Vector2f temp = asteroids.get(i);
			g.drawImage(asteroid, temp.x, temp.y);
		}
		//draws ores
		for(int i = 0; i < ores.size(); i++)
		{
			Vector2f temp = ores.get(i);
			g.drawImage(ore, temp.x, temp.y);
		}
		//draws stations
		for(int i = 0; i < stations.size(); i++)
		{
			Vector2f temp = stations.get(i);
			g.drawImage(station, temp.x, temp.y);
		}
	}
	
	public void addAsteroid(float x, float y) { asteroids.add(new Vector2f(x, y)); }
	public void addOre(float x, float y) { ores.add(new Vector2f(x, y)); }
	public void addStation(float x, float y) { stations.add(new Vector2f(x, y)); }
}

package server;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Vector2f;

public class Map 
{
	
	private int offset = 25;
	private BufferedImage imageMap = new BufferedImage(20,20,BufferedImage.TYPE_INT_RGB);
	private ArrayList<Vector2f> asteroids = new ArrayList<Vector2f>();
	private ArrayList<Vector2f> ores = new ArrayList<Vector2f>();
	private ArrayList<Vector2f> stations = new ArrayList<Vector2f>();
	
	public Map(String map)
	{
		loadImageMap(map);
	}
	
	private void loadImageMap(String map)
	{
		
		//loads image
		try 
		{
			imageMap = ImageIO.read(new File(map));
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		//reads each pixel of the image checking colors
		for(int i = 0; i < imageMap.getHeight(); i++)
		{
			for(int j = 0; j < imageMap.getWidth(); j++)
			{
				int temp = imageMap.getRGB(j, i);
				//gets the rgb of pixel
				Color pixelRGB = getPixelARGB(temp);
				
				//checks rgb with constants
				if(pixelRGB.equals(Constants.ASTEROID_RGB))
					asteroids.add(new Vector2f(j*offset, i*offset));
				else if(pixelRGB.equals(Constants.ORE_RGB))
					ores.add(new Vector2f(j*offset, i*offset));
				else if(pixelRGB.equals(Constants.STATION_RGB))
					stations.add(new Vector2f(j*offset, i*offset));
			}
		}
	}
	
	private Color getPixelARGB(int pixel) 
	{

	    int red = (pixel >> 16) & 0xff;
	    int green = (pixel >> 8) & 0xff;
	    int blue = (pixel) & 0xff;

	    return new Color(red, green, blue);
	}
	
	public ArrayList<Vector2f> getAsteroids() { return asteroids; }
	public ArrayList<Vector2f> getOres() { return ores; }
	public ArrayList<Vector2f> getStations() { return stations; }
}

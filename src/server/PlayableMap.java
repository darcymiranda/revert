package server;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Vector2f;

public class PlayableMap 
{
	
	private int offset = 25;
	private BufferedImage imageMap = new BufferedImage(20,20,BufferedImage.TYPE_INT_RGB);
	private ArrayList<Vector2f> asteroidsSM = new ArrayList<Vector2f>();
	private ArrayList<Vector2f> asteroidsMD = new ArrayList<Vector2f>();
	private ArrayList<Vector2f> asteroidsLG = new ArrayList<Vector2f>();
	private ArrayList<Vector2f> ores = new ArrayList<Vector2f>();
	private ArrayList<Vector2f> stations = new ArrayList<Vector2f>();
	
	public PlayableMap(String map)
	{
		loadImageMap(map);
	}
	
	private void loadImageMap(String map)
	{
		
		int sx = -1;
		int fx = -1;
		int sy = -1;
		int fy = -1;
		
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
				
				//area to skip for bigger sprites
				if(j >= sx && j < fx && i >= sy && i < fy)
				{
					//skip
					System.out.println("SKIP**********************");
				}
				else
				{
					
					sx = -1;
					fx = -1;
					sy = -1;
					fy = -1;
					
					//checks rgb with constants
					if(pixelRGB.equals(Constants.ASTEROID_SM_RGB))
					{
						asteroidsSM.add(new Vector2f(j*offset, i*offset));
						System.out.println("SMALL:  (" + j*offset + "," + i*offset + ")");
					}
					else if(pixelRGB.equals(Constants.ASTEROID_MD_RGB))
					{
						asteroidsMD.add(new Vector2f(j*offset, i*offset));
						System.out.println("MEDIUM: (" + j*offset + "," + i*offset + ")");
						sx = j;
						fx = j+2;
						sy = i;
						fy = i+2;
					}
					else if(pixelRGB.equals(Constants.ASTEROID_LG_RGB))
					{
						asteroidsLG.add(new Vector2f(j*offset, i*offset));
						System.out.println("LARGE:  (" + j*offset + "," + i*offset + ")");
						sx = j;
						fx = j+2;
						sy = i;
						fy = i+2;
					}
					else if(pixelRGB.equals(Constants.ORE_RGB))
						ores.add(new Vector2f(j*offset, i*offset));
					else if(pixelRGB.equals(Constants.STATION_RGB))
						stations.add(new Vector2f(j*offset, i*offset));				
					
				}
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
	
	public ArrayList<Vector2f> getAsteroidsSM() { return asteroidsSM; }
	public ArrayList<Vector2f> getAsteroidsMD() { return asteroidsMD; }
	public ArrayList<Vector2f> getAsteroidsLG() { return asteroidsLG; }
	public ArrayList<Vector2f> getOres() { return ores; }
	public ArrayList<Vector2f> getStations() { return stations; }
}

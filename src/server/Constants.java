package server;

import org.newdawn.slick.Color;

public class Constants {
	public static final int TICKRATE = 1;
	public static final int SERVER_SLOTS = 0;	// allowed clients connected to server - not working
	public static final int WORLD_PLAYER_SIZE = 32;	// allowed clients in game world
	public static final int WORLD_ENTITY_SIZE = 1024;
	public static final int NET_TIMEOUT = 10;
	public static final float SPAWN_POSITION_X = 200;
	public static final float SPAWN_POSITION_Y = 200;
	
	public static final String MAP_001 = "img/map_001.png";
	
	public static final Color ASTEROID_SM_RGB = new Color(185,122,87);
	public static final Color ASTEROID_MD_RGB = new Color(137,85,56);
	public static final Color ASTEROID_LG_RGB = new Color(77,48,32);
	public static final Color STATION_RGB = new Color(255,242,0);
	public static final Color ORE_RGB = new Color(237,28,36);
}

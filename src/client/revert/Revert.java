package client.revert;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.Input;
import org.newdawn.slick.tiled.TiledMap;

import packet.Packet;

import server.Constants;

import client.NetUser;
import client.Player;
import client.revert.gui.UserInterface;

public class Revert extends BasicGame {
	
	public static NetUser net;
	
	public Player[] players = new Player[Constants.WORLD_PLAYER_SIZE];
	public EntityController ec;
	
	private TiledMap map;
	private Camera cam;
	
	private Image imgShip;
	private UnicodeFont font;
	
	private UserInterface ui;
	
	private short ticks = 0;

	public Revert() {
		super("Revert");
	}
	
	/**
	 * First method that is executed, upon start up.
	 * @param gc
	 * @param g
	 */
	@SuppressWarnings("unchecked")
	public void init(GameContainer gc){
		
		gc.setVSync(true);
		
		/** Init Font **/
	    Font awtFont = new Font("Verdana", Font.PLAIN, 12); 
	    font = new UnicodeFont(awtFont, 12, false, false); 
	    font.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
	    font.addAsciiGlyphs();
	    try {
			font.loadGlyphs();
		} catch (SlickException e1) {
			e1.printStackTrace();
		}
	   
		/** Init Configuration File **/
		String host = null;
		int port = 0;
		String username = null;
		File conf = new File("revert.conf");
		
		try{
			BufferedReader br = new BufferedReader(new FileReader(conf));
			host = br.readLine();
			port = Integer.parseInt(br.readLine());
			username = br.readLine();
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		/** Init Images **/
		try {
			imgShip = new Image("img/ship.png");
			map = new TiledMap("maps/map01.tmx");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
		/** Init Networking **/
		net = new NetUser(this, host, port);
		new Thread(net).start();
		net.username = username;
		
		/** Init Game**/
		ec = new EntityController();
		cam = new Camera(gc, map);
		
		/** Init User Interface **/
		ui = new UserInterface(gc, ec);
		
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		
		cam.drawMap();
		
		//draw user interface

		
		cam.translateGraphics();
		

		ec.render(g);
		
		cam.untranslateGraphics();
		ui.render(g);
		
	}
	
	short counter = 0;

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {

		Player localPlayer = getLocalPlayer();
		
		// Send position update packet to server
		if(ticks > 6){
			if(localPlayer != null && localPlayer.getShip() != null){
				if(localPlayer.getShip().isAlive()){
					
					// send ship
					net.send(localPlayer.getShip().getPacket());
					
				}
			}
			ticks = 0;
		}
		ticks++;
		
		ec.checkCollisions();
		ec.update(gc, delta);
		
		if(localPlayer != null)
		{
			if(localPlayer.getShip() != null)
				cam.centerOn(localPlayer.getShip());
		}
		
		ui.update();
		
		
	}
	
	public void createShip(int id, boolean c){

		Ship ship = new Ship(new Vector2f(Constants.SPAWN_POSITION_X,Constants.SPAWN_POSITION_Y), id, c);
		ship.setImage(imgShip);
		ship.displayText = players[id].getUsername();
		ship.font = font;
		
		System.out.println("ship created for " + id + " " + net.username);
		
		players[id].setShip(ship);
		ec.add(ship);
	}
	
	public Player getLocalPlayer(){
		for(Player player : players){
			if(player == null) continue;
			
			// get local player
			if(player.id == net.id){
				return player;
			}
		}
		return null;
	}

	/**
	 * Keyboard input
	 */
	public void keyPressed(int key, char c){
		
		if(key == Input.KEY_ENTER && !players[net.id].readyStatus){
			net.send(new Packet(Packet.READY_MARKER, net.id, !players[net.id].readyStatus));
			System.out.println(!players[net.id].readyStatus + " ready status sent to server.");
		}
		
		Packet packet = new Packet(Packet.UPDATE_SELF_INPUT);
		boolean hasChanged = false;
		
		if(key == Input.KEY_SPACE){
			packet.setKeySpace(true);
			hasChanged = true;
		}
		
		if(hasChanged)
			net.send(packet);
	}
	
	public void keyReleased(int key, char c){
		
		Packet packet = new Packet(Packet.UPDATE_SELF_INPUT);
		boolean hasChanged = false;
		
		if(key == Input.KEY_SPACE){
			packet.setKeySpace(false);
			hasChanged = true;
		}
		
		if(hasChanged)
			net.send(packet);
	}
	
	public static void main(String args[]){
		
        try {

            AppGameContainer agc = new AppGameContainer(new Revert(), 1280, 800, false);
            agc.start();

        } catch (SlickException e) {
            e.printStackTrace();
        }
	}

}

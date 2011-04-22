package client.revert;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.Input;
import org.newdawn.slick.tiled.TiledMap;

import server.Constants;

import client.ActionSender;
import client.NetUser;
import client.Player;
import client.ex.NetException;

public class Revert extends BasicGame {
	
	private TiledMap map;
	private Camera cam;
	private NetUser net;
	public ActionSender actionSender;
	
	private Image imgShip;
	
	public Player[] players = new Player[Constants.WORLD_PLAYER_SIZE];
	public EntityController ec;
	
	public UnicodeFont font;
	public String username;

	public Revert() {
		super("Revert");
	}
	
	/**
	 * First method that is executed, upon start up.
	 * @param gc
	 * @param g
	 */
	public void init(GameContainer gc){
		
		gc.setVSync(true);
		
	    Font awtFont = new Font("Verdana", Font.PLAIN, 12); 
	    font = new UnicodeFont(awtFont, 12, false, false); 
	    font.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
	    font.addAsciiGlyphs();
	    try {
			font.loadGlyphs();
		} catch (SlickException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	   
		String host = null;
		int port = 0;
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
		
		try {
			imgShip = new Image("img/ship.png");
			map = new TiledMap("maps/map01.tmx");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		

		
		// Network
		net = new NetUser(this, host, port);
		new Thread(net).start();
		actionSender = new ActionSender(net);
		
		net.username = username;
		
		ec = new EntityController();
		cam = new Camera(gc, map);
		
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		
		cam.drawMap();
		cam.translateGraphics();
		ec.render(g);
		
	}
	
	short counter = 0;
	@Override
	public void update(GameContainer gc, int delta) throws SlickException {

		Entity ship = ec.getControlledShip();
		Player thePlayer = null;
		
		// Get the player controlling this game
		for(Player player : players){
			if(player == null) continue;
			if(player.id == net.id){
				thePlayer = player;
				break;
			}
		}
		
		// Send position update packet to server
		if(counter > 10){
			if(thePlayer != null && thePlayer.ship != null){
				if(ship.isAlive()){
					net.send(thePlayer.ship.getPacket());
				}
			}
			counter = 0;
		}
		counter++;
		
		ec.update(gc, delta);
		cam.centerOn(ship);
		
	}
	
	public void createShip(int id, boolean c){

		Ship ship = new Ship().createInstance(new Vector2f(200,200), id, c);
		ship.setImage(imgShip);
		
		/** Temp **/
		ship.username = net.username;
		ship.font = font;
		
		System.out.println("ship created for " + id + " " + net.username);
		
		players[id].ship = ship;
		ec.add(ship);
	}

	/**
	 * Keyboard input
	 */
	public void keyPressed(int key, char c){
		
		if(key == Input.KEY_ENTER){
			try {
				actionSender.sendReady(!net.readyStatus);
				System.out.println(!net.readyStatus + " ready status sent to server.");
			} catch (NetException e) {
				e.printStackTrace();
			}
		}
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

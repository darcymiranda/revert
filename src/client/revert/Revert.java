package client.revert;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
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

		try {
			imgShip = new Image("img/ship.png");
			map = new TiledMap("maps/map01.tmx");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
		// Network
		net = new NetUser(this);
		new Thread(net).start();
		actionSender = new ActionSender(net);
		
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
		
		System.out.println("ship created for " + id);
		
		players[id].ship = ship;
		ec.add(ship);
	}
	
	// Keys
	private boolean w, a, s, d, q, e;
	
	/**
	 * Sends a packet to the server contained which current keys are being pressed when a key is released.
	 */
	public void keyReleased(int key, char c){
		if(key == Input.KEY_W || key == Input.KEY_A  || key == Input.KEY_S  || key == Input.KEY_D || key == Input.KEY_Q || key == Input.KEY_E ){
			if(key == Input.KEY_W){
				w = false;
			}
			if(key == Input.KEY_A){
				a = false;
			}
			if(key == Input.KEY_S){
				s = false;
			}
			if(key == Input.KEY_D){
				d = false;
			}
			if(key == Input.KEY_Q){
				q = false;
			}
			if(key == Input.KEY_E){
				e = false;
			}
		}
		
		//pc.send(new Packet(clientId, w, a, s, d));
		//System.out.println(clientId + " " + w + " " + a + " " + s + " " + d);
		/*
		try {
			if(isSpawned)	// bandage so they cant send update packets when ship doesnt even exist yet
				actionSender.sendMoveUpdate(w, a, s, d, q, e);
		} catch (NetException e) {
			e.printStackTrace();
		}
		*/
		
	}

	/**
	 * Sends a packet to the server containing which current keys are being pressed when a key gets pressed.
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
		
		if(key == Input.KEY_W || key == Input.KEY_A  || key == Input.KEY_S  || key == Input.KEY_D || key == Input.KEY_Q || key == Input.KEY_E ){
			if(key == Input.KEY_W){
				w = true;
			}
			if(key == Input.KEY_A){
				a = true;
			}
			if(key == Input.KEY_S){
				s = true;
			}
			if(key == Input.KEY_D){
				d = true;
			}
			if(key == Input.KEY_Q){
				q = true;
			}
			if(key == Input.KEY_E){
				e = true;
			}			
			//pc.send(new Packet(clientId, w, a, s, d));
			//System.out.println(clientId + " " + w + " " + a + " " + s + " " + d);
			/*
			try {
				if(isSpawned)	// bandage so they cant send update packets when ship doesnt even exist yet
					//actionSender.sendMoveUpdate(w, a, s, d, q, e);
			} catch (NetException e) {
				e.printStackTrace();
			}
			*/
			
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

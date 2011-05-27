package server;

import java.util.ArrayList;

import org.newdawn.slick.util.Log;

import packet.Packet;
import server.entities.Bullet;
import server.entities.Entity;
import server.entities.Missile;
import server.entities.Ship;

/**
 * The World handles all the processing requests by the server and keeps track of 
 * all the clients ship locations, projectiles, health, etc.
 * 
 * Important Note: This class is not the source of the clients, it just holds a reference
 * to clients.
 * @author dmiranda
 *
 */
public class World {
	
	public static World instance = null;
	private Server server;
	private PlayableMap map;
	
	private Client[] clients = new Client[Constants.WORLD_PLAYER_SIZE];
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	
	private int unique_id = 0; // TESTING
	
	public void init(){
		// Load the map.
		Log.info(" Loading map " + Constants.MAP_001);
		map = new PlayableMap(Constants.MAP_001);
		Log.info(" Complete.");
	}
	
	/**
	 * Process everything within the game world. This includes velocity calculations, health,
	 * locations, etc.
	 */
	public void process(){
		
		// send self updates
		for(Client client : clients){
			if(client == null) continue;
			Ship ship = client.getShip();
			if( ship !=  null && client.getSpawnStatus() && client.isAlive()){
				if( ship.hasPositionChanged() )
					client.send(new Packet(Packet.UPDATE_SELF, ship.position.x, ship.position.y));
					
					/* test to see server side position of bullets */
					for(int i = 0; i < bullets.size(); i++){
						Bullet b = bullets.get(i);
						Packet packet = new Packet((byte) 105, b.test_id, b.position.x, b.position.y,
								b.velocity.x, b.velocity.y, b.rotation);
						server.sendToAll(packet, true);
					}
				if( !ship.isAlive() && client.getSpawnStatus()){
					server.sendToAll(new Packet(Packet.UPDATE_DEATH, client.id), true);
					client.setSpawnStatus(false);
					server.sendToAll(new Packet(Packet.READY_MARKER, client.id, false), true);
					System.out.println("Sent death packet to " + client.id);
				}
			}
			client.process();
			
		}
		
		// send updates to all other clients
		for(Client client : clients){
			if(client == null) continue;
			
			// TODO: Do a distance check to prevent unneeded update packets
			// actions
			for(int i = 0; i < clients.length; i++){
				if(clients[i] == null ) continue;
				if(clients[i].id == client.id) continue;	// dont send to self
				
				Ship ship = (Ship) clients[i].getShip();
				if(ship == null || !ship.isAlive()) continue;
				
				if(ship.isAlive()){
					if(ship.hasPositionChanged())
						client.send(new Packet(Packet.UPDATE_OTHER, clients[i].id, ship.position.x,
								ship.position.y, ship.velocity.x, ship.velocity.y, ship.rotation));
					
					for(int b = 0; b < bullets.size(); b++){
						Bullet bullet = bullets.get(b);
						if(bullet instanceof Missile){
							if(bullet.id != client.id) continue;	// get the owner's bullets only
							if(bullet.hasPositionChanged())
								client.send(new Packet(Packet.UPDATE_MISSILE, clients[i].id, bullet.position.x,
										bullet.position.y, bullet.velocity.x, bullet.velocity.y));
						}
					}
					
					if(ship.hasShootingChanged()){
						Packet packet = new Packet(Packet.UPDATE_OTHER_BULLET, clients[i].id);
						packet.setKeySpace(ship.isShooting());
						client.send(packet);
					}
				}
			}
		}
		
		// Update and remove expired bullets
		for(int i = 0; i < bullets.size(); i++){
			bullets.get(i).update();
			if(bullets.get(i).hasExpired()) bullets.remove(i);
		}
		
		// Check for collisions
		for(int i = 0; i < bullets.size(); i++){
			
			Bullet b = bullets.get(i);
			
			for(int j = 0; j < clients.length; j++){
				
				if(clients[j] == null) continue;
				if(clients[j].id == b.getId()) continue;	// ignore own bullets
				
				Entity s = clients[j].getShip();
				if(s != null && s.isAlive()){
					
					if(b.getHitBox().intersects(s.getHitBox())){
						
						b.collide(s);
						s.collide(b);
						server.sendToAll(new Packet(Packet.UPDATE_DAMAGE, clients[j].id), true);
						System.out.println("HIT");
						
						// clean up bullet if need be
						if(b.hasExpired())
							bullets.remove(b);
						
					}
				}
			}
		}
		
	}
	
	/**
	 * Add a bullet to the game world.
	 * @param packet
	 * @param client
	 */
	public void addBullet(Packet packet, Client client){
		
		Ship ship = client.getShip();
		if(ship == null){
			Log.warn(client + " tried to shoot with a null ship.");
			return;
		}
		
		switch(packet.getBulletType()){
		
			case 1:
				Bullet bullet = ship.shoot(packet);
		
				/* testing client side view of bullets */
				bullet.test_id = unique_id;
				unique_id++;
				/* */
				
				bullets.add(bullet);
				break;
			case 2:
				Client trackClient = getPlayerById(packet.getId());
				Ship trackShip = null;
				Missile missile = null;
				
				if(trackClient != null) 
					trackShip = client.getShip();
				
				if(ship != null)
					missile = ship.shootTrackable(packet, trackShip);
				
				bullets.add(missile);
				break;
			
		}
		
	}
	
	/**
	 * Add a player to the game world. Also sets the player's client id to its index in
	 * the players array.
	 * @param client
	 */
	public void addPlayer(Client client){
		for(int i = 0; i < clients.length; i++){
			if(clients[i] == null){
				clients[i] = client;
				clients[i].id = i;
				return;
			}
		}
	}
	
	/**
	 * Removes a player from the game world. This method does not disconnect the player's client.
	 * @param client
	 */
	public void removePlayer(Client client){
		for(int i = 0; i < clients.length; i++){
			if(i == client.id){
				clients[i] = null;
			}
		}
	}
	
	public Client getPlayerById(int id){
		
		Client client = null;
		for(int i = 0; i < clients.length; i++){
			if(clients[i] == null) continue;
			if(clients[i].id == id)
				client = clients[i];
		}
		
		return client;
	}
	
	public static World getInstance(){
		if(instance == null)
			instance = new World();
		return instance;
	}
	
	public void setServer(Server server){
		this.server = server;
	}
	
	public PlayableMap getCurrentMap(){ return map; }

}

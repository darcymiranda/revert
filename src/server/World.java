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
	private Bullet[] bullets = new Bullet[Constants.WORLD_ENTITY_SIZE];
	//private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	
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
					for(int i = 0; i < bullets.length; i++){
						if(bullets[i] == null) continue;
						Bullet b = bullets[i];
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
					
					if(ship.hasShootingChanged()){
						Packet packet = new Packet(Packet.UPDATE_OTHER_BULLET);
						packet.id = clients[i].id;
						packet.bulletType = 1;
						packet.setKeySpace(ship.isShooting());
						client.send(packet);
					}
				}
			}
		}
		
		// check if a missile lock has changed
		for(int b = 0; b < bullets.length; b++){
			if(bullets[b] == null) continue;
			Bullet bullet = bullets[b];
			if(bullet instanceof Missile){
				Missile missile = (Missile) bullet;
				if(missile.id != missile.id) continue;	// get the owner's bullets only
				if(missile.hasTargetChanged()){
					System.out.println("TARGET CHANGED");
					Packet packet = new Packet(Packet.UPDATE_OTHER_BULLET);
					packet.id = missile.getOwner().id;
					packet.bulletType = 2;
					packet.bulletId = missile.id;
					packet.targetId = missile.getTargetedEntity().id;
					System.out.println(packet.id + "'s missile is tracking " + packet.targetId);
					server.sendToAll(packet, true);
				}
			}
		}
		
		// Update and remove expired bullets
		for(int i = 0; i < bullets.length; i++){
			if(bullets[i] == null) continue;
			bullets[i].update();
			if(bullets[i].hasExpired()) bullets[i] = null;
		}
		
		// Check for collisions
		for(int i = 0; i < bullets.length; i++){
			if(bullets[i] == null) continue;
			Bullet b = bullets[i];
			
			for(int j = 0; j < clients.length; j++){
				
				if(clients[j] == null) continue;
				if(clients[j].id == b.getOwner().id) continue;	// ignore own bullets
				
				
				Entity s = clients[j].getShip();
				if(s != null && s.isAlive()){
					
					
					if(b.getHitBox().intersects(s.getHitBox())){
						
						b.collide(s);
						s.collide(b);
						server.sendToAll(new Packet(Packet.UPDATE_DAMAGE, clients[j].id), true);
						System.out.println("HIT");
						
						// clean up bullet if need be
						if(b.hasExpired())
							bullets[i] = null;
						
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
		System.out.println(ship.getId() + " is requesting to add a bullet");
		
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
				
				// add bullet to list
				for(int i = 0; i < bullets.length; i++){
					if(bullets[i] == null){
						bullets[i] = bullet;
						bullets[i].id = i;
						break;
					}
				}
				break;
			case 2:
				Ship trackShip = null;
				Missile missile = null;
				
				// Should check server side if ship is in range to prevent cheating.
				if(ship != null)
					missile = ship.shootTrackable(trackShip);
				
				// add missile to list
				for(int i = 0; i < bullets.length; i++){
					if(bullets[i] == null){
						bullets[i] = missile;
						bullets[i].id = i;
						
						// send the server's bullet id to the client
						Packet p = new Packet(Packet.UPDATE_ID);
						p.id = client.id;
						p.bulletId = i;
						client.send(p);
						
						// tell other clients
						p = new Packet(Packet.SPAWN_BULLET);
						p.id = client.id;
						p.bulletId = i;
						p.bulletType = 2;
						server.sendToAll(p, false);

						break;
						
					}
				}
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
	
	public Ship getNearestShip(Entity owner, float range){
		
		float distance = range;
		Ship target = null;
		for(int i = 0; i < clients.length; i++){
			
			Ship other;
			
			if(clients[i] == null) continue;
			other = clients[i].getShip();
			if(other == null || !other.isAlive()
					|| owner.id == other.id) continue;	// don't get self
			
			float dx = (owner.position.x + owner.getWidth() /2) - (other.position.x + other.getWidth() /2);
			float dy = (owner.position.y + owner.getHeight() /2) - (other.position.y + other.getHeight() /2);
			
			float tempDistance = (float) Math.sqrt((dx * dx) + (dy * dy));
			if(tempDistance < distance)
				target = other;
			
		}
		
		return target;
		
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

package server;

import java.util.ArrayList;

import packet.Packet;

/**
 * The World is a controller class that handles all the processing requests by the server
 * and keeps track of all the clients ship locations, projectiles, health, etc.
 * 
 * Important Note: This class is not the source of the clients, it just holds a reference
 * to clients.
 * @author dmiranda
 *
 */
public class World {
	
	public static World instance = null;
	private Server server;
	
	private Client[] clients = new Client[Constants.WORLD_PLAYER_SIZE];
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	
	private int unique_id = 0; // TESTING
	
	/**
	 * Process everything within the game world. This includes velocity calculations, health,
	 * locations, etc.
	 */
	public void process(){
		
		// send self updates
		for(Client client : clients){
			if(client == null) continue;
			Ship ship = client.getShip();
			if( ship !=  null && client.getReadyStatus() && client.isAlive()){
				if( ship.hasPositionChanged() )
					client.send(new Packet(Packet.UPDATE_SELF, ship.x, ship.y));
					
					/* test to see server side position of bullets */
					for(int i = 0; i < bullets.size(); i++){
						Bullet b = bullets.get(i);
						Packet packet = new Packet((byte) 105, b.test_id, b.x, b.y, b.xv, b.yv, b.r);
						server.sendToAll(packet, true);
					}
				if( !ship.isAlive() && client.getReadyStatus()){
					server.sendToAll(new Packet(Packet.UPDATE_DEATH, client.id), true);
					client.setReadyStatus(false);
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
				
				Ship ship = clients[i].getShip();
				if(ship == null || !ship.isAlive()) continue;
				
				if(ship.isAlive()){
					if(ship.hasPositionChanged())
						client.send(new Packet(Packet.UPDATE_OTHER, clients[i].id, ship.x, ship.y, ship.xv, ship.yv, ship.r));
					
					//for(int b = 0; b < bullets.size(); b++){
					//	if(bullets.get(b) instanceof Missile){
					//		System.out.println("test");
					//	}
					//}
					
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
			bullets.get(i).tick();
			if(bullets.get(i).hasExpired()) bullets.remove(i);
		}
		
		// Check for collisions
		for(int i = 0; i < bullets.size(); i++){
			
			Bullet b = bullets.get(i);
			
			for(int j = 0; j < clients.length; j++){
				
				if(clients[j] == null) continue;
				if(clients[j].id == b.getId()) continue;	// ignore own bullets
				
				Ship s = clients[j].getShip();
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
	
	public void addBullet(Packet packet, Client client){
		Bullet bullet = new Bullet(packet.getPositionX(), packet.getPositionY(),
				packet.getVelocityX(), packet.getVelocityY(), packet.getRotationR(),
				client.id);
		
		/* testing client side view of bullets */
		bullet.test_id = unique_id;
		unique_id++;
		/* */
		
		bullets.add(bullet);
		
		
		
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
	
	public static World getInstance(){
		if(instance == null)
			instance = new World();
		return instance;
	}
	
	public void setServer(Server server){
		this.server = server;
	}

}

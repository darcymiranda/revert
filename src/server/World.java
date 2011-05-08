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
				if(ship == null) continue;
				
				if(clients[i].getReadyStatus()){
					if(ship.hasPositionChanged())
						client.send(new Packet(Packet.UPDATE_OTHER, clients[i].id, ship.x, ship.y, ship.xv, ship.yv, ship.r));
					
					if(ship.hasShootingChanged()){
						Packet packet = new Packet(Packet.UPDATE_OTHER_BULLET, clients[i].id);
						packet.setKeySpace(ship.isShooting());
						client.send(packet);
					}
				}
			}
			
			// TODO: Do a distance check to prevent unneeded update packets
			// collisons
			ArrayList<Bullet> bullets = client.getShip().getBullets();
			Bullet bullet;
			for(int b = 0; b < bullets.size(); b++){
				
				bullet = bullets.get(b);
				Client other;
				for(int j = 0; j < clients.length; j++){
					
					other = clients[j];
					if(other == null) continue;
					
					Ship otherShip = other.getShip();
					if(client != other && other.isAlive()){
						
						//System.out.println(bullet.x + " " + bullet.y + "  /  " + otherShip.x + " " + otherShip.y);
						if(bullet.getHitBox().intersects(otherShip.getHitBox())){
							
							otherShip.collide(bullet);
							bullet.collide(otherShip);
							server.sendToAll(new Packet(Packet.UPDATE_DAMAGE, other.id), true);
							System.out.println(client + " hit " + other);
							
						}
					}
				}
			}
			
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
	
	public static World getInstance(){
		if(instance == null)
			instance = new World();
		return instance;
	}
	
	public void setServer(Server server){
		this.server = server;
	}

}

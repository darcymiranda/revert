package client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

import client.revert.Revert;

import packet.Packet;
import packet.Snapshot;
	/**
	 * This class is the network class that will be attached to the user's
	 * game client. Visibility of objects and methods may need to be modified.
	 *
	 */
	public class NetUser implements Runnable {
		
		public Socket socket;
		private NetUserRecieve in;
		private ObjectOutputStream out;
		private Snapshot outSnapshot;
		
		private String host = "";
		private int port = 0;
		
		// Game related
		private Revert client;
		public int id = -1;
		public String username;
		
		public NetUser(Revert client, String host, int port){
			this.client = client;
			this.host = host;
			this.port = port;
		}
		
		/**
		 * Initialize the networking classes.
		 * @throws IOException
		 */
		public void init() throws IOException{
			this.socket = new Socket(host, port);
			in = new NetUserRecieve(this);
			out = new ObjectOutputStream(socket.getOutputStream());
			in.start();
		}
		
		@Override
		public void run(){
			
			try{
				
				// Start the connection to the server and initialize the input
				// and output streams.
				init();
				
			}catch(ConnectException ce){
				ce.printStackTrace();
				System.err.println("Failed to connect to server.");
				return;
			}catch(Exception ex){
				ex.printStackTrace();
			}
				
			try{	
				
				Snapshot snapshot = null;
				for(;;){
					
					// Read packets from the server and process them.
					if(in.hasNextSnapshot()){
						snapshot = in.getNextSnapshot();
						for(Packet packet : snapshot.getPackets()){
							process(packet);
						}
					}
					
					// Send out packets to the server.
					if(outSnapshot != null){
						out.writeObject(outSnapshot);
						out.flush();
						outSnapshot = null;
					}
					
					Thread.sleep(1);
					
				}
			
			}catch(Exception e){
				System.err.println("Connection to server was lost.");
				e.printStackTrace();
				disconnect();
			}
		}
		
		/**
		 * Determines what to do with incoming packets.
		 */
		public void process(Packet packet){
			
			if(packet.type == Packet.UPDATE_OTHER){
				
				int pId = packet.getId();
				if(client.players[pId] != null){
					if(client.players[pId].getShip() != null){
						client.players[pId].getShip().updatePacket(packet);
					}
				}
				
			}
			/** Test to see server side bullets **/
			else if(packet.type == (byte) 105){
				
				boolean created = false;
				
				if(client.serverBullets.size() < 1){
					client.revert.Bullet b = new client.revert.Bullet( packet.getPositionX(), packet.getPositionY(), packet.getRotationR(),
							new Vector2f(0,0));
					b.velocity.x = packet.getVelocityX();
					b.velocity.y = packet.getVelocityY();
					b.test_id = packet.getId();
					b.setImage((Image) Revert.cache.get("test_bullet"));
					client.serverBullets.add(b);
				}
				
				for(int i = 0; i < client.serverBullets.size(); i++){
					client.revert.Bullet b = client.serverBullets.get(i);
					if(b.test_id == packet.getId()){
						b.clientPosition.x = packet.getPositionX();
						b.clientPosition.y = packet.getPositionY();
						b.velocity.x = packet.getVelocityX();
						b.velocity.y = packet.getVelocityY();
						b.setRotation(packet.getRotationR());
						created = true;
					}

				}
				if(!created){
					client.revert.Bullet b = new client.revert.Bullet( packet.getPositionX(), packet.getPositionY(), packet.getRotationR(),
							new Vector2f(0,0));
					b.velocity.x = packet.getVelocityX();
					b.velocity.y = packet.getVelocityY();
					b.test_id = packet.getId();
					b.setImage((Image) Revert.cache.get("test_bullet"));
					client.serverBullets.add(b);
				}
			}
			else if(packet.type == Packet.UPDATE_OTHER_BULLET){
				
				int pId = packet.getId();
				if(client.players[pId] != null){
					if(client.players[pId].getShip() != null){
						client.players[pId].getShip().setShooting(packet.getPressedSpace());
					}
				}
			}
			else if(packet.type == Packet.UPDATE_SELF){
				
				client.players[id].getShip().updatePacket(packet);
					
			}
			else if(packet.type == Packet.UPDATE_DAMAGE){
				
				int pId = packet.getId();
				if(client.players[pId] != null){
					if(client.players[pId].getShip() != null){
						client.players[pId].getShip().takeDamage();
					}
				}
			}
			else if(packet.type == Packet.UPDATE_DEATH){
				
				int pId = packet.getId();
				if(client.players[pId] != null){
					if(client.players[pId].getShip() != null){
						client.players[pId].getShip().setAlive(false);
						client.ec.removeEntity(client.players[pId].getShip());
						client.bc.addMessage(client.players[pId].getUsername() + " has died!.");
					}
				}		
				
			}
			else if(packet.type == Packet.CHAT){
				
				//test chat in prompt output
				System.out.println( "[" + packet.getUsername() + "]: " + packet.getMessage() );
				
			}
			else if(packet.type == Packet.READY_MARKER){
				
				String names = "You are"; // debug
				client.players[packet.getId()].readyStatus = packet.getStatus();
				
				if(packet.getId() == id){
					client.players[id].readyStatus = packet.getStatus();
					if(client.players[id].readyStatus){
						if(client.players[packet.getId()].getShip() == null){
							client.createShip(id, true);
						}
						else if(!client.players[id].getShip().isAlive()){
							client.createShip(id, true);
						}
					}
				}
				else{
					names = client.players[packet.getId()].getUsername() + " is"; // debug
					
					// create a ship if they have true ready status and dont already have one
					if(packet.getStatus()){
						if(client.players[packet.getId()].getShip() == null){
							client.createShip(packet.getId(), false);
						}
						else if(!client.players[packet.getId()].getShip().isAlive()){
							client.createShip(packet.getId(), false);
						}
					}
					
				}
				
				// debugging
				String rstatus = packet.getStatus() ? "ready" : "not ready";
				System.out.println(names + " " + rstatus + ".");
				
			}
			else if(packet.type == Packet.CLIENT_INFO){
				
				if(packet.getId() != id){
					for(Player player : client.players){
						if(player == null) continue;
						if(player.id == id) continue; // ignore yourself
							
						// Create a ship for the player if he connected ready
						if(packet.getStatus() && player.getShip() == null)
							client.createShip(packet.getId(), false);
							
					}
				}
			}
			else if(packet.type == Packet.CONNECT){
				
				if(id == -1){

					id = packet.getId();
					
					client.players[id] = new Player(id, username);
					System.out.println("Connection established with server.");
					
					send(new Packet(Packet.CONNECT, id, username));
				
				}
				else{
					// TODO: Should probably check if that player already exists????
					Player tempPlayer = new Player(packet.getId(), packet.getUsername(), packet.getStatus());
					client.players[packet.getId()] = tempPlayer;
					
					System.out.println(tempPlayer + " connected.");
					client.bc.addMessage(tempPlayer.getUsername() + " has connected.");
				}
			}
			else if(packet.type == Packet.DISCONNECT){
				
				// find client that matchs id from packet and remove them from local list
				for(int i = 0; i < client.players.length; i++){
					if(client.players[i] == null) continue;
					
					// remove the player from the game
					if(client.players[i].id == packet.getId()){
						System.out.println(client.players[i].getUsername() + " has disconnected.");
						client.bc.addMessage(client.players[i].getUsername() + " has disconnected.");
						client.ec.removeEntity(client.players[i].getShip());
						client.players[i] = null;
					}
					break;
				}
				
			}
			else if(packet.type == Packet.SERVER_MESSAGE){
				System.out.println("Server: " + packet.getMessage());
			}
			else if(packet.type == Packet.UPDATE_ASTEROIDS){
				client.map.addAsteroid(packet.getPositionX(), packet.getPositionY());
			}
			else if(packet.type == Packet.UPDATE_ORES){
				client.map.addOre(packet.getPositionX(), packet.getPositionY());
			}
			else if(packet.type == Packet.UPDATE_STATIONS){
				client.map.addStation(packet.getPositionX(), packet.getPositionY());
			}
			else if(packet.type == Packet.LAST_MAP){
				client.map.setComplete(true);
				client.bc.addMessage("map complete");
			}
		}
		
		/**
		 * Send a packet to the packet pool waiting to be sent to the server.
		 * @param packet
		 */
		public void send(Packet packet){
			if(outSnapshot == null)
				outSnapshot = new Snapshot();
			
			outSnapshot.addPacket(packet);
		}
		
		public void disconnect(){
			try{
				in.getInStream().close();
				out.close();
				socket.close();
				id = -1;
			}catch(IOException ioe){
				//ioe.printStackTrace();
			}
		}
	}

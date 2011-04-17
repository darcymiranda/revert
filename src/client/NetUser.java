package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import client.revert.Entity;
import client.revert.Revert;

import packet.Packet;
import server.Constants;

	/**
	 * This class is the network class that will be attached to the user's
	 * game client. Visibility of objects and methods may need to be modified.
	 *
	 */
	public class NetUser implements Runnable {
		
		public Socket socket;
		private NetUserRecieve in;
		private ObjectOutputStream out;
		private List<Packet> packets = new LinkedList<Packet>();	// OUTBOUND
		private long tick = 0;
		
		private final String host = "127.0.0.1";
		private final int port = 55274;
		
		// Game related
		private Revert client;
		public int id = -1;
		public boolean readyStatus;
		public String username;
		
		public NetUser(Revert client){
			this.client = client;
		}
		
		/**
		 * Initialize the networking classes.
		 * @throws IOException
		 */
		private void init() throws IOException{
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
				
				Packet packet = null;
				for(;;){
					
					// Read packets from the server and process them.
					if(in.hasPackets()){
						packet = in.getPacket();
						process(packet);
					}
					
					// Send out packets to the server.
					if(!packets.isEmpty()){
						packet = packets.get(0);
						out.writeObject(packet);
						out.flush();
						packets.remove(packet);
					}
					
					packet = null;
					
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
					client.players[pId].ship.setPacket(packet);
				}
				
			}
			else if(packet.type == Packet.UPDATE_SELF){
				
				client.players[id].ship.setPacket(packet);
					
			}
			else if(packet.type == Packet.CHAT){
				
				//test chat in prompt output
				System.out.println( "[" + packet.getUsername() + "]: " + packet.getMessage() );
				
			}
			else if(packet.type == Packet.READY_MARKER){
				
				String names = "You are"; // debug
				client.players[packet.getId()].readyStatus = packet.getStatus();
				
				if(packet.getId() == id){
					readyStatus = packet.getStatus();
					client.createShip(id, true);
				}
				else{
					names = client.players[packet.getId()].username + " is"; // debug
					
					// create a ship if they have true ready status and dont already have one
					if(packet.getStatus() && client.players[packet.getId()].ship == null){
						client.createShip(packet.getId(), false);
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
						if(packet.getStatus() && player.ship == null)
							client.createShip(packet.getId(), false);
							
					}
				}
			}
			else if(packet.type == Packet.CONNECT){
				
				if(id == -1){

					id = packet.getId();
					
					username = "GraalTest"+id; // debug
					
					client.players[id] = new Player(id, username);
					System.out.println("Connection established with server.");
					
					send(new Packet(Packet.CONNECT, id, username));
				
				}
				else{
					// TODO: Should probably check if that player already exists????
					Player tempPlayer = new Player(packet.getId(), packet.getUsername(), packet.getStatus());
					client.players[packet.getId()] = tempPlayer;
					
					System.out.println(tempPlayer + " connected.");
				}
			}
			else if(packet.type == Packet.DISCONNECT){
				
				// find client that matchs id from packet and remove them from local list
				for(int i = 0; i < client.players.length; i++){
					if(client.players[i] == null) continue;
					
					// remove the player from the game
					if(client.players[i].id == packet.getId()){
						System.out.println(client.players[i].username + " has disconnected.");
						client.ec.remove(client.players[i].ship);
						client.players[i] = null;
					}
					break;
				}
			}
			else if(packet.type == Packet.SERVER_MESSAGE){
				System.out.println("Server: " + packet.getMessage());
			}
		}
		
		/**
		 * Send a packet to the packet pool waiting to be sent to the server.
		 * @param packet
		 */
		public void send(Packet packet){
			packets.add(packet);
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
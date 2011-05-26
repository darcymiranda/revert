package server;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.Sys;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.Log;

import packet.Packet;
import packet.Snapshot;

/**
 * Server that will start when a client hosts a lobby.
 * 
 * @author dmiranda
 *
 */
public class Server implements Runnable{
	
	private AcceptConnections accepter;
	private boolean acceptConnections;	// Determines weither to accepting connections.
	private boolean isGameStarted;	// Controls the status of the connection loop.
	
	private ServerSocket serverSocket;
	private InetSocketAddress address;
	private List<Client> clients = new LinkedList<Client>();
	private World world;
	
	
	private long time;
	private final int tickRate = 16;
	
	public Server(int port){
		address = new InetSocketAddress(port);
	}
	
	/**
	 * Initialize the server and bind to the port. Start up the new thread that
	 * will block until a client connects.
	 * @throws IOException
	 */
	public void init() throws IOException{
		
		// Start the world.
		Log.info(" Starting world");
		world = World.getInstance();
		world.setServer(this);
		world.init();
		
		// Begin accepting connections.
		Log.info(" Starting server on " + address);
		serverSocket = new ServerSocket();
		serverSocket.bind(address);
		accepter = new AcceptConnections();
		accepter.start();
		isGameStarted = false;
		acceptConnections = true;
		
	}

	@Override
	public void run(){
		
		try{
			
			init();
			Log.info(" Server online.\n");
			
			for(;;){
				
				// Processes recieved packets.
				process();
				
				
				// Remove clients that have disconnected.
				Client client;
				for(int i = 0; i < clients.size(); i ++){
					client = clients.get(i);
					if(client.socket.isClosed()){
						client.disconnect();
						clients.remove(i);
						world.removePlayer(client);

						int tempId = client.id;
						
						// Send a notification packet to remaining clients that a client has
						// been disconnected.
						for(int j = 0; j < clients.size(); j++){
							Client disClient = clients.get(j);
							disClient.send(new Packet(Packet.DISCONNECT, tempId));
						}
						
					}
				}
				
				sleep();
				
			}
			
		}catch(IOException ioe){
			try {
				serverSocket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Main loop.
	 */
	public void process(){
		
		//List<Packet> inPackets;
		//Packet packet;
		Snapshot snapshot;
		for(Client client: clients){
			
			if(client == null) continue;
			if(client.receive == null) continue;
			
			
			snapshot = client.receive.getNextSnapshot();
			if(snapshot == null) continue;
			
			/* Process Packets here */
			for(Packet packet : snapshot.getPackets()){
			
				// update the client's game related data
				if(packet.type == Packet.UPDATE_SELF){
					client.getShip().netUpdate(packet);
				// update the client's bullets
				}else if(packet.type == Packet.UPDATE_SELF_BULLET){
					world.addBullet(packet, client);
				// update client's keyboard
				}else if(packet.type == Packet.UPDATE_SELF_INPUT){
					client.getShip().updateInput(packet);
				}
				// Performes a connection handshake with the client.
				else if(packet.type == Packet.CONNECT){
					
					client.setUsername(packet.getUsername());
						
					client.setConnectionStatus(true);
					System.out.println(client + " connection established.");
					
					// Sends around the names of clients/ids to eachother.
					// Send to all clients including self to confirm ready mark client side.
					sendToAll(new Packet(Packet.CONNECT, client.id, packet.getUsername(), client.getSpawnStatus()), false);
					
					// Sends data from currently connected clients to the newly connected client.
					sendAllToClient(client, new Packet(Packet.CONNECT));
					sendAllToClient(client, new Packet(Packet.READY_MARKER));	// make sure new client knows of everyones ready status
	
				}
				
				// Set ready status flag for clients
				else if(packet.type == Packet.READY_MARKER && !client.getSpawnStatus()){
					client.setSpawnStatus(packet.getStatus());
					
					if(client.getSpawnStatus()) 
						client.createShip();
					
					String rstatus = packet.getStatus() ? "ready" : "not ready";
					System.out.println(client + " is " + rstatus + ".");
					
					// Send to all clients including self to confirm ready mark client side.
					sendToAll(new Packet(Packet.READY_MARKER, client.id, packet.getStatus()), true);
					// Send currently connected client ready info to this client
					sendAllToClient(client, new Packet(Packet.READY_MARKER));
				}
	
				// Chat or Commands	-- has not been tested
				else if(packet.type == Packet.CHAT){
					// Check if the message is a command
					String packetMsg = packet.getMessage();
					int packetId = packet.getId();
					if(packetMsg.matches("/{1,1}")){
						String command = packetMsg.toLowerCase().substring(1);
						commandHandler(command);
					}else{
						
						// Grab username
						String tempUsername = null;
						for(int i = 0; i < clients.size(); i++)
							if(clients.get(i).getId() == packetId)
								tempUsername = clients.get(i).getUsername();
						
						// Send to all clients excluding self.
						sendToAll(new Packet(Packet.CHAT, packetId, tempUsername, packetMsg), false);
						System.out.println(client + " says: " + packetMsg);
					}
				}
				
				else if(packet.type == Packet.REQUEST_MAP){
					
					//send client the map
					ArrayList<Vector2f> list;
					PlayableMap map = world.getCurrentMap();
					list = map.getAsteroids();
					
					//sends asteroids
					for(int i = 0; i < list.size(); i++)
					{
						Vector2f temp = list.get(i);
						client.send(new Packet(Packet.UPDATE_ASTEROIDS, temp.x, temp.y));
					}
					//sends ores
					list = map.getOres();
					for(int i = 0; i < list.size(); i++)
					{
						Vector2f temp = list.get(i);
						client.send(new Packet(Packet.UPDATE_ORES, temp.x, temp.y));
					}
					//sends stations
					list = map.getStations();
					for(int i = 0; i < list.size(); i++)
					{
						Vector2f temp = list.get(i);
						client.send(new Packet(Packet.UPDATE_STATIONS, temp.x, temp.y));
					}
					
					// tell client that server finished sending map
					client.send(new Packet(Packet.LAST_MAP));
					System.out.println("Sent map to - " + client);
				}
			}
			
		}
		
		world.process();
		
	}
	
	/**
	 * This method is called when all players are ready to start the game.
	 */
	public void startGame(){
		isGameStarted = true;
	}
	
	/**
	 * Send to all connected clients
	 * @param packet
	 * @param self true send to everyone including self, false send to everyone excluding self
	 */
	public final void sendToAll(Packet packet, boolean self){
		
		// Send to all clients excluding the orginal sender.
		if(!self){
			for(Client client : clients){
				if(client.id != packet.getId())
					client.send(packet);
			}
		}
		// Send to all clients including the orginal sender.
		else{
			for(Client client : clients){
				client.send(packet);
				//!//System.out.println("send id " + packet.getId() + " to " + client);
			}
		}
	}
	
	/**
	 * Sends data from currently connected clients to the client passed in.
	 * @param client		the client receiving the packets
	 * @param packet	the packet must have a type defined
	 */
	public void sendAllToClient(Client client, Packet packet){
		
		// Sends data from currently connected clients to the newly connected client.
		Client iClient;
		Packet tempPacket;
		for(int i = 0; i < clients.size(); i++){
			
			iClient = clients.get(i);
			tempPacket = new Packet(packet.type);
			
			// Make sure we don't send it's own data to itself.
			if(iClient.id == client.id) continue;
			
			// Determine what kind of packet it is and fill accordingly.
			if(packet.type == Packet.READY_MARKER){
				tempPacket.setId(iClient.id);
				tempPacket.setStatus(iClient.getSpawnStatus());
			}
			else if(packet.type == Packet.CONNECT){
				tempPacket.setId(iClient.id);
				tempPacket.setUsername(iClient.getUsername());
				tempPacket.setStatus(iClient.getSpawnStatus());
			}
			else{
				tempPacket.type = Packet.SERVER_MESSAGE;
				tempPacket.setMessage("Error - Failed to obtain other players information.");
				System.err.println("CRITICAL: Failed to provide client information to " + client);
			}
			
			// Give the client data about the currently connected clients.
			client.send(tempPacket);
		}
	}
	
	/**
	 * Handles commands sent to the server via chat.
	 * @param command
	 */
	public void commandHandler(String command){
		
	}
	
	/**
	 * Restart the server to defaults. Can be used to start up the server aswell.
	 */
	public final void restart() throws IOException{
		disconnect();
		init();
		for(Client client : clients){
			client.reset();
		}
	}
	
	/**
	 * Disconnect the server and clean up the server.
	 * @throws IOException
	 */
	public synchronized final void disconnect() throws IOException{
		for(Client client : clients){
			client.disconnect();
			clients.remove(client);
			client = null;
		}
		accepter = null;
		serverSocket.close();
		System.out.println("Server offline.");
	}
	
	/**
	 * Wait till the next ready tick. This is determined by the defined tick rate.
	 */
	private void sleep() throws InterruptedException {
		
		long sleepTime = tickRate - (System.currentTimeMillis() - time);
		if (sleepTime > 0) {
			Thread.sleep(sleepTime);
		} else {
			// The server has reached maximum load, players may now experiance lag.
			try{
				Log.warn("Server load: " + (100 + (Math.abs(sleepTime) / (tickRate / 100))) + "%!");
			}catch(ArithmeticException ae){
				// WHAT YOU CANT DIVIDE BY ZERO!?!?!
			}
		}
		time = System.currentTimeMillis();
		
	}
	
	/**
	 * Thread to accept new connections.
	 * @author dmiranda
	 *
	 */
	private class AcceptConnections extends Thread {
		
		@Override
		public void run(){
			
			// Accept all connections until game is started.
			while(!isGameStarted){
				
				if(acceptConnections){
					try{
					
						// Create a new client object to represent this player server side.
						// Attatch the connected socket to it and immediatly send out a
						// connection packet that will initalite the connection handshake.
						Socket socket;
						socket = serverSocket.accept();
						socket.setTcpNoDelay(true);
						Client client = new Client(socket);
						world.addPlayer(client);
						client.send(new Packet(Packet.CONNECT, client.id));
						clients.add(client);
						System.out.println(client + " connected.");
						
					}catch(Exception e){
						//e.printStackTrace();
						try {
							serverSocket.close();
							System.out.println("Stopped accepting connections.");
							acceptConnections = false;
							
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		}
	}
}

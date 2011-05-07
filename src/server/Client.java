package server;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import packet.Packet;
import packet.Snapshot;

/**
 * 
 * @author dmiranda
 *
 */

public class Client extends Thread {
	
	public Socket socket;
	public int id = -1;
	public ClientReceive receive;
	
	private ObjectOutputStream out;
	private InetAddress host;
	
	/* is the client ready to start a game (Ready button has been clicked) */
	private boolean isReady;
	
	/* determines wiether a connection is fully established ( a connection packet went back and forth ) */
	private boolean connEstablished;
	
	/* name associated with this client. user specific */
	private String username = "unknown";
	
	/* the ship that is attached to this client */
	private Ship ship;
	
	private Snapshot outSnapshot = null;
	
	public Client(Socket socket) throws IOException{
		this.socket = socket;
		
		out = new ObjectOutputStream(socket.getOutputStream());
		receive = new ClientReceive(this);
		
		host = this.socket.getLocalAddress();
		
		// start handling packets
		this.start();
		
		// start reading packets
		receive.start();
		
		ship = new Ship(Constants.SPAWN_POSITION_X,Constants.SPAWN_POSITION_Y,false);
		
	}
	
	@Override
	public void run(){
		
		// hacky.. =/
		while(socket != null && !socket.isClosed()){
			
			try{
				
				if(outSnapshot != null){
					out.writeObject(outSnapshot);
					out.flush();
					outSnapshot = null;
				}
				
				Thread.sleep(1);
				
			}catch(SocketException se){
				disconnect();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * This method is called from the World class and is used to do all calculations on
	 * this current tick increment.
	 */
	public void process(){
		ship.tick();
	}
	
	/**
	 * Add a packet into the write queue.
	 * @param packet
	 */
	public void send(Packet packet){
		if(packet != null){
			if(outSnapshot ==  null)
				outSnapshot = new Snapshot();
			
			outSnapshot.addPacket(packet);
		}
	}
	
	/**
	 * Disconnect the client from the server.
	 */
	public void disconnect(){
		try{
			if(receive.getInStream() != null)
				receive.getInStream().close();
			out.close();
			socket.close();
		}catch(IOException ioe){
			
		}catch(Exception e ){
			e.printStackTrace();
		}

		System.out.println(this + " disconnected.");
	}
	
	public void reset(){
		isReady = false;
		connEstablished = false;
		username = "unkown";
	}
	
	public boolean getReadyStatus(){ return isReady; }
	public void setReadyStatus(boolean r){ this.isReady = r; }
	
	public boolean getConnectionStatus(){ return connEstablished; }
	public void setConnectionStatus(boolean s){ this.connEstablished = s; }
	
	public String getUsername(){ return username; }
	public void setUsername(String n){ this.username = n; }
	
	public Ship getShip(){ return ship; }
	
	@Override
	public String toString(){
		return username + " " + host + " id(" + id + ")";
	}

}

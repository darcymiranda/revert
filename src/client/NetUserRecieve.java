package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.List;

import packet.Snapshot;

/**
 * 
 * @author dmiranda
 *
 */

public class NetUserRecieve extends Thread{

	private NetUser user;
	private ObjectInputStream in;
	
	private List<Snapshot> snapshots = new LinkedList<Snapshot>();
	
	public NetUserRecieve(NetUser user){
		this.user = user;
	}
	
	private void init() throws IOException{
		in = new ObjectInputStream(user.socket.getInputStream());
	}
	
	@Override
	public void run(){
		
		try {
			
			init();
			
			//Packet packet;
			Snapshot snapshot;
			for(;;){
				
				// read packets from the server and add into packet queue
				//packet = (Packet) in.readObject();
				snapshot = (Snapshot) in.readObject();
				//packets.add(packet);
				snapshots.add(snapshot);
				
			}
			
		} catch (IOException ioe){
		
		} catch (Exception e) {
			//System.err.println("Connection to server was lost.");
			//user.disconnect();
			e.printStackTrace();
		}
		
	}
	
	public Snapshot getNextSnapshot(){
		Snapshot snapshot = null;
		if(!snapshots.isEmpty()){
			snapshot = snapshots.get(0);
			snapshots.remove(0);
		}
		return snapshot;
	}
	
	public boolean hasNextSnapshot(){
		return !snapshots.isEmpty();
	}
	
	public ObjectInputStream getInStream(){ return in; }
	
}

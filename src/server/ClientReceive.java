package server;
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

public class ClientReceive extends Thread{

	private Client client;
	private ObjectInputStream in;
	
	public List<Snapshot> snapshots = new LinkedList<Snapshot>();		// INBOUND
	
	public ClientReceive(Client client){
		this.client = client;
	}
	
	private void init() throws IOException{
		in = new ObjectInputStream(client.socket.getInputStream());
	}
	
	@Override
	public void run(){
		
		try {
			
			init();
			
			Snapshot snapshot;
			for(;;){
				
				// read packets from the server and add into packet queue
				snapshot = (Snapshot) in.readObject();
				snapshots.add(snapshot);
				
			}
			
		} catch (IOException ioe){
			client.disconnect();
		} catch (Exception e) {
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

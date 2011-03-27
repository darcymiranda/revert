package server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.List;

import packet.Packet;

/**
 * 
 * @author dmiranda
 *
 */

public class ClientReceive extends Thread{

	private Client client;
	private ObjectInputStream in;
	
	public List<Packet> packets = new LinkedList<Packet>();		// INBOUND
	
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
			
			Packet packet;
			for(;;){
				
				// read packets from the server and add into packet queue
				packet = (Packet) in.readObject();
				packets.add(packet);
				
			}
			
		} catch (IOException ioe){
			client.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Get lastest packet from the queue.
	 * @return Packet
	 */
	public Packet getPacket(){
		Packet p = null;
		if(!packets.isEmpty()){
			p = packets.get(0);
			packets.remove(0);
		}
		return p;
	}
	
	/**
	 * Get all the packets currently in the queue.
	 * @return
	 */
	public List<Packet> getPackets(){
		List<Packet> p = new LinkedList<Packet>(packets);
		packets.clear();
		return p;
	}
	
	/**
	 * Returns true if there are packets in the queue waiting to be read.
	 * @return
	 */
	public boolean hasPackets(){
		return !packets.isEmpty();
	}
	
	public ObjectInputStream getInStream(){ return in; }
	
}

package packet;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Snapshot implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<Packet> packets = new LinkedList<Packet>();
	
	public void Screnshot(){
		
	}
	
	public void addPacket(Packet packet){
		if(packet != null)
			packets.add(packet);
		else
			System.err.println("Null packet attempted to be added to the snapshot.");
	}
	
	public Packet getNextPacket(){
		Packet p = null;
		if(!packets.isEmpty()){
			p = packets.get(0);
			packets.remove(0);
		}
		return p;
	}
	
	public Iterator<Packet> getIterator(){
		return packets.iterator();
	}
	
	public List<Packet> getPackets(){
		return packets;
	}

}

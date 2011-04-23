package packet;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Snapshot {
	
	private List<Packet> packets = new LinkedList<Packet>();
	private int increment = 0;
	
	public void Screnshot(){
		
	}
	
	public void addPacket(Packet packet){
		if(packet != null)
			packets.add(packet);
		else
			System.err.println("Null packet attempted to be added to the screenshot.");
	}
	
	public Iterator<Packet> getIterator(){
		return packets.iterator();
	}
	
	public List<Packet> getPackets(){
		return packets;
	}

}

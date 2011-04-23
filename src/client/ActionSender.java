package client;
import client.ex.NetException;
import packet.Packet;


/**
 * Abstracts out all of the networking that involves sending packets to the server. Mearly
 * calling one of the defined methods, sends a specific packet that will be read and
 * processed accordingly by the server.
 * @author dmiranda
 *
 */
public class ActionSender {
	
	private NetUser net;
	private boolean tw, ta, ts, td, tq, te;		// temp directions to not send out duplicate packets
	private float tx, ty, txv, tyv, tr;
	
	/**
	 * Create an instance of the ActionSender.
	 * @param net	the networking portion
	 */
	public ActionSender(NetUser net){
		this.net = net;
	}
	
	/**
	 * Sends a chat message to the server that will be seen by all users.
	 * @param message
	 * @throws NetException
	 */
	public void sendChat(String message) throws NetException{
		checkInit();
		if(net.username == null) 
			throw new NetException("Network username has not be initalized yet." +
					"Cannot send chat.");
		
		net.send(new Packet(Packet.CHAT, net.id, net.username, message));
	}
	
	/**
	 * Tells the server your ready status and the users of your ready status.
	 * @param ready
	 * @throws NetException
	 */
	public void sendReady(boolean ready) throws NetException{
		checkInit();
		if(net.readyStatus == ready)
			throw new NetException("Cannot send a ready command that matchs the" +
					" current ready status.");
		
		net.send(new Packet(Packet.READY_MARKER, net.id, ready));
	}
	
	public void sendMoveUpdate2(float x, float y, float xv, float yv, float r) throws NetException{
		
		checkInit();
		// check if the last directions are the same as the new ones
		if(!(tx != x || ty != y || xv != txv || yv != tyv || tr != r)){
			net.send(new Packet(Packet.UPDATE_SELF, x, y, xv, yv, r));
			tx = x; ty = y; txv = xv; tyv = yv; tr = r;
		}	
		
	}
	
	public void sendMoveUpdate(Packet packet) throws NetException{
		checkInit();
		net.send(packet);
	}
	
	public void sendShot() throws NetException{
		
	}
	
	public void sendHit() throws NetException{
		
	}
	
	/**
	 * Checks weither this client has established a connection to the server.
	 * @throws NetException
	 */
	private void checkInit() throws NetException{
		if(net.id == -1)
			throw new NetException("Network id has not been recieved by the server. " +
					"Cannot perform network opreations.");
	}
}

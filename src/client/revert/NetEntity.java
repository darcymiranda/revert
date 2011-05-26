package client.revert;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;

import packet.Packet;

/**
 * 
 * Display State - entity information to be rendered
 * Previous State - last ticks entity information
 * Simulated State - information of entity ahead by one tick
 * 
 * @author dmiranda
 *
 */
public abstract class NetEntity extends Entity {
	
	private EntityState displayState, previousState, simulateState;
	private float smoothing = 0;
	private boolean isLocal;

	protected Vector2f serverPosition;
	
	public NetEntity(){
		super();
		displayState = new EntityState(this);
		previousState =  new EntityState(this);
		simulateState =  new EntityState(this);
		serverPosition = new Vector2f(clientPosition.x,clientPosition.y);
	}
	
	/**
	 * Requires a packet object to set this ships x and y serverPosition.
	 * @param p the packet to be read
	 */
	public void updatePacket(Packet p){
		
		serverPosition.x = p.getPositionX();
		serverPosition.y = p.getPositionY();
		
		if(!isLocal){
			
			smoothing = 1;
			
			previousState.setState(simulateState);
			
			simulateState.setVelocity(p.getVelocityX(), p.getVelocityY());
			simulateState.setRotation(p.getRotationR());
		
			// Teleport entity to proper location, if the client and server positions are out of sync.
			float distance = serverPosition.distance(displayState.position);
			if(distance > 50 || distance < -50)
				simulateState.setPosition((serverPosition.x - p.getVelocityX()),
						(serverPosition.y + p.getVelocityY()));

		}
	}
	
	/**
	 * Returns a packet object containing the rotation; x, y positions and x, y, velocities.
	 * @return
	 */
	public Packet getPacket(){
		return new Packet(Packet.UPDATE_SELF, displayState.position.x, displayState.position.y,
				displayState.velocity.x, displayState.velocity.y, displayState.rotation);
	}
	
	public void update(GameContainer gc, int delta){
		
		super.update(gc, delta);
		
		// interpolate non-local entities
		if(!isLocal){
			previousState.update(delta);
			simulateState.update(delta);
			
			// determine smoothing factor - six equals ticks per packet sent
			smoothing -= (1 / 6);
			if(smoothing < 0) smoothing = 0;
			
			// interpolate
			displayState.setPosition(previousState.position.x + (simulateState.position.x - previousState.position.x) * smoothing, 
					displayState.position.y = previousState.position.y + (simulateState.position.y - previousState.position.y) * smoothing);
			displayState.setRotation(previousState.rotation + (simulateState.rotation - previousState.rotation) * smoothing);
			
			// set new positions
			clientPosition.x = displayState.getPosition().x;
			clientPosition.y = displayState.getPosition().y;
			rotation = displayState.getRotation();
			
		}
		
	}
	
	public boolean isLocal(){ return isLocal; }
	public void setLocal(boolean isLocal){ this.isLocal = isLocal; }

	public Vector2f getServerPosition(){ return new Vector2f(serverPosition); }
	
}

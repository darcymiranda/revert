package packet;
import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * 
 * @author dmiranda
 *
 */

public class TestPacket implements Serializable  {
	
	ByteBuffer bytes = ByteBuffer.allocate(512);
	
	/*
	public static final byte DISCONNECT = -1;
	public static final byte CONNECT = 1;
	public static final byte READY_MARKER = 2;
	public static final byte CHAT = 3;
	public static final byte UPDATE_SELF = 4;
	public static final byte UPDATE_OTHER = 5;
	public static final byte CLIENT_INFO = 9;
	public static final byte SERVER_MESSAGE = 10;

	private static final long serialVersionUID = 1L;
	
	private final long timeStamp = System.currentTimeMillis();
	private int id;
	private String message;
	private String username;
	private boolean status;
	private boolean w,s,d,a;
	private float x,y,r;
	
	public byte type;
	
	public void setMessage(String message){ this.message = message; }
	public String getMessage(){ return message; }
	
	public int getId(){ return id; }
	public void setId(int id){ this.id = id; }
	
	public boolean getStatus(){ return status; }
	public void setStatus(boolean s){ this.status = s; }
	
	public String getUsername(){ return username; }
	public void setUsername(String n){ this.username = n; }
	
	public boolean getPressedW(){ return w; }
	public boolean getPressedA(){ return a; }
	public boolean getPressedS(){ return s; }
	public boolean getPressedD(){ return d; }
	
	public float getPositionX(){ return x; }
	public float getPositionY(){ return y; }
	public float getRotationR(){ return r; }
	
	public long getTimeStamp(){ return timeStamp; }
	
	@Override
	public String toString(){
		return "ID: " + id + "  Time: " + "  Type: " + type;
	}
	*/

}

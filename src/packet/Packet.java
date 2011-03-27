package packet;
import java.io.Serializable;

/**
 * 
 * @author dmiranda
 *
 */

public class Packet implements Serializable  {
	
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
	private int id = -1;
	private String message;
	private String username;
	private boolean status;
	private boolean w,s,d,a,q,e;
	private float x,y,r;
	
	public byte type;
	
	public Packet(byte t){
		this.type = t;
	}
	
	public Packet(byte t, int id){
		this.id = id;
		this.type = t;
	}
	
	public Packet(byte t, String msg){
		this.type = t;
		this.message = msg;
	}
	
	public Packet(byte t, int id, String name){
		this.type = t;
		this.id = id;
		this.username = name;
	}
	
	public Packet(byte t, int id, boolean status){
		this.id = id;
		this.type = t;
		this.status = status;
	}
	
	public Packet(byte t, int id, String name, boolean status){
		this.id = id;
		this.type = t;
		this.username = name;
		this.status = status;		
	}
	
	public Packet(byte t, int id, String name, String msg){
		this.type = t;
		this.id = id;
		this.username = name;
		this.message = msg;
	}
	
	public Packet(byte t, float x, float y){
		this.type = t;
		this.x = x;
		this.y = y;
	}
	
	public Packet(byte t, boolean w, boolean a, boolean s, boolean d, boolean q, boolean e){
		this.type = t;
		this.w = w;	this.s = s; this.d = d; this.a = a; this.q = q; this.e = e;
	}
	
	public Packet(byte t, int id, float x, float y, float r){
		this.type = t;
		this.id = id;
		this.x = x;
		this.y = y;
		this.r = r;
	}
	
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
	public boolean getPressedQ(){ return q; }
	public boolean getPressedE(){ return e; }
	
	public float getPositionX(){ return x; }
	public float getPositionY(){ return y; }
	public float getRotationR(){ return r; }
	
	public long getTimeStamp(){ return timeStamp; }
	
	@Override
	public String toString(){
		return "ID: " + id + "  Time: " + "  Type: " + type;
	}

}

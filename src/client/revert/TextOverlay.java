package client.revert;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Vector2f;


public class TextOverlay {
	
	private Vector2f position;
	private UnicodeFont font;
	private int delta;
	
	private final float PADDING = 25;
	
	private Entity tracking;
	
	public TextOverlay(Vector2f position, UnicodeFont font){
		this.position = position;
		this.font = font;
	}
	
	public void trackNetEntity(Entity e){
		tracking = e;
	}
	
	public void update(int delta){
		this.delta = delta;
	}
	
	public void render(Graphics g){
		g.setFont(font);
		g.setColor(Color.white);
		
		if(tracking == null) return;
		
		g.drawString("Client Position: " + tracking.getClientPosition().x + " : " + tracking.getClientPosition().y,
				position.x, position.y);
		
		g.drawString("       Velocity: " + tracking.getVelocity().x * delta + " : " + tracking.getVelocity().y * delta,
				position.x, position.y - PADDING);
	}

}

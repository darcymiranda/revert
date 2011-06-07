package client.revert;

import java.text.DecimalFormat;

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
	
	private DecimalFormat format;
	
	public TextOverlay(Vector2f position, UnicodeFont font){
		this.position = position;
		this.font = font;
		format = new DecimalFormat("#,##0.0000");
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
		
		g.drawString("Client Position: " + format.format(tracking.getClientPosition().x) + " : " + format.format(tracking.getClientPosition().y),
				position.x, position.y);
		
		g.drawString("       Velocity: " + format.format(tracking.getVelocity().x * delta) + " : " + format.format(tracking.getVelocity().y * delta),
				position.x, position.y - PADDING);
		g.drawString("Rotation: " + format.format(tracking.rotation), position.x, position.y + PADDING);
		g.drawString("Max Vel: " + format.format(tracking.maxVelocity.x * delta) + " : " + format.format(tracking.maxVelocity.y * delta),
				position.x, position.y + (PADDING*2));
	}

}

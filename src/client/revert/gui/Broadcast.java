package client.revert.gui;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Vector2f;

public class Broadcast {
	
	private List<Message> messages = new LinkedList<Message>();
	
	private Vector2f position;
	private UnicodeFont font;
	private float width, height;
	
	private boolean isFade = true;
	
	final int decayTime = 4;
	final int fadeTime = 2;
	
	/**
	 * Display messages to the local player. Messages will be bumped up but will be deleted if
	 * passed the height that is passed in.
	 * @param position
	 * @param width
	 * @param height
	 */
	public Broadcast(Vector2f position, float width, float height){
		this.position = position;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Adds a message to be displayed on the local player's screen.
	 * @param msg
	 */
	public void addMessage(String msg){
		
		int fontWidth = 0,fontHeight = 0;
		if(font != null){
			fontWidth = font.getWidth(msg);
			fontHeight = font.getHeight(msg);
		}
		
		// create the message
		messages.add(new Message(msg, 
				new Vector2f(position.x - fontWidth, position.y)));
		
		for(int i = 0; i < messages.size(); i++){
			messages.get(i).position.y -= fontHeight;
		}
	}
	
	/**
	 * Set the broadcasting font.
	 * @param font
	 */
	public void setFont(UnicodeFont font){
		this.font = font;
	}
	
	/**
	 * Determines if the text will fade out or just disappear. Default to true.
	 * @param f
	 */
	public void setFade(boolean f){
		this.isFade = f;
	}
	
	public void update(){
		
		Message msg;
		for(int i = 0; i < messages.size(); i++){
			msg = messages.get(i);
			
			float time = System.currentTimeMillis() / 1000;
			
			// remove decayed messages
			if(time - msg.creationTime >= decayTime){
				messages.remove(i);
				continue;
			}
			
			if(msg.position.y > (position.y + height)){
				messages.remove(i);
				continue;
			}
			
			//fade out messages
			if(isFade && (time - msg.creationTime) >= fadeTime){
				messages.get(i).fade();
			}
			
		}
		
	}
	
	public void render(Graphics g){
		
		g.setFont(font);
		for(int i = 0; i < messages.size(); i++)
			messages.get(i).render(g);
		
	}
	
	private class Message{
		
		public Vector2f position;
		
		private String msg;
		
		private int alpha = 255;
		private long creationTime;
		
		private final int fadeSpeed = 4;
		
		public Message(String msg, Vector2f position){
			this.msg = msg;
			this.position = position;
			creationTime = System.currentTimeMillis() / 1000;
		}
		
		public void render(Graphics g){
			
			g.setColor(new Color(200, 200, 200, alpha));
			g.drawString(msg, position.x, position.y);
		}
		
		public void fade(){
			alpha -= fadeSpeed;
		}
		
	}

}

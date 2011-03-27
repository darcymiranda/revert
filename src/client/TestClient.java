package client;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import client.ex.NetException;



/**
 * TODO: Have a LinkList of Clients to represent a local copy of all client side information.
 */

/**
 * A test class to test the client.
 * @author dmiranda
 *
 */
public class TestClient extends Thread {
	
	public List<Player> players = new LinkedList<Player>();
	private Keyboard keyboard;
	private ActionSender actionSender;
	
	public TestClient(){
		NetUser netUser = new NetUser(this);
		Thread netUserThread = new Thread(netUser);
		netUserThread.start();
		keyboard = new Keyboard();
		JFrame j = new JFrame();
		j.setSize(256, 256);
		j.addKeyListener(keyboard);
		j.setVisible(true);
		actionSender = new ActionSender(netUser);
		this.start();
	}
	
	public void run(){
		while(true){
			try{
				
				try{
					actionSender.sendMoveUpdate(keyboard.w, keyboard.a, keyboard.s, keyboard.d);
				}catch (NetException e){
					System.err.println(e.getMessage());
				}
				
				Thread.sleep(150);
	
			}catch(InterruptedException ie){
				ie.printStackTrace();
			}
		}

	}
	
	public Player getPlayerById(int id){
		for(Player player: players){
			if(player.id == id) return player;
		}
		return null;
	}
	
	public static void main(String[] args){
		new TestClient();
	}
	
	private class Keyboard implements KeyListener{
	
		public boolean w, a, s, d;
	
		public Keyboard(){
	
		}

		@Override
		public void keyTyped(KeyEvent e) {
			
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_W){
				w = true;
			}
			if(e.getKeyCode() == KeyEvent.VK_A){
				a = true;
			}
			if(e.getKeyCode() == KeyEvent.VK_S){
				s = true;
			}
			if(e.getKeyCode() == KeyEvent.VK_D){
				d = true;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_W){
				w = false;
			}
			if(e.getKeyCode() == KeyEvent.VK_A){
				a = false;
			}
			if(e.getKeyCode() == KeyEvent.VK_S){
				s = false;
			}
			if(e.getKeyCode() == KeyEvent.VK_D){
				d = false;
			}			
		}
	
	}
	

}

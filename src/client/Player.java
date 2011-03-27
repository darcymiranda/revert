package client;

import client.revert.Ship;

/**
 * Keep other player's information on the local machine.
 * @author dmiranda
 *
 */
public class Player {

	public int id = -1;
	public boolean readyStatus;
	public String username = "unknown";
	public Ship ship = null;
	
	public boolean tackingShip = false;
	
	public Player(int id, String username){
		this.id = id;
		this.username = username;
	}
	
	public Player(int id, String username, boolean status){
		this.id = id;
		this.username = username;
		readyStatus = status;
	}
	
	public String toString(){
		return username + " id(" + id + ")";
	}
	
}

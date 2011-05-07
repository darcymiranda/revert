package client.revert;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class EntityController {
	
	private ArrayList<Entity> pool;
	
	public EntityController(){
		pool = new ArrayList<Entity>();
	}
	
	/**
	 * Adds the passed Entity to the pool.
	 * @param e the Entity
	 */
	public void add(Entity e){
		
		pool.add(e);
		
	}
	
	public void remove(Entity e){
		pool.remove(e);
	}
	
	public Entity getNext(int i){
		if(i >= pool.size()){
			i = 0;
		}
		else{
			i++;
		}
		return (Ship) pool.get(i);
	}
	
	public Entity getControlledShip(){
		
		Entity e = null;
		
		for(int i = 0; i < pool.size(); i++){
			if(pool.get(i).isLocal() == true){
				e = pool.get(i);
				break;
			}
		}
		
		return e;
	}
	
	public void checkCollisions(){
	}
	
	public void render(Graphics g){
		Entity e;
		for(int i = 0; i < pool.size(); i++){
			e = pool.get(i);
			e.render(g);
		}
		
	}
	
	public void update(GameContainer gc, int delta){
		Entity e;
		for(int i = 0; i < pool.size(); i++){
			e = pool.get(i);
			if(e != null)
				e.update(gc, delta, true);
		}
	}
	
	public ArrayList<Entity> getPool(){
		return pool;
	}


}

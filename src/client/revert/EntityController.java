package client.revert;

import java.util.ArrayList;
import java.util.Iterator;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

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
	
	/**
	 * Find an entity matching the passed id.
	 * @param id the unique id given to each entity
	 * @return Entity
	 */
	public Entity findById(int id){
		
		Entity e = null;
		
		for(int i = 0; i < pool.size(); i++){
			if(pool.get(i).getId() == id)
				e = pool.get(i);
		}
		
		return e;
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
	
	public Iterator<Entity> getIterator(){
		return pool.iterator();
	}


}

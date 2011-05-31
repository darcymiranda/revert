package client.revert;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class EntityController {
	
	private ArrayList<Entity> entityPool = new ArrayList<Entity>();;
	private ArrayList<Bullet> bulletPool = new ArrayList<Bullet>();
	
	/**
	 * Adds the passed Entity to the pool.
	 * @param e the Entity
	 */
	public void addEntity(Entity e){
		entityPool.add(e);
	}
	
	/**
	 * Adds the passed Bullet to the pool.
	 * @param e the Entity
	 */
	public void addBullet(Bullet b){
		bulletPool.add(b);
	}
	
	public void removeEntity(Entity e){
		entityPool.remove(e);
	}
	
	public void removeBullet(Bullet b){
		bulletPool.remove(b);
	}
	
	public Entity getNextEntity(int i){
		if(i >= entityPool.size()){
			i = 0;
		}else
			i++;
		return entityPool.get(i);
	}
	
	public Entity getEntityById(int index){
		for(int i = 0; i < entityPool.size(); i++){
			if(index == entityPool.get(i).id){
				return entityPool.get(i);
			}
		}
		return null;
	}
	
	public Bullet getBulletById(int id){
		for(int i = 0; i < bulletPool.size(); i++){
			Bullet bullet = bulletPool.get(i);
			if(id == bullet.id){
				return bullet;
			}
		}
		return null;		
	}
	
	/**
	 * Set the the server id to the last created bullet.
	 * @param id
	 * @param owner
	 */
	public void setBulletId(int id, int ownerId){
		for(int i = 0; i < bulletPool.size(); i++){
			Bullet b = bulletPool.get(i);
			if(b.id == -1 && b.owner.id == ownerId){
				b.id = id;
				return;
			}
		}
	}
	
	/**
	 * Returns the nearest entity to the passed entity.
	 * @param owner
	 * @return
	 */
	public Entity getNearestEntity(Entity owner, float range){
		
		float distance = range;
		Entity target = null;
		for(int i = 0; i < entityPool.size(); i++){
			
			Entity other = entityPool.get(i);
			
			if(owner.id == other.id) continue;
			
			float dx = (owner.clientPosition.x + owner.getWidth() /2) - (other.clientPosition.x + other.getWidth() /2);
			float dy = (owner.clientPosition.y + owner.getHeight() /2) - (other.clientPosition.y + other.getHeight() /2);
			
			float tempDistance = (float) Math.sqrt((dx * dx) + (dy * dy));
			if(tempDistance < distance)
				target = other;
			
		}
		
		return target;
		
	}
	
	/**
	 * Check collisions between entites/entites and entites/bullets. This method will fire the
	 * entity/bullet collision methods.
	 */
	public void checkCollisions(){
		
		Entity entity;
		Bullet bullet;
		for(int i = 0; i < bulletPool.size(); i++){
			
			bullet = bulletPool.get(i);
			for(int j = 0; j < entityPool.size(); j++){
				
				entity = entityPool.get(j);
				if(bullet.owner.id != entity.id){
					if(bullet.getHitBox().intersects(entity.getHitBox())){
						bullet.collide(entity);
						entity.collide(bullet);
						if(bullet.hasExpired()) bulletPool.remove(i);
					}
				}
			}
		}
		
	}
	
	public void render(Graphics g){
		Bullet b;
		for(int i = 0; i < bulletPool.size(); i++){
			b = bulletPool.get(i);
			b.render(g);
		}	
		
		Entity e;
		for(int i = 0; i < entityPool.size(); i++){
			e = entityPool.get(i);
			e.render(g);
		}	
	}
	
	public void update(GameContainer gc, int delta){
		Bullet b;
		for(int i = 0; i < bulletPool.size(); i++){
			b = bulletPool.get(i);
			if(b.hasExpired()){ 
				bulletPool.remove(i);
				continue;
			}
			
			b.update(gc, delta);
		}
		
		Entity e;
		for(int i = 0; i < entityPool.size(); i++){
			e = entityPool.get(i);
			e.update(gc, delta);
		}
	}
	
	public ArrayList<Entity> getEntityPool(){
		return entityPool;
	}
	
	public ArrayList<Bullet> getBulletPool(){
		return bulletPool;
	}


}

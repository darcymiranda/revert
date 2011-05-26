package server.entites;


public class Missile extends Bullet{

	private final int HEIGHT = 10,
					WIDTH = 2;
	
	public Missile(float x, float y, float xv, float yv, float r, int id) {
		super(x, y, xv, yv, r, id);
		super.height = HEIGHT;
		super.width = WIDTH;
	}
	
	public void update(){
		super.update();
	}

}

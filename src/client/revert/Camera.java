
package client.revert;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.tiled.TiledMap;

public class Camera {

	private TiledMap map;
	
   private int numTilesX;
   private int numTilesY;
   private int mapHeight;
   private int mapWidth;
   private int tileWidth;
   private int tileHeight;

   private GameContainer gc;

   private float cameraX;
   private float cameraY;
   
   /** the last position of the camera to prevent null when focused object gets destroyed */
   private Vector2f lastPosition;
   
   /**
    * Create a new camera
    *
    * @param gc the GameContainer, used for getting the size of the GameCanvas
    * @param map the TiledMap used for the current scene
    */
   public Camera(GameContainer gc, TiledMap map) {
      this.map = map;
      
      this.numTilesX = map.getWidth();
      this.numTilesY = map.getHeight();
      
      this.tileWidth = map.getTileWidth();
      this.tileHeight = map.getTileHeight();
      
      this.mapHeight = this.numTilesX * this.tileWidth;
      this.mapWidth = this.numTilesY * this.tileHeight;
      
      lastPosition = new Vector2f();
      
      this.gc = gc;
  
   }
   
   /**
    * "locks" the camera on the given coordinates. The camera tries to keep the location in it's center.
    *
    * @param x the real x-coordinate (in pixel) which should be centered on the screen
    * @param y the real y-coordinate (in pixel) which should be centered on the screen
    */
   public void centerOn(Vector2f position) {
	   
	  // if the object does not exist let the camera focus on the last position
	  if(position == null)
		  position = lastPosition;
	  else
		  lastPosition = position;

      //try to set the given position as center of the camera by default
      cameraX = position.x - gc.getWidth()  / 2;
      cameraY = position.y - gc.getHeight() / 2;
      
      //if the camera is at the right or left edge lock it to prevent a black bar
      if(cameraX < 0) cameraX = 0;
      if(cameraX + gc.getWidth() > mapWidth) cameraX = mapWidth - gc.getWidth();
      
      //if the camera is at the top or bottom edge lock it to prevent a black bar
      if(cameraY < 0) cameraY = 0;
      if(cameraY + gc.getHeight() > mapHeight) cameraY = mapHeight - gc.getHeight();
      
   }
   
   /**
    * "locks" the camera on the center of the given Rectangle. The camera tries to keep the location in it's center.
    *
    * @param x the x-coordinate (in pixel) of the top-left corner of the rectangle
    * @param y the y-coordinate (in pixel) of the top-left corner of the rectangle
    * @param height the height (in pixel) of the rectangle
    * @param width the width (in pixel) of the rectangle
    */
   public void centerOn(Vector2f position, float height, float width) {
      this.centerOn(new Vector2f(position.x + width / 2, position.y + height / 2));
   }

   /**
    * "locks the camera on the center of the given Shape. The camera tries to keep the location in it's center.
    * @param shape the Shape which should be centered on the screen
    */
   public void centerOn(Shape shape) {
      this.centerOn(new Vector2f(shape.getCenterX(), shape.getCenterY()));
   }
   
   /**XXX: Temporarly set at server position for testing, should be client position.
   /**
    * Locks the camera on the center of the given Entity. If the entity doesn't exist, the camera
    * will focus on the default 200/200 position.
    * @param entity
    */
   public void centerOn(Entity entity){
	   if(entity != null)
		   this.centerOn(new Vector2f(entity.serverPosition.x + entity.velocity.x, entity.serverPosition.y + entity.velocity.y));
   }
   
   /**
    * draws the part of the map which is currently focused by the camera on the screen
    */
   public void drawMap() {
      this.drawMap(0, 0);
   }
   
   /**
    * draws the part of the map which is currently focussed by the camera on the screen.<br>
    * You need to draw something over the offset, to prevent the edge of the map to be displayed below it<br>
    * Has to be called before Camera.translateGraphics() !
    * @param offsetX the x-coordinate (in pixel) where the camera should start drawing the map at
    * @param offsetY the y-coordinate (in pixel) where the camera should start drawing the map at
    */
   
   public void drawMap(int offsetX, int offsetY) {
       //calculate the offset to the next tile (needed by TiledMap.render())
       int tileOffsetX = (int) - (cameraX % tileWidth);
       int tileOffsetY = (int) - (cameraY % tileHeight);
      
       //calculate the index of the leftmost tile that is being displayed
       int tileIndexX = (int) (cameraX / tileWidth);
       int tileIndexY = (int) (cameraY / tileHeight);
      
       //finally draw the section of the map on the screen
       map.render(   
             tileOffsetX + offsetX,
             tileOffsetY + offsetY,
             tileIndexX, 
             tileIndexY,
                (gc.getWidth()  - tileOffsetX) / tileWidth  + 1,
                (gc.getHeight() - tileOffsetY) / tileHeight + 1);
   }
   
   /**
    * Translates the Graphics-context to the coordinates of the map - now everything
    * can be drawn with it's NATURAL coordinates.
    */
   public void translateGraphics() {
      gc.getGraphics().translate(-cameraX, -cameraY);
   }
   /**
    * Reverses the Graphics-translation of Camera.translatesGraphics().
    * Call this before drawing HUD-elements or the like
    */
   public void untranslateGraphics() {
      gc.getGraphics().translate(cameraX, cameraY);
   }
   
}
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
/*
 Directly updates game graphics and variables 
*/
public class GameManager extends GameFramework {

    public static void main( String[] args ) {
        new GameManager().run();
    }

    private static final double DEC_FUEL = 0.0012;

    private boolean gameOver;
    private boolean collided;

    private Point collisionPoint = new Point();
    private TileMap map;
    private ResourceManager resourceManager;
    private InputManager inputManager;
    private MapManager renderer;

    private GameState rotateLeft;
    private GameState rotateRight;
    private GameState moveDown;
    private GameState moveUp;

    private GameState configAction;

    private int numLives;
    private double fuel;
    private boolean gameWon;

    private Image gameWin;
    private Image gameEnd;
    private Image life_1;
    private Image life_2;
    private Image life_3;
    private Image fuelBar;

    // init game, set up managers for getting and updating game assets & getting user input, load game assets
    public void init() {
        super.init();
        setInput();
        setGameOver(false);

        resourceManager = new ResourceManager('Z');
        renderer = new MapManager();
        while(!configAction.isPressed()) {
        	 if (configAction.isPressed()) break;
        }
        renderer.setBackground(resourceManager.loadImage("background.png"));
        map = resourceManager.loadNextMap();

        life_1 = resourceManager.loadImage("littleShip.png");
        life_2 = resourceManager.loadImage("littleShip.png");
        life_3 = resourceManager.loadImage("littleShip.png");
        fuelBar = resourceManager.loadImage("health_20.png");

        gameWin = resourceManager.loadImage("gameWin.png");
        gameEnd = resourceManager.loadImage("gameOver.png");

        // initialized game state values for player
        fuel = 20;
        numLives = 3;
        gameWon = false;
    }


    public void stop() {
        super.stop();
    }

    // map user input to modify player state appropriately, use game state and input manager classes
    private void setInput() {
        rotateLeft = new GameState("rotateLeft");
        rotateRight = new GameState("rotateRight");
        moveDown = new GameState("moveDown");
        moveUp = new GameState("moveUp");

        configAction = new GameState("config", GameState.DETECT_INITAL_PRESS_ONLY);

        inputManager = new InputManager(screen.getWindow());
        inputManager.setCursor(InputManager.INVISIBLE_CURSOR);
        inputManager.mapToKey(rotateLeft, KeyEvent.VK_LEFT);
        inputManager.mapToKey(rotateRight, KeyEvent.VK_RIGHT);
        inputManager.mapToKey(moveDown, KeyEvent.VK_DOWN);
        inputManager.mapToKey(moveUp, KeyEvent.VK_UP);

        inputManager.mapToKey(configAction, KeyEvent.VK_SPACE);
    }

    // manage current view 
    public void draw(Graphics2D g) {
        renderer.draw(g, map, screen.getWidth(), screen.getHeight());
        drawHUD(g);

        if (isGameOver()) {
            drawGameOver(g);
        }

        if (gameWon) {
        	drawGamewin(g);
        }
    }

    private void drawGamewin(Graphics2D g) {
    	g.drawImage(gameWin, 0, 0, null);
    }

    public boolean isCollided() {
    	return collided;
    }

    // update views that are always on screen and change on player state (lives remaining, fuel)
    private void drawHUD(Graphics2D g) {
    	g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    	if (numLives == 3) {
    		g.drawImage(life_1, 840, 30, null);
    		g.drawImage(life_2, 876, 30, null);
    		g.drawImage(life_3, 912, 30, null);

    	} else if (numLives == 2) {
    		g.drawImage(life_1, 840, 30, null);
    		g.drawImage(life_2, 876, 30, null);

    	} else if (numLives == 1) {
    		g.drawImage(life_1, 840, 30, null);
    	}

    	g.drawImage(fuelBar, 660, 50, null);

    	long tmp = (long)fuel;
    	if (tmp % 1 == 0 && tmp > 0)
    		fuelBar = resourceManager.loadImage("health_" +tmp+".png");
    }


    private void drawGameOver(Graphics2D g2) {
  	  g2.drawImage(gameEnd, 0, 0, null);
    }

    public TileMap getMap() {
        return map;
    }

    // obtain collision coordinates between sprite and tiles
    public Point getTileCollision(Sprite sprite, double newX, double newY) {
        double fromX = Math.min(sprite.getX(), newX);
        double fromY = Math.min(sprite.getY(), newY);
        double toX = Math.max(sprite.getX(), newX);
        double toY = Math.max(sprite.getY(), newY);

        int fromTileX = MapManager.pixelsToTiles((int)fromX);
        int fromTileY = MapManager.pixelsToTiles((int)fromY);
        int toTileX = MapManager.pixelsToTiles((int)toX + sprite.getWidth() - 1);
        int toTileY = MapManager.pixelsToTiles((int)toY + sprite.getHeight() - 1);

        for (int x = fromTileX; x <= toTileX; x++) {
            for (int y = fromTileY; y <= toTileY; y++) {
                if (x < 0 || x >= map.getWidth() || map.getTile(x, y) != null) {
                	collisionPoint.setLocation(x, y);
                    return collisionPoint;
                }
            }
        }
        return null;
    }

    // update player views and state
    public void updatePlayer(Player player, long elapsedTime) {
        // change player position on input
        if (rotateLeft.isPressed()) {
            player.update(elapsedTime, 3);
        } else if (rotateRight.isPressed()) {
            player.update(elapsedTime, 4);
        } else if (moveUp.isPressed() && fuel > 0) {
        	  player.update(elapsedTime, 1);
        } else if (moveDown.isPressed() && fuel > 0) {
            player.update(elapsedTime, 2);
        } else {
            player.update(elapsedTime, 0);
        }

        // handle collisions, update player & game state
        double dy =  player.getVelocityY();
  	    double oldY = player.getY();
  	    double nextY = oldY + dy * elapsedTime;
  	    
  	    

  	     Point tile = getTileCollision(player, player.getX(), nextY);
  	     if (tile != null) {
  		       if (dy > 0) {
  			          player.setY(MapManager.tilesToPixels(tile.y) - player.getHeight());
  		       } else if (dy < 0) {
  			          player.setY(MapManager.tilesToPixels(tile.y) + player.getHeight());
  		       }
  		       player.stop();
             // verify if player has collided with landing area
  		       if (checkWin(player)) {
  			          manageNextLevel(player);
  		       } else {
  			          player.setState(1);
  		       }
  		   }
    }

    // landing location is currently located at the same tiles in every map
    public boolean checkWin(Player player) {
		    int minTile = MapManager.tilesToPixels(86);
		    int maxTile = MapManager.tilesToPixels(91);

		    if (player.getX() > minTile && player.getX() < maxTile) {
			       return true;
		    } else {
			       return false;
		    }
	  }

    // update map and level after player has won a given level, load next map or set game to win
	  public void manageNextLevel(Player player) {
		    if (resourceManager.getCurrentMap() == 3) {
			       gameWon = true;
		    } else if (resourceManager.getCurrentMap() < 3)  {
			       resourceManager.incrementMap();
			       map = resourceManager.loadNextMap();
			       numLives = 3;
			       fuel = 20;
		    }
	  }

    // directly update player state
    public void update(long elapsedTime) {
        Player player = (Player) map.getPlayer();
        updatePlayer(player, elapsedTime);
        fuel = fuel - DEC_FUEL;

        // state associated with collision, update lives & fuel, determine if game over or level reset
        if (player.getState() == 1) {
            this.numLives--;
            if (numLives == 0) {
                setGameOver(true);
            } else {
            	  setCollided(true);
                map = resourceManager.reloadMap();
                fuel = 20;
            }
        }
    }


    public boolean isGameOver() {
        return gameOver;
    }


    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }


    public void setCollided(boolean val) {
    	collided = val;
    }

}

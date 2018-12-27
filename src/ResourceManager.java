import java.awt.Image;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.swing.ImageIcon;
/*
Handles the retrieval and storage of game assets, updating game assets, reading files
*/
public class ResourceManager {
    private ArrayList<Image> tiles;
    private int currentMap;
    private Sprite playerSprite;
    private AnimatedSprite[] animArray = new AnimatedSprite[2];

    public ResourceManager(char lastTileID) {
        loadTileImages(lastTileID);
        loadAnimation();
        currentMap = 1;
    }

    //read and load images (tiles, sprites, displays)
    public Image loadImage(String name) {
        String path = "Assets/images/" + name;
        return new ImageIcon(getClass().getResource(path)).getImage();
    }


    public void incrementMap() {
  	   currentMap++;
    }

    //update map in accordance to current level
    public TileMap loadNextMap() {
        TileMap map = null;
        while (map == null) {
            try {
                map = loadMap("map" + currentMap + ".txt");
            } catch (IOException ex) { }
        }
        initPlayer(map);
        return map;
    }

    //initialize player values and position for map, link player to map
    public void initPlayer(TileMap map) {
    	 Player player = new Player(animArray[0], animArray[1]);
         player.setX(MapManager.tilesToPixels(6));
         player.setY(128);
         map.setPlayer(player);
    }

    //update map
    public TileMap reloadMap() {
        try {
            return loadMap( "map" + currentMap + ".txt" );
        } catch (IOException ex) {
            return null;
        }
    }

    //strategy for reading tilemap text files, text files create character to tile mappings
    private TileMap loadMap(String name) throws IOException {
        String filename = "Levels/" + name;
        ArrayList<String> lines = new ArrayList<String>();
        int width = 0;
        int height = 0;
        BufferedReader reader;

        //read from text file, generate tilemap with coordinates based on row and column dimensions of file
        try {
            reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(filename)));
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    reader.close();
                    break;
                }
                if (!line.startsWith("#")) {
                    lines.add(line);
                    width = Math.max(width, line.length());
                }
            }

            height = lines.size();
            TileMap newMap = new TileMap(width, height);

            //retrieve tile images and set to map
            for (int y = 0; y < height; y++) {
                String line = (String) lines.get(y);

                for (int x = 0; x < line.length(); x++) {
                    char ch = line.charAt(x);
                    int tile = ch - 'A';
                    if (tile >= 0 && tile < tiles.size()) {
                        newMap.setTile(x, y, (Image) tiles.get(tile));
                    }
                }
            }
            initPlayer(newMap);
            return newMap;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    public int getCurrentMap() {
    	return currentMap;
    }

    //retrieve and load tiles from assets folder, store to be rendered
    public void loadTileImages(char max) {
        tiles = new ArrayList<Image>();
        char ch = 'A';
        while ( ch <= max ) {
            String name = "tile_" + ch + ".png";
            tiles.add(loadImage(name));
            ch++;
        }
    }


    public void loadAnimation() {
        long t = 30;
    	  AnimatedSprite shipRight = new AnimatedSprite();
    	  shipRight.addFrame(loadImage("ship_r1.png"), t);
    	  shipRight.addFrame(loadImage("ship_r2.png"), t);
    	  shipRight.addFrame(loadImage("ship_r3.png"), t);
    	  animArray[0] = shipRight;

    	  AnimatedSprite shipLeft = new AnimatedSprite();
    	  shipLeft.addFrame(loadImage("ship_l1.png"), t);
    	  shipLeft.addFrame(loadImage("ship_l2.png"), t);
    	  shipLeft.addFrame(loadImage("ship_l3.png"), t);
    	  animArray[1] = shipLeft;

    	  playerSprite = new Sprite(animArray[0]);
    }
}

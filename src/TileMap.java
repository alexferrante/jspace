 import java.awt.Image;
import java.util.Iterator;
import java.util.LinkedList;
/*
Defines the mapping used for each level
*/
public class TileMap {
    private Image[][] tiles;
    private LinkedList<Sprite> sprites;
    private Sprite player;

    // image array set by loaded tile map file and actual resolution of tiles, store for retrieval
    public TileMap(int width, int height) {
        tiles = new Image[width][height];
        sprites = new LinkedList<Sprite>();
    }

    public Image getTile(int x, int y) {
        if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) {
            return null;
        } else {
            return tiles[x][y];
        }
    }

    public int getWidth() {
        return tiles.length;
    }

    public int getHeight() {
        return tiles[0].length;
    }

    public void setTile(int x, int y, Image tile) {
        tiles[x][y] = tile;
    }

    public Sprite getPlayer() {
        return player;
    }

    public void setPlayer(Sprite player) {
        this.player = player;
    }

    public void addSprite(Sprite sprite) {
        sprites.add(sprite);
    }

    public void removeSprite(Sprite sprite) {
        sprites.remove(sprite);
    }

    public Iterator getSprites() {
        return sprites.iterator();
    }  
}

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Iterator;
/*
Render tile map and sprites
*/
public class MapManager {
    private static final int TILE_SIZE = 16;
    private static final int TILE_BITS = 4;
    private Image background;

    // render tile map, get offsets based on defined screen resolution and tile size
    public void draw(Graphics2D g, TileMap map, int screenWidth, int screenHeight) {
        Sprite player = map.getPlayer();
        int mapWidth = tilesToPixels(map.getWidth());

        int offsetX = screenWidth / 2 - Math.round((int)player.getX()) - TILE_SIZE;
        offsetX = Math.min(offsetX, 0);
        offsetX = Math.max(offsetX, screenWidth - mapWidth);

        int offsetY = screenHeight - tilesToPixels(map.getHeight());
        // display background, use background dimensions and screen definitions to render the map
        if (background != null) {
            int x = offsetX * (screenWidth - background.getWidth(null)) / (screenWidth - mapWidth);
            int y = screenHeight - background.getHeight(null);
            g.drawImage(background, x, y, null);
        }

        if (background == null || screenHeight > background.getHeight(null)) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, screenWidth, screenHeight);
        }
        // get positioning of first and last tiles of map, use as limits for the rest of the tiles
        int firstTileX = pixelsToTiles(-offsetX);
        int lastTileX = firstTileX + pixelsToTiles(screenWidth) + 1;

        // iterate through the map, retrieve tile images
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = firstTileX; x <= lastTileX; x++) {
                Image image = map.getTile(x, y);
                if (image != null) {
                    g.drawImage(image,tilesToPixels(x) + offsetX, tilesToPixels(y) + offsetY, null);
                }
            }
        }
        // add player to map, render remaining sprites
        g.drawImage(player.getImage(), Math.round((int)player.getX()) + offsetX, Math.round((int)player.getY()) +
        			      offsetY, null);

        Iterator i = map.getSprites();
        while (i.hasNext()) {
            Sprite sprite = (Sprite)i.next();
            int x = Math.round((int)sprite.getX()) + offsetX;
            int y = Math.round((int)sprite.getY()) + offsetY;
            g.drawImage(sprite.getImage(), x, y, null);
       }
    }

    // bitwise shift to tiles, get tile coordinates from pixel coordinates
    public static int pixelsToTiles(int pixels) {
        return pixels >> TILE_BITS;
    }

    public static int pixelsToTiles(float pixels) {
        return pixelsToTiles(Math.round(pixels));
    }

    // bitwise shift to pixels, get pixel coordinates from tile coordinates
    public static int tilesToPixels(int numTiles) {
        return numTiles << TILE_BITS;
    }


    public void setBackground(Image background) {
        this.background = background;
    }
}

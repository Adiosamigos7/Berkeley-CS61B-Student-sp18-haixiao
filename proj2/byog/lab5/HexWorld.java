package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;

    private static final long SEED = 23;
    private static final Random RANDOM = new Random(SEED);

    /** draw hexgon based on the input size. */
    public static void addHexagon (TETile[][] tiles, int posx, int posy, int hexsize) {
        TETile t = randomTile();
        int hexheight = hexsize * 2;
        int hexwidth = hexsize * 3 - 2;
        for (int x = 0; x < hexwidth; x += 1) {
            for (int y = 0; y < hexheight; y += 1) {
                int a = x;
                int b = y;
                if (a > hexwidth / 2) {
                    a = hexwidth - a - 1;
                }
                if (b > hexheight / 2) {
                    b = hexheight - b - 1;
                }
                if (tiles[x + posx][y + posy] == Tileset.NOTHING) {
                    if (a + b <= hexsize - 2) {
                        tiles[x + posx][y + posy] = Tileset.NOTHING;
                    } else {
                        tiles[x + posx][y + posy] = t;
                    }
                }
            }
        }
    }

    /** add method for random colors **/
    /**add method for random patterns **/
    /** Add method to locate different hexagons.
     *
     * ultimately should take param world.height, world.width, hexagon size
     * for now, assume 19 total size-3 hexagons.
     */
    private static Integer[][] hexlocator(int width, int height, int hexsize) {
//        Integer[][] locations = new Integer[19][2];
        int numhexvertical =  height / hexsize / 2;
        int numhexhorizontal = (width - hexsize) / (2 * hexsize - 1);

        int numhexes = numhexhorizontal;
        for (int x = 0; x < numhexhorizontal / 2; x++) {
            numhexes += (numhexhorizontal - x - 1) * 2;
        }
        Integer[][] locations = new Integer[numhexes][2];
        locations[0][1] = 0;
        locations[0][0] = width / 2 - hexsize - 1;
        int track = 1;
        /** middle column */
        for (int i = 0; i < numhexhorizontal - 1; i++) {
            locations[track][0] = locations[track - 1][0];
            locations[track][1] = locations[track - 1][1] + 2 * hexsize;
            track++;
        }
        /** next to middle column */
        int xpos1 = locations[0][0] - (2 * hexsize - 1);
        int xpos2 = locations[0][0] + (2 * hexsize - 1);
        for (int i = 0; i < numhexhorizontal - 1; i++) {
            locations[track][0] = xpos1;
            locations[track][1] = hexsize * (2 * i + 1);
            track++;
            locations[track][0] = xpos2;
            locations[track][1] = hexsize * (2 * i + 1);
            track++;
        }
        /** next to middle column */
        xpos1 = locations[0][0] - 2 * (2 * hexsize - 1);
        xpos2 = locations[0][0] + 2 * (2 * hexsize - 1);
        for (int i = 0; i < numhexhorizontal - 2; i++) {
            locations[track][0] = xpos1;
            locations[track][1] = hexsize * (2 * i + 2);
            track++;
            locations[track][0] = xpos2;
            locations[track][1] = hexsize * (2 * i + 2);
            track++;
        }
        return locations;
    }

    /** Picks a RANDOM tile with a 33% change of being
     *  a wall, 33% chance of being a flower, and 33%
     *  chance of being GRASS.
     */
    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(8);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.GRASS;
            case 3: return Tileset.WATER;
            case 4: return Tileset.SAND;
            case 5: return Tileset.TREE;
            case 6: return Tileset.MOUNTAIN;
            case 7: return Tileset.LOCKED_DOOR;
            default: return Tileset.FLOWER;
        }
    }
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        int width = 29;
        int height = 30;
        int hexsize = 3;
        int numhexvertical =  height / hexsize / 2;
        int numhexhorizontal = (width - hexsize) / (2 * hexsize - 1);
        ter.initialize(width, height);
        /** calculate the positions of hexagons. */
        Integer[][] locations = hexlocator(width, height, hexsize);
        /** declare the tiles for the entire map. */
        TETile[][] map = new TETile[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                map[i][j] = Tileset.NOTHING;
            }
        }
        for (int i = 0; i < locations.length; i++) {
            addHexagon(map, locations[i][0], locations[i][1], hexsize);
        }

        ter.renderFrame(map);
    }

}

package byog.Core;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.*;
import java.util.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;




public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public static final long SEED = 4;
    public static final TETile[][] tiles = new TETile[WIDTH][HEIGHT];;
    private static final Random RANDOM = new Random(SEED);
    /** the method of world generation comes from:
     * @source: https://gamedevelopment.tutsplus.com/tutorials/
     * how-to-use-bsp-trees-to-generate-game-maps--gamedev-12268
     * Which implements binary space partitioning.
     */
    private ArrayList<Leaf> world;
    /** maximum leaf size. */
    private final int maxleafsize = 20;
    /** minimum leaf size */
    private final int minleafsize = 6;
    /** private class of binary spacing partitioning. */

    private class Leaf {
        public String name;
        /** position and size of the leaf */
        public int x, y, width, height;
        /** left and right child of the leaf. */
        public Leaf leftChild, rightChild;
        /** room inside the leaf */
        /** TODO: make sure how to use Rectangle. */
        public Rectangle room;
        /** hallways to connect this leaf to other leafs */
        public ArrayList<Rectangle> halls;
        /** constructor */
        public Leaf(int xpos, int ypos, int w, int h, String s) {
            this.x = xpos;
            this.y = ypos;
            this.width = w;
            this.height = h;
            this.name = s;
        }

        /** Split a leaf. If successfully split, return true. */
        public boolean split() {
            /** if already split, return false */
            if (this.leftChild != null || this.rightChild != null) {
                return false;
            }
            /** Determine the direction of split.
             *  If the width is >25% larger than height, we split vertically.
             *  if the height is >25% larger than the width, we split horizontally.
             *  Otherwise we split randomly.
             */
            boolean splitH = RANDOM.nextBoolean();
            if (width / height >= 1.25) {
                splitH = false;
            } else if (height / width >= 1.25) {
                splitH = true;
            }
            /** determine the maximum height or width. */
            int max;
            if (splitH) {
                max = height - minleafsize;
            } else {
                max = width - minleafsize;
            }
            if (max <= minleafsize) {
                return false;
            }
            /** Determine where to split */
            int split = RANDOM.nextInt(max - minleafsize);
            split += minleafsize;
            if (splitH) {
                this.leftChild = new Leaf(this.x, this.y, width, split, this.name.concat("-hl"));
                this.rightChild = new Leaf(this.x, this.y + split, width, height - split, this.name.concat("-hr"));
            } else {
                this.leftChild = new Leaf(this.x, this.y, split, height, this.name.concat("-vl"));
                this.rightChild = new Leaf(this.x + split, this.y, width - split, height, this.name.concat("-vr"));
            }
            return true;
        }

        /** method to create rooms. */
        public void createRooms() {
            if (this.leftChild != null || this.rightChild != null) {
                if (this.leftChild != null) {
                    this.leftChild.createRooms();
                }
                if (this.rightChild != null) {
                    this.rightChild.createRooms();
                }
                /** if there are both left and right children in this Leaf, create a hallway between  */
                if (leftChild != null && rightChild != null) {
                    createHall(leftChild.getRoom(), rightChild.getRoom());
                }
            } else {
                /** leaf does not have child. Ready to make a room. */
                Point roomSize = new Point(RANDOM.nextInt(this.width - minleafsize + 1) + minleafsize / 2,
                                        RANDOM.nextInt(this.height - minleafsize + 1) + minleafsize / 2);
                Point roomPos = new Point(RANDOM.nextInt(width - roomSize.x - 2) + 1,
                                            RANDOM.nextInt(height - roomSize.y - 2) + 1);
                this.room = new Rectangle(x + roomPos.x, y + roomPos.y, roomSize.x, roomSize.y);
                this.drawroom();
            }
        }


        /** method to convert room to tiles */
        public void drawroom() {
            for(int tx = this.room.x; tx <= this.room.x + this.room.width; tx++) {
                for(int ty = this.room.y; ty <= this.room.y + this.room.height; ty++) {
                    if (tx == this.room.x || tx == this.room.x + this.room.width ||
                        ty == this.room.y || ty == this.room.y + this.room.height) {
                        tiles[tx][ty] = Tileset.WALL;
                    } else {
                        tiles[tx][ty] = Tileset.FLOOR;
                    }
                }
            }
        }

        /** method to iterate from any leaf into one of the rooms inside. For creating hall.*/
        public Rectangle getRoom() {
            /** iterate all the way thorugh leafs to find a room if exists. */
            if (this.room != null) {
                return this.room;
            } else {
                Rectangle lRoom = new Rectangle();
                lRoom = null;
                Rectangle rRoom = new Rectangle();
                rRoom = null;
                if (this.leftChild != null) {
                    lRoom = this.leftChild.getRoom();
                }
                if (this.rightChild != null) {
                    rRoom = this.rightChild.getRoom();
                }
                if (lRoom == null && rRoom == null) {
                    return null;
                } else if (lRoom == null) {
                    return rRoom;
                } else if (rRoom == null) {
                    return lRoom;
                } else if (RANDOM.nextDouble() > 0.5) {
                    return lRoom;
                } else {
                    return rRoom;
                }
            }
        }

        /** method to create one or two two-tile-thick rectangles (hallway) that connects two rooms together.
         * For now, just draw straight line or right angle line to connect.
         * Can be improved later.*/
        public void createHall(Rectangle l, Rectangle r) {
            halls = new ArrayList<Rectangle>();
            int hallwidth = 1;
            int p1x = l.x + 1, p1y = l.y + 1, p2x = r.x + 1, p2y = r.y + 1;
            if (l.width > 3) {
                p1x += RandomUtils.uniform(RANDOM,1 , l.width - 2);
            }
            if (l.height > 3) {
                p1y += RandomUtils.uniform(RANDOM, 1, l.height - 2);
            }
            if (r.width > 3) {
                p2x += RandomUtils.uniform(RANDOM,1 , r.width - 2);
            }
            if (r.height > 3) {
                p2y += RandomUtils.uniform(RANDOM, 1, r.height - 2);
            }
            int w = p2x - p1x;
            int h = p2y - p1y;
            int abw = Math.abs(w) + 1;
            int abh = Math.abs(h) + 1;
            Rectangle h1, h2;
            h1 = null;
            h2 = null;
            if (w < 0) {
                if (h < 0) {
                    if (RANDOM.nextDouble() < 0.5) {
                        h1 = new Rectangle(p2x, p1y, abw, hallwidth);
                        h2 = new Rectangle(p2x, p2y, hallwidth, abh);
//                        h1 = new Rectangle(r.x + r.width - 1, p1y, l.x - r.x - r.width + 2, hallwidth);
//                        h2 = new Rectangle(p2x, r.y + r.height - 1, hallwidth, p1y - r.y - r.height + 2);
                    } else {
                        h1 = new Rectangle(p2x, p2y, abw, hallwidth);
                        h2 = new Rectangle(p1x, p2y, hallwidth, abh);
//                        h1 = new Rectangle(r.x + r.width - 1, p2y, p1x - r.x - r.width + 2, hallwidth);
//                        h2 = new Rectangle(p1x, p2y - 1, hallwidth, l.y - p2y + 2);
                    }
                } else if (h > 0) {
                    if (RANDOM.nextDouble() < 0.5) {
                        h1 = new Rectangle(p2x, p2y, abw, hallwidth);
                        h2 = new Rectangle(p1x, p1y, hallwidth, abh);
//                        h1 = new Rectangle(r.x + r.width - 1, p2y, p1x - r.x - r.width + 2, hallwidth);
//                        h2 = new Rectangle(p1x, l.y + l.height - 1, hallwidth, p2y - l.y - l.height + 2);
                    } else {
                        h1 = new Rectangle(p2x, p1y, abw, hallwidth);
                        h2 = new Rectangle(p2x, p1y, hallwidth, abh);
//                        h1 = new Rectangle(p2x - 1, p1y, l.x - p2x + 2, hallwidth);
//                        h2 = new Rectangle(p2x, p1y - 1, hallwidth, r.y - p1y + 2);

                    }
                } else {
                    h1 = new Rectangle(p2x, p2y, abw, hallwidth);
//                    h1 = new Rectangle(r.x + r.width - 1, p2y, l.x - r.x - r.width + 2, hallwidth);
                }
            } else if (w > 0) {
                if (h < 0) {
                    if (RANDOM.nextDouble() < 0.5) {
                        h1 = new Rectangle(p1x, p2y, abw, hallwidth);
                        h2 = new Rectangle(p1x, p2y, hallwidth, abh);
//                        h1 = new Rectangle(p1x - 1, p2y, r.x - p1x + 2, hallwidth);
//                        h2 = new Rectangle(p1x, p2y - 1, hallwidth, l.y - p2y + 2);
                    } else {
                        h1 = new Rectangle(p1x, p1y, abw, hallwidth);
                        h2 = new Rectangle(p2x, p2y, hallwidth, abh);
//                        h1 = new Rectangle(l.x  + l.width - 1, p1y, p2x - l.x - l.width + 2, hallwidth);
//                        h2 = new Rectangle(p2x, r.y + r.height - 1, hallwidth, p1y - r.y - r.height + 2);
                    }
                } else if (h > 0) {
                    if (RANDOM.nextDouble() < 0.5) {
                        h1 = new Rectangle(p1x, p1y, abw, hallwidth);
                        h2 = new Rectangle(p2x, p1y, hallwidth, abh);
//                        h1 = new Rectangle(l.x + l.width - 1, p1y, p2x - l.x - l.width + 2, hallwidth);
//                        h2 = new Rectangle(p2x, p1y - 1, hallwidth, r.height - p1y + 2);
                    } else {
                        h1 = new Rectangle(p1x, p2y, abw, hallwidth);
                        h2 = new Rectangle(p1x, p1y, hallwidth, abh);
//                        h1 = new Rectangle(p1x - 1, p2y, r.x - p1x + 1, hallwidth);
//                        h2 = new Rectangle(p1x, l.y + l.height - 1, hallwidth, p2y - l.y - l.height + 2);
                    }
                } else {
                    h1 = new Rectangle(p1x, p1y, abw, hallwidth);
//                    h1 = new Rectangle(l.x + l.width - 1, p2y, r.x - l.x - l.width + 2, hallwidth);
                }
            } else {
                if (h < 0) {
                    h2 = new Rectangle(p2x, p2y, hallwidth, abh);
//                    h2 = new Rectangle(p2x, p2y + r.height - 1, hallwidth, l.y - r.height - r.y + 2);
                } else if (h > 0) {
                    h2 = new Rectangle(p1x, p1y, hallwidth, abh);
//                    h2 = new Rectangle(p2x, p1y + l.height - 1, hallwidth, r.y - l.height - l.y + 2);
                }
            }
            if (h1 != null) {
                drawhall(h1, "h");
                halls.add(h1);
            }
            if (h2 != null) {
                drawhall(h2, "v");
                halls.add(h2);
            }
        }
        /** method to convert hall to tiles */
        public void drawhall(Rectangle h, String s) {
            if (s == "h") {
                int ty = h.y;
                for (int tx = h.x; tx < h.x + h.width; tx++) {
                    tiles[tx][ty] = Tileset.FLOOR;
                }
                for (int tx = h.x - 1; tx <= h.x + h.width; tx++) {
                    if (tiles[tx][ty - 1] == Tileset.NOTHING) {
                        tiles[tx][ty - 1] = Tileset.WALL;
                    }
                    if (tiles[tx][ty + 1] == Tileset.NOTHING) {
                        tiles[tx][ty + 1] = Tileset.WALL;
                    }
                }
            } else if (s == "v") {
                int tx = h.x;
                for (int ty = h.y; ty < h.y + h.height; ty++) {
                    tiles[tx][ty] = Tileset.FLOOR;
                }
                for (int ty = h.y - 1; ty <= h.y + h.height; ty++) {
                    if (tiles[tx - 1][ty] == Tileset.NOTHING) {
                        tiles[tx - 1][ty] = Tileset.WALL;
                    }
                    if (tiles[tx + 1][ty] == Tileset.NOTHING) {
                        tiles[tx + 1][ty] = Tileset.WALL;
                    }
                }
            }
        }


        /** for tests - print leafs. */
        public void printleaf() {
            System.out.println("Leaf " + this.name + ": XY Position: (" + x + ", " + y +
                                "), Width: " + width + ", Height: " + height + ".");
        }
    }



    /**initialize world tiles */
    private void tileInit() {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                tiles[i][j] = Tileset.NOTHING;
            }
        }
    }
    /** Construct the world frame */
    public void World() {
        world = new ArrayList<Leaf>();
        /** Create a root of all leafs. */
        Leaf root = new Leaf(0,0, WIDTH, HEIGHT,"root");
        world.add(root);
        tileInit();
        boolean issplit = true;
        /** loop through every leaf in the ArrayList over and over again,
         * until no more leafs.
         */
        while (issplit) {
            issplit = false;
            for (int i = 0; i < world.size(); i++) {
                Leaf l = world.get(i);
                if (l.leftChild == null && l.rightChild == null) {
                    if (l.width > maxleafsize || l.height > maxleafsize || RANDOM.nextDouble() > 0.25) {
                        if (l.split()) {
                            world.add(l.leftChild);
                            world.add(l.rightChild);
                            issplit = true;
                        }
                    }
                }
            }
        }
        root.createRooms();

        /** hard fix of hallway boundary conditions. */
//        fixhallways();

    }

    /** Hard fix the boundary conditions of hallways. */
    private void fixhallways() {
        for(int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (tiles[i][j] == Tileset.FLOWER) {
                    if (tiles[i-1][j] == Tileset.NOTHING || tiles[i+1][j] == Tileset.NOTHING ||
                            tiles[i][j+1] == Tileset.NOTHING || tiles[i][j-1] == Tileset.NOTHING) {
                        tiles[i][j] = Tileset.WALL;
                    } else {
                        tiles[i][j] = Tileset.FLOOR;
                    }
                }
            }
        }
    }

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().

        TETile[][] finalWorldFrame = null;
        return finalWorldFrame;
    }
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);


        Game g = new Game();
        g.World();
        for (Leaf s : g.world) {
            s.printleaf();
        }

        Set<String> checker = new HashSet<String>();
        for (Leaf s: g.world) {
            /** Check if any duplicate leafs. */
            if (checker.add(s.name) == false) {
                System.out.println("Duplicate Leaf:" + s.name);
            }
            /** Check if any leaf is illegal. */
            if (s.width < g.minleafsize || s.width < g.minleafsize) {
                System.out.println("Too small Leaf:");
                s.printleaf();
            }
            /** Check if any big leafs without child. */
            if (s.leftChild == null || s.rightChild == null) {
                if (s.width > g.minleafsize * 2 || s.height > g.minleafsize * 2) {
                    System.out.println("too big leafs without child:");
                    s.printleaf();
                }
            }
        }

        ter.renderFrame(g.tiles);
    }

}

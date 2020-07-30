package byog.Core;

import java.awt.Rectangle;
import java.awt.Point;
import java.util.Random;
import java.util.ArrayList;


import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;




public class Game {
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public static final long SEED = 4;
    public static final TETile[][] TILES = new TETile[WIDTH][HEIGHT];;
    private static Random RANDOM;
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
        /** position and size of the leaf */
        private int x, y, width, height;
        /** left and right child of the leaf. */
        private Leaf leftChild, rightChild;
        /** room inside the leaf */
        private Rectangle room;
        /** hallways to connect this leaf to other leafs */
        private ArrayList<Rectangle> halls;
        /** constructor */
        public Leaf(int xpos, int ypos, int w, int h) {
            this.x = xpos;
            this.y = ypos;
            this.width = w;
            this.height = h;
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
                this.leftChild = new Leaf(this.x, this.y, width, split);
                this.rightChild = new Leaf(this.x, this.y + split, width, height - split);
            } else {
                this.leftChild = new Leaf(this.x, this.y, split, height);
                this.rightChild = new Leaf(this.x + split, this.y, width - split, height);
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
                /** if there are both left and right children in this Leaf,
                 * create a hallway between leafs. */
                if (leftChild != null && rightChild != null) {
                    createHall(leftChild.getRoom(), rightChild.getRoom());
                }
            } else {
                /** leaf does not have child. Ready to make a room. */
                Point roomSize = new Point(RANDOM.nextInt(this.width - minleafsize + 1)
                        + minleafsize / 2,
                                        RANDOM.nextInt(this.height - minleafsize + 1)
                                                + minleafsize / 2);
                Point roomPos = new Point(RANDOM.nextInt(width - roomSize.x - 2) + 1,
                                            RANDOM.nextInt(height - roomSize.y - 2) + 1);
                this.room = new Rectangle(x + roomPos.x, y + roomPos.y, roomSize.x, roomSize.y);
                this.drawroom();
            }
        }


        /** method to convert room to tiles */
        public void drawroom() {
            for (int tx = this.room.x; tx <= this.room.x + this.room.width; tx++) {
                for (int ty = this.room.y; ty <= this.room.y + this.room.height; ty++) {
                    if (tx == this.room.x || tx == this.room.x + this.room.width
                            || ty == this.room.y || ty == this.room.y + this.room.height) {
                        TILES[tx][ty] = Tileset.WALL;
                    } else {
                        TILES[tx][ty] = Tileset.FLOOR;
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

        /** method to create one or two two-tile-thick rectangles (hallway)
         *  that connects two rooms together.
         * For now, just draw straight line or right angle line to connect.
         * Can be improved later.*/
        public void createHall(Rectangle l, Rectangle r) {
            halls = new ArrayList<Rectangle>();
            int hallwidth = 1;
            int p1x = l.x + 1, p1y = l.y + 1, p2x = r.x + 1, p2y = r.y + 1;
            if (l.width > 3) {
                p1x += RandomUtils.uniform(RANDOM, 1, l.width - 2);
            }
            if (l.height > 3) {
                p1y += RandomUtils.uniform(RANDOM, 1, l.height - 2);
            }
            if (r.width > 3) {
                p2x += RandomUtils.uniform(RANDOM, 1, r.width - 2);
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
                    } else {
                        h1 = new Rectangle(p2x, p2y, abw, hallwidth);
                        h2 = new Rectangle(p1x, p2y, hallwidth, abh);
                    }
                } else if (h > 0) {
                    if (RANDOM.nextDouble() < 0.5) {
                        h1 = new Rectangle(p2x, p2y, abw, hallwidth);
                        h2 = new Rectangle(p1x, p1y, hallwidth, abh);
                    } else {
                        h1 = new Rectangle(p2x, p1y, abw, hallwidth);
                        h2 = new Rectangle(p2x, p1y, hallwidth, abh);

                    }
                } else {
                    h1 = new Rectangle(p2x, p2y, abw, hallwidth);
                }
            } else if (w > 0) {
                if (h < 0) {
                    if (RANDOM.nextDouble() < 0.5) {
                        h1 = new Rectangle(p1x, p2y, abw, hallwidth);
                        h2 = new Rectangle(p1x, p2y, hallwidth, abh);
                    } else {
                        h1 = new Rectangle(p1x, p1y, abw, hallwidth);
                        h2 = new Rectangle(p2x, p2y, hallwidth, abh);
                    }
                } else if (h > 0) {
                    if (RANDOM.nextDouble() < 0.5) {
                        h1 = new Rectangle(p1x, p1y, abw, hallwidth);
                        h2 = new Rectangle(p2x, p1y, hallwidth, abh);
                    } else {
                        h1 = new Rectangle(p1x, p2y, abw, hallwidth);
                        h2 = new Rectangle(p1x, p1y, hallwidth, abh);
                    }
                } else {
                    h1 = new Rectangle(p1x, p1y, abw, hallwidth);
                }
            } else {
                if (h < 0) {
                    h2 = new Rectangle(p2x, p2y, hallwidth, abh);
                } else if (h > 0) {
                    h2 = new Rectangle(p1x, p1y, hallwidth, abh);
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
            if (s.equals("h")) {
                int ty = h.y;
                for (int tx = h.x; tx < h.x + h.width; tx++) {
                    TILES[tx][ty] = Tileset.FLOOR;
                }
                for (int tx = h.x - 1; tx <= h.x + h.width; tx++) {
                    if (TILES[tx][ty - 1] == Tileset.NOTHING) {
                        TILES[tx][ty - 1] = Tileset.WALL;
                    }
                    if (TILES[tx][ty + 1] == Tileset.NOTHING) {
                        TILES[tx][ty + 1] = Tileset.WALL;
                    }
                }
            } else if (s.equals("v")) {
                int tx = h.x;
                for (int ty = h.y; ty < h.y + h.height; ty++) {
                    TILES[tx][ty] = Tileset.FLOOR;
                }
                for (int ty = h.y - 1; ty <= h.y + h.height; ty++) {
                    if (TILES[tx - 1][ty] == Tileset.NOTHING) {
                        TILES[tx - 1][ty] = Tileset.WALL;
                    }
                    if (TILES[tx + 1][ty] == Tileset.NOTHING) {
                        TILES[tx + 1][ty] = Tileset.WALL;
                    }
                }
            }
        }
    }

    /**initialize world tiles */
    private void tileInit() {
        for (int i = 0; i < TILES.length; i += 1) {
            for (int j = 0; j < TILES[0].length; j += 1) {
                TILES[i][j] = Tileset.NOTHING;
            }
        }
    }
    /** Construct the world frame */
    public TETile[][] world(long seed) {
        RANDOM = new Random(seed);
        world = new ArrayList<Leaf>();
        Leaf root = new Leaf(0, 0, WIDTH, HEIGHT);
        world.add(root);
        tileInit();
        boolean issplit = true;
        /** loop through every leaf in the ArrayList over and over again,
         * until no more leafs. */
        while (issplit) {
            issplit = false;
            for (int i = 0; i < world.size(); i++) {
                Leaf l = world.get(i);
                if (l.leftChild == null && l.rightChild == null) {
                    if (l.width > maxleafsize || l.height > maxleafsize
                            || RANDOM.nextDouble() > 0.25) {
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
        return TILES;
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
        long seed = Long.parseLong(input.substring(1, input.length() - 2));
        TETile[][] finalWorldFrame = null;
        Game g = new Game();
        finalWorldFrame = g.world(seed);
        return finalWorldFrame;
    }

}

package byog.Core;

import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Color;
import java.awt.Font;
import java.io.Serializable;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.ArrayList;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;


public class Game implements Serializable {
    /* Feel free to change the width and height. */
    private boolean isfromLoaded = false;
    public static int WIDTH = 80;
    public static int HEIGHT = 30;
    private long SEED;
    transient private final TETile[][] TILES = new TETile[WIDTH][HEIGHT];
    private final char[][] tilechars = new char[WIDTH][HEIGHT];
    private int pxpos, pypos, dxpos, dypos;
    private static Random RANDOM;
    private String tileInfo = "";
    /** the method of world generation comes from:
     * @source: https://gamedevelopment.tutsplus.com/tutorials/
     * how-to-use-bsp-trees-to-generate-game-maps--gamedev-12268
     * Which implements binary space partitioning.
     */
    transient ArrayList<Leaf> world;
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
    public TETile[][] world() {
        RANDOM = new Random(SEED);
        world = new ArrayList<Leaf>();
        Leaf root = new Leaf(0, 0, WIDTH, HEIGHT - 1);
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
        boolean player = false, door = false;

        while (!door) {
            dxpos = RANDOM.nextInt(WIDTH);
            dypos = RANDOM.nextInt(HEIGHT);
            boolean wallset = false, floorset = false;
            if (TILES[dxpos][dypos] == Tileset.WALL) {
                if (dxpos > 1 && dxpos < WIDTH - 1
                        && dypos > 1 && dypos < HEIGHT - 1) {
                    if (TILES[dxpos - 1][dypos] == Tileset.WALL
                            && TILES[dxpos + 1][dypos] == Tileset.WALL) {
                        wallset = true;
                        if (TILES[dxpos][dypos - 1] == Tileset.NOTHING
                        && TILES[dxpos][dypos + 1] == Tileset.FLOOR) {
                            floorset = true;
                        } else if (TILES[dxpos][dypos + 1] == Tileset.NOTHING
                                && TILES[dxpos][dypos - 1] == Tileset.FLOOR) {
                            floorset = true;
                        }
                    } else if (TILES[dxpos][dypos - 1] == Tileset.WALL
                            && TILES[dxpos][dypos + 1] == Tileset.WALL) {
                        wallset = true;
                        if (TILES[dxpos - 1][dypos] == Tileset.NOTHING
                                && TILES[dxpos + 1][dypos] == Tileset.FLOOR) {
                            floorset = true;
                        } else if (TILES[dxpos + 1][dypos] == Tileset.NOTHING
                                && TILES[dxpos - 1][dypos] == Tileset.FLOOR) {
                            floorset = true;
                        }
                    }
                } else if ((dxpos == 1 || dxpos == WIDTH - 1) && dypos > 1 && dypos < HEIGHT - 1) {
                    if (TILES[dxpos][dypos - 1] == Tileset.WALL
                            && TILES[dxpos][dypos + 1] == Tileset.WALL) {
                        wallset = true;
                        if (dxpos == 1 && TILES[dxpos + 1][dypos] == Tileset.FLOOR) {
                            floorset = true;
                        } else if (dxpos == WIDTH - 1 && TILES[dxpos - 1][dypos] == Tileset.FLOOR) {
                            floorset = true;
                        }
                    }
                } else if (dxpos > 1 && dxpos < HEIGHT - 1 && (dypos == 1 || dypos == WIDTH - 1)) {
                    if (TILES[dxpos - 1][dypos] == Tileset.WALL
                            && TILES[dxpos + 1][dypos] == Tileset.WALL) {
                        wallset = true;
                        if (dypos == 1 && TILES[dxpos][dypos + 1] == Tileset.FLOOR) {
                            floorset = true;
                        } else if (dypos == WIDTH - 1 && TILES[dxpos][dypos - 1] == Tileset.FLOOR) {
                            floorset = true;
                        }
                    }
                }
            }
            if (wallset && floorset) {
                TILES[dxpos][dypos] = Tileset.LOCKED_DOOR;
                door = true;
            }
        }
        while (!player) {
            pxpos = RANDOM.nextInt(WIDTH);
            pypos = RANDOM.nextInt(HEIGHT);
            if (TILES[pxpos][pypos] == Tileset.FLOOR) {
                TILES[pxpos][pypos] = Tileset.PLAYER;
                player = true;
            }
        }
        return TILES;
    }
    /** construct world from loaded game object. */
    public TETile[][] worldfromloaded(Game g) {
        this.RANDOM = g.RANDOM;
        this.SEED = g.SEED;
        this.dxpos = g.dxpos;
        this.dypos = g.dypos;
        this.pxpos = g.pxpos;
        this.pypos = g.pypos;
        System.out.println("Loaded Player position: " + g.pxpos + ", " + g.pypos);
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                char x = g.tilechars[i][j];
                TETile tile;
                if (x == '@') {
                    tile = Tileset.PLAYER;
                } else if (x == '#') {
                    tile = Tileset.WALL;
                } else if (x == '█') {
                    tile = Tileset.LOCKED_DOOR;
                } else if (x == '·') {
                    tile = Tileset.FLOOR;
                } else {
                    tile = Tileset.NOTHING;
                }
                this.TILES[i][j] = tile;
            }
        }
        this.pxpos = g.pxpos;
        this.pypos = g.pypos;
        this.dxpos = g.dxpos;
        this.dypos = g.dypos;
        this.tileInfo = g.tileInfo;
        return this.TILES;
    }
    /** constructor. set up preface. */
    public Game() {
        RANDOM = new Random(SEED);
    }

    /** update per player keyboard inputs. */
    private boolean updatemove(char move) {
        if (move == 'w') {
            if (pypos < HEIGHT - 1) {
                if (TILES[pxpos][pypos + 1] == Tileset.FLOOR) {
                    TILES[pxpos][pypos + 1] = Tileset.PLAYER;
                    TILES[pxpos][pypos] = Tileset.FLOOR;
                    pypos += 1;
                }
                if (TILES[pxpos][pypos + 1] == Tileset.LOCKED_DOOR) {
                    return true;
                }
            }
        } else if (move == 's') {
            if (pypos > 1) {
                if (TILES[pxpos][pypos - 1] == Tileset.FLOOR) {
                    TILES[pxpos][pypos - 1] = Tileset.PLAYER;
                    TILES[pxpos][pypos] = Tileset.FLOOR;
                    pypos -= 1;
                }
                if (TILES[pxpos][pypos - 1] == Tileset.LOCKED_DOOR) {
                    return true;
                }
            }
        } else if (move == 'a') {
            if (pxpos > 1) {
                if (TILES[pxpos - 1][pypos] == Tileset.FLOOR) {
                    TILES[pxpos - 1][pypos] = Tileset.PLAYER;
                    TILES[pxpos][pypos] = Tileset.FLOOR;
                    pxpos -= 1;
                }
                if (TILES[pxpos - 1][pypos] == Tileset.LOCKED_DOOR) {
                    return true;
                }
            }
        } else if (move == 'd') {
            if (pxpos < WIDTH - 1) {
                if (TILES[pxpos + 1][pypos] == Tileset.FLOOR) {
                    TILES[pxpos + 1][pypos] = Tileset.PLAYER;
                    TILES[pxpos][pypos] = Tileset.FLOOR;
                    pxpos += 1;
                }
                if (TILES[pxpos + 1][pypos] == Tileset.LOCKED_DOOR) {
                    return true;
                }
            }
        }
        return false;
    }
    /** update the upperright information. */
    private void upperrightinfo() {
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.filledRectangle(WIDTH - 4, HEIGHT - 1, 4, 1);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.textRight(WIDTH - 1, HEIGHT - 1, tileInfo);
        StdDraw.show();
    }

    /** update the upperright information. */
    private void uppermidinfo(String midinfo) {
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.filledRectangle(WIDTH / 2, HEIGHT - 1, 8, 1);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT - 1, midinfo);
        StdDraw.show();
    }

    private void infoBoard(String status) {
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.textLeft(1, HEIGHT - 1, "CONTROL: WASD");
        StdDraw.text(WIDTH / 2, HEIGHT - 1, status);
        StdDraw.line(0, HEIGHT - 2, WIDTH, HEIGHT - 2);
        StdDraw.show();
    }

    private void startgame(int width, int height) {
        StdDraw.setCanvasSize(width * 16, height * 16);
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        Font font = new Font("Courier", Font.BOLD, 2 * width);
        StdDraw.setFont(font);
        StdDraw.text(width / 2, height / 4 * 3, "Maze Escape");
        font = new Font("Courier", Font.BOLD, 16);
        StdDraw.setFont(font);
        StdDraw.text(width / 2, height / 2, "(N) New Game");
        StdDraw.text(width / 2, height / 2 - 2, "(L) Load Game");
        StdDraw.text(width / 2, height / 2 - 4, "(Q) Quit Game");
        StdDraw.show();
    }

    /** track mouse movement. */
    private void mousetrack() {
        int mousex = (int) StdDraw.mouseX();
        int mousey = (int) StdDraw.mouseY();
        if (mousex >= 0 && mousex <= WIDTH - 1 && mousey >= 0 && mousey <= HEIGHT - 1) {
            char character = TILES[mousex][mousey].character();
            if (character == '#') {
                tileInfo = "Wall";
            } else if (character == '@') {
                tileInfo = "Player";
            } else if (character == '█') {
                tileInfo = "LOCKED_DOOR";
            } else if (character == '·') {
                tileInfo = "Floor";
            } else if (character == ' ') {
                tileInfo = " ";
            }
        }
    }


    /** Serialization to save game progress. */
    public void saveprogress() {
        File f = new File("progress.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            for (int i = 0; i < WIDTH; i++) {
                for (int j = 0; j < HEIGHT; j++) {
                    tilechars[i][j] = TILES[i][j].character();
                }
            }
            os.writeObject(this);
            os.close();
            System.out.println("Save successful");
        }  catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }

    }

    /**Deserialization to load game progress. */
    public Game loadprogress() {
        File f = new File("progress.txt");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                Game loadWorld = (Game) os.readObject();
                os.close();
                return loadWorld;
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }
        return null;
    }

    /** game section - for play with keyboard. */
    public void gamesection(Game oldg, TERenderer ter) {
        if (oldg == null) {
            world();
        } else {
            worldfromloaded(oldg);
        }
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(TILES);
        infoBoard("Playing...");
        StdDraw.show();
        boolean win = false;
        boolean isQuit  = false;
        while (!win) {
            mousetrack();
            upperrightinfo();
            outer: if (!isQuit) {
                if (StdDraw.hasNextKeyTyped()) {
                    char x = StdDraw.nextKeyTyped();
                    if (x == ':') {
                        isQuit = true;
                        break outer;
                    }
                    win = updatemove(x);
                    ter.renderFrame(TILES);
                    infoBoard("Playing...");
                    if (win) {
                        uppermidinfo("You Win!");
                        StdDraw.pause(2000);
                        System.exit(0);
                    }
                }
            }
            if (StdDraw.hasNextKeyTyped()) {
                char x  = StdDraw.nextKeyTyped();
                if (x == 'q' || x == 'Q') {
                    isfromLoaded = true;
                    saveprogress();
                    uppermidinfo("Game Saved!");
                    StdDraw.pause(2000);
                    System.exit(0);
                } else {
                    isQuit = false;
                }
            }
        }
    }

    /** game section - for play with keyboard. */
    public TETile[][] gamesection(Game oldg, int charat, String input) {
        if (oldg == null) {
            world();
        } else {
            worldfromloaded(oldg);
        }
        int commandlength = input.length();
        boolean win = false;
        boolean isQuit  = false;
        while (!win && charat < commandlength) {
            outer:
            if (!isQuit) {
                char x = input.charAt(charat);
                charat += 1;
                if (x == ':') {
                    isQuit = true;
                    break outer;
                }
                win = updatemove(x);
                if (win) {
                    break;
                }
            }
            if (charat < commandlength) {
                char nextchar = input.charAt(charat);
                charat += 1;
                if (nextchar == 'q' || nextchar == 'Q') {
                    isfromLoaded = true;
                    saveprogress();
                    System.out.println("Game Saved!");
                    break;
                } else {
                    isQuit = false;
                }
            }
        }
        return TILES;
    }

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        int width = 40, height = 40;
        startgame(width, height);
        TERenderer ter = new TERenderer();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char ch = StdDraw.nextKeyTyped();
                if (ch == 'n' || ch == 'N') {
                    StdDraw.clear(Color.BLACK);
                    StdDraw.text(width / 2, height / 2, "Please Enter The Random Seed:");
                    StdDraw.text(width / 2, height / 2 - 2, "When You Finish, " +
                            "Press 's' to Enter the Game.");
                    StdDraw.show();
                    String temp = "";
                    while (true) {
                        if (StdDraw.hasNextKeyTyped()) {
                            char t = StdDraw.nextKeyTyped();
                            if ("1234567890".indexOf(t) != -1) {
                                temp += t;
                                StdDraw.clear(Color.black);
                                StdDraw.text(width / 2, height / 2,  "Random Seed Entered:");
                                StdDraw.text(width / 2, height / 2 - 6, "When You Finish, " +
                                        "Press 's' to Enter the Game.");
                                StdDraw.text(width / 2, height / 2 - 2, temp);
                                StdDraw.show();
                            }  else if (t == 's') {
                                if (temp.length() == 0) {
                                    StdDraw.clear(Color.BLACK);
                                    StdDraw.text(width / 2, height / 2, "No Random Seed Entered!");
                                    StdDraw.text(width / 2, height / 2 - 2,
                                            "Please Enter Random Seed:");
                                    StdDraw.show();
                                } else {
                                    this.SEED = Long.parseLong(temp);
                                    gamesection(null, ter);
                                }
                            }
                        }

                    }
                } else if (ch == 'l' || ch == 'L') {
                    System.out.println("Loading...");
                    Game g = loadprogress();
                    gamesection(g, ter);
                } else if (ch == 'q' || ch == 'Q') {
                    System.exit(0);
                }
            }
        }
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
        String seeder;
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        if (input == null || input.length() == 0) {
            System.out.println("No String Input Entered.");
            System.exit(0);
        }
        int charat = 0;
        int commandlength = input.length();
        bigloop: while (charat < commandlength) {
            char ch = input.charAt(charat);
            charat += 1;
            if (ch == 'n' || ch == 'N') {
                String temp = "";
                while (charat < commandlength) {
                    char t = input.charAt(charat);
                    charat += 1;
                    if ("1234567890".indexOf(t) != -1) {
                        temp += t;
                    } else if (t == 's' || t == 'S') {
                        if (temp.length() == 0) {
                            System.out.println("No random seed entered. Quiting...");
                            System.exit(0);
                        } else {
                            this.SEED = Long.parseLong(temp);
                            finalWorldFrame = gamesection(null, charat, input);
                            break bigloop;
                        }
                    }
                }
            } else if (ch == 'l' || ch == 'L') {
                System.out.println("Loading...");
                Game oldg = loadprogress();
                finalWorldFrame = gamesection(oldg, charat, input);
                break bigloop;
            } else if (ch == 'q' || ch == 'Q') {
                System.exit(0);
            }
        }

        return finalWorldFrame;
    }

}

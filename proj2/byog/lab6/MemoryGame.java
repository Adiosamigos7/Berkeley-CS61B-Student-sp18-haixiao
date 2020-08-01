package byog.lab6;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(40, 40);
        game.rand = new Random(seed);
        game.startGame();
    }

    public MemoryGame(int width, int height) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.setPenColor(Color.white);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
    }

    public String generateRandomString(int n) {
        //TODO: Generate random string of letters of length n
        if (n == 0) {
            return null;
        }
        char[] chars = new char[n];
        for (int i = 0; i < n; i++) {
            chars[i] = CHARACTERS[rand.nextInt(26)];
        }
        return new String(chars);
    }

    public void drawFrame(String s) {
        //TODO: Take the string and display it in the center of the screen
        //TODO: If game is not over, display relevant game information at the top of the screen
        StdDraw.clear(Color.black);
        StdDraw.text(this.width / 2, this.height / 2, s);
        InfoBoard("Watch!");
        StdDraw.show();
        StdDraw.pause(1000);
        StdDraw.clear(Color.black);
        InfoBoard("Watch!");
        StdDraw.show();
        StdDraw.pause(500);
    }

    private void InfoBoard(String status) {
        StdDraw.textLeft(1, height - 1, "Round" + round);
        StdDraw.text(width / 2, height - 1, status);
        StdDraw.textRight(width - 1, height - 1, "You're a star!");
        StdDraw.line(0, height - 2, width, height - 2);
    }

    public void flashSequence(String letters) {
        for (int i = 0; i < letters.length(); i++) {
            drawFrame(letters.substring(i, i  + 1));
        }
    }

    public String solicitNCharsInput(int n) {
        int i = 0;
        String inputs = "";
        StdDraw.clear(Color.black);
        InfoBoard("Type!");
        StdDraw.text(width / 2, height / 2,  "Your answer: \n");
        StdDraw.show();
        while (i < n) {
            if (StdDraw.hasNextKeyTyped()) {
                inputs += StdDraw.nextKeyTyped();
                i += 1;
                StdDraw.clear(Color.black);
                InfoBoard("Type!");
                StdDraw.text(width / 2, height / 2,  "Your answer: \n" + inputs);
                StdDraw.show();
            }
        }
        return inputs;
    }

    public void startGame() {
        round = 1;
        gameOver = false;
        while (!gameOver) {
            StdDraw.clear(Color.black);
            StdDraw.text(width / 2, height / 2, "Round " + round);
            StdDraw.show();
            StdDraw.pause(1000);
            String solution = generateRandomString(round);
            flashSequence(solution);
            String answer = solicitNCharsInput(round);
            StdDraw.clear(Color.black);
            if (answer.equals(solution)) {
                StdDraw.text(width / 2, height / 2, "You got this! The answer is: " + answer);
                InfoBoard("Type!");
                StdDraw.show();
                StdDraw.pause(1000);
                round += 1;
            } else {
                StdDraw.text(width / 2, height / 2, "Game Over! You made it to round: " + round);
                StdDraw.show();
                gameOver = true;
            }
        }

    }

}

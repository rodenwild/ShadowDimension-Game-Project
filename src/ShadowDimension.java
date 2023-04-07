import bagel.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Skeleton Code for SWEN20003 Project 2, Semester 2, 2022
 * Please enter your name below
 *
 * @author Roden Wild
 */

public class ShadowDimension extends AbstractGame {
    public static final int REFRESH_RATE = 60;
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String FIRST_LEVEL_FILE_PATH = "res/level0.csv";
    private final static String SECOND_LEVEL_FILE_PATH = "res/level1.csv";
    private final static String FIRST_BACKGROUND_PATH = "res/background0.png";
    private final static String SECOND_BACKGROUND_PATH = "res/background1.png";
    private final static String GAME_TITLE = "SHADOW DIMENSION";
    private final static String GAME_SUBTITLE = "PRESS SPACE TO START\nUSE ARROW KEYS TO FIND GATE";
    private final static String LEVEL_COMPLETE_MESSAGE = "LEVEL COMPLETE!";
    private final static String ATTACK_GAME_SUBTITLE = "PRESS SPACE TO START\nPRESS A TO ATTACK\nUSE ARROW KEYS TO " +
            "FIND GATE";
    private final static String WIN_MESSAGE = "CONGRATULATIONS!";
    private final static String LOSE_MESSAGE = "GAME OVER!";
    private final static int LEVEL_COMPLETE_SCREEN_TIME = 3;
    private final static int LEVEL_FILE_VALUES = 3;
    private final static int FINISH_X = 950;
    private final static int FINISH_Y = 670;
    private final static int TITLE_X = 260;
    private final static int TITLE_Y = 250;
    private final static int SUBTITLE_X_OFFSET = 90;
    private final static int SUBTITLE_Y_OFFSET = 190;
    private final static int HEALTH_X = 20;
    private final static int HEALTH_Y = 25;
    private final static int GREEN_HEALTH_LEVEL = 65;
    private final static int ORANGE_HEALTH_LEVEL = 35;
    private final Font titleFont = new Font("res/frostbite.ttf", 75);
    private final Font subtitleFont = new Font("res/frostbite.ttf", 40);
    private final Font healthFont = new Font("res/frostbite.ttf", 30);
    private final Font enemyHealthFont = new Font("res/frostbite.ttf", 15);
    private Image backgroundImage;
    private Level level = new Level();
    private boolean gameStarted = false;
    private boolean levelComplete = false;
    private boolean hasWon = false;
    private boolean hasLost = false;
    private int levelNumber = 0;
    private long frame = 0;
    private long levelCompleteFrame = 0;

    public ShadowDimension() {
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
        backgroundImage = new Image(FIRST_BACKGROUND_PATH);
        readCSV(FIRST_LEVEL_FILE_PATH);
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowDimension game = new ShadowDimension();
        game.run();
    }

    /**
     * Method used to read file and create level objects
     */
    private void readCSV(String path) {
        String row = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            while ((row = br.readLine()) != null) {
                String[] info = row.split(",");
                // If there are not enough values in the row, skip
                if (info.length < LEVEL_FILE_VALUES)
                    continue;

                double x = Double.parseDouble(info[1]);
                double y = Double.parseDouble(info[2]);
                // Add level objects based on their type
                switch (info[0]) {
                    case "Fae":
                        level.addPlayer(x, y);
                        break;
                    case "Demon":
                        level.addDemon(x, y);
                        break;
                    case "Navec":
                        level.addNavec(x, y);
                        break;
                    case "Wall":
                        level.addWall(x, y);
                        break;
                    case "Tree":
                        level.addTree(x, y);
                        break;
                    case "Sinkhole":
                        level.addSinkhole(x, y);
                        break;
                    case "TopLeft":
                        level.setTop((int) y);
                        level.setLeft((int) x);
                        break;
                    case "BottomRight":
                        level.setBottom((int) y);
                        level.setRight((int) x);
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }
        frame++;
        checkWin();
        checkDeath();
        checkStartGame(input);
        drawTextScreens();
        if (!gameStarted)
            return;
        backgroundImage.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
        level.drawLevel();
        level.getPlayer().draw();
        drawHealth();
        drawEnemyHealth();
        level.checkTimescale(input);
        level.moveDemons();
        level.checkInvincibility(frame, REFRESH_RATE);
        level.checkAttack(input, frame, REFRESH_RATE);
        level.checkDemonAttack(frame);
        level.checkMovement(input);
        level.checkHealth();
    }

    /**
     * Checks if the game has started
     */
    private boolean checkStartGame(Input input) {
        if (!gameStarted && !hasLost && !hasWon) {
            if (input.wasPressed(Keys.SPACE)) {
                gameStarted = true;
            }
        }
        return gameStarted;
    }

    /**
     * Draws the players health
     */
    public void drawHealth() {
        DrawOptions drawOptions = new DrawOptions();

        // Changes text colour based off current health
        if (level.getPlayer().getHealth() >= GREEN_HEALTH_LEVEL) {
            drawOptions.setBlendColour(0, 0.8, 0.2);
        } else if (level.getPlayer().getHealth() >= ORANGE_HEALTH_LEVEL) {
            drawOptions.setBlendColour(0.9, 0.6, 0);
        } else {
            drawOptions.setBlendColour(1, 0, 0);
        }
        healthFont.drawString(Math.round(100 * level.getPlayer().getHealth() / Player.MAX_HEALTH) + "%", HEALTH_X,
                HEALTH_Y, drawOptions);
    }

    /**
     * Draws the enemies health
     */
    public void drawEnemyHealth() {
        for (Enemy e : level.getEnemies()) {
            DrawOptions drawOptions = new DrawOptions();

            // Changes text colour based off current health
            if (e.getHealth() >= GREEN_HEALTH_LEVEL) {
                drawOptions.setBlendColour(0, 0.8, 0.2);
            } else if (e.getHealth() >= ORANGE_HEALTH_LEVEL) {
                drawOptions.setBlendColour(0.9, 0.6, 0);
            } else {
                drawOptions.setBlendColour(1, 0, 0);
            }
            enemyHealthFont.drawString(Math.round(100 * e.getHealth() / e.getMaxHealth()) + "%", e.x, e.y - 6,
                    drawOptions);
        }
    }

    /**
     * Checks players death
     */
    public boolean checkDeath() {
        // Draws death screen if health is 0
        if (level.getPlayer().getHealth() == 0) {
            hasLost = true;
            gameStarted = false;
            return true;
        }
        return false;
    }

    /**
     * Checks if player has won
     */
    public boolean checkWin() {
        // Level complete screen is only shown for LEVEL_COMPLETE_SCREEN_TIME seconds
        if (levelComplete && (frame - levelCompleteFrame) / REFRESH_RATE > LEVEL_COMPLETE_SCREEN_TIME) {
            levelComplete = false;
        }
        if (levelComplete) {
            return false;
        }
        // Draws win screen if player has reached the end or defeated Navec
        switch (levelNumber) {
            case 0:
                if (level.getPlayer().x >= FINISH_X && level.getPlayer().y >= FINISH_Y) {
                    titleFont.drawString(WIN_MESSAGE, (double) WINDOW_WIDTH / 2 - titleFont.getWidth(WIN_MESSAGE) / 2
                            , (double) WINDOW_HEIGHT / 2);
                    gameStarted = false;
                    levelCompleteFrame = frame;
                    levelComplete = true;
                    this.levelNumber++;
                    this.level = new Level();
                    backgroundImage = new Image(SECOND_BACKGROUND_PATH);
                    readCSV(SECOND_LEVEL_FILE_PATH);
                    return true;
                }
                return false;
            case 1:
                if (level.getBoss().getHealth() == 0) {
                    gameStarted = false;
                    hasWon = true;
                    return true;
                }
            default:
                return false;
        }
    }

    /**
     * Draws message screens based on the state of the game
     * */
    private void drawTextScreens() {
        if (gameStarted) {
            return;
        } else if (hasWon) {
            titleFont.drawString(WIN_MESSAGE, (double) WINDOW_WIDTH / 2 - titleFont.getWidth(WIN_MESSAGE) / 2,
                    (double) WINDOW_HEIGHT / 2);
        } else if (hasLost) {
            titleFont.drawString(LOSE_MESSAGE, (double) WINDOW_WIDTH / 2 - titleFont.getWidth(LOSE_MESSAGE) / 2,
                    (double) WINDOW_HEIGHT / 2);
        } else if (!levelComplete) {
            // Starting screens of level 0 and 1
            switch (levelNumber) {
                case 0:
                    titleFont.drawString(GAME_TITLE, TITLE_X, TITLE_Y);
                    subtitleFont.drawString(GAME_SUBTITLE, TITLE_X + SUBTITLE_X_OFFSET, TITLE_Y + SUBTITLE_Y_OFFSET);
                    break;
                case 1:
                    subtitleFont.drawString(ATTACK_GAME_SUBTITLE, TITLE_X + SUBTITLE_X_OFFSET,
                            TITLE_Y + SUBTITLE_Y_OFFSET);
                    break;
            }
        } else if (levelComplete) {
            titleFont.drawString(LEVEL_COMPLETE_MESSAGE,
                    (double) WINDOW_WIDTH / 2 - titleFont.getWidth(WIN_MESSAGE) / 2, (double) WINDOW_HEIGHT / 2);
        }
    }
}

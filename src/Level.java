import bagel.DrawOptions;
import bagel.Input;
import bagel.Keys;
import bagel.util.Rectangle;

import java.util.ArrayList;
import java.util.Iterator;

public class Level {
    private static final int MOVEMENT_SPEED = 2;
    private static final int SINKHOLE_DAMAGE = 30;
    private final int MAX_TIMESCALE = 3;
    private final int MIN_TIMESCALE = -3;
    private ArrayList<Wall> walls;
    private ArrayList<Tree> trees;
    private ArrayList<Sinkhole> sinkholes;
    private ArrayList<Enemy> enemies;
    private Player player;
    private Enemy boss;
    private int top;
    private int bottom;
    private int left;
    private int right;
    private int timescale = 0;

    public Level() {
        this.walls = new ArrayList<>();
        this.trees = new ArrayList<>();
        this.sinkholes = new ArrayList<>();
        this.enemies = new ArrayList<>();
    }

    public Enemy getBoss() {
        return boss;
    }

    public void setBoss(Enemy boss) {
        this.boss = boss;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(ArrayList<Enemy> enemies) {
        this.enemies = enemies;
    }

    public ArrayList<Wall> getWalls() {
        return walls;
    }

    public void setWalls(ArrayList<Wall> walls) {
        this.walls = walls;
    }

    public ArrayList<Tree> getTrees() {
        return trees;
    }

    public void setTrees(ArrayList<Tree> trees) {
        this.trees = trees;
    }

    public ArrayList<Sinkhole> getSinkholes() {
        return sinkholes;
    }

    public void setSinkholes(ArrayList<Sinkhole> sinkholes) {
        this.sinkholes = sinkholes;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void addPlayer(double x, double y) {
        player = new Player(x, y);
    }

    public void addWall(double x, double y) {
        walls.add(new Wall(x, y));
    }

    public void addTree(double x, double y) {
        trees.add(new Tree(x, y));
    }

    public void addSinkhole(double x, double y) {
        sinkholes.add(new Sinkhole(x, y));
    }

    public void addDemon(double x, double y) {
        enemies.add(new Demon(x, y));
    }

    public void addNavec(double x, double y) {
        Navec navec = new Navec(x, y);
        enemies.add(navec);
        this.boss = navec;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    /**
     * Draws the level objects
     */
    public void drawLevel() {
        for (LevelObject w : walls) {
            w.draw();
        }
        for (LevelObject t : trees) {
            t.draw();
        }
        for (LevelObject s : sinkholes) {
            s.draw();
        }
        for (LevelObject d : enemies) {
            d.draw();
        }
    }

    /**
     * Checks for timescale change input and changes accordingly
     * */
    public void checkTimescale(Input input) {
        if (input.wasPressed(Keys.L) && timescale < MAX_TIMESCALE) {
            timescale++;
            System.out.println("Sped up, Speed: " + timescale);
        } else if (input.wasPressed(Keys.K) && timescale > MIN_TIMESCALE) {
            timescale--;
            System.out.println("Slowed down, Speed: " + timescale);
        }
    }

    /**
     * Takes player movement and moves if it doesn't collide
     */
    public void checkMovement(Input input) {
        // For each movement direction, the player can only move in that direction if they are within the bounds
        // If the player intersects a wall or tree after moving, undo the move
        if (input.isDown(Keys.UP) && (player.y - MOVEMENT_SPEED) >= top) {
            player.move(0, -MOVEMENT_SPEED);
            boolean hasMoved = true;
            for (Wall w : walls) {
                if (player.rectangle.intersects(w.rectangle)) {
                    player.move(0, MOVEMENT_SPEED);
                    hasMoved = false;
                    break;
                }
            }
            if (hasMoved) {
                for (Tree t : trees) {
                    if (player.rectangle.intersects(t.rectangle)) {
                        player.move(0, MOVEMENT_SPEED);
                        break;
                    }
                }
            }
        }
        if (input.isDown(Keys.DOWN) && (player.y + MOVEMENT_SPEED) <= bottom) {
            player.move(0, MOVEMENT_SPEED);
            boolean hasMoved = true;
            for (Wall w : walls) {
                if (player.rectangle.intersects(w.rectangle)) {
                    player.move(0, -MOVEMENT_SPEED);
                    hasMoved = false;
                    break;
                }
            }
            if (hasMoved) {
                for (Tree t : trees) {
                    if (player.rectangle.intersects(t.rectangle)) {
                        player.move(0, -MOVEMENT_SPEED);
                        break;
                    }
                }
            }
        }
        if (input.isDown(Keys.RIGHT) && (player.x + MOVEMENT_SPEED) <= right) {
            player.setDirection(Direction.RIGHT);
            player.move(MOVEMENT_SPEED, 0);
            boolean hasMoved = true;
            for (Wall w : walls) {
                if (player.rectangle.intersects(w.rectangle)) {
                    player.move(-MOVEMENT_SPEED, 0);
                    hasMoved = false;
                    break;
                }
            }
            if (hasMoved) {
                for (Tree t : trees) {
                    if (player.rectangle.intersects(t.rectangle)) {
                        player.move(-MOVEMENT_SPEED, 0);
                        break;
                    }
                }
            }
        }
        if (input.isDown(Keys.LEFT) && (player.x - MOVEMENT_SPEED) >= left) {
            player.setDirection(Direction.LEFT);
            player.move(-MOVEMENT_SPEED, 0);
            boolean hasMoved = true;
            for (Wall w : walls) {
                if (player.rectangle.intersects(w.rectangle)) {
                    player.move(MOVEMENT_SPEED, 0);
                    hasMoved = false;
                    break;
                }
            }
            if (hasMoved) {
                for (Tree t : trees) {
                    if (player.rectangle.intersects(t.rectangle)) {
                        player.move(MOVEMENT_SPEED, 0);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Checks player attacks and damages enemies accordingly
     * */
    public void checkAttack(Input input, long frame, int refreshRate) {
        if (player.state == State.IDLE) {
            if (input.isDown(Keys.A)) {
                player.state = State.ATTACK;
                player.setAttackFrame(frame);
            }
        }
        if (player.state == State.ATTACK) {
            // If the players attack time is up, they return to idle
            if ((frame - player.getAttackFrame()) / refreshRate > Player.ATTACK_TIME / 1000) {
                player.state = State.IDLE;
            } else {
                // Check attacks on every enemy, if the player intersects them, they get damaged
                // If the enmies health goes to 0, they are removed
                Iterator<Enemy> i = enemies.iterator();
                while (i.hasNext()) {
                    Enemy d = i.next();
                    if (player.rectangle.intersects(d.rectangle) && !d.isInvincible()) {
                        d.changeHealth(-Player.ATTACK_DAMAGE);
                        d.setInvincibilityFrame(frame);
                        d.setInvincible(true);
                        System.out.println("Fae inflicts " + Player.ATTACK_DAMAGE + " damage points on " + d.getName() + ". " + d.getName() + "’s current " + "health: " + (int) d.getHealth() + "/" + d.getMaxHealth());
                    }
                    if (d.getHealth() == 0) {
                        i.remove();
                    }
                }
            }
        }
    }

    /**
     * Checks players health and updates it
     */
    public void checkHealth() {
        // Use an iterator in order to remove items while looping
        Iterator<Sinkhole> i = sinkholes.iterator();
        while (i.hasNext()) {
            Sinkhole s = i.next();
            // If the player is colliding with a sinkhole, their health reduces and the sinkhole is removed
            if (player.rectangle.intersects(s.rectangle)) {
                player.changeHealth(-SINKHOLE_DAMAGE);
                i.remove();
                System.out.println("Sinkhole inflicts " + SINKHOLE_DAMAGE + " damage points on Fae. Fae’s current " + "health: " + (int) player.getHealth() + "/" + Player.MAX_HEALTH);
            }
        }
        // Keep player health within bounds
        if (player.getHealth() > Player.MAX_HEALTH)
            player.setHealth(Player.MAX_HEALTH);
        if (player.getHealth() < 0)
            player.setHealth(0);
    }

    /**
     * Moves demons according to timescale and their directions
     * */
    public void moveDemons() {
        // Calculate timescale factor of demons
        double timescaleFactor = 1;
        if (timescale > 0) {
            for (int i = 0; i < timescale; i++) {
                timescaleFactor *= 1.5;
            }
        } else if (timescale < 0) {
            for (int i = 0; i > timescale; i--) {
                timescaleFactor *= 0.5;
            }
        }
        // For each enemy, if they are not passive, they are moved
        // If they intersect the bounds, a tree or sinkhole after moving,
        // their move is undone, and they move in the opposite direction
        for (Enemy d : enemies) {
            if (d.mood == Mood.PASSIVE)
                continue;
            boolean canMove = true;
            switch (d.getDirection()) {
                case UP:
                    d.move(0, -d.speed * timescaleFactor);
                    if (d.y <= top)
                        canMove = false;
                    for (LevelObject o : trees) {
                        if (d.rectangle.intersects(o.rectangle))
                            canMove = false;
                    }
                    for (LevelObject o : sinkholes) {
                        if (d.rectangle.intersects(o.rectangle))
                            canMove = false;
                    }
                    if (!canMove) {
                        d.move(0, d.speed * timescaleFactor);
                        d.setDirection(Direction.DOWN);
                    }
                    break;
                case DOWN:
                    d.move(0, d.speed * timescaleFactor);
                    if (d.y >= bottom)
                        canMove = false;
                    for (LevelObject o : trees) {
                        if (d.rectangle.intersects(o.rectangle))
                            canMove = false;
                    }
                    for (LevelObject o : sinkholes) {
                        if (d.rectangle.intersects(o.rectangle))
                            canMove = false;
                    }
                    if (!canMove) {
                        d.move(0, -d.speed * timescaleFactor);
                        d.setDirection(Direction.UP);
                    }
                    break;
                case LEFT:
                    d.move(-d.speed * timescaleFactor, 0);
                    if (d.x <= left)
                        canMove = false;
                    for (LevelObject o : trees) {
                        if (d.rectangle.intersects(o.rectangle))
                            canMove = false;
                    }
                    for (LevelObject o : sinkholes) {
                        if (d.rectangle.intersects(o.rectangle))
                            canMove = false;
                    }
                    if (!canMove) {
                        d.move(d.speed * timescaleFactor, 0);
                        d.setDirection(Direction.RIGHT);
                    }
                    break;
                case RIGHT:
                    d.move(d.speed * timescaleFactor, 0);
                    if (d.x >= right)
                        canMove = false;
                    for (LevelObject o : trees) {
                        if (d.rectangle.intersects(o.rectangle))
                            canMove = false;
                    }
                    for (LevelObject o : sinkholes) {
                        if (d.rectangle.intersects(o.rectangle))
                            canMove = false;
                    }
                    if (!canMove) {
                        d.move(-d.speed * timescaleFactor, 0);
                        d.setDirection(Direction.LEFT);
                    }
                    break;
            }
        }
    }

    /**
     * Checks enemy attacks, draws fire and checks if player gets damaged
     * */
    public void checkDemonAttack(long frame) {
        // Players centre coordinates
        double playerCentreX = player.x + player.image.getWidth() / 2;
        double playerCentreY = player.y + player.image.getHeight() / 2;
        for (Enemy d : enemies) {
            // Demons centre coordinates
            double centreX = d.x + d.image.getWidth() / 2;
            double centreY = d.y + d.image.getHeight() / 2;
            double distance =
                    Math.sqrt((playerCentreX - centreX) * (playerCentreX - centreX) + (playerCentreY - centreY) * (playerCentreY - centreY));
            // If the player is in attacking range, fire is drawn in the right direction and checkPlayerAttacked()
            // is called to check if the fire damages the player
            if (distance <= d.getAttackRange()) {
                d.state = State.ATTACK;
                DrawOptions drawOptions = new DrawOptions();
                if (playerCentreX <= centreX && playerCentreY <= centreY) {
                    // fire should be drawn from top-left.
                    drawOptions.setRotation(0);
                    double x = d.x - d.getFireImage().getWidth();
                    double y = d.y - d.getFireImage().getHeight();
                    d.getFireImage().drawFromTopLeft(x, y, drawOptions);
                    checkPlayerAttacked(d.getAttackDamage(), new Rectangle(x, y, d.getFireImage().getWidth(),
                            d.getFireImage().getHeight()), frame, d.getName());
                }
                if (playerCentreX <= centreX && playerCentreY > centreY) {
                    // fire should be drawn from bottom-left.
                    drawOptions.setRotation(1.5 * Math.PI);
                    double x = d.x - d.getFireImage().getWidth();
                    double y = d.y + d.image.getHeight();
                    d.getFireImage().drawFromTopLeft(x, y, drawOptions);
                    checkPlayerAttacked(d.getAttackDamage(), new Rectangle(x, y, d.getFireImage().getWidth(),
                            d.getFireImage().getHeight()), frame, d.getName());
                }
                if (playerCentreX > centreX && playerCentreY <= centreY) {
                    // fire should be drawn from top-right.
                    drawOptions.setRotation(0.5 * Math.PI);
                    double x = d.x + d.image.getWidth();
                    double y = d.y - d.getFireImage().getHeight();
                    d.getFireImage().drawFromTopLeft(x, y, drawOptions);
                    checkPlayerAttacked(d.getAttackDamage(), new Rectangle(x, y, d.getFireImage().getWidth(),
                            d.getFireImage().getHeight()), frame, d.getName());
                }
                if (playerCentreX > centreX && playerCentreY > centreY) {
                    // fire should be drawn from bottom-right.
                    drawOptions.setRotation(Math.PI);
                    double x = d.x + d.image.getWidth();
                    double y = d.y + d.image.getHeight();
                    d.getFireImage().drawFromTopLeft(x, y, drawOptions);
                    checkPlayerAttacked(d.getAttackDamage(), new Rectangle(x, y, d.getFireImage().getWidth(),
                            d.getFireImage().getHeight()), frame, d.getName());
                }
            }
        }
    }

    /**
     * Checks if player has been hit with fire
     * */
    private void checkPlayerAttacked(int damage, Rectangle fireRectangle, long frame, String name) {
        if (player.isInvincible())
            return;
        if (player.rectangle.intersects(fireRectangle)) {
            player.changeHealth(-damage);
            player.setInvincible(true);
            player.setInvincibilityFrame(frame);
            System.out.println(name + " inflicts " + damage + " damage points on Fae. Fae’s current " + "health: " + (int) player.getHealth() + "/" + Player.MAX_HEALTH);
        }
    }

    /**
     * Turns off invincibility if time limit has been reached
     * */
    void checkInvincibility(long frame, int refreshRate) {
        if (player.isInvincible()) {
            if ((frame - player.getInvincibilityFrame()) / refreshRate > Player.INVINCIBLE_TIME / 1000) {
                player.setInvincible(false);
            }
        }
        for (Enemy d : enemies) {
            if (d.isInvincible()) {
                if ((frame - d.getInvincibilityFrame()) / refreshRate > d.getInvincibleTime() / 1000) {
                    d.setInvincible(false);
                }
            }
        }
    }
}

import bagel.Image;
import bagel.util.Rectangle;

import java.util.Random;

public class Navec extends Enemy {
    public static final int MAX_HEALTH = 80;
    public static final int ATTACK_TIME = 1000;
    public static final int ATTACK_COOLDOWN = 2000;
    public static final int INVINCIBLE_TIME = 3000;
    private static final String NAVEC_RIGHT_PATH = "res/navec/navecRight.png";
    private static final String NAVEC_LEFT_PATH = "res/navec/navecLeft.png";
    private static final String NAVEC_INVINCIBLE_RIGHT_PATH = "res/navec/navecInvincibleRight.png";
    private static final String NAVEC_INVINCIBLE_LEFT_PATH = "res/navec/navecInvincibleLeft.png";
    private static final String NAVEC_FIRE_PATH = "res/navec/navecFire.png";
    public final int ATTACK_DAMAGE = 20;
    public final int ATTACK_RANGE = 200;

    public Navec(double x, double y) {
        this.name = "Navec";
        rightImage = new Image(NAVEC_RIGHT_PATH);
        leftImage = new Image(NAVEC_LEFT_PATH);
        rightInvincibleImage = new Image(NAVEC_INVINCIBLE_RIGHT_PATH);
        leftInvincibleImage = new Image(NAVEC_INVINCIBLE_LEFT_PATH);
        fireImage = new Image(NAVEC_FIRE_PATH);
        health = MAX_HEALTH;

        Random r = new Random();
        // Random number {0, 1} to determine passive or aggressive
        int randNum = r.nextInt(2);
        this.mood = randNum == 0 ? Mood.AGGRESSIVE : Mood.PASSIVE;
        // Random number between 2 and 7 to determine speed
        double randomSpeed = r.nextInt((7 - 2) + 1) + 2;
        randomSpeed /= 10;
        this.speed = randomSpeed;

        // Random number to determine direction
        randNum = r.nextInt(4);
        switch (randNum) {
            case 0:
                this.direction = Direction.UP;
                break;
            case 1:
                this.direction = Direction.DOWN;
                break;
            case 2:
                this.direction = Direction.LEFT;
                break;
            case 3:
                this.direction = Direction.RIGHT;
                break;
            default:
                this.direction = Direction.RIGHT;
                break;
        }
        this.x = x;
        this.y = y;
        this.image = rightImage;
        this.rectangle = new Rectangle(x, y, image.getWidth(), image.getHeight());
    }

    /**
     * Changes the health while keeping within bounds
     * */
    public void changeHealth(double healthChange) {
        // Ensures that the health does not go out of bounds
        if ((this.health + healthChange) < 0) {
            this.health = 0;
        } else if ((this.health + healthChange) > MAX_HEALTH) {
            this.health = MAX_HEALTH;
        } else {
            this.health += healthChange;
        }
    }

    public int getAttackTime() {
        return ATTACK_TIME;
    }

    public int getAttackCooldown() {
        return ATTACK_COOLDOWN;
    }

    public int getInvincibleTime() {
        return INVINCIBLE_TIME;
    }

    public int getAttackDamage() {
        return ATTACK_DAMAGE;
    }

    public int getAttackRange() {
        return ATTACK_RANGE;
    }

    public int getMaxHealth() {
        return MAX_HEALTH;
    }
}

import bagel.Image;
import bagel.util.Rectangle;

import java.util.Random;

public class Demon extends Enemy {
    private static final String DEMON_RIGHT_PATH = "res/demon/demonRight.png";
    private static final String DEMON_LEFT_PATH = "res/demon/demonLeft.png";
    private static final String DEMON_INVINCIBLE_RIGHT_PATH = "res/demon/demonInvincibleRight.png";
    private static final String DEMON_INVINCIBLE_LEFT_PATH = "res/demon/demonInvincibleLeft.png";
    private static final String DEMON_FIRE_PATH = "res/demon/demonFire.png";
    public static final int MAX_HEALTH = 40;
    public static final int ATTACK_TIME = 1000;
    public static final int ATTACK_COOLDOWN = 2000;
    public static final int INVINCIBLE_TIME = 3000;
    public final int ATTACK_DAMAGE = 10;
    public final int ATTACK_RANGE = 150;

    public Demon(double x, double y) {
        this.name = "Demon";
        rightImage = new Image(DEMON_RIGHT_PATH);
        leftImage = new Image(DEMON_LEFT_PATH);
        rightInvincibleImage = new Image(DEMON_INVINCIBLE_RIGHT_PATH);
        leftInvincibleImage = new Image(DEMON_INVINCIBLE_LEFT_PATH);
        fireImage = new Image(DEMON_FIRE_PATH);
        health = MAX_HEALTH;

        Random r = new Random();
        int randNum = r.nextInt(2);
        this.mood = randNum == 0 ? Mood.AGGRESSIVE : Mood.PASSIVE;
        double randomSpeed = r.nextInt((7 - 2) + 1) + 2;
        randomSpeed /= 10;
        this.speed = randomSpeed;
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
        this.image = new Image(DEMON_RIGHT_PATH);
        this.rectangle = new Rectangle(x, y, image.getWidth(), image.getHeight());
    }

    /**
     * Change demons health, staying within bounds
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

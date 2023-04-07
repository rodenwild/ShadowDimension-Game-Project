import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Player extends LevelObject implements Movable {
    public static final int MAX_HEALTH = 100;
    public static final int ATTACK_TIME = 1000;
    public static final int ATTACK_COOLDOWN = 2000;
    public static final int INVINCIBLE_TIME = 3000;
    public static final int ATTACK_DAMAGE = 20;
    private static final String FAE_RIGHT_PATH = "res/fae/faeRight.png";
    private static final String FAE_LEFT_PATH = "res/fae/faeLeft.png";
    private static final String FAE_ATTACK_RIGHT_PATH = "res/fae/faeAttackRight.png";
    private static final String FAE_ATTACK_LEFT_PATH = "res/fae/faeAttackLeft.png";
    private final Image rightImage = new Image(FAE_RIGHT_PATH);
    private final Image leftImage = new Image(FAE_LEFT_PATH);
    private final Image rightAttackImage = new Image(FAE_ATTACK_RIGHT_PATH);
    private final Image leftAttackImage = new Image(FAE_ATTACK_LEFT_PATH);
    private double health = 100;
    private Direction direction = Direction.RIGHT;
    private long attackFrame = 0;
    private long invincibilityFrame = 0;
    private boolean isInvincible = false;

    public Player(double x, double y) {
        this.x = x;
        this.y = y;
        this.image = new Image(FAE_RIGHT_PATH);
        this.rectangle = new Rectangle(x, y, image.getWidth(), image.getHeight());
        this.state = State.IDLE;
    }

    public long getInvincibilityFrame() {
        return invincibilityFrame;
    }

    public void setInvincibilityFrame(long invincibilityFrame) {
        this.invincibilityFrame = invincibilityFrame;
    }

    public boolean isInvincible() {
        return isInvincible;
    }

    public void setInvincible(boolean invincible) {
        isInvincible = invincible;
    }

    public long getAttackFrame() {
        return attackFrame;
    }

    public void setAttackFrame(long attackFrame) {
        this.attackFrame = attackFrame;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Changes players health
     */
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

    /**
     * Overrides the draw method so that fae can be drawn facing left or right
     */
    @Override
    public void draw() {
        if (state == State.ATTACK) {
            if (direction == Direction.LEFT) {
                leftAttackImage.drawFromTopLeft(x, y);
            } else {
                rightAttackImage.drawFromTopLeft(x, y);
            }
        } else {
            if (direction == Direction.LEFT) {
                leftImage.drawFromTopLeft(x, y);
            } else {
                rightImage.drawFromTopLeft(x, y);
            }
        }
    }

    /**
     * Moves player and updates its rectangle
     * */
    public void move(double x, double y) {
        this.x += x;
        this.y += y;
        this.rectangle.moveTo(new Point(this.x, this.y));
    }
}

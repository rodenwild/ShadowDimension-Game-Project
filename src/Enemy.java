import bagel.Image;
import bagel.util.Point;

public abstract class Enemy extends LevelObject implements Movable {
    public Mood mood;
    public double speed;
    protected Image rightImage;
    protected Image leftImage;
    protected Image rightInvincibleImage;
    protected Image leftInvincibleImage;
    protected Image fireImage;
    protected Direction direction = Direction.RIGHT;
    protected double health;
    protected String name = "Enemy";
    private long attackFrame = 0;
    private long invincibilityFrame = 0;
    private boolean isInvincible = false;

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

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public Image getRightImage() {
        return rightImage;
    }

    public Image getLeftImage() {
        return leftImage;
    }

    public Image getRightInvincibleImage() {
        return rightInvincibleImage;
    }

    public Image getLeftInvincibleImage() {
        return leftInvincibleImage;
    }

    public Image getFireImage() {
        return fireImage;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public long getAttackFrame() {
        return attackFrame;
    }

    public void setAttackFrame(long attackFrame) {
        this.attackFrame = attackFrame;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Overrides the draw method so that enemy can be drawn facing left or right
     */
    @Override
    public void draw() {
        if (isInvincible) {
            if (direction == Direction.LEFT) {
                leftInvincibleImage.drawFromTopLeft(x, y);
                this.image = leftInvincibleImage;
            } else {
                rightInvincibleImage.drawFromTopLeft(x, y);
                this.image = rightInvincibleImage;
            }
        } else {
            if (direction == Direction.LEFT) {
                leftImage.drawFromTopLeft(x, y);
                this.image = leftImage;
            } else {
                rightImage.drawFromTopLeft(x, y);
                this.image = rightImage;
            }
        }
    }

    /**
     * Moves enemy and updates its rectangle
     * */
    public void move(double x, double y) {
        this.x += x;
        this.y += y;
        this.rectangle.moveTo(new Point(this.x, this.y));
    }

    abstract public void changeHealth(double healthChange);

    abstract public int getAttackTime();

    abstract public int getAttackCooldown();

    abstract public int getInvincibleTime();

    abstract public int getAttackDamage();

    abstract public int getAttackRange();

    abstract public int getMaxHealth();
}

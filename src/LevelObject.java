import bagel.Image;
import bagel.util.Rectangle;

public abstract class LevelObject {
    protected double x;
    protected double y;
    protected Image image;
    protected Rectangle rectangle;
    protected State state;

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void draw() {
        image.drawFromTopLeft(x, y);
    }
}
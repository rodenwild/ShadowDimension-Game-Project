import bagel.Image;
import bagel.util.Rectangle;

public class Sinkhole extends LevelObject {
    public static final String IMAGE_PATH = "res/sinkhole.png";

    public Sinkhole(double x, double y) {
        this.x = x;
        this.y = y;
        this.image = new Image(IMAGE_PATH);
        this.rectangle = new Rectangle(x, y, image.getWidth(), image.getHeight());
    }
}

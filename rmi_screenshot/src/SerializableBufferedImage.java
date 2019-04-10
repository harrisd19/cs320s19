import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializableBufferedImage implements Serializable {

    private transient BufferedImage image;

    SerializableBufferedImage(BufferedImage image) {
        this.image = image;
    }

    public void writeObject(ObjectOutputStream out) throws IOException {
        ImageIO.write(image, "png", out);
    }

    public void readObject(ObjectInputStream in) throws IOException {
        image = ImageIO.read(in);
    }

    public BufferedImage getImage() {
        return image;
    }
}

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ScreenshotClient extends JFrame {

    ScreenshotClient(BufferedImage image) {
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        ImageIcon imageIcon = new ImageIcon(image);
        JLabel imageLabel = new JLabel(imageIcon);
        getContentPane().add(imageLabel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) throws IOException, NotBoundException {
        String host = "localhost";
        Registry registry = LocateRegistry.getRegistry(host);
        Screenshot stub = (Screenshot) registry.lookup("screenshot");
        byte bytes[] = stub.getScreenshot();
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
        new ScreenshotClient(image);
    }
}

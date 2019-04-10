import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ScreenshotServer implements Screenshot {

    @Override
    public byte[] getScreenshot() throws RemoteException {
        BufferedImage image = null;
        try {
            image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            ByteOutputStream out = new ByteOutputStream();
            ImageIO.write(image, "png", out);
            return out.getBytes();
        } catch (AWTException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws RemoteException {
        String host = "localhost";

        ScreenshotServer obj = new ScreenshotServer();
        Screenshot skeleton = (Screenshot) UnicastRemoteObject.exportObject(obj, 0);

        Registry registry = LocateRegistry.getRegistry(host);
        registry.rebind("screenshot", skeleton);
        System.out.println("Server ready!");
    }
}

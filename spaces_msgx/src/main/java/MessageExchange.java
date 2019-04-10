import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.UrlSpaceConfigurer;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class MessageExchange implements Runnable {

    private GigaSpace space;

    MessageExchange() {
        UrlSpaceConfigurer url = new UrlSpaceConfigurer(Configuration.SPACE_URL);
        GigaSpaceConfigurer conf = new GigaSpaceConfigurer(url);
        space = conf.gigaSpace();
    }

    void runMain() {
        Scanner sc = new Scanner(System.in);
        Random r = new Random();
        System.out.println("Enter a message (empty message quits)");
        while (true) {
            String text = sc.nextLine();
            if (text.length() == 0)
                break;
            Message msg = new Message();
            msg.setId(r.nextInt(100));
            msg.setText(text);
            space.write(msg);
        }
        System.out.println("Done!");
        System.exit(1);
    }

    public void run() {
        while (true) {
            Message msgs[] = space.readMultiple(new Message());
            System.out.println(Arrays.toString(msgs));
            try {
                Thread.sleep(5000);
            }
            catch (InterruptedException ex) {

            }
        }
    }

    public static void main(String[] args) {
        MessageExchange msgX = new MessageExchange();

        // start the readMultiple thread
        new Thread(msgX).start();

        // run the user interface thread
        msgX.runMain();
    }
}

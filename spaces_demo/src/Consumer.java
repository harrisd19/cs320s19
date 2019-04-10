import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.UrlSpaceConfigurer;

public class Consumer {

    private GigaSpace space;

    Consumer() {
        UrlSpaceConfigurer url = new UrlSpaceConfigurer(Configuration.SPACE_URL);
        GigaSpaceConfigurer conf = new GigaSpaceConfigurer(url);
        space = conf.gigaSpace();
    }

    void run() {
        for (int i = 1; i <= Configuration.NUMBER_MSGS; i++) {
            Message msg = space.take(new Message());
            System.out.println(msg + " was received");
        }
        System.out.println("Done!");
    }

    public static void main(String[] args) {
        Consumer consumer = new Consumer();
        consumer.run();
    }
}

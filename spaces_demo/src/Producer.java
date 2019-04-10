import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.UrlSpaceConfigurer;

public class Producer {

    private GigaSpace space;

    Producer() {
        UrlSpaceConfigurer url = new UrlSpaceConfigurer(Configuration.SPACE_URL);
        GigaSpaceConfigurer conf = new GigaSpaceConfigurer(url);
        space = conf.gigaSpace();
    }

    void run() {
        for (int i = 1; i <= Configuration.NUMBER_MSGS; i++) {
            Message msg = new Message();
            msg.setId(i);
            msg.setText("msg #" + i);
            space.write(msg);
            System.out.println(msg + " was sent");
        }
        System.out.println("Done!");
    }

    public static void main(String[] args) {
        Producer producer = new Producer();
        producer.run();
    }
}

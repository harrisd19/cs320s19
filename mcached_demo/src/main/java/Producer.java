import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;

public class Producer {

    private MemcachedClient mc;

    Producer() throws IOException {
        ConnectionFactoryBuilder builder = new ConnectionFactoryBuilder();
        builder.setProtocol(ConnectionFactoryBuilder.Protocol.BINARY);
        List<InetSocketAddress> addresses = AddrUtil.getAddresses(Configuration.SERVERS);
        mc = new MemcachedClient(builder.build(), addresses);
    }

    void run() {
        for (int i = 0; i < Configuration.NUMBER_TASKS; i++) {
            String key = "" + (i + 1);
            String text = "Task #" + key;
            mc.set(key, Configuration.NO_EXPIRATION, text);
            System.out.println(text + " created!");
        }
        System.out.println("Producer done!");
    }

    public static void main(String[] args) throws IOException {
        new Producer().run();
    }

}

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BulkRead {

    private MemcachedClient mc;
    static final String HOST = "localhost";
    static final int    PORT = 11211;
    static final int    NO_EXPIRATION = 0;
    static final int    TOTAL_MESSAGES = 10;

    BulkRead() throws IOException {
        ConnectionFactoryBuilder builder = new ConnectionFactoryBuilder();
        builder.setProtocol(ConnectionFactoryBuilder.Protocol.BINARY);
        List<InetSocketAddress> addresses = AddrUtil.getAddresses(HOST + ":" + PORT);
        mc = new MemcachedClient(builder.build(), addresses);
    }

    void run() {
        // sending TOTAL_MESSAGES messages
        List<String> keys = new LinkedList<String>();
        for (int i = 0; i < TOTAL_MESSAGES; i++) {
            Message msg = new Message((i + 1) + "", "Message #" + (i + 1));
            mc.set(msg.getId(), NO_EXPIRATION, msg);
            keys.add(msg.getId());
        }

        // reading and delete all messages
        Map<String, Object> msgs = mc.getBulk(keys);
        for (String key: msgs.keySet()) {
            Message msg = (Message) msgs.get(key);
            System.out.println(msg);
            mc.delete(msg.getId());
        }
    }

    public static void main(String[] args) throws IOException {
        BulkRead bulkRead = new BulkRead();
        bulkRead.run();
    }
}

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class HelloMemcached {

    private MemcachedClient mc;
    static final String HOST = "localhost";
    static final int    PORT = 11211;  // specific to memcached
    static final int    NO_EXPIRATION = 0;

    HelloMemcached() throws IOException {
        ConnectionFactoryBuilder builder = new ConnectionFactoryBuilder();
        builder.setProtocol(ConnectionFactoryBuilder.Protocol.BINARY);
        String addressesString = HOST + ":" + PORT;
//        String addressesString = "localhost:9000,localhost:9001";
        List<InetSocketAddress> addresses = AddrUtil.getAddresses(addressesString);
        mc = new MemcachedClient(builder.build(), addresses);
    }

    void run() {
//        // creating an object in memcached
//        Message msg = new Message("1", "Message 1");
//        // mc.set(msg.getId(), NO_EXPIRATION, msg);
//
//        // read the object
//        msg = (Message) mc.get("1");
//        System.out.println(msg);

        mc.set("test", NO_EXPIRATION, "1");
        mc.incr("test", 1);
        System.out.println(mc.get("test"));
    }

    public static void main(String[] args) throws IOException {
        HelloMemcached helloMc = new HelloMemcached();
        helloMc.run();
    }
}

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
    static final int    PORT = 11211;
    static final int    NO_EXPIRATION = 0;

    HelloMemcached() throws IOException {
        ConnectionFactoryBuilder builder = new ConnectionFactoryBuilder();
        builder.setProtocol(ConnectionFactoryBuilder.Protocol.BINARY);
        List<InetSocketAddress> addresses = AddrUtil.getAddresses(HOST + ":" + PORT);
        mc = new MemcachedClient(builder.build(), addresses);
    }

    void run() {
        // creating an object in memcached
        Message msg = new Message("1", "Hello Memcached!");
        mc.set(msg.getId(), NO_EXPIRATION, msg);

        // read the object
        msg = (Message) mc.get("1");
        System.out.println(msg);
    }

    public static void main(String[] args) throws IOException {
        HelloMemcached helloMc = new HelloMemcached();
        helloMc.run();
    }
}

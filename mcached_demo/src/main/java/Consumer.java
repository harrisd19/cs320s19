import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.rmi.ConnectIOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Consumer {

    private MemcachedClient mc;
    private static final int TIMEOUT = 500; // ms

    Consumer() throws IOException {
        ConnectionFactoryBuilder builder = new ConnectionFactoryBuilder();
        builder.setProtocol(ConnectionFactoryBuilder.Protocol.BINARY);
        List<InetSocketAddress> addresses = AddrUtil.getAddresses(Configuration.SERVERS);
        mc = new MemcachedClient(builder.build(), addresses);
    }

    void run() {
        int key = 1;
        while (key < Configuration.NUMBER_TASKS) {
            Future<Object> getFuture = mc.asyncGet(key + "");
            try {
                String task = (String) getFuture.get(TIMEOUT, TimeUnit.MILLISECONDS);
                Future<Boolean> delFuture = mc.delete(key + "");
                boolean success = delFuture.get(TIMEOUT, TimeUnit.MILLISECONDS);
                if (success)
                    System.out.println(task + " consumed!");
            }
            catch (Exception ex) {
            }
            key++;
            try {
                Thread.sleep(new Random().nextInt(TIMEOUT));
            }
            catch (InterruptedException ex) {

            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Consumer().run();
    }
}

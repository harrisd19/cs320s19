import redis.clients.jedis.Jedis;

public class HelloRedis {

    private Jedis jd;
    static final String HOST = "localhost";
    static final int    PORT = 9000;  // specific to memcached

    HelloRedis() {
        jd = new Jedis(HOST, PORT);
    }

    void run() {
        // creating an object in memcached
        Message msg = new Message("1", "Message 1");
        jd.set(msg.getId(), msg.getText());

        // read the object
        String text = jd.get("1");
        msg = new Message("1", text);
        System.out.println(msg);
    }

    public static void main(String[] args) {
        HelloRedis helloRedis = new HelloRedis();
        helloRedis.run();
    }
}

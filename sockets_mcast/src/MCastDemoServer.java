import java.io.IOException;
import java.net.*;
import java.util.Random;

public class MCastDemoServer {

    static final int    PORT          = 60000;
    static final String MCAST_ADDRESS = "230.0.0.1";
    static final int    BUFFER_SIZE   = 1024;
    static final int    NUMBER_RANGE  = 100;
    static final int    SLEEP_TIME    = 5000;
    static Random rnd = new Random();

    static byte[] updateData() {
        int number = rnd.nextInt(NUMBER_RANGE) + 1;
        String msg = "Your lucky number now is " + number;
        return msg.getBytes();
    }

    public static void main(String[] args) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        DatagramPacket packet;
        DatagramSocket socket = new DatagramSocket();
        while (true) {
            buffer = updateData();
            packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(MCAST_ADDRESS), PORT);
            socket.send(packet);
            System.out.println("Mcast packet sent!");
            try {
                Thread.sleep(SLEEP_TIME);
            }
            catch (InterruptedException ex) {

            }
        }
    }
}

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MCastDemoClient {

    static final int    PORT          = 60000;
    static final String MCAST_ADDRESS = "230.0.0.1";
    static final int    BUFFER_SIZE   = 1024;

    public static void main(String[] args) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        DatagramPacket packet = new DatagramPacket(buffer, BUFFER_SIZE);
        MulticastSocket mcastSocket = new MulticastSocket(PORT);
        mcastSocket.joinGroup(InetAddress.getByName(MCAST_ADDRESS));
        while (true) {
            mcastSocket.receive(packet);
            String msg = new String(buffer).substring(0, packet.getLength());
            System.out.println(msg);
        }
    }
}

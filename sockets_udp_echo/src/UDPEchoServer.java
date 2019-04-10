import java.io.IOException;
import java.net.*;

public class UDPEchoServer {

    static final int PORT = 80;
    static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) throws IOException {
        InetAddress myIP = InetAddress.getByName("10.180.128.221");
        DatagramSocket datagramSocket = new DatagramSocket(PORT, myIP);
        while (true) {
            System.out.println(UDPEchoServer.class + " listening on " + datagramSocket.getLocalAddress());
            byte buffer[] = new byte[BUFFER_SIZE];
            DatagramPacket datagramPacket = new DatagramPacket(buffer, BUFFER_SIZE);
            datagramSocket.receive(datagramPacket);
            System.out.print("Message received: ");
            String msg = new String(buffer).substring(0, datagramPacket.getLength());
            System.out.println(msg);
        }
//        datagramSocket.close();
    }
}
